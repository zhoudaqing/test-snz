package io.terminus.snz.user.manager;

import com.google.common.base.*;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.Joiners;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.PurchaserAuthorityDao;
import io.terminus.snz.user.dao.SupplierResourceMaterialInfoDao;
import io.terminus.snz.user.dao.SupplierResourceMaterialLogDao;
import io.terminus.snz.user.dao.SupplierResourceMaterialSubjectDao;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRequestDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRichRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialRichInfoDto;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.PurchaserAuthorityService;
import io.terminus.snz.user.tool.BcHelper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.*;
import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
@Slf4j
@Component
public class SupplierResourceMaterialManager {

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    @Autowired
    private SupplierResourceMaterialInfoDao supplierResourceMaterialInfoDao;

    @Autowired
    private SupplierResourceMaterialLogDao supplierResourceMaterialLogDao;

    @Autowired
    private SupplierResourceMaterialSubjectDao supplierResourceMaterialSubjectDao;

    @Autowired
    private PurchaserAuthorityDao purchaserAuthorityDao;

    @Autowired
    private PurchaserAuthorityService purchaserAuthorityService;

    @Autowired
    private BcHelper bcHelper;

    @Autowired
    private CompanyService companyService;

    /**
     * 新增日志记录通用操作
     */
    private void insertLog(Long supplierId, Long checkerId, Long times, Integer type, Integer status, Object contentObj) {
        log.debug("Log Action: ==> supplierId={}, checkerId={}, times={}, type={}, status={}, contentObj={}",
                supplierId, checkerId, times, type, status, contentObj);
        SupplierResourceMaterialLog log1 = new SupplierResourceMaterialLog();
        log1.setSupplierId(supplierId);
        log1.setCheckerId(checkerId);
        log1.setTimes(times);
        log1.setType(type);
        log1.setStatus(MoreObjects.firstNonNull(status, 1)); // 若status为null，默认1. 通过
        if (notNull(contentObj)) {
            log1.setContent(JSON_MAPPER.toJson(contentObj));
        }
        checkState(supplierResourceMaterialLogDao.create(log1) > 0);
    }

    public Optional<SupplierResourceMaterialInfo> getInfo(Long supplierId) {
        return Optional.fromNullable(supplierResourceMaterialInfoDao.findOneBySupplierId(supplierId));
    }

    private PurchaserAuthority purify(PurchaserAuthority orig) {
        PurchaserAuthority auth = new PurchaserAuthority();
        auth.setUserId(orig.getUserId());
        auth.setContent(orig.getContent());
        auth.setPosition(orig.getPosition());
        auth.setRole(orig.getRole());
        return auth;
    }

    private Set<PurchaserAuthority> buildAuthMatrix(Set<String> roles, Set<Long> bcIds) {
        Set<PurchaserAuthority> matrix = Sets.newHashSet();
        for (String role : roles) {
            for (Long bcId : bcIds) {
                for (Integer position : ImmutableList.of(1, 2)) {
                    PurchaserAuthority params = new PurchaserAuthority();
                    params.setType(1);
                    params.setContent(bcId.toString());
                    params.setPosition(position);
                    params.setRole(role);
                    List<PurchaserAuthority> exists = purchaserAuthorityDao.findBy(params);
                    for (PurchaserAuthority exist : exists) {
                        matrix.add(purify(exist));
                    }
                }
            }
        }
        return matrix;
    }

    @Transactional
    public boolean applyForNormal(Long supplierId, List<Long> bcIds, Integer type) {
        SupplierResourceMaterialInfo info1 = getInfo(supplierId).get();
        checkState(info1.getTimes() >= 0);
        info1.setStatus(SupplierResourceMaterialInfo.Status.SUBMITTED.value());
        info1.setTimes(info1.getTimes() + 1);
        info1.setLastSubmitTime(DateTime.now().toDate());
        checkState(supplierResourceMaterialInfoDao.update(info1));

        SupplierResourceMaterialLogRequestDto requestDto = new SupplierResourceMaterialLogRequestDto();
        List<SupplierResourceMaterialSubject> subjects = getAllValidSubjects();
        requestDto.setSubjectIds(FluentIterable.from(subjects).transform(new SubjectHelper().toId()).toSet());
        requestDto.setBcIds(FluentIterable.from(bcIds).toSet());
        requestDto.setRoles(FluentIterable.from(subjects).transform(new SubjectHelper().toRole()).toSet());
        requestDto.setAuthMatrix(buildAuthMatrix(requestDto.getRoles(), requestDto.getBcIds()));
        requestDto.setCheckedMatrix(Collections.<PurchaserAuthority>emptySet());
        insertLog(supplierId, 0L, info1.getTimes(), type, 1, requestDto);
        return true;
    }

