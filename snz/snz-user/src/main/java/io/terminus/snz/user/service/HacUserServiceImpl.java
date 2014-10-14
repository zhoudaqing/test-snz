package io.terminus.snz.user.service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haier.openplatform.hac.dto.HacUserDTO;
import com.haier.openplatform.hac.dto.UserIDSDTO;
import com.haier.openplatform.hac.dto.UserMergeDTO;
import com.haier.openplatform.hac.service.HacUserServiceCli;
import com.haier.openplatform.hac.service.agent.HacUserServiceClient;
import com.haier.openplatform.util.ExecuteResult;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.PurchaserExtraDao;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dto.PurchaserDto;
import io.terminus.snz.user.dto.RichSupplierDto;
import io.terminus.snz.user.dto.TeamMemeberDto;
import io.terminus.snz.user.dto.UserDto;
import io.terminus.snz.user.manager.AccountManagerHac;
import io.terminus.snz.user.model.PurchaserExtra;
import io.terminus.snz.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 海尔用户权限中心服务实现
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-4
 */
@Slf4j
public class HacUserServiceImpl extends BaseAccountServiceImpl {
    /**
     * 用户手机验证方式
     */
    private static final String USER_VERIFY_MOBILE = "mobile";

    /**
     * 内部用户登录
     */
    private static final String LOGIN_BY_IDM = "IDM";
    /**
     * 外部用户登录
     */
    private static final String LOGIN_BY_IDS = "IDS";

    @Autowired
    private HacUserServiceClient hacUserServiceClient;

    @Autowired
    private HacUserServiceCli hacUserServiceCli;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PurchaserExtraDao purchaserExtraDao;

    @Autowired
    private AccountManagerHac accountManagerHac;

    private static final Configuration CONF = new Configuration();

    public HacUserServiceImpl(String coSessionId, String idsAppName, String passwdKey) {
        CONF.coSessionId = coSessionId;
        CONF.idsAppName = idsAppName;
        CONF.passwdKey = passwdKey;
    }

