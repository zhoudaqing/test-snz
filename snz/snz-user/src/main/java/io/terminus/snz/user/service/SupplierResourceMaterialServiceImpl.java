package io.terminus.snz.user.service;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.primitives.Longs;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MailService;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRequestDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRichRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialRichInfoDto;
import io.terminus.snz.user.manager.SupplierResourceMaterialManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.BcHelper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
@Slf4j
@Service
public class SupplierResourceMaterialServiceImpl implements SupplierResourceMaterialService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    private static final JavaType LOG_RECORD_DTO = JSON_MAPPER.createCollectionType(List.class, SupplierResourceMaterialLogRecordDto.class);

    private static final JavaType BC_LIST = JSON_MAPPER.createCollectionType(List.class, BackendCategory.class);

    @Autowired
    private SupplierResourceMaterialManager supplierResourceMaterialManager;

    @Autowired
    private AccountService<User> accountService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BackendCategoryService backendCategoryService;

    @Autowired
    private BcHelper bcHelper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MailService mailService;

    @Override
    public Response<SupplierResourceMaterialInfo> getInfo(BaseUser baseUser) {
        Response<SupplierResourceMaterialInfo> result = new Response<SupplierResourceMaterialInfo>();
        if (isNull(baseUser)) {
            log.error("must login first", baseUser);
            result.setError("user.not.login");
            return result;
        }
        return getInfoByUserId(baseUser.getId());
    }

    @Override
    public Response<SupplierResourceMaterialInfo> getInfoByUserId(Long userId) {
        Response<SupplierResourceMaterialInfo> result = new Response<SupplierResourceMaterialInfo>();
        try {
            checkArgument(userId > 0);
            Response<Company> companyResp = companyService.findCompanyByUserId(userId);
            if (!companyResp.isSuccess()) {
                result.setError(companyResp.getError());
                return result;
            }
            Long supplierId = companyResp.getResult().getId();
            result.setResult(supplierResourceMaterialManager.getInfo(supplierId)
                    .or(genNonInfo(supplierId)));
        } catch (Exception e) {
            log.error("getInfoByUserId(userId={}) failed, cause:{}", userId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<Integer> getInfoInBcIds(Long supplierId, List<Long> bcIds) {
        Response<Integer> result = new Response<Integer>();
        try {
            checkArgument(supplierId > 0);
            checkArgument(!bcIds.isEmpty());
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (infoOpt.isPresent()) {
                Set<Long> approvedBcIds = FluentIterable.from(Splitters.COMMA.splitToList(infoOpt.get().getApprovedModuleIds()))
                        .transform(Longs.stringConverter()).toSet();
                Set<Long> notApprovedBcIds = FluentIterable.from(Splitters.COMMA.splitToList(infoOpt.get().getNotApprovedModuleIds()))
                        .transform(Longs.stringConverter()).toSet();

                // 全部通过
                if (FluentIterable.from(bcIds).allMatch(Predicates.in(approvedBcIds))) {
                    result.setResult(SupplierResourceMaterialInfo.Status.QUALIFIED.value());
                } else {
                    Optional<SupplierResourceMaterialLogRequestDto> requestDtoOpt =
                            supplierResourceMaterialManager.getProperLastRequestDto(supplierId, infoOpt.get().getTimes());

                    Set<Long> approvingBcIds;
                    if (!requestDtoOpt.isPresent()) {
                        log.error("no request for qualify found, supplierId={}, set approvingModuleIds as empty set", supplierId);
                        approvingBcIds = Collections.emptySet();
                    } else {
                        approvingBcIds = requestDtoOpt.get().getBcIds();
                    }
                    // 存在一个等待审核中
                    if (FluentIterable.from(notApprovedBcIds)
                            .anyMatch(Predicates.in(approvingBcIds))) {
                        result.setResult(SupplierResourceMaterialInfo.Status.SUBMITTED.value());
                    } else if (FluentIterable.from(bcIds)
                            .anyMatch(Predicates.not(Predicates.in(notApprovedBcIds)))) {
                        // 存在一个不在不通过列表中，即为未提交
                        result.setResult(SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
                    } else {
                        result.setResult(SupplierResourceMaterialInfo.Status.QUALIFY_FAILED.value());
                    }
                }
            } else {
                // 供应商尚未提交，默认为未提交
                result.setResult(SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
            }
        } catch (Exception e) {
            log.error("getInfoInBcIds(supplierId={}, bcIds={}) failed, cause:{}",
                    supplierId, bcIds, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<Long>> getApprovedBcIds(Long supplierId) {
        Response<List<Long>> result = new Response<List<Long>>();
        try {
            checkArgument(supplierId > 0);
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (infoOpt.isPresent()) {
                result.setResult(Lists.transform(Splitters.COMMA.splitToList(infoOpt.get().getApprovedModuleIds()), Longs.stringConverter()));
            } else {
                result.setResult(Collections.<Long>emptyList());
            }
        } catch (Exception e) {
            log.error("getApprovedBcIds(supplierId={}) failed, cause:{}",
                    supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<Map<Long, List<Long>>> bulkGetApprovedBcIds(List<Long> supplierIds) {
        Response<Map<Long, List<Long>>> result = new Response<Map<Long, List<Long>>>();
        try {
            checkArgument(!supplierIds.isEmpty());
            Map<Long, List<Long>> mBcIds = Maps.newHashMap();
            for (Long supplierId : supplierIds) {
                Response<List<Long>> bcIdsResp = getApprovedBcIds(supplierId);
                if (!bcIdsResp.isSuccess()) {
                    result.setError(bcIdsResp.getError());
                    return result;
                }
                mBcIds.put(supplierId, bcIdsResp.getResult());
            }
            result.setResult(mBcIds);
        } catch (Exception e) {
            log.error("bulkGetApprovedBcIds(supplierIds={}) failed, cause:{}",
                    supplierIds, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> forceApprove(Long checkerId, Long supplierId, List<Long> bcIds) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            Response<Company> companyResp = companyService.findCompanyById(supplierId);
            if (!companyResp.isSuccess()) {
                result.setError(companyResp.getError());
                return result;
            }
            supplierResourceMaterialManager.forceApprove(
                    MoreObjects.firstNonNull(checkerId, 0L), supplierId, companyResp.getResult().getCorporation(), bcIds);
            Response<Boolean> approvedHookResp = approvedHook(supplierId);
            if (!approvedHookResp.isSuccess()) {
                result.setError(approvedHookResp.getError());
                return result;
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("forceApprove(checkerId={}, supplierId={}, bcIds={}) failed, cause:{}",
                    checkerId, supplierId, bcIds, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.force.approve.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> forceApproveAll(Long checkerId, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            Response<Company> companyResp = companyService.findCompanyById(supplierId);
            if (!companyResp.isSuccess()) {
                result.setError(companyResp.getError());
                return result;
            }
            Response<List<BackendCategory>> bcsResp = backendCategoryService.findByLevels(3);
            if (!bcsResp.isSuccess()) {
                result.setError(bcsResp.getError());
                return result;
            }
            List<Long> bcIds = Lists.transform(bcsResp.getResult(), bcHelper.toIds());
            supplierResourceMaterialManager.forceApprove(
                    MoreObjects.firstNonNull(checkerId, 0L), supplierId, companyResp.getResult().getCorporation(), bcIds);
            Response<Boolean> approvedHookResp = approvedHook(supplierId);
            if (!approvedHookResp.isSuccess()) {
                result.setError(approvedHookResp.getError());
                return result;
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("forceApproveAll(checkerId={}, supplierId={}) failed, cause:{}",
                    checkerId, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.force.approve.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> applyFor(Long supplierId, String modules, Integer type) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            checkArgument(supplierId > 0);
            checkArgument(type == 5 || type == 6); // 5. 供应商主动提交, 6. 系统自动提交
            List<BackendCategory> bcs = JSON_MAPPER.fromJson(modules, BC_LIST);
            List<Long> bcIds = Lists.transform(bcs, new Function<BackendCategory, Long>() {
                @Override
                public Long apply(BackendCategory input) {
                    return input.getId();
                }
            });
            if (isNull(bcIds) || bcIds.isEmpty()) {
                result.setError("supplier.resource.material.apply.module.not.null");
                return result;
            }
            Response<Company> companyResp = companyService.findCompanyById(supplierId);
            if (!companyResp.isSuccess()) {
                log.error("no company found");
                result.setError(companyResp.getError());
                return result;
            }
            Response<Boolean> isCompleteResp = companyService.isComplete(companyResp.getResult().getUserId());
            if (!isCompleteResp.isSuccess()) {
                log.error("supplier must complete");
                result.setError(isCompleteResp.getError());
                return result;
            }
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (!infoOpt.isPresent()) {
                log.debug("user not submit a qualify apply");
                supplierResourceMaterialManager.applyForFirstAttempt(supplierId, companyResp.getResult().getCorporation(), bcIds, type);
            } else {
                if (infoOpt.get().getTimes() <= 0) {
                    log.error("illegal state");
                    result.setError("supplier.resource.material.apply.illegal.state");
                    return result;
                }
                if (Objects.equal(infoOpt.get().getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value())) {
                    if (Objects.equal(type, SupplierResourceMaterialLog.Type.SUBMIT_BY_SUPPLIER.value())) {
                        log.error("can not submitted when auditing");
                        result.setError("supplier.resource.material.apply.can.not.submit");
                        return result;
                    }
                    // 系统提交时
                    supplierResourceMaterialManager.applyForWithBcsAppend(supplierId, bcIds);
                } else {
                    Response<List<Long>> moduleResp = getApprovedBcIds(supplierId);
                    if (!moduleResp.isSuccess()) {
                        log.error("approvedModules find failed");
                        result.setError(moduleResp.getError());
                        return result;
                    }

                    // 真正应该提交的后台类目id
                    Collection<Long> willAuditModuleIds = FluentIterable.from(bcIds)
                            .filter(Predicates.not(Predicates.in(moduleResp.getResult()))).toSet();

                    if (willAuditModuleIds.isEmpty()) {
                        log.error("truly audit module ids can not be null");
                        result.setError("supplier.resource.material.apply.module.truly.null");
                        return result;
                    }

                    supplierResourceMaterialManager.applyForNormal(supplierId, bcIds, type);
                }
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("applyFor(supplierId={}, modules={}, type={}) failed, cause:{}",
                    supplierId, modules, type, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.apply.fail");
        }
        return result;
    }

    @Override
    public Response<List<SupplierResourceMaterialSubject>> getSubjectsNeedQualify(BaseUser baseUser, Long supplierId) {
        Response<List<SupplierResourceMaterialSubject>> result = new Response<List<SupplierResourceMaterialSubject>>();
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("company.not.found");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            Long userId = userResp.getResult().getId();
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            Optional<SupplierResourceMaterialLogRequestDto> requestDtoOpt = supplierResourceMaterialManager.getProperLastRequestDto(supplierId, infoOpt.get().getTimes());
            Set<String> roles = Sets.newHashSet();
            for (PurchaserAuthority authority : requestDtoOpt.get().getAuthMatrix()) {
                if (Objects.equal(authority.getUserId(), userId) && Objects.equal(authority.getPosition(), 2)) {
                    boolean checked = false;
                    for (PurchaserAuthority checkedAuth : requestDtoOpt.get().getCheckedMatrix()) {
                        if (Objects.equal(checkedAuth.getUserId(), userId) && Objects.equal(checkedAuth.getPosition(), 2)) {
                            if (Objects.equal(authority.getRole(), checkedAuth.getRole()) && Objects.equal(authority.getContent(), checkedAuth.getContent())) {
                                checked = true;
                                break;
                            }
                        }
                    }
                    if (!checked) {
                        roles.add(authority.getRole());
                    }
                }
            }
            List<SupplierResourceMaterialSubject> subjects = Lists.newArrayList();
            for (Long subjectId : requestDtoOpt.get().getSubjectIds()) {
                SupplierResourceMaterialSubject subject = supplierResourceMaterialManager.getSubjectById(subjectId).get();
                if (roles.contains(subject.getRole())) {
                    subjects.add(subject);
                }
            }
            result.setResult(subjects);
        } catch (Exception e) {
            log.error("getSubjectsNeedQualify(baseUser={}, supplierId={}) failed, cause:{}",
                    baseUser, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<List<SupplierResourceMaterialLogRichRecordDto>> getQualifiedSubjects(BaseUser baseUser, Long supplierId) {
        Response<List<SupplierResourceMaterialLogRichRecordDto>> result = new Response<List<SupplierResourceMaterialLogRichRecordDto>>();
        if (isNull(baseUser)) {
            log.error("checker must login first");
            result.setError("user.not.login");
            return result;
        }
        try {
            Long userId = baseUser.getId();
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (!infoOpt.isPresent()) {
                log.error("not found supplier(id={})'s qualify info", supplierId);
                result.setError("supplier.resource.material.query.fail");
                return result;
            }
            Long times = checkNotNull(infoOpt.get().getTimes());
            result.setResult(supplierResourceMaterialManager.getQualifiedSubjects(userId, supplierId, times));
        } catch (Exception e) {
            log.error("get qualified subjects failed, baseUser={}, supplierId={}, cause:{}",
                    baseUser, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> qualifyByChecker(BaseUser baseUser, String details) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(baseUser)) {
            log.error("qualifyByChecker(baseUser={}, details={}) failed, must login first", baseUser, details);
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("qualifyByChecker(baseUser={}, details={}) failed, user not found", baseUser, details);
            result.setError(userResp.getError());
            return result;
        }
        try {
            final List<SupplierResourceMaterialLogRecordDto> recordDtos = JSON_MAPPER.fromJson(details, LOG_RECORD_DTO);
            checkState(notNull(recordDtos) && !recordDtos.isEmpty());
            Long supplierId = recordDtos.get(0).getSupplierId();
            log.debug("recordDtos={}, supplierId={}", recordDtos, supplierId);
            // 检查是否所有必填项都有
            Response<List<SupplierResourceMaterialSubject>> allSubjectNeedCheckResp = getSubjectsNeedQualify(baseUser, supplierId);
            if (!allSubjectNeedCheckResp.isSuccess()) {
                result.setError(allSubjectNeedCheckResp.getError());
                return result;
            }
            boolean doesMandatory = FluentIterable.from(allSubjectNeedCheckResp.getResult())
                    .filter(new Predicate<SupplierResourceMaterialSubject>() {
                        @Override
                        public boolean apply(SupplierResourceMaterialSubject input) {
                            return Objects.equal(input.getType(), 1); // 必填项
                        }
                    })
                    .allMatch(new Predicate<SupplierResourceMaterialSubject>() {
                        @Override
                        public boolean apply(SupplierResourceMaterialSubject input) {
                            return transformRecordsToSubjectIds(recordDtos).contains(input.getId());
                        }
                    });

            if (!doesMandatory) {
                log.error("qualifyByChecker(baseUser={}, details={}) failed, some subject no submit", baseUser, details);
                result.setError("supplier.resource.material.apply.exist.no.check");
                return result;
            }
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            // 只有已提交审核的才能去评价
            if (!infoOpt.isPresent() || !Objects.equal(infoOpt.get().getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value())) {
                log.error("qualifyByChecker(baseUser={}, details={}) failed, only supplier in SUBMITTED status allowed");
                result.setError("supplier.resource.material.no.apply");
                return result;
            }
            boolean isPassed = supplierResourceMaterialManager.qualifyByChecker(userResp.getResult(), supplierId, infoOpt.get(), recordDtos);
            if (isPassed) {
                Response<Boolean> approvedHookResp = approvedHook(supplierId);
                if (!approvedHookResp.isSuccess()) {
                    result.setError(approvedHookResp.getError());
                    return result;
                }
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("qualifyByChecker(baseUser={}, details={}) failed, cause:{}",
                    baseUser, details, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.check.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> askForReject(BaseUser baseUser, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("company.not.found");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            result.setResult(supplierResourceMaterialManager.askForReject(userResp.getResult(), supplierId));
        } catch (Exception e) {
            log.error("askForReject(baseUser={}, supplierId={}) failed, cause:{}",
                    baseUser, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.ask.reject.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> rejectByHigherChecker(BaseUser baseUser, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("company.not.found");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            result.setResult(supplierResourceMaterialManager.rejectByHigherChecker(userResp.getResult(), supplierId));
        } catch (Exception e) {
            log.error("rejectByHigherChecker(baseUser={}, supplierId={}) failed, cause:{}",
                    baseUser, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.reject.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> inviteAnotherToCheck(BaseUser baseUser, String nick, String role, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("must specify another checker");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        if (Objects.equal(baseUser.getNickName(), nick)) {
            log.error("invitor and invitee are same");
            result.setError("illegal.params");
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        Response<User> anotherUserResp = accountService.findUserByNick(nick);
        if (!anotherUserResp.isSuccess()) {
            log.error("another user not found");
            result.setError(anotherUserResp.getError());
        }
        try {
            result.setResult(supplierResourceMaterialManager.inviteAnotherToCheck(
                    userResp.getResult(), anotherUserResp.getResult(), role, supplierId));
        } catch (Exception e) {
            log.error("inviteAnotherToCheck(baseUser={}, nick={}, supplierId={}) failed, cause:{}",
                    baseUser, nick, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.invite.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> bulkInviteAnotherToCheck(BaseUser baseUser, String nicks, String role, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        if (Strings.isNullOrEmpty(nicks)) {
            log.error("must specify another checkers");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            List<User> users = Lists.newArrayList();
            for (String nick : Splitters.COMMA.splitToList(nicks)) {
                Response<User> anotherUserResp = accountService.findUserByNick(nick);
                if (anotherUserResp.isSuccess()) {
                    users.add(anotherUserResp.getResult());
                } else {
                    log.warn("bulkInviteAnotherToCheck ... one user not found: {}", nick);
                }
            }
            if (users.isEmpty()) {
                log.error("bulkInviteAnotherToCheck ... no invitees specified");
                result.setError("supplier.resource.material.invite.fail");
                return result;
            }
            log.debug("bulkInviteAnotherToCheck ... invitees={}", users);
            result.setResult(supplierResourceMaterialManager.bulkInviteAnotherToCheck(
                    userResp.getResult(), users, role, supplierId));
        } catch (Exception e) {
            log.error("bulkInviteAnotherToCheck(baseUser={}, nicks={}, supplierId={}) failed, cause:{}",
                    baseUser, nicks, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.invite.fail");
        }
        return result;
    }

    @Override
    public Response<List<BackendCategory>> getBcsCanInvite(BaseUser baseUser, Long supplierId) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        try {
            Long userId = baseUser.getId();
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (!infoOpt.isPresent()) {
                log.error("supplier has not submit a request");
                result.setError("supplier.resource.material.no.apply");
                return result;
            }
            Optional<SupplierResourceMaterialLogRequestDto> requestDtoOpt = supplierResourceMaterialManager.getProperLastRequestDto(supplierId, infoOpt.get().getTimes());
            if (!requestDtoOpt.isPresent()) {
                log.error("supplier has not submit a request");
                result.setError("supplier.resource.material.no.apply");
                return result;
            }
            Set<Long> bcIds = Sets.newHashSet();
            for (PurchaserAuthority authority : requestDtoOpt.get().getAuthMatrix()) {
                if (!Objects.equal(authority.getPosition(), 2)) {
                    continue;
                }
                if (!Objects.equal(authority.getUserId(), userId)) {
                    continue;
                }
                boolean isChecked = false;
                for (PurchaserAuthority checkedAuth : requestDtoOpt.get().getCheckedMatrix()) {
                    if (!Objects.equal(checkedAuth.getPosition(), 2)) {
                        continue;
                    }
                    if (!Objects.equal(checkedAuth.getUserId(), userId)) {
                        continue;
                    }
                    // same role
                    if (Objects.equal(checkedAuth.getRole(), authority.getRole()) && Objects.equal(checkedAuth.getContent(), authority.getContent())) {
                        isChecked = true;
                        break;
                    }
                }
                if (!isChecked) {
                    bcIds.add(Longs.tryParse(authority.getContent()));
                }
            }
            result.setResult(FluentIterable.from(bcIds).transform(bcHelper.toFull()).toList());
        } catch (Exception e) {
            log.error("getBcsCanInvite(baseUser={}, supplierId={}) failed, cause:{}",
                    baseUser, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    // TODO: to be refactored
    private String getRole(Long userId, Set<PurchaserAuthority> authMatrix) {
        for (PurchaserAuthority authority : authMatrix) {
            if (Objects.equal(authority.getUserId(), userId)) {
                return authority.getRole();
            }
        }
        return null;
    }

    @Override
    public Response<Boolean> inviteAnotherToCheckWithBcs(BaseUser baseUser, String nick, String role, List<Long> bcIds, Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("must specify another checker");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(supplierId)) {
            log.error("must specify a supplier");
            result.setError("illegal.params");
            return result;
        }
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        if (Objects.equal(baseUser.getNickName(), nick)) {
            log.error("invitor and invitee are same");
            result.setError("illegal.params");
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        Response<User> anotherUserResp = accountService.findUserByNick(nick);
        if (!anotherUserResp.isSuccess()) {
            log.error("another user not found");
            result.setError(anotherUserResp.getError());
            return result;
        }
        try {
            Response<List<BackendCategory>> bcsCanInviteResp = getBcsCanInvite(baseUser, supplierId);
            if (!bcsCanInviteResp.isSuccess()) {
                log.error("get bcIds can invite fail");
                result.setError(bcsCanInviteResp.getError());
                return result;
            }
            List<Long> bcIdsCanInvite = FluentIterable.from(bcsCanInviteResp.getResult()).transform(bcHelper.toIds()).toList();
            if (!bcIdsCanInvite.containsAll(bcIds)) {
                log.error("no permission to invite, can invite: ({}), query: ({})", bcIdsCanInvite, bcIds);
                result.setError("supplier.resource.material.invite.no.permission");
                return result;
            }
            Optional<SupplierResourceMaterialInfo> infoOpt = supplierResourceMaterialManager.getInfo(supplierId);
            if (!infoOpt.isPresent()) {
                log.error("supplier has not submit a request");
                result.setError("supplier.resource.material.no.apply");
                return result;
            }
            Optional<SupplierResourceMaterialLogRequestDto> requestDtoOpt = supplierResourceMaterialManager.getProperLastRequestDto(supplierId, infoOpt.get().getTimes());
            if (!requestDtoOpt.isPresent()) {
                log.error("supplier has not submit a request");
                result.setError("supplier.resource.material.no.apply");
                return result;
            }
            String havingRole = checkNotNull(getRole(userResp.getResult().getId(), requestDtoOpt.get().getAuthMatrix()));
            if (Objects.equal(havingRole, "resource")) {
                if (Objects.equal(havingRole, role)) {
                    supplierResourceMaterialManager.inviteAnotherToCheckWithBcsGiving(userResp.getResult().getId(), anotherUserResp.getResult().getId(), role, bcIds, supplierId);
                } else {
                    supplierResourceMaterialManager.inviteAnotherToCheckWithBcsAdding(userResp.getResult().getId(), anotherUserResp.getResult().getId(), role, bcIds, supplierId);
                }
            } else {
                if (Objects.equal(havingRole, role)) {
                    supplierResourceMaterialManager.inviteAnotherToCheckWithBcsGiving(userResp.getResult().getId(), anotherUserResp.getResult().getId(), role, bcIds, supplierId);
                } else {
                    log.error("only resource can invite other role, you only have {}", havingRole);
                    result.setError("supplier.resource.material.invite.no.permission");
                    return result;
                }
            }
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("inviteAnotherToCheckWithBcs(baseUser={}, nick={}, role={}, bcIds={}, supplierId={} failed, cause:{}",
                    baseUser, nick, role, bcIds, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.invite.fail");
        }
        return result;
    }

    @Override
    public Response<Paging<SupplierResourceMaterialRichInfoDto>> pagingBy(BaseUser baseUser, String supplierName, Integer status, Integer pageNo, Integer size) {
        Response<Paging<SupplierResourceMaterialRichInfoDto>> result = new Response<Paging<SupplierResourceMaterialRichInfoDto>>();
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            result.setResult(supplierResourceMaterialManager.pagingBy(
                    userResp.getResult(), supplierName, status, new PageInfo(pageNo, size)));
        } catch (Exception e) {
            log.error("pagingByStatus(baseUser={}, supplierName={}, status={}, pageNo={}, size={}) failed, cause:{}",
                    baseUser, supplierName, status, pageNo, size, Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> bulkSendMessages() {
        Response<Boolean> result = new Response<Boolean>();
        try {
            // 10天到8天的发站内信
            Set<Long> supplierIds = supplierResourceMaterialManager.findSuppliersBetween(
                    DateTime.now().minusDays(10).toDate(), DateTime.now().minusDays(8).toDate());
            List<Mail<?>> mails = Lists.newArrayList();
            for (Long supplierId : supplierIds) {
                Response<Company> companyResp = companyService.findCompanyById(supplierId);
                if (!companyResp.isSuccess()) {
                    log.warn("find supplier fail by id={}", supplierId);
                    continue;
                }
                Set<Long> checkerIds = supplierResourceMaterialManager.getRevolvedCheckerIds(supplierId);
                if (checkerIds.isEmpty()) {
                    continue;
                }
                Response<Boolean> messageResp = messageService.push(Message.Type.SUPPLIER_QUALIFY_DEADLINE,
                        0L /* TODO: is 0L proper? */, FluentIterable.from(checkerIds).toList(),
                        ImmutableMap.of(
                                "supplierId", supplierId,
                                "supplierName", companyResp.getResult().getCorporation()));
                if (!messageResp.isSuccess()) {
                    log.warn("message push fail for supplier(id={})", supplierId);
                }
                for (Long checkerId : checkerIds) {
                    Response<User> checkerResp = accountService.findUserById(checkerId);
                    if (!checkerResp.isSuccess()) {
                        continue;
                    }
                    Mail<Map<String, Object>> mail = new Mail<Map<String, Object>>();
                    mail.setType(Mail.Type.SUPPLIER_QUALIFY_DEADLINE);
                    mail.setTo(checkerResp.getResult().getEmail());
                    mail.setData(ImmutableMap.<String, Object>of(
                            "name", checkerResp.getResult().getName(),
                            "supplierName", companyResp.getResult().getCorporation()));
                }
            }
            mailService.batchSend(mails);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("bulkSendMessages failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.message.send.fail");
        }
        return result;
    }

    private boolean safeFailSupplier(Long supplierId) {
        try {
            return supplierResourceMaterialManager.failSupplier(supplierId);
        } catch (Exception e) {
            log.warn("fail this supplier(id={}) failed", supplierId);
            return false;
        }
    }

    @Override
    public Response<Long> bulkFailSuppliers() {
        Response<Long> result = new Response<Long>();
        try {
            // 超过10天的
            Set<Long> supplierIds = supplierResourceMaterialManager.findSuppliersBetween(
                    null, DateTime.now().minusDays(10).toDate());
            Long count = 0L;
            for (Long supplierId : supplierIds) {
                if (safeFailSupplier(supplierId)) {
                    ++count;
                }
            }
            result.setResult(count);
        } catch (Exception e) {
            log.error("bulkFailSuppliers failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.auto.fail.fail");
        }
        return result;
    }

    @Override
    public Response<List<SupplierResourceMaterialSubject>> getAllValidSubjects() {
        Response<List<SupplierResourceMaterialSubject>> result = new Response<List<SupplierResourceMaterialSubject>>();
        try {
            result.setResult(supplierResourceMaterialManager.getAllValidSubjects());
            log.debug("result ==> {}", result.getResult());
        } catch (Exception e) {
            log.error("getAllValidSubjects() failed, cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("supplier.resource.material.subject.query.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> bulkOverwriteSubjects(BaseUser baseUser, String subjects) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(baseUser)) {
            log.error("must login first");
            result.setError("user.not.login");
            return result;
        }
        Response<User> userResp = accountService.findUserByNick(baseUser.getNickName());
        if (!userResp.isSuccess()) {
            log.error("user not found");
            result.setError(userResp.getError());
            return result;
        }
        try {
            List<String> roles = Lists.newArrayList(Splitters.COMMA.splitToList(userResp.getResult().getRoleStr()));
            log.debug("bulkOverwriteSubjects ... roles={}", roles);
            // TODO: only resource supported
            if (!roles.contains("resource")) {
                log.error("not auth to change subjects, only resource supported");
                result.setError("supplier.resource.material.subject.change.no.auth");
                return result;
            }
            List<SupplierResourceMaterialSubject> subs = JSON_MAPPER.fromJson(subjects,
                    JSON_MAPPER.createCollectionType(List.class, SupplierResourceMaterialSubject.class));
            result.setResult(supplierResourceMaterialManager.bulkOverwriteSubjects(subs));
            log.debug("result ==> {}", result.getResult());
        } catch (Exception e) {
            log.error("bulkOverwriteSubjects(baseUser={}, subjects={}) failed, cause:{}",
                    baseUser, subjects);
            result.setError("supplier.resource.material.subject.change.fail");
        }
        return result;
    }

    private SupplierResourceMaterialInfo genNonInfo(Long supplierId) {
        SupplierResourceMaterialInfo nonInfo = new SupplierResourceMaterialInfo();
        nonInfo.setSupplierId(supplierId);
        nonInfo.setApprovedModuleIds("");
        nonInfo.setNotApprovedModuleIds("");
        nonInfo.setTimes(0L);
        nonInfo.setStatus(SupplierResourceMaterialInfo.Status.NO_SUBMISSION.value());
        return nonInfo;
    }

    private List<Long> transformRecordsToSubjectIds(List<SupplierResourceMaterialLogRecordDto> records) {
        return Lists.transform(records, new Function<SupplierResourceMaterialLogRecordDto, Long>() {
            @Override
            public Long apply(SupplierResourceMaterialLogRecordDto input) {
                return input.getSubjectId();
            }
        });
    }

    private Response<Boolean> approvedHook(Long supplierId) {
        Response<Boolean> result = new Response<Boolean>();
        Response<Company> companyResp = companyService.findCompanyById(supplierId);
        if (!companyResp.isSuccess()) {
            result.setError(companyResp.getError());
            return result;
        }
        return tagService.addTag(companyResp.getResult().getUserId(), User.SupplierTag.RESOURCE_MATERIAL);
    }
}