    @Transactional
    public boolean applyForFirstAttempt(Long supplierId, String supplierName, List<Long> bcIds, Integer type) {
        SupplierResourceMaterialInfo info1 = new SupplierResourceMaterialInfo();
        info1.setSupplierId(supplierId);
        info1.setSupplierName(supplierName);
        info1.setApprovedModuleIds(""); // 初始没有通过任何类目
        info1.setNotApprovedModuleIds("");
        info1.setTimes(0L); // 没有提交
        info1.setLastSubmitTime(DateTime.now().toDate());
        info1.setStatus(SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
        checkState(supplierResourceMaterialInfoDao.create(info1) > 0);

        return applyForNormal(supplierId, bcIds, type);
    }

    /**
     * 系统自动提交申请时，若供应商在待审核，将提交的类目加入待审核的类目列表
     * @param supplierId 供应商id
     * @param bcIds 待申请的类目id列表
     * @return 是否提交成功
     */
    @Transactional
    public boolean applyForWithBcsAppend(Long supplierId, List<Long> bcIds) {
        SupplierResourceMaterialInfo info1 = getInfo(supplierId).get();
        info1.setLastSubmitTime(DateTime.now().toDate());
        checkState(supplierResourceMaterialInfoDao.update(info1));
        SupplierResourceMaterialLogRequestDto requestDto = checkNotNull(getLastRequestDto(supplierId, info1.getTimes()));

        Set<Long> willAppendBcIds = FluentIterable.from(bcIds).filter(Predicates.not(Predicates.in(requestDto.getBcIds()))).toSet();
        if (willAppendBcIds.isEmpty()) {
            return false;
        }

        Set<String> roles = FluentIterable.from(requestDto.getRoles()).toSet();
        Set<PurchaserAuthority> matrix = requestDto.getAuthMatrix();
        for (String role : roles) {
            for (Long bcId : willAppendBcIds) {
                for (Integer position : ImmutableList.of(1, 2)) {
                    PurchaserAuthority params = new PurchaserAuthority();
                    params.setType(1);
                    params.setContent(bcId.toString());
                    params.setPosition(position);
                    params.setRole(role);
                    List<PurchaserAuthority> exists = purchaserAuthorityDao.findBy(params);
                    for (PurchaserAuthority exist : exists) {
                        matrix.add(purify(exist));
                    }
                }
            }
        }
        requestDto.getBcIds().addAll(willAppendBcIds);
        requestDto.setAuthMatrix(matrix);
        insertLog(supplierId, 0L, info1.getTimes(), SupplierResourceMaterialLog.Type.SUBMIT_BY_SYSTEM.value(), 1, requestDto);
        return true;
    }

    public Optional<SupplierResourceMaterialSubject> getSubjectById(Long id) {
        return Optional.fromNullable(supplierResourceMaterialSubjectDao.findById(id));
    }

    public List<SupplierResourceMaterialSubject> getAllValidSubjects() {
        return supplierResourceMaterialSubjectDao.findAllValid();
    }

    @Transactional
    public Boolean bulkOverwriteSubjects(List<SupplierResourceMaterialSubject> subjects) {
        Long version = 1L; // 默认1
        for (SupplierResourceMaterialSubject existSubject : getAllValidSubjects()) {
            version = existSubject.getVersion() + 1;
            existSubject.setStatus(0); // 逻辑删除
            supplierResourceMaterialSubjectDao.update(existSubject);
        }
        for (SupplierResourceMaterialSubject subject : subjects) {
            subject.setStatus(1);
            subject.setVersion(version);
            supplierResourceMaterialSubjectDao.create(subject);
        }
        return true;
    }

    public Paging<SupplierResourceMaterialRichInfoDto> pagingBy(
            User user, String supplierName, Integer status, PageInfo pageInfo) {
        SupplierResourceMaterialInfo criteria = new SupplierResourceMaterialInfo();
        criteria.setSupplierName(supplierName);
        criteria.setStatus(status);
        Paging<SupplierResourceMaterialInfo> infoPaging = supplierResourceMaterialInfoDao.pagingBy(
                criteria, pageInfo.getOffset(), pageInfo.getLimit());
        if (infoPaging.getData().isEmpty()) {
            return new Paging<SupplierResourceMaterialRichInfoDto>(infoPaging.getTotal(), Collections.<SupplierResourceMaterialRichInfoDto>emptyList());
        }

        List<SupplierResourceMaterialRichInfoDto> richInfoDtos = Lists.newArrayList();
        for (SupplierResourceMaterialInfo info1 : infoPaging.getData()) {
            SupplierResourceMaterialRichInfoDto richInfoDto = new SupplierResourceMaterialRichInfoDto();
            richInfoDto.setInfo(info1);
            Response<Company> companyResp = companyService.findCompanyById(info1.getSupplierId());
            checkState(companyResp.isSuccess());
            richInfoDto.setUserId(companyResp.getResult().getUserId());
            SupplierResourceMaterialLogRequestDto requestDto = getLastRequestDto(info1.getSupplierId(), info1.getTimes());
            if (notNull(requestDto)) {
                richInfoDto.setApprovingBcs(JSON_MAPPER.toJson(
                        FluentIterable.from(requestDto.getBcIds()).transform(bcHelper.toFull()).toList()));
            }
            switch (SupplierResourceMaterialInfo.Status.from(info1.getStatus())) {
                case SUBMITTED:
                    boolean canDoWithIt = false;
                    if (notNull(requestDto)) {
                        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
                            if (Objects.equal(authority.getUserId(), user.getId()) && Objects.equal(authority.getPosition(), 2)) {
                                boolean checked = false;
                                for (PurchaserAuthority checkedAuth : requestDto.getCheckedMatrix()) {
                                    if (Objects.equal(checkedAuth.getUserId(), user.getId()) && Objects.equal(checkedAuth.getPosition(), 2)) {
                                        if (Objects.equal(authority.getRole(), checkedAuth.getRole()) && Objects.equal(authority.getContent(), checkedAuth.getContent())) {
                                            checked = true;
                                            break;
                                        }
                                    }
                                }
                                if (!checked) {
                                    canDoWithIt = true;
                                    break;
                                }
                            }
                        }
                    }
                    boolean canInvite = false;
                    if (notNull(requestDto)) {
                        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
                            if (Objects.equal(authority.getUserId(), user.getId()) && Objects.equal(authority.getPosition(), 2)) {
                                canInvite = true;
                                break;
                            }
                        }
                    }
                    richInfoDto.setCheckable(canDoWithIt);
                    richInfoDto.setInviteable(canInvite);
                    break;
                case QUALIFIED:
                    boolean canQueryForReject = false;
                    if (notNull(requestDto)) {
                        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
                            if (Objects.equal(authority.getUserId(), user.getId())) {
                                canQueryForReject = true;
                                break;
                            }
                        }
                    }
                    richInfoDto.setCheckable(canQueryForReject);
                    richInfoDto.setInviteable(false);
                    break;
                case QUERY_FOR_REJECTED:
                    boolean canReject = false;
                    if (notNull(requestDto)) {
                        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
                            if (Objects.equal(authority.getUserId(), user.getId()) && Objects.equal(authority.getPosition(), 1)) {
                                canReject = true;
                                break;
                            }
                        }
                    }
                    richInfoDto.setCheckable(canReject);
                    richInfoDto.setInviteable(false);
                    break;
                default:
                    richInfoDto.setCheckable(false);
                    richInfoDto.setInviteable(false);
            }

            richInfoDtos.add(richInfoDto);
        }
        return new Paging<SupplierResourceMaterialRichInfoDto>(infoPaging.getTotal(), richInfoDtos);
    }

    private Predicate<PurchaserAuthority> onlyUserId(final Long userId) {
        return new Predicate<PurchaserAuthority>() {
            @Override
            public boolean apply(PurchaserAuthority input) {
                return Objects.equal(input.getUserId(), userId);
            }
        };
    }

    private Predicate<PurchaserAuthority> inAuth(final Set<PurchaserAuthority> auths) {
        return new Predicate<PurchaserAuthority>() {
            @Override
            public boolean apply(PurchaserAuthority input) {
                for (PurchaserAuthority auth : auths) {
                    if (Objects.equal(auth.getUserId(), input.getUserId()) &&
                            Objects.equal(auth.getRole(), input.getRole()) &&
                            Objects.equal(auth.getPosition(), 2) && // 小微成员
                            Objects.equal(auth.getContent(), input.getContent())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    private Set<PurchaserAuthority> getTrulyAuth(SupplierResourceMaterialLogRequestDto requestDto, Long userId) {
        checkNotNull(requestDto);
        return FluentIterable.from(requestDto.getAuthMatrix())
                .filter(Predicates.and(onlyUserId(userId), Predicates.not(inAuth(requestDto.getCheckedMatrix())))).toSet();
    }

    private boolean isFullChecked(SupplierResourceMaterialLogRequestDto requestDto) {
        for (final Long bcId : requestDto.getBcIds()) {
            for (final String role : requestDto.getRoles()) {
                if (!FluentIterable.from(requestDto.getCheckedMatrix()).anyMatch(new Predicate<PurchaserAuthority>() {
                    @Override
                    public boolean apply(PurchaserAuthority input) {
                        return Objects.equal(input.getContent(), bcId.toString()) &&
                                Objects.equal(input.getRole(), role);
                    }
                })) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isQualified(Long supplierId, Long times) {
        SupplierResourceMaterialLog params = new SupplierResourceMaterialLog();
        params.setSupplierId(supplierId);
        params.setTimes(times);
        params.setType(SupplierResourceMaterialLog.Type.LEVEL1.value());
        for (SupplierResourceMaterialLog log1 : supplierResourceMaterialLogDao.findBy(params)) {
            if (Objects.equal(log1.getStatus(), 0)) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public boolean qualifyByChecker(User user, Long supplierId, SupplierResourceMaterialInfo info1, final List<SupplierResourceMaterialLogRecordDto> recordDtos) {
        final SupplierResourceMaterialLogRequestDto requestDto = getLastRequestDto(info1.getSupplierId(), info1.getTimes());
        checkState(notNull(requestDto));

        // 审查结果写入日志
        for (SupplierResourceMaterialLogRecordDto recordDto : recordDtos) {
            log.debug("qualifyByChecker ... {}", recordDto);
            insertLog(supplierId, user.getId(), info1.getTimes(), 1, recordDto.getStatus(), recordDto);
        }

        Set<PurchaserAuthority> willAuditAuth = getTrulyAuth(requestDto, user.getId());
        requestDto.getCheckedMatrix().addAll(willAuditAuth);

        insertLog(supplierId, user.getId(), info1.getTimes(), 3, 1, requestDto);

        // 每次提交后检查是否审核完

        if (isFullChecked(requestDto)) {
            if (isQualified(supplierId, info1.getTimes())) {
                info1.setStatus(SupplierResourceMaterialInfo.Status.QUALIFIED.value());
                info1.setApprovedModuleIds(Joiners.COMMA.join(FluentIterable.from(Splitters.COMMA.splitToList(info1.getApprovedModuleIds()))
                        .transform(Longs.stringConverter())
                        .append(requestDto.getBcIds())
                        .toSet()));
                supplierResourceMaterialInfoDao.update(info1);
                insertLog(supplierId, 0L, info1.getTimes(), 11, 1, info1);
                return true;
            } else {
                info1.setStatus(SupplierResourceMaterialInfo.Status.QUALIFY_FAILED.value());
                info1.setNotApprovedModuleIds(Joiners.COMMA.join(FluentIterable.from(Splitters.COMMA.splitToList(info1.getNotApprovedModuleIds()))
                        .transform(Longs.stringConverter())
                        .append(requestDto.getBcIds())
                        .toSet()));
                supplierResourceMaterialInfoDao.update(info1);
                insertLog(supplierId, 0L, info1.getTimes(), -11, 0, info1);
                return false;
            }
        }
        return false;
    }

    @Transactional
    public Boolean askForReject(User user, Long supplierId) {
        SupplierResourceMaterialInfo info1 = supplierResourceMaterialInfoDao.findOneBySupplierId(supplierId);
        checkState(notNull(info1));
        // 供应商已通过了才能申请驳回
        checkState(Objects.equal(info1.getStatus(), SupplierResourceMaterialInfo.Status.QUALIFIED.value()));

        info1.setStatus(SupplierResourceMaterialInfo.Status.QUERY_FOR_REJECTED.value());
        supplierResourceMaterialInfoDao.update(info1);
        insertLog(supplierId, user.getId(), info1.getTimes(), 13, 1, info1);
        return true;
    }

    @Transactional
    public Boolean rejectByHigherChecker(User user, Long supplierId) {
        SupplierResourceMaterialInfo info1 = supplierResourceMaterialInfoDao.findOneBySupplierId(supplierId);
        checkState(notNull(info1));
        // 供应商在被提交了(申请修改)了才能驳回
        checkState(Objects.equal(info1.getStatus(), SupplierResourceMaterialInfo.Status.QUERY_FOR_REJECTED.value()));
        SupplierResourceMaterialLogRequestDto requestDto = getLastRequestDto(info1.getSupplierId(), info1.getTimes());
        checkState(notNull(requestDto));

        info1.setApprovedModuleIds(Joiners.COMMA.join(
                FluentIterable.from(Splitters.COMMA.splitToList(info1.getApprovedModuleIds()))
                        .transform(Longs.stringConverter())
                        .filter(Predicates.not(Predicates.in(requestDto.getBcIds())))));
        info1.setStatus(SupplierResourceMaterialInfo.Status.REJECTED.value());
        supplierResourceMaterialInfoDao.update(info1);
        insertLog(supplierId, user.getId(), info1.getTimes(), -13, 0, info1);
        return true;
    }

    private SupplierResourceMaterialLogRequestDto getLastRequestDto(Long supplierId, Long times) {
        SupplierResourceMaterialLog params = new SupplierResourceMaterialLog();
        params.setSupplierId(supplierId);
        params.setTimes(times);
        Optional<SupplierResourceMaterialLog> existLog = FluentIterable.from(supplierResourceMaterialLogDao.findBy(params))
                .firstMatch(new Predicate<SupplierResourceMaterialLog>() {
                    @Override
                    public boolean apply(SupplierResourceMaterialLog input) {
                        return ImmutableList.of(3, 5, 6, 20).contains(input.getType());
                    }
                });
        if (!existLog.isPresent()) {
            return null;
        }
        return JSON_MAPPER.fromJson(existLog.get().getContent(),
                JSON_MAPPER.createCollectionType(SupplierResourceMaterialLogRequestDto.class));
    }

    public Optional<SupplierResourceMaterialLogRequestDto> getProperLastRequestDto(Long supplierId, Long times) {
        checkArgument(supplierId > 0 && times > 0);
        return Optional.fromNullable(getLastRequestDto(supplierId, times));
    }

    @Transactional
    public Boolean inviteAnotherToCheck(User user, User anotherUser, String role, Long supplierId) {
        SupplierResourceMaterialInfo info1 = supplierResourceMaterialInfoDao.findOneBySupplierId(supplierId);
        checkState(notNull(info1));
        // 供应商提交了才能检查
        checkState(Objects.equal(info1.getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value()));
        SupplierResourceMaterialLogRequestDto requestDto = getLastRequestDto(info1.getSupplierId(), info1.getTimes());
        checkState(notNull(requestDto));

        Response<List<Long>> authorityResp = purchaserAuthorityService.getAuthorizedBcIds(anotherUser.getId(), 2, role);
        checkState(authorityResp.isSuccess());

        if (authorityResp.getResult().isEmpty()) {
            throw new RuntimeException("no permission");
        }

        Set<PurchaserAuthority> authorities = Sets.newHashSet();
        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
            if (Objects.equal(authority.getUserId(), user.getId()) && Objects.equal(authority.getPosition(), 2)) {
                PurchaserAuthority neoAuth = new PurchaserAuthority();
                neoAuth.setUserId(anotherUser.getId());
                neoAuth.setType(1);
                neoAuth.setContent(authority.getContent());
                neoAuth.setRole(role);
                authorities.add(neoAuth);
            }
        }
        authorities.addAll(requestDto.getAuthMatrix());
        requestDto.setAuthMatrix(authorities);

        insertLog(supplierId, user.getId(), info1.getTimes(), 20, 1, requestDto);
        return true;
    }

    @Transactional
    public Boolean bulkInviteAnotherToCheck(User invitor, List<User> invitees, String role, Long supplierId) {
        for (User invitee : invitees) {
            inviteAnotherToCheck(invitor, invitee, role, supplierId);
        }
        return true;
    }

    public Set<Long> findSuppliersBetween(Date startAt, Date endAt) {
        return FluentIterable
                .from(supplierResourceMaterialInfoDao.findBy(
                        SupplierResourceMaterialInfo.Status.SUBMITTED.value(), startAt, endAt))
                .transform(new Function<SupplierResourceMaterialInfo, Long>() {
                    @Override
                    public Long apply(SupplierResourceMaterialInfo input) {
                        return input.getSupplierId();
                    }
                }).toSet();
    }

    @Transactional
    public boolean failSupplier(Long supplierId) {
        Optional<SupplierResourceMaterialInfo> infoOpt = getInfo(supplierId);
        checkState(infoOpt.isPresent());
        SupplierResourceMaterialInfo info1 = infoOpt.get();
        checkState(Objects.equal(info1.getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value()));
        info1.setStatus(SupplierResourceMaterialInfo.Status.QUALIFY_FAILED.value());
        supplierResourceMaterialInfoDao.update(info1);
        insertLog(supplierId, 0L, info1.getTimes(), SupplierResourceMaterialLog.Type.FAIL_BY_SYSTEM.value(), 1, info1);
        return true;
    }

    public Set<Long> getRevolvedCheckerIds(Long supplierId) {
        Set<Long> checkerIds = Sets.newHashSet();
        Optional<SupplierResourceMaterialInfo> infoOpt = getInfo(supplierId);
        try {
            checkState(infoOpt.isPresent());
            checkState(Objects.equal(infoOpt.get().getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value()));
            SupplierResourceMaterialLogRequestDto requestDto = checkNotNull(getLastRequestDto(supplierId, infoOpt.get().getTimes()));

            for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
                if (!checkerIds.contains(authority.getUserId())) {
                    checkerIds.add(authority.getUserId());
                }
            }
        } catch (Exception e) {
            log.warn("one supplier(id={}) can found his revolved checker", supplierId);
        }
        return checkerIds;
    }

    @Transactional
    public boolean forceApprove(Long checkerId, Long supplierId, String supplierName, List<Long> bcIds) {
        SupplierResourceMaterialInfo info1 = supplierResourceMaterialInfoDao.findOneBySupplierId(supplierId);
        if (isNull(info1)) {
            info1 = new SupplierResourceMaterialInfo();
            info1.setSupplierId(supplierId);
            info1.setSupplierName(supplierName);
            info1.setApprovedModuleIds(Joiners.COMMA.join(bcIds));
            info1.setNotApprovedModuleIds("");
            info1.setTimes(1L); // 第一次提交
            info1.setLastSubmitTime(DateTime.now().toDate());
            info1.setStatus(SupplierResourceMaterialInfo.Status.FORCE_QUALIFIED.value());
            checkState(supplierResourceMaterialInfoDao.create(info1) > 0);
        } else {
            info1.setApprovedModuleIds(Joiners.COMMA.join(FluentIterable.from(Splitters.COMMA.splitToList(info1.getApprovedModuleIds()))
                    .transform(Longs.stringConverter()).append(bcIds).toSet()));
            checkState(supplierResourceMaterialInfoDao.update(info1));
        }
        insertLog(supplierId, MoreObjects.firstNonNull(checkerId, 0L), info1.getTimes(),
                SupplierResourceMaterialLog.Type.FORCE_APPROVE.value(), 1, info1);
        return true;
    }

    @Transactional
    public void inviteAnotherToCheckWithBcsGiving(Long checkerId, Long anotherCheckerId, String role, List<Long> bcIds, Long supplierId) {
        Long times = getInfo(supplierId).get().getTimes();
        SupplierResourceMaterialLogRequestDto requestDto = getProperLastRequestDto(supplierId, times).get();
        List<PurchaserAuthority> authorities = Lists.newArrayList();
        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
            if (Objects.equal(authority.getUserId(), checkerId) && bcIds.contains(Longs.tryParse(authority.getContent())) &&
                    Objects.equal(authority.getRole(), role) && Objects.equal(authority.getPosition(), 2)) {
                // TODO: filter checkedAuth or refactor
                PurchaserAuthority neoAuth = new PurchaserAuthority();
                neoAuth.setUserId(anotherCheckerId);
                neoAuth.setType(1);
                neoAuth.setContent(authority.getContent());
                neoAuth.setRole(authority.getRole());
                neoAuth.setPosition(2);
                authorities.add(neoAuth);
            } else {
                authorities.add(authority);
            }
        }
        requestDto.setAuthMatrix(FluentIterable.from(authorities).toSet());
        insertLog(supplierId, checkerId, times, SupplierResourceMaterialLog.Type.INVITE_ANOTHER.value(), 1, requestDto);
    }

    @Transactional
    public void inviteAnotherToCheckWithBcsAdding(Long checkerId, Long anotherCheckerId, String role, List<Long> bcIds, Long supplierId) {
        Long times = getInfo(supplierId).get().getTimes();
        SupplierResourceMaterialLogRequestDto requestDto = getProperLastRequestDto(supplierId, times).get();
        List<PurchaserAuthority> authorities = Lists.newArrayList();
        for (PurchaserAuthority authority : requestDto.getAuthMatrix()) {
            if (bcIds.contains(Longs.tryParse(authority.getContent())) &&
                    Objects.equal(authority.getRole(), role) && Objects.equal(authority.getPosition(), 2)) {
                // TODO: filter checkedAuth or refactor
                PurchaserAuthority neoAuth = new PurchaserAuthority();
                neoAuth.setUserId(anotherCheckerId);
                neoAuth.setType(1);
                neoAuth.setContent(authority.getContent());
                neoAuth.setRole(authority.getRole());
                neoAuth.setPosition(2);
                authorities.add(neoAuth);
            } else {
                authorities.add(authority);
            }
        }
        requestDto.setAuthMatrix(FluentIterable.from(authorities).toSet());
        insertLog(supplierId, checkerId, times, SupplierResourceMaterialLog.Type.INVITE_ANOTHER.value(), 1, requestDto);
    }

    public List<SupplierResourceMaterialLogRichRecordDto> getQualifiedSubjects(Long userId, Long supplierId, Long times) {
        SupplierResourceMaterialLog params = new SupplierResourceMaterialLog();
        params.setSupplierId(supplierId);
        params.setCheckerId(userId);
        params.setTimes(times);
        params.setType(SupplierResourceMaterialLog.Type.LEVEL1.value());
        List<SupplierResourceMaterialLog> logs = supplierResourceMaterialLogDao.findBy(params);

        List<SupplierResourceMaterialLogRichRecordDto> result = Lists.newArrayList();
        for (SupplierResourceMaterialLog log1 : logs) {
            SupplierResourceMaterialLogRecordDto recordDto = JSON_MAPPER.fromJson(log1.getContent(), SupplierResourceMaterialLogRecordDto.class);
            SupplierResourceMaterialSubject subject = supplierResourceMaterialSubjectDao.findById(recordDto.getSubjectId());
            if (isNull(recordDto) || isNull(subject)) {
                continue;
            }
            SupplierResourceMaterialLogRichRecordDto richRecordDto = new SupplierResourceMaterialLogRichRecordDto();
            richRecordDto.setSubject(subject);
            richRecordDto.setRecord(recordDto);
            result.add(richRecordDto);
        }
        return result;
    }

    private class SubjectHelper {

        public Function<SupplierResourceMaterialSubject, Long> toId() {
            return new Function<SupplierResourceMaterialSubject, Long>() {
                @Override
                public Long apply(SupplierResourceMaterialSubject subject) {
                    return subject.getId();
                }
            };
        }

        public Function<SupplierResourceMaterialSubject, String> toRole() {
            return new Function<SupplierResourceMaterialSubject, String>() {
                @Override
                public String apply(SupplierResourceMaterialSubject subject) {
                    return subject.getRole();
                }
            };
        }

        public Function<Long, SupplierResourceMaterialSubject> toFull() {
            return new Function<Long, SupplierResourceMaterialSubject>() {
                @Override
                public SupplierResourceMaterialSubject apply(Long subjectId) {
                    return supplierResourceMaterialSubjectDao.findById(subjectId);
                }
            };
        }
    }
}
