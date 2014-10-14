package io.terminus.snz.user.service;

import com.google.common.base.*;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.services.MailService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.event.ApproveEvent;
import io.terminus.snz.user.event.SupplierEventBus;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-5
 */
@Slf4j
public abstract class BaseAccountServiceImpl implements AccountService<User> {

    @Autowired
    private BlackListDao blackListDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PurchaserExtraDao purchaserExtraDao;

    @Autowired
    private SupplierIndexService supplierIndexService;

    @Autowired
    private SupplierEventBus eventBus;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private SupplierApproveLogDao supplierApproveLogDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private CompanyMainBusinessTmpDao companyMainBusinessTmpDao;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Autowired
    private MailService mailService;

    private static final String COUNT_ENTER_PASS_SUPPLIER = "count-enter-pass-supplier";

    protected final LoadingCache<Long, Optional<User>> userCache;

    private final LoadingCache<String, Long> countSupplierCache;

    public BaseAccountServiceImpl() {
        userCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<Long, Optional<User>>() {
            @Override
            public Optional<User> load(Long id) throws Exception {
                return Optional.fromNullable(userDao.findById(id));
            }
        });

        countSupplierCache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(new CacheLoader<String, Long>() {
            @Override
            public Long load(String key) throws Exception {
                return userDao.countEnterPassSupplier(null, null);
            }
        });
    }

    /**
     * 根据id寻找user
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Override
    public Response<User> findUserById(Long id) {
        Response<User> result = new Response<User>();
        if (id == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }
        try {
            Optional<User> userO = userCache.getUnchecked(id);
            if (!userO.isPresent()) {
                log.error("failed to find user where id={}", id);
                result.setError("user.not.found");
                return result;
            }

            result.setResult(userO.get());
            return result;
        } catch (Exception e) {
            log.error("failed to find user where id={},cause:{}", id, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
            return result;
        }
    }

    @Override
    public Response<List<User>> findUserByIds(List<Long> ids) {
        Response<List<User>> resp = new Response<List<User>>();

        try {
            if (Iterables.isEmpty(ids)) {
                resp.setResult(Collections.<User>emptyList());
            }
            resp.setResult(userDao.findByIds(ids));
        } catch (Exception e) {
            log.error("failed to find user by ids={}, error code={}", ids, Throwables.getStackTraceAsString(e));
            resp.setError("user.find.fail");
        }
        return resp;
    }

    @Override
    public Response<User> findUserByNick(String nick) {
        Response<User> result = new Response<User>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("user nick can not be null");
            result.setError("user.nick.empty");
            return result;
        }
        try {
            User user = userDao.findByNick(nick);
            if (user == null) {
                log.error("failed to find user where nick={}", nick);
                result.setError("user.not.found");
                return result;
            }

            result.setResult(user);
            return result;
        } catch (Exception e) {
            log.error("failed to find user where nick={},cause:{}", nick, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
            return result;
        }
    }

    /**
     * HacUserServiceImpl需要实现
     */
    @Override
    public Response<Boolean> activeUser(BaseUser user, String code) {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);
        log.error("local system not supoort active user.");
        return resp;
    }

    /**
     * HacUserServiceImpl需要实现
     */
    @Override
    public Response<Boolean> resendActiveCode(BaseUser user) {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);
        log.error("local system not supoort resend active code.");
        return resp;
    }


    /**
     * 更改密码
     *
     * @param baseUser    用户
     * @param oldPassword 老密码
     * @param newPassword 新密码
     * @return 是否更新成功
     */
    @Override
    public Response<Boolean> changePassword(BaseUser baseUser, String oldPassword, String newPassword, String confirmPassword) {
        Response<Boolean> result = new Response<Boolean>();
        Long userId = baseUser.getId();

        if (userId == null) {
            log.error("id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }
        if (Strings.isNullOrEmpty(oldPassword) || Strings.isNullOrEmpty(newPassword)) {
            log.error("password should be provided");
            result.setError("user.password.empty");
            return result;
        }

        User updated = new User();
        updated.setId(userId);
        updated.setEncryptedPassword(PasswordUtil.encryptPassword(newPassword));
        try {
            User user = userDao.findById(userId);
            if (user == null) {
                log.error("can not find user whose id={}", userId);
                result.setError("user.not.found");
                return result;
            }
            if (!PasswordUtil.passwordMatch(oldPassword, user.getEncryptedPassword())) {
                log.error("password not match for user(id={})", userId);
                result.setError("user.password.incorrect");
                return result;
            }
            userDao.update(updated);
            userCache.invalidate(userId);
            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("failed to change password for user (id={}),cause:{}", userId, Throwables.getStackTraceAsString(e));
            result.setError("user.change.password.fail");
            return result;
        }
    }

    public Response<SupplierApproveExtra> findSupplierApproveExtraByUserId(Long userId) {
        Response<SupplierApproveExtra> result = new Response<SupplierApproveExtra>();

        try {

            if (userId == null) {
                log.error("user id can not be null");
                result.setError("user.id.not.null.fail");
                return result;
            }

            SupplierApproveExtra supplierApproveExtra = new SupplierApproveExtra();

            //TODO 最近修改基本信息时间

            SupplierApproveLog enterApproveLog = supplierApproveLogDao.findLastByUserIdAndApproveType(userId, SupplierApproveLog.ApproveType.ENTER.value());
            if (enterApproveLog != null) {
                supplierApproveExtra.setEnterApprovedAt(enterApproveLog.getApprovedAt());
                supplierApproveExtra.setEnterApproverName(enterApproveLog.getApproverName());
            }

            result.setResult(supplierApproveExtra);

        } catch (Exception e) {
            log.error("fail to query supplier approve extra where user id={},cause:{}", userId, Throwables.getStackTraceAsString(e));
            result.setError("query.supplier.approve.extra.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> logout(String ssoSessionId) {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);
        return resp;
    }

    protected void checkSupplierDtoAndInitUser(RichSupplierDto richSupplierDto) {
        checkRichSupplierDto(richSupplierDto);
        userExisted(richSupplierDto.getUser().getNick());
        companyExisted(richSupplierDto.getCompany().getCorporation());

        User user = richSupplierDto.getUser();
        user.setType(User.Type.SUPPLIER.value());
        user.setRoleStr(User.Type.SUPPLIER.name());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.INIT.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setOrigin(User.Origin.NORMAL.value());
        user.setQualifyStatus(User.QualifyStatus.NO_SUBMISSION.value()); // 初始未提交申请
        richSupplierDto.getCompany().setParticipateCount(0);
    }

    protected void checkRichSupplierDto(RichSupplierDto richSupplierDto) {
        checkUser(richSupplierDto.getUser());
        checkCompany(richSupplierDto.getCompany());
        checkCompanyMainBusiness(richSupplierDto.getCompanyMainBusinesses());
        checkBlack(richSupplierDto.getCompany());
    }

    /**
     * 检查用户信息
     */
    protected void checkUser(User user) {
        if (Strings.isNullOrEmpty(user.getNick())) {
            log.error("user's nick can't be empty");
            throw new ServiceException("user.nick.null");
        }
        if (Strings.isNullOrEmpty(user.getEncryptedPassword())) {
            log.error("user's password can't be empty");
            throw new ServiceException("user.password.empty");
        }
        if (Strings.isNullOrEmpty(user.getMobile())) {
            log.error("user's mobile can't be empty");
            throw new ServiceException("user.mobile.empty");
        }
        if (Strings.isNullOrEmpty(user.getEmail())) {
            log.error("user's email can't be empty");
            throw new ServiceException("user.email.empty");
        }
    }

    /**
     * 检查企业基本信息
     *
     * @param company 企业基本信息
     */
    protected void checkCompany(Company company) {
        if (company == null) {
            log.error("company cant't be null");
            throw new ServiceException("company.not.null");
        }
        if (Strings.isNullOrEmpty(company.getCorporation())) {
            log.error("company's corporation cant't be empty");
            throw new ServiceException("corporation.not.null");
        }
        if (Strings.isNullOrEmpty(company.getInitAgent())) {
            log.error("company's initAgent cant't be empty");
            throw new ServiceException("initAgent.not.null");
        }
        if (Strings.isNullOrEmpty(company.getDesc())) {
            log.error("company's desc cant't be empty");
            throw new ServiceException("company.desc.not.null");
        }
        if (company.getRegCountry() == null) {
            log.error("company's regCountry cant't be empty");
            throw new ServiceException("regCountry.not.null");
        }
        if (Strings.isNullOrEmpty(company.getCustomers())) {
            log.error("company's customers cant't be empty");
            throw new ServiceException("customers.not.null");
        }
    }

    /**
     * 检查主营业务信息
     *
     * @param companyMainBusinesses 主营业务
     */
    protected void checkCompanyMainBusiness(List<CompanyMainBusiness> companyMainBusinesses) {
        if (companyMainBusinesses == null || companyMainBusinesses.isEmpty()) {
            log.error("company main businesses can not be null");
            throw new ServiceException("company.main.business.not.null");
        }
        for (CompanyMainBusiness companyMainBusiness : companyMainBusinesses) {
            if (companyMainBusiness == null) {
                log.error("company main business can not be null");
                throw new ServiceException("company.main.business.not.null");
            }
            if (companyMainBusiness.getMainBusinessId() == null) {
                log.error("company main business id can not be null");
                throw new ServiceException("company.main.business.id.not.null");
            }
            if (Strings.isNullOrEmpty(companyMainBusiness.getName())) {
                log.error("company main business name can not be null");
                throw new ServiceException("company.main.business.name.not.null");
            }
        }
    }

    /**
     * 检查法人公司是否在黑名单内和是否包含黑名单关键字
     *
     * @param company 公司信息
     */
    protected void checkBlack(Company company) {
        if (Strings.isNullOrEmpty(company.getCorporation())) {
            log.error("company's corporation cant't be empty");
            throw new ServiceException("corporation.not.null");
        }

        List<BlackList> blackList = blackListDao.findAll();
        if (blackList == null) {
            return;
        }

        String corporation = company.getCorporation().replaceAll("\\(|\\)|（|）", "").toUpperCase();

        for (BlackList bl : blackList) {

            String name = bl.getName().replaceAll("\\(|\\)|（|）", "").toUpperCase();

            //完全匹配，不允许注册
            if (corporation.equals(name)) {
                log.error("{} is in black list", company.getCorporation());
                throw new ServiceException("corporation.in.blacklist");
            } else if (bl.getKeywords() != null && corporation.contains(bl.getKeywords().toUpperCase())) { //关键词匹配
                company.setIncludeKeywords(bl.getKeywords());
                break;
            } else if (company.getInitAgent().equalsIgnoreCase(bl.getInitAgent())) {//法人匹配
                company.setIncludeKeywords(bl.getInitAgent());
                break;
            }
        }
    }

    /**
     * 检查用户是否已经存在
     *
     * @param nick 昵称
     */
    protected void userExisted(String nick) {
        if (Strings.isNullOrEmpty(nick)) {
            log.error("nick can not be null");
            throw new ServiceException("login.nick.empty");
        }
        User user = userDao.findByNick(nick);
        if (user != null) {
            log.error("the user nick({}) is existed!", nick);
            throw new ServiceException("user.nick.duplicated");
        }
    }

    /**
     * 检查公司是否已经存在
     *
     * @param corporation 法人公司名称
     */
    protected void companyExisted(String corporation) {
        List<Company> companies = companyDao.findByCorporation(corporation);
        if (companies != null && !companies.isEmpty()) {
            log.error("the company corporation({}) is existed!", corporation);
            throw new ServiceException("company.corporation.duplicated");
        }
    }

    /**
     * 更新用户对象
     *
     * @param user 用户
     */
    @Override
    public Response<Boolean> updateUser(User user) {
        Response<Boolean> result = new Response<Boolean>();

        if (user.getId() == null) {
            log.error("user id can not be null when updated");
            result.setError("user.null.fail");
            return result;
        }

        try {
            userDao.update(user);
            userCache.invalidate(user.getId());
            result.setResult(Boolean.TRUE);
            return result;
        } catch (Exception e) {
            log.error("failed to updated user {},cause:{}", user, Throwables.getStackTraceAsString(e));
            result.setError("user.update.fail");
            return result;
        }
    }

    @Override
    public Response<List<User>> findUserByRoleStr(String role) {
        Response<List<User>> result = new Response<List<User>>();
        try {
            if (Strings.isNullOrEmpty(role)) {
                log.error("role can not be null or empty");
                result.setError("illegal.argument");
                return result;
            }
            result.setResult(userDao.findByRoleStr(role));
        } catch (Exception e) {
            log.error("find user by role={} failed, cause:{}", role, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
        }
        return result;
    }

    /**
     * 用户登录
     *
     * @param nick     昵称
     * @param password 密码
     * @return 用户
     */
    @Override
    public Response<UserDto> login(String nick, String password) {
        Response<UserDto> result = new Response<UserDto>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("nick can not be empty");
            result.setError("login.nick.empty");
            return result;
        }
        User user;
        try {
            user = userDao.findByNick(nick);
        } catch (Exception e) {
            log.error("failed to find user where nick ={} ,cause:{}",
                    nick, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
            return result;
        }

        if (user == null) {
            log.error("user not found  where nick ={}", nick);
            result.setError("user.not.found");
            return result;
        }

        if (Objects.equal(User.Status.FROZEN.value(), user.getStatus())) {
            log.error("user account frozen  where id ={}", user.getId());
            result.setError("user.account.frozen");
            return result;
        }

        String storedPassword = user.getEncryptedPassword();
        if (PasswordUtil.passwordMatch(password, storedPassword)) {
            user.setLastLoginAt(DateTime.now().toDate());
            userDao.update(user);
            result.setResult(new UserDto(user, null));
            return result;
        } else {
            log.error("failed to login user  where nick ={},cause:password mismatch ", nick);
            result.setError("user.password.incorrect");
            return result;
        }
    }

    @Override
    public Response<UserDto> dirtyLogin(String nick, String password) {
        Response<UserDto> result = new Response<UserDto>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("nick can not be empty");
            result.setError("login.nick.empty");
            return result;
        }
        User user;
        try {
            user = userDao.findByNick(nick);
        } catch (Exception e) {
            log.error("failed to find user where nick ={} ,cause:{}",
                    nick, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
            return result;
        }

        if (user == null) {
            log.error("user not found  where nick ={}", nick);
            result.setError("user.not.found");
            return result;
        }

        if (Objects.equal(User.Status.FROZEN.value(), user.getStatus())) {
            log.error("user account frozen  where id ={}", user.getId());
            result.setError("user.account.frozen");
            return result;
        }

        String storedPassword = user.getEncryptedPassword();
        if (PasswordUtil.passwordMatch(password, storedPassword)) {
            user.setLastLoginAt(DateTime.now().toDate());
            userDao.update(user);
            result.setResult(new UserDto(user, null));
            return result;
        } else {
            log.error("failed to login user  where nick ={},cause:password mismatch ", nick);
            result.setError("user.password.incorrect");
            return result;
        }
    }

    /**
     * 检查用户是否存在
     *
     * @param nick 昵称
     * @return 是否存在
     */
    @Override
    public Response<Boolean> userExists(String nick) {
        Response<Boolean> result = new Response<Boolean>();
        if (Strings.isNullOrEmpty(nick)) {
            log.error("nick can not be null");
            result.setError("login.nick.empty");
            return result;
        }

        try {
            User user = userDao.findByNick(nick);
            result.setResult(user != null);
            return result;
        } catch (Exception e) {
            log.error("failed to check user exists where nick ={},cause:{}", nick, Throwables.getStackTraceAsString(e));
            result.setError("user.query.fail");
            return result;
        }
    }

    /**
     * 更新用户的状态
     *
     * @param id     用户id
     * @param status 是否更新成功
     */
    @Override
    public Response<Boolean> updateStatus(Long id, Integer status) {
        Response<Boolean> result = new Response<Boolean>();
        try {

            User user = checkIfUserExist(id);
            doUpdateUserStatus(id, status);
            userCache.invalidate(id);

            //如果冻结用户，准实时dump
            if (Objects.equal(status, User.Status.FROZEN.value())) {
                Response<Boolean> indexR = supplierIndexService.realTimeIndex(Lists.newArrayList(id), User.SearchStatus.DELETE);
                if (!indexR.isSuccess()) {
                    log.error("fail to realTime index supplier id={},status={},error code:{}",
                            id, User.Status.from(status), indexR.getError());
                }
            }
            //如果恢复用户，type为供应商并且审核通过，准实时索引
            if (Objects.equal(status, User.Status.OK.value())) {
                if (Objects.equal(user.getType(), User.Type.SUPPLIER.value())
                        && (Objects.equal(user.getApproveStatus(), User.ApproveStatus.OK.value())
                        || Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_FAIL.value())
                        || Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value()))) {
                    Response<Boolean> indexR = supplierIndexService.realTimeIndex(Lists.newArrayList(id), User.SearchStatus.INDEX);
                    if (!indexR.isSuccess()) {
                        log.error("fail to realTime index supplier id={},type={},approveStatus={},error code:{}",
                                id, user.getType(), user.getApproveStatus(), indexR.getError());
                    }
                }
            }

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("update user status fail with (id:{}, status:{})");
            result.setError("user.status.update.fail");
            return result;
        }
    }

    @Override
    public Response<PurchaserDto> findPurchaserById(Long id) {
        Response<PurchaserDto> result = new Response<PurchaserDto>();

        if (id == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }
        try {
            Optional<User> userO = userCache.getUnchecked(id);
            if (!userO.isPresent()) {
                log.error("failed to find user where id={}", id);
                result.setError("user.not.found");
                return result;
            }
            User user = userO.get();
            PurchaserExtra purchaserExtra = purchaserExtraDao.findByUserId(user.getId());
            if (purchaserExtra == null) {
                log.error("failed to find purchaser extra where user id={}", user.getId());
                result.setError("purchaser.extra.not.found");
                return result;
            }

            PurchaserDto purchaserDto = new PurchaserDto();
            purchaserDto.setUser(user);
            purchaserDto.setPurchaserExtra(purchaserExtra);

            result.setResult(purchaserDto);
            return result;
        } catch (Exception e) {
            log.error("failed to find purchaser where id={},cause:{}", id, Throwables.getStackTraceAsString(e));
            result.setError("purchaser.query.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> approveSupplier(BaseUser baseUser, SupplierApproveDto supplierApproveDto) {
        Response<Boolean> result = new Response<Boolean>();

        if (supplierApproveDto == null) {
            log.error("supplier approved content can not be null");
            result.setError("supplier.approved.content.not.null.fail");
            return result;
        }

        Long userId = supplierApproveDto.getUserId();

        if (userId == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        if (supplierApproveDto.getOperation() == null) {
            log.error("approve operation can not be null  where user id ={}", userId);
            result.setError("approve.operation.not.null");
            return result;
        }

        try {
            User user = userDao.findById(userId);

            if (user == null) {
                log.error("user not found  where user id ={}", userId);
                result.setError("user.not.found");
                return result;
            }

            supplierApproveDto.setApproveStatus(user.getApproveStatus());
            supplierApproveDto.setApproverId(baseUser.getId());
            supplierApproveDto.setApproverName(baseUser.getName());

            ApproveEvent approveEvent = accountManager.approveSupplier(supplierApproveDto);
            approveEvent.setSenderId(baseUser.getId());
            approveEvent.setReceiverId(userId);
            approveEvent.setApprover(baseUser.getName());
            approveEvent.setMobile(baseUser.getMobile());
            approveEvent.setDescription(supplierApproveDto.getDescription());

            //推送审批结果给供应商
            eventBus.post(approveEvent);

            //准实时dump, 只有在type为supplier，approveStatus为ok的情况下才触发
            if (Objects.equal(user.getType(), User.Type.SUPPLIER.value())) {
                if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.OK.value())) {
                    Response<Boolean> indexR = supplierIndexService.realTimeIndex(Lists.newArrayList(userId), User.SearchStatus.INDEX);
                    if (!indexR.isSuccess()) {
                        log.error("fail to realTime index supplier id={},type={},approveStatus={},error code:{}",
                                userId, user.getType(), user.getApproveStatus(), indexR.getError());
                    }
                }
            }

            userCache.invalidate(userId);
            countSupplierCache.invalidate(COUNT_ENTER_PASS_SUPPLIER);

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to approve supplier where user id={}", userId);
            result.setError("approve.supplier.fail");
            return result;
        }

    }

    @Override
    public Response<Boolean> needCommitPaperwork(BaseUser baseUser) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            User user = userDao.findById(baseUser.getId());
            if (user == null) {
                log.error("can not find user whose id={}", baseUser.getId());
                result.setError("user.not.found");
                return result;
            }

            if (Objects.equal(user.getStatus(), User.Status.OK.value()) &&
                    Objects.equal(user.getApproveStatus(), User.ApproveStatus.INIT.value())) {

                result.setResult(Boolean.TRUE);
                return result;
            }

            result.setResult(Boolean.FALSE);
            return result;

        } catch (Exception e) {
            log.error("fail to check supplier(user id={}) if need commit paperwork,cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("check.supplier.need.commit.paperwork.fail");
            return result;
        }
    }

    @Override
    public Response<Boolean> inSupplierConsole(BaseUser baseUser) {
        Response<Boolean> result = new Response<Boolean>();

        if (baseUser.getId() == null) {
            log.error("user id can not be null");
            result.setError("user.id.not.null.fail");
            return result;
        }

        try {

            User user = userDao.findById(baseUser.getId());

            if (user == null) {
                log.error("user not found  where user id ={}", baseUser.getId());
                result.setError("user.not.found");
                return result;
            }

            if (Objects.equal(user.getStatus(), User.Status.FROZEN.value())) {
                result.setResult(Boolean.FALSE);
                return result;
            }

            //入驻审批不通过是否能进后台?

            result.setResult(Boolean.TRUE);
            return result;

        } catch (Exception e) {
            log.error("fail to check supplier(id={}) can in console", baseUser.getId());
            result.setError("check.supplier.can.in.console.fail");
            return result;
        }

    }

    private void doUpdateUserStatus(Long id, Integer status) {
        User updated = new User();
        updated.setId(id);
        updated.setStatus(status);
        boolean success = userDao.update(updated);
        checkState(success, "user.update.fail");
    }

    private User checkIfUserExist(Long id) {
        User user = userDao.findById(id);
        checkState(user != null, "user.not.exist");
        return user;
    }

    @Override
    public Response<Long> createSupplier(RichSupplierDto richSupplierDto) {
        Response<Long> resp = new Response<Long>();
        try {
            checkSupplierDtoAndInitUser(richSupplierDto);

            Long userId = accountManager.createSupplier(richSupplierDto);
            resp.setResult(userId);
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to createSupplier({}), cause: {}",
                    richSupplierDto, Throwables.getStackTraceAsString(e));
            resp.setError("create.supplier.fail");
        }
        return resp;
    }

    public Response<Long> createBizSupplier(RichSupplierDto richSupplierDto) {
        Response<Long> resp = new Response<Long>();
        try {
            checkRichSupplierDto(richSupplierDto);
            userExisted(richSupplierDto.getUser().getNick());
            companyExisted(richSupplierDto.getCompany().getCorporation());

            User user = richSupplierDto.getUser();
            user.setType(User.Type.SUPPLIER.value());
            user.setRoleStr(User.Type.SUPPLIER.name());
            user.setStatus(User.Status.OK.value());
            user.setApproveStatus(User.ApproveStatus.OK.value());
            user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
            user.setOrigin(User.Origin.BIZ.value());
            richSupplierDto.getCompany().setParticipateCount(0);

            Long userId = accountManager.createSupplier(richSupplierDto);
            resp.setResult(userId);
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to createSupplier({}), cause: {}",
                    richSupplierDto, Throwables.getStackTraceAsString(e));
            resp.setError("create.supplier.fail");
        }
        return resp;
    }

    @Override
    public Response<TeamMemeberDto> findStaffByWorkNo(String workNo) {
        Response<TeamMemeberDto> resp = new Response<TeamMemeberDto>();

        User user = new User();
        user.setId(1l);
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("123@qq.com");
        user.setEncryptedPassword("ewfdf543646");
        user.setMobile("18969973054");
        user.setPhone("110");
        user.setName("测试数据用户");
        user.setNick("测试数据用户");
        user.setRoleStr("supplier_init");
        user.setType(User.Type.SUPPLIER.value());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setTags("fsds");

        user.setTags("fsds");

        TeamMemeberDto teamMemeber = new TeamMemeberDto(user);
        teamMemeber.setOrgan("xx部门");
        resp.setResult(teamMemeber);
        return resp;
    }

    @Override
    public Response<List<TeamMemeberDto>> findStaffByName(String name) {
        Response<List<TeamMemeberDto>> result = new Response<List<TeamMemeberDto>>();

        int num = new Random().nextInt(100);
        User user = new User();
        user.setId(new Long(num));
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("123@qq.com");
        user.setEncryptedPassword("ewfdf543646");
        user.setMobile("18969973054");
        user.setPhone("110");
        user.setName("测试数据用户" + num);
        user.setNick("测试数据用户" + num);
        user.setRoleStr("supplier_init");
        user.setType(User.Type.SUPPLIER.value());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setTags("fsds");

        TeamMemeberDto teamMemeber = new TeamMemeberDto(user);
        teamMemeber.setOrgan("xx部门");
        result.setResult(Lists.newArrayList(teamMemeber));
        return result;
    }

    @Override
    public Response<Long> countEnterPassSupplier() {
        Response<Long> result = new Response<Long>();

        try {

            Long count = countSupplierCache.getUnchecked(COUNT_ENTER_PASS_SUPPLIER);
            result.setResult(count == null ? 0 : count);

        } catch (Exception e) {
            log.error("fail to count enter pass supplier,cause:{} ", Throwables.getStackTraceAsString(e));
            result.setError("count.enter.pass.supplier.fail");
        }

        return result;
    }

    public Response<Boolean> reCommitApproval(BaseUser baseUser) {
        Response<Boolean> result = new Response<Boolean>();

        try {

            User user = userDao.findById(baseUser.getId());

            if (user == null) {
                log.error("user not found where user id={}", baseUser.getId());
                result.setError("user.not.found");
                return result;
            }

            User updated = new User();
            updated.setId(baseUser.getId());

            if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.ENTER_FAIL.value())) {
                updated.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
            } else if (Objects.equal(user.getApproveStatus(), User.ApproveStatus.MODIFY_INFO_FAIL.value())) {
                updated.setApproveStatus(User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value());
            } else {
                log.error("user's approve status incorrect when commit approval again where user id={}", baseUser.getId());
                result.setError("approve.status.incorrect");
                return result;
            }

            updated.setLastSubmitApprovalAt(new Date());
            userDao.update(updated);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to commit approval again where user id={},cause:{}", baseUser.getId(), Throwables.getStackTraceAsString(e));
            result.setError("commit.approval.again.fail");
        }

        return result;
    }

    @Override
    public Response<String> findLeader(BaseUser user) {
        Response<String> resp = new Response<String>();
        try {
            PurchaserExtra purchaserExtra = purchaserExtraDao.findByUserId(user.getId());
            String leader = purchaserExtra.getLeader();
            User myLeader = userDao.findByNick(leader);
            if (myLeader != null) { //上级信息有显示姓名
                resp.setResult(myLeader.getName());
            } else {                //没有则显示工号
                resp.setResult(leader);
            }
        } catch (Exception e) {
            log.error("failed to find leader of user({nick={}})", user.getNickName());
            resp.setError("user.findleader.fail");
        }
        return resp;
    }

    public Response<Boolean> remindOfApproving() {
        Response<Boolean> result = new Response<Boolean>();

        try {

            List<Long> userIds = userDao.findApprovingUserIdsWithSubmitAt(DateTime.now().minusDays(2).toDate());

            if (userIds == null || userIds.isEmpty()) {
                log.info("not approving supplier beyond deadline");
                result.setResult(Boolean.TRUE);
                return result;
            }

            Map<Long, Mail<Map<String, Object>>> mailMap = Maps.newHashMap();

            for (Long userId : userIds) {

                Company company = companyDao.findByUserId(userId);
                if (company == null) {
                    log.warn("company(user id={}) not found when remind of approving", userId);
                    continue;
                }

                List<Long> mainBusinessIds = findCurrentMainBusinessIds(userId);
                if (mainBusinessIds == null || mainBusinessIds.isEmpty()) {
                    log.warn("supplier(user id={}) main business not found when remind of approving", userId);
                    continue;
                }

                List<User> approvers = findApprovers(mainBusinessIds);
                if (approvers == null || approvers.isEmpty()) {
                    log.warn("approvers not found where main business ids={} when remind of approving", mainBusinessIds, userId);
                    continue;
                }

                for (User approver : approvers) {

                    if (Strings.isNullOrEmpty(approver.getEmail())) {
                        log.info("email not found where approver id={} when remind of approving", approver.getId());
                        continue;
                    }

                    if (mailMap.containsKey(approver.getId())) {
                        List<String> supplierNames = (List<String>) mailMap.get(approver.getId()).getData().get("supplierNames");
                        supplierNames.add(company.getCorporation());
                    } else {
                        Mail<Map<String, Object>> mail = new Mail<Map<String, Object>>();
                        mail.setType(Mail.Type.SUPPLIER_APPROVE_DEADLINE);
                        mail.setTo(approver.getEmail());

                        Map<String, Object> data = Maps.newHashMap();
                        data.put("approverName", approver.getName());
                        data.put("supplierNames", Lists.newArrayList(company.getCorporation()));
                        mail.setData(data);

                        mailMap.put(approver.getId(), mail);
                    }

                }

            }

            if (!mailMap.isEmpty()) {
                List<Mail<?>> mails = Lists.<Mail<?>>newArrayList(mailMap.values());
                mailService.batchSend(mails);
            }

            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to remind of approving,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("remind.of.approving.fail");
        }
        return result;
    }

    /**
     * 查找供应商当前的主营业务编号
     */
    private List<Long> findCurrentMainBusinessIds(Long userId) {

        //先从临时表查询
        List<CompanyMainBusinessTmp> companyMainBusinessTmps = companyMainBusinessTmpDao.findByUserId(userId);
        if (companyMainBusinessTmps != null && !companyMainBusinessTmps.isEmpty()) {
            return Lists.transform(companyMainBusinessTmps, new Function<CompanyMainBusinessTmp, Long>() {
                public Long apply(CompanyMainBusinessTmp companyMainBusinessTmp) {
                    return companyMainBusinessTmp.getMainBusinessId();
                }
            });
        }

        //如果临时表没有，则说明没有修改主营业务，再查主营业务表
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(userId);
        if (companyMainBusinesses != null && !companyMainBusinesses.isEmpty()) {
            return Lists.transform(companyMainBusinesses, new Function<CompanyMainBusiness, Long>() {
                public Long apply(CompanyMainBusiness companyMainBusiness) {
                    return companyMainBusiness.getMainBusinessId();
                }
            });
        }

        return null;
    }

    /**
     * 根据主营业务编号查询审核人信息
     */
    private List<User> findApprovers(List<Long> businessIds) {

        List<MainBusinessApprover> mainBusinessApprovers = mainBusinessApproverDao.findByMainBusinessIds(businessIds);
        if (mainBusinessApprovers == null || mainBusinessApprovers.isEmpty()) {
            return null;
        }

        List<String> approverEmployeeIds = Lists.newArrayList();
        for (MainBusinessApprover mainBusinessApprover : mainBusinessApprovers) {
            if (!approverEmployeeIds.contains(mainBusinessApprover.getMemberId())) {
                approverEmployeeIds.add(mainBusinessApprover.getMemberId());
            }
            if (!approverEmployeeIds.contains(mainBusinessApprover.getLeaderId())) {
                approverEmployeeIds.add(mainBusinessApprover.getLeaderId());
            }
        }

        if (approverEmployeeIds.isEmpty()) {
            return null;
        }

        return userDao.findByNicks(approverEmployeeIds);
    }


}
