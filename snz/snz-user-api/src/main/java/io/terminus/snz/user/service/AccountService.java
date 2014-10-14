package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.User;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-5.
 */
public interface AccountService<T extends BaseUser> {

    /**
     * 根据id寻找user
     *
     * @param id 用户id
     * @return 用户对象
     */
    @Export(paramNames = {"id"})
    Response<T> findUserById(Long id);

    /**
     * 根据昵称查找user
     *
     * @param nick 昵称
     * @return 用户对象
     */
    @Export(paramNames = {"nick"})
    Response<T> findUserByNick(String nick);

    /**
     * 更新用户对象
     *
     * @param user 用户
     */
    @Export(paramNames = {"user"})
    Response<Boolean> updateUser(T user);

    /**
     * 根据roleStr查询用户
     * @param role roleStr
     * @return 用户列表
     */
    Response<List<T>> findUserByRoleStr(String role);

    /**
     * 用户登录
     *
     * @param nick     昵称
     * @param password 密码
     * @return 用户
     */
    @Export(paramNames = {"nick", "password"})
    public Response<UserDto> login(String nick, String password);

    /**
     * TODO: 非常恶心的方法实现注册后自动登录，以后一定要重构下！
     *
     * @param nick     昵称
     * @param password 密码
     * @return 用户
     */
    @Export(paramNames = {"nick", "password"})
    public Response<UserDto> dirtyLogin(String nick, String password);

    /**
     * 用户登出
     *
     * @param ssoSessionId 登录返回session, 如7D02CD5B2F0E75FDDDE6FCC5527768D6-10.135.106.115
     */
    @Export(paramNames = {"ssoSessionId"})
    public Response<Boolean> logout(String ssoSessionId);

    /**
     * 检查用户是否存在
     *
     * @param nick 昵称
     * @return 是否存在
     */
    @Export(paramNames = {"nick"})
    public Response<Boolean> userExists(String nick);

    /**
     * 更改密码
     *
     * @param baseUser        用户
     * @param oldPassword     老密码
     * @param newPassword     新密码
     * @param confirmPassword 确认密码
     * @return 是否更新成功
     */
    @Export(paramNames = {"baseUser", "oldPassword", "newPassword", "confirmPassword"})
    Response<Boolean> changePassword(BaseUser baseUser, String oldPassword, String newPassword, String confirmPassword);

    /**
     * 更新用户的状态
     *
     * @param id     用户id
     * @param status 是否更新成功
     */
    @Export(paramNames = {"id", "status"})
    Response<Boolean> updateStatus(Long id, Integer status);

    /**
     * 根据id查找采购商
     *
     * @param id 用户id
     * @return 采购商详细信息
     */
    @Export(paramNames = {"id"})
    public Response<PurchaserDto> findPurchaserById(Long id);

    /**
     * 供应商审核
     *
     * @param baseUser           当前操作用户
     * @param supplierApproveDto 审核信息
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser", "supplierApproveDto"})
    public Response<Boolean> approveSupplier(BaseUser baseUser, SupplierApproveDto supplierApproveDto);

    /**
     * 检查供应商登录后是否需要上传三证
     *
     * @param baseUser 供应商
     * @return 是：true，否：false
     */
    @Export(paramNames = {"baseUser"})
    public Response<Boolean> needCommitPaperwork(BaseUser baseUser);

    /**
     * 检查用户能否进入供应商后台
     *
     * @param baseUser 用户
     * @return 能：true，反之，false
     */
    @Export(paramNames = {"baseUser"})
    public Response<Boolean> inSupplierConsole(BaseUser baseUser);

    /**
     * 供应商入驻
     *
     * @param richSupplierDto 供应商信息
     * @return 用户编号
     */
    @Export(paramNames = {"richSupplierDto"})
    public Response<Long> createSupplier(RichSupplierDto richSupplierDto);

    /**
     * 激活用户
     *
     * @param user 当前用户
     * @param code 激活码
     * @return 激活成功返回TRUE, 反之FALSE
     */
    @Export(paramNames = {"user", "code"})
    Response<Boolean> activeUser(BaseUser user, String code);

    /**
     * 再次发送激活码到用户手机
     *
     * @param user 当前用户
     * @return 激活成功返回TRUE, 反之FALSE
     */
    @Export(paramNames = {"user"})
    Response<Boolean> resendActiveCode(BaseUser user);

    /**
     * 创建从百卓接入的供应商
     *
     * @param richSupplierDto 供应商信息
     * @return 用户编号
     */
    @Export(paramNames = {"richSupplierDto"})
    public Response<Long> createBizSupplier(RichSupplierDto richSupplierDto);

    /**
     * 根据用户编号获取所有的用户信息
     *
     * @param ids 用户编号
     * @return T
     * 返回用户信息
     */
    @Export(paramNames = {"ids"})
    Response<List<User>> findUserByIds(List<Long> ids);

    /**
     * 根据用户编号查询供应商的审批额外信息
     *
     * @param userId 用户编号
     * @return 审批额外信息
     */
    @Export(paramNames = {"userId"})
    public Response<SupplierApproveExtra> findSupplierApproveExtraByUserId(Long userId);

    /**
     * 根据工号查询姓名，现在就海尔内部用户这边会用
     *
     * @param workNo 工号
     * @return 员工姓名, 结果唯一
     */
    @Export(paramNames = {"workNo"})
    public Response<TeamMemeberDto> findStaffByWorkNo(String workNo);

    /**
     * 通过姓名模糊查找采购商，现在就海尔内部人员
     *
     * @param name 员工姓名
     * @return 员工信息列表
     */
    @Export(paramNames = {"name"})
    public Response<List<TeamMemeberDto>> findStaffByName(String name);

    /**
     * 统计入驻审核通过的供应商数量
     *
     * @return 供应商数量
     */
    @Export
    public Response<Long> countEnterPassSupplier();

    /**
     * 重新提交审核
     *
     * @return 是否成功
     */
    @Export(paramNames = {"baseUser"})
    public Response<Boolean> reCommitApproval(BaseUser baseUser);

    /**
     * 查找当前用户上级, 上级未登录则返回其工号，否则返回名字
     *
     * @param user 当前用户
     * @return 上级工号或姓名
     */
    @Export(paramNames = {"user"})
    public Response<String> findLeader(BaseUser user);

    /**
     * 提醒采购商对供应商审核（入驻和修改审核）
     *
     * @return 是否成功
     */
    public Response<Boolean> remindOfApproving();

}