    @Override
    public Response<UserDto> login(String nick, String password) {
        Response<UserDto> resp = new Response<UserDto>();
        try {
            checkParamsLogin(nick, password);
            ExecuteResult<UserMergeDTO> res = doLoginIDM(nick, password);
            if (!res.isSuccess()) {
                log.error("failed to login to haier internal user(nick={}, password={}), cause:{}",
                        nick, password, res.getErrorMessages());
                resp.setError(res.getErrorMessages().get(0));
                return resp;
            }
            User u = doSyncPurchaser(res.getResult());
            resp.setResult(new UserDto(u, null));
        } catch (ServiceException e) {
            log.error("failed to login(nick={}, password={})", nick, password);
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to login(nick={}, password={}), cause:{}", nick, password,
                    Throwables.getStackTraceAsString(e));
            resp.setError("user.login.fail");
        }
        return resp;
    }

    /**
     * 海尔portal用户登录
     */
    private ExecuteResult<UserMergeDTO> doLoginIDM(String nick, String password) {
        UserMergeDTO userMergeDTO = new UserMergeDTO();
        userMergeDTO.setUserName(nick);
        userMergeDTO.setPassword(password);
        userMergeDTO.setRegistSrc(LOGIN_BY_IDM);
        return hacUserServiceCli.mergeUserLogin(userMergeDTO);
    }

    /**
     * 同步采购商信息到本地
     */
    private User doSyncPurchaser(UserMergeDTO userMergeDTO) {
        String nick = userMergeDTO.getUserName();
        User u = userDao.findByNick(nick);
        if (u != null) {
            // fix for some manu-insert users
            User neoUser = toUserForFix(userMergeDTO);
            neoUser.setId(u.getId());
            userDao.update(neoUser);
            u = userDao.findByNick(nick); // re-find
            // 更新采购商信息
            PurchaserExtra purchaserExtra = toPurchaserExtra(userMergeDTO);
            purchaserExtra.setUserId(u.getId());
            if (purchaserExtraDao.findByUserId(u.getId()) == null) {
                //添加采购商额外信息
                purchaserExtraDao.create(purchaserExtra);
            } else {
                //更新采购上额外信息
                purchaserExtraDao.updateByUserId(purchaserExtra);
            }
            return u;
        }
        // 用户信息
        u = toUser(userMergeDTO);
        // 采购商额外信息
        PurchaserExtra purchaserExtra = toPurchaserExtra(userMergeDTO);
        PurchaserDto purchaserDto = new PurchaserDto(u, purchaserExtra);
        accountManagerHac.createPurcharser(purchaserDto);
        return u;
    }

    private PurchaserExtra toPurchaserExtra(UserMergeDTO userMergeDTO) {
        PurchaserExtra purchaserExtra = new PurchaserExtra();
        purchaserExtra.setPosition(Strings.isNullOrEmpty(userMergeDTO.getBusinessPosition()) ? "无" : userMergeDTO.getBusinessPosition());
        purchaserExtra.setLeader(userMergeDTO.getHaierUserFirstLineID());
        purchaserExtra.setDepartment(userMergeDTO.getUserOU());
        purchaserExtra.setEmployeeId(userMergeDTO.getUserName()); //海尔portal用工号登录
        return purchaserExtra;
    }

    @Override
    public Response<Boolean> logout(String ssoSessionId) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            if (Strings.isNullOrEmpty(ssoSessionId)) {
                log.error("ssoSessionId can't be empty when logout");
                resp.setError("user.ssosession.empty");
                return resp;
            }
            String ip = extractIp(ssoSessionId);
            UserIDSDTO userIDSDTO = newUserIDSDTO();
            userIDSDTO.setClientIp(ip);
            userIDSDTO.setSsoSessionId(ssoSessionId);
            ExecuteResult<String> res = hacUserServiceClient.userLogout(userIDSDTO);
            if (!res.isSuccess()) {
                log.error("failed to logout from hac(ssoSessionId={}), cause: {}",
                        ssoSessionId, res.getResult());
                resp.setError("user.logout.fail");
            }
            resp.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to logout, cause: {}", Throwables.getStackTraceAsString(e));
            resp.setError("user.logout.fail");
        }
        return resp;
    }

    @Override
    public Response<TeamMemeberDto> findStaffByWorkNo(String workNo) {
        Response<TeamMemeberDto> resp = new Response<TeamMemeberDto>();
        try {

            ExecuteResult<List<HacUserDTO>> res = hacUserServiceCli.searchIDMUser(workNo, "", "IN");
            if (!res.isSuccess()) {
                log.error("failed to search user by workNo({})", workNo);
                resp.setError(res.getErrorMessages().get(0));
            }
            List<HacUserDTO> userDTOs = res.getResult();
            if (Iterables.isEmpty(userDTOs)) {
                log.error("can't find the user(workNo={})", workNo);
                resp.setError("user.not.exist");
            }
            HacUserDTO userDTO = userDTOs.get(0);
            // userDTO.getName()为nick
            User u = userDao.findByNick(userDTO.getName());
            if (u == null) {
                // 不存在则保存用户信息
                u = saveUser(userDTO);
            }
            TeamMemeberDto teamMemeber = new TeamMemeberDto(u);
            teamMemeber.setOrgan(userDTO.getUserOU());
            resp.setResult(teamMemeber);
        } catch (Exception e) {
            log.error("failed to find name by workNo({}), cause:{}", workNo, Throwables.getStackTraceAsString(e));
        }
        return resp;
    }

    /**
     * 保存用户
     *
     * @param hacUserDTO HacUserDTO对象
     */
    private User saveUser(HacUserDTO hacUserDTO) {
        User u = toUser(hacUserDTO);
        userDao.create(u);
        return u;
    }

    @Override
    public Response<List<TeamMemeberDto>> findStaffByName(String name) {
        Response<List<TeamMemeberDto>> resp = new Response<List<TeamMemeberDto>>();
        try {
            if (Strings.isNullOrEmpty(name)) {
                log.error("find name can't be empty");
                resp.setError("purchaser.name.empty");
                return resp;
            }
            if (name.length() < 2) {
                log.error("findStaffByName's name length must >= 2.");
                resp.setError("purchaser.name.length.lt2");
                return resp;
            }
            String decodeName = URLDecoder.decode(name, "UTF-8");
            ExecuteResult<List<HacUserDTO>> res = hacUserServiceCli.searchIDMUser("", decodeName, "IN");
            if (!res.isSuccess()) {
                log.error("failed to find purchaser by name, cause: {}", res.getErrorMessages());
                resp.setError(res.getErrorMessages().get(0));
                return resp;
            }
            List<HacUserDTO> hacUserDTOs = res.getResult();
            // 得到<工号, 组织> map
            Map<String, String> workNoOrganMap = toWorkNoOrganMap(hacUserDTOs);
            List<User> users = toUsers(hacUserDTOs);
            //保存未存在的用户
            List<User> savedUsers = saveUsers(users);
            List<TeamMemeberDto> teamMemebers = Lists.newArrayListWithCapacity(savedUsers.size());
            TeamMemeberDto teamMemeber;
            User user;
            for (int i = 0; i < savedUsers.size(); i++) {
                user = savedUsers.get(i);
                teamMemeber = new TeamMemeberDto(user);
                teamMemeber.setOrgan(workNoOrganMap.get(user.getNick()));
                teamMemebers.add(teamMemeber);
            }
            resp.setResult(teamMemebers);
        } catch (Exception e) {
            log.error("failed to find staff by name({}), cause: {}",
                    name, Throwables.getStackTraceAsString(e));
            resp.setError("user.findstaff.fail");
        }
        return resp;
    }

    /**
     * 得到<工号, 组织> 映射
     *
     * @param hacUserDTOs HacUserDTO列表
     * @return <工号, 组织> 映射
     */
    private Map<String, String> toWorkNoOrganMap(List<HacUserDTO> hacUserDTOs) {
        Map<String, String> workNoOrganMap = Maps.newHashMapWithExpectedSize(hacUserDTOs.size());
        for (HacUserDTO hacUserDTO : hacUserDTOs) {
            workNoOrganMap.put(hacUserDTO.getName(), hacUserDTO.getUserOU());
        }
        return workNoOrganMap;
    }

    /**
     * 将不存在的用户保存在本地
     *
     * @param users 用户列表
     * @return 经过创建过滤后的用户对象列表
     */
    private List<User> saveUsers(List<User> users) {
        if (!Iterables.isEmpty(users)) {
            List<String> nicks = Lists.newArrayListWithCapacity(users.size());
            for (User u : users) {
                nicks.add(u.getNick());
            }
            // existed users
            List<User> existedUsers = userDao.findByNicks(nicks);
            // existed nicks
            List<String> existedNicks = Lists.newArrayList();
            for (User u : existedUsers) {
                existedNicks.add(u.getNick());
            }

            // non existed nicks
            nicks.removeAll(existedNicks);
            // non existed users
            List<User> nonExistedUsers = Lists.newArrayListWithCapacity(users.size() - existedUsers.size());
            // save non existed users
            for (User u : users) {
                if (nicks.contains(u.getNick())) {
                    userDao.create(u);
                    nonExistedUsers.add(u);
                }
            }
            // combine
            existedUsers.addAll(nonExistedUsers);
            return existedUsers;
        }
        return Collections.emptyList();
    }

    /**
     * 转换HacUserDTO对象列表为PurchaserExtra对象列表
     *
     * @param hacUserDTOs HacUserDTO对象列表
     * @return 用户对象列表
     */
    private List<User> toUsers(List<HacUserDTO> hacUserDTOs) {
        if (hacUserDTOs == null || Iterables.isEmpty(hacUserDTOs)) {
            return Collections.emptyList();
        }
        List<User> users = Lists.newArrayListWithCapacity(hacUserDTOs.size());
        for (HacUserDTO hacUserDTO : hacUserDTOs) {
            users.add(toUser(hacUserDTO));
        }
        return users;
    }

    /**
     * 转换HacUserDTO为User对象
     *
     * @param hacUserDTO HacUserDTO对象
     * @return User对象
     */
    private User toUser(HacUserDTO hacUserDTO) {
        User u = new User();
        u.setOuterId(hacUserDTO.getId());
        u.setNick(hacUserDTO.getName());        //HacUserDTO.name为nick
        u.setName(hacUserDTO.getNickName());    //HacUserDTO.nickname为name
        u.setEncryptedPassword(Strings.isNullOrEmpty(hacUserDTO.getPassword()) ? "" : hacUserDTO.getPassword());
        u.setType(User.Type.PURCHASER.value());
        u.setRoleStr(User.Type.PURCHASER.name());
        u.setEmail(Strings.isNullOrEmpty(hacUserDTO.getEmail()) ? "无" : hacUserDTO.getEmail());
        u.setMobile(Strings.isNullOrEmpty(hacUserDTO.getPhone()) ? "无" : hacUserDTO.getPhone());
        u.setOrigin(User.Origin.HAIER_PORTAL.value());
        u.setApproveStatus(User.ApproveStatus.OK.value());
        u.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        //u.setQualifyStatus(User.QualifyStatus.SUBMITTED.value());
        u.setStatus(User.Status.OK.value());
        return u;
    }

    /**
     * 将UserMergeDTO转换为User对象
     *
     * @param userMergeDTO UserMergeDTO对象
     * @return User对象
     */
    private User toUser(UserMergeDTO userMergeDTO) {
        User u = new User();
        u.setOuterId(userMergeDTO.getId());
        u.setName(userMergeDTO.getNickName()); //nickname为真实姓名
        u.setNick(userMergeDTO.getUserName()); //username为工号
        u.setEncryptedPassword(Strings.isNullOrEmpty(userMergeDTO.getPassword()) ? "" : userMergeDTO.getPassword());
        u.setType(User.Type.PURCHASER.value());
        u.setRoleStr(User.Type.PURCHASER.name());
        u.setEmail(Strings.isNullOrEmpty(userMergeDTO.getEmaile()) ? "无" : userMergeDTO.getEmaile());
        u.setMobile(Strings.isNullOrEmpty(userMergeDTO.getMobile()) ? "无" : userMergeDTO.getMobile());
        u.setOrigin(User.Origin.HAIER_PORTAL.value());
        u.setApproveStatus(User.ApproveStatus.OK.value());
        u.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        //u.setQualifyStatus(User.QualifyStatus.SUBMITTED.value());
        u.setStatus(User.Status.OK.value());
        return u;
    }

    private User toUserForFix(UserMergeDTO userMergeDTO) {
        User u = new User();
        u.setOuterId(userMergeDTO.getId());
        u.setName(userMergeDTO.getNickName());
//        u.setNick(userMergeDTO.getUserName()); // 工号，needless
        u.setEncryptedPassword(Strings.isNullOrEmpty(userMergeDTO.getPassword())? "" : userMergeDTO.getPassword());
//        u.setType(User.Type.PURCHASER.value());
        u.setEmail(Strings.isNullOrEmpty(userMergeDTO.getEmaile()) ? "无" : userMergeDTO.getEmaile());
        u.setMobile(Strings.isNullOrEmpty(userMergeDTO.getMobile()) ? "无" : userMergeDTO.getMobile());
//        u.setApproveStatus(User.ApproveStatus.OK.value());
//        u.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
//        u.setStatus(User.Status.OK.value());
        return u;
    }

    /**
     * 从ssoSessionId中抽出ip
     *
     * @param ssoSessionId session id, 如7D02CD5B2F0E75FDDDE6FCC5527768D6-10.135.106.115
     * @return ip
     */
    private static String extractIp(String ssoSessionId) {
        return ssoSessionId.substring(ssoSessionId.indexOf('-') + 1);
    }

    @Override
    public Response<Long> createSupplier(RichSupplierDto richSupplierDto) {
        Response<Long> resp = new Response<Long>();
        try {
            checkSupplierDtoAndInitUser(richSupplierDto);

            Long userId = accountManagerHac.createSupplier(richSupplierDto);
            resp.setResult(userId);
        } catch (IllegalStateException e) {
            // IllegalStateException 是走 cas 注册是时候 checkState()抛出的
            log.error("create supplier failed due to cas:{}", Throwables.getStackTraceAsString(e));
            resp.setError(e.getMessage());
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to create supplier: {}", Throwables.getStackTraceAsString(e));
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

            Long userId = accountManagerHac.createSupplier(richSupplierDto);
            resp.setResult(userId);
        } catch (IllegalStateException e) {
            // IllegalStateException 是走 cas 注册是时候 checkState()抛出的
            log.error("create supplier failed due to cas:{}", Throwables.getStackTraceAsString(e));
            resp.setError(e.getMessage());
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
    public Response<Boolean> activeUser(BaseUser user, String code) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            if (user == null) {
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            if (Strings.isNullOrEmpty(code)) {
                log.error("user active code can't be empty");
                resp.setError("user.activecode.empty");
                return resp;
            }
            UserIDSDTO userIDS = newUserIDSDTO();
            userIDS.setAttributeName(USER_VERIFY_MOBILE);
            userIDS.setActivationCode(code);
            ExecuteResult<String> res = hacUserServiceClient.activeUser(userIDS);
            if (!res.isSuccess()) {
                log.error("failed to active user(nick={}), active code={}, cause: {}",
                        user.getNickName(), code, res.getResult());
                resp.setError("user.active.fail");
                return resp;
            }
            resp.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to active user(code={}), cause: {}",
                    code, Throwables.getStackTraceAsString(e));
            resp.setError("user.active.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> updateUser(User user) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            checkParamsUpdateUser(user);
            accountManagerHac.updateUser(user);
            resp.setResult(Boolean.TRUE);
            userCache.invalidate(user.getId());
        } catch (Exception e) {
            log.error("failed to update user({}), cause: {}",
                    user, Throwables.getStackTraceAsString(e));
            resp.setError("user.update.failed");
        }
        return resp;
    }

    /**
     * 参数验证
     */
    private void checkParamsUpdateUser(User user) {
        if (user == null) {
            log.error("user isn't login");
            throw new ServiceException("user.not.login");
        }
        if (Strings.isNullOrEmpty(user.getNick())) {
            log.error("user's nick can't be empty");
            throw new ServiceException("user.nick.null");
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

    @Override
    public Response<Boolean> changePassword(BaseUser baseUser, String oldPassword, String newPassword, String confirmPassword) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            Response<User> userRes = findUserById(checkNotNull(baseUser).getId());
            if (!userRes.isSuccess()) {
                log.error("user not login");
                resp.setError(userRes.getError());
                return resp;
            }
            // 供应商特判，走CAS修改密码接口
            if (Objects.equal(User.Type.SUPPLIER.value(), userRes.getResult().getType())) {
                checkParamsChgPwdForSupplier(userRes.getResult(), oldPassword, newPassword, confirmPassword);
                accountManagerHac.changePasswordForSupplier(userRes.getResult(), oldPassword, newPassword);
                userCache.invalidate(userRes.getResult().getId());
            } else {
                checkParamsChgPwd(baseUser, oldPassword, newPassword, confirmPassword);
                // TODO 直接更新HAC, 待接口完善
            }

            resp.setResult(Boolean.TRUE);
        } catch (IllegalStateException e) {
            // 供应商走CAS修改密码错误处理
            log.error("failed change password in cas, msg:{}", e.getMessage());
        } catch (ServiceException e) {
            resp.setError(e.getMessage());
        } catch (Exception e) {
            log.error("failed to change user({})'s password, old={}, new={}, confirm={}",
                    baseUser, oldPassword, newPassword, confirmPassword);
            resp.setError("user.chgpwd.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> resendActiveCode(BaseUser user) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            if (user == null) {
                log.error("user is not login");
                resp.setError("user.not.login");
                return resp;
            }
            UserIDSDTO userIDSDTO = newUserIDSDTO();
            userIDSDTO.setUserName(user.getNickName());
            userIDSDTO.setAttributeName(USER_VERIFY_MOBILE);
            userIDSDTO.setAttributeValue(user.getMobile());
            ExecuteResult<String> res = hacUserServiceClient.resendActiveCode(userIDSDTO);
            if (!res.isSuccess()) {
                log.error("failed to send active code to user(nick={}), cause: {}",
                        user.getNickName(), res.getResult());
                resp.setError("user.sendactivecode.fail");
                return resp;
            }
            resp.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to resend active code to user({}), cause: {}",
                    user, Throwables.getStackTraceAsString(e));
            resp.setError("user.resendactivecode.fail");
        }
        return resp;
    }

    /**
     * 校验参数, 进行一次HAC登录, 以验证旧密码正确否
     */
    private void checkParamsChgPwd(BaseUser user, String oldPassword, String newPassword, String confirmPassword) {
        if (user == null) {
            log.error("user isn't login");
            throw new ServiceException("user.not.login");
        }
        if (Strings.isNullOrEmpty(oldPassword)) {
            log.error("user's old password can't be empty");
            throw new ServiceException("user.oldpwd.empty");
        }
        // do once login
        Response<UserDto> resp = login(user.getNickName(), oldPassword);
        if (!resp.isSuccess()) {
            log.error("failed to login with user({}) and old password({})", user.getNickName(), oldPassword);
            throw new ServiceException(resp.getError());
        }
        if (Strings.isNullOrEmpty(newPassword)) {
            log.error("user's new password can't be empty");
            throw new ServiceException("user.newpwd.empty");
        }
        if (Strings.isNullOrEmpty(confirmPassword)) {
            log.error("user's confirm password can't be empty");
            throw new ServiceException("user.confirmpwd.empty");
        }
        if (!Objects.equal(newPassword, confirmPassword)) {
            log.error("new password({}) isn't equal to confirm password({})", newPassword, oldPassword);
            throw new ServiceException("user.newconpwd.notmatch");
        }
    }

    private void checkParamsChgPwdForSupplier(User user, String oldPassword, String newPassword, String confirmPassword) {
        if (user == null) {
            log.error("user not login");
            throw new ServiceException("user.not.login");
        }
        if (Strings.isNullOrEmpty(oldPassword)) {
            log.error("user's old password can't be empty");
            throw new ServiceException("user.oldpwd.empty");
        }
//        if (!PasswordUtil.passwordMatch(oldPassword, user.getEncryptedPassword())) {
//            log.error("password not match for user={}", user);
//            throw new ServiceException("user.passwd.not.fit");
//        }
        if (Strings.isNullOrEmpty(newPassword)) {
            log.error("user's new password can't be empty");
            throw new ServiceException("user.newpwd.empty");
        }
//        if (Strings.isNullOrEmpty(confirmPassword)) {
//            log.error("user's confirm password can't be empty");
//            throw new ServiceException("user.confirmpwd.empty");
//        }
//        if (!Objects.equal(newPassword, confirmPassword)){
//            log.error("new password({}) isn't equal to confirm password({})", newPassword, oldPassword);
//            throw new ServiceException("user.newconpwd.notmatch");
//        }

    }

    /**
     * 登录参数验证
     *
     * @param nick     用户名
     * @param password 密码
     * @throws ServiceException
     */
    private void checkParamsLogin(String nick, String password) throws ServiceException {
        if (Strings.isNullOrEmpty(nick)) {
            throw new ServiceException("user.nick.empty");
        }
        if (Strings.isNullOrEmpty(password)) {
            throw new ServiceException("user.password.empty");
        }
    }

    /**
     * 将本地User转换为IDS用户, 仅设置IDS应用信息
     *
     * @return IDS用户
     */
    public static UserIDSDTO newUserIDSDTO() {
        UserIDSDTO userIDSDTO = new UserIDSDTO();
        userIDSDTO.setCoSessionId(CONF.coSessionId);
        userIDSDTO.setRegistSrc(CONF.idsAppName);
        userIDSDTO.setPasswdKey(CONF.passwdKey);
        return userIDSDTO;
    }

    /**
     * 构建UserMergeDTO, 仅设置IDS应用信息, 且未IDS用户
     *
     * @return UserMergeDTO对象
     */
    public static UserMergeDTO newUserMergeDTO() {
        UserMergeDTO userMergeDTO = new UserMergeDTO();
        userMergeDTO.setRegistSrc(LOGIN_BY_IDS);
        userMergeDTO.setAppName(CONF.idsAppName);
        userMergeDTO.setCoSessionId(CONF.coSessionId);
        userMergeDTO.setPasswdKey(CONF.passwdKey);
        return userMergeDTO;
    }

    private static class Configuration {
        private String coSessionId;     //ids应用编号
        private String idsAppName;      //ids应用名称
        private String passwdKey;        //ids应用key
    }
}
