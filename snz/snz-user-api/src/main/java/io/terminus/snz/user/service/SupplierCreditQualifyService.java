package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierCreditQualifyDto;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.model.User;

import java.util.List;

/**
 * Date: 7/31/14
 * Time: 11:03
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface SupplierCreditQualifyService {

    /**
     * 查找一个用户的信用等级记录，用于用户登录后查看信用等级核审状态
     *
     * @param userId    用户的ID
     * @return          一条用户信用等级记录
     */
    @Export(paramNames = "userId")
    public Response<SupplierCreditQualify> findCreditQualifyByUserId(Long userId);

    /**
     * 根据用户编号列表查询用户信用等级信息
     * @param userIds   用户编号
     * @return  List
     * 返回用户等级信息列表
     */
    public Response<List<SupplierCreditQualify>> findCreditQualifyByUserIds(List<Long> userIds);

    /**
     * 提交信用等级核审请求
     *
     * @param currentUser    当前登录用户
     * @return               成功创建后返回记录的ID
     */
    @Export(paramNames = "currentUser")
    public Response<Long> applyCreditQualify(BaseUser currentUser);

    /**
     * 系统自动通过供应商信用等级审核
     *
     * @param uid        审核的供应商 user id
     * @param msg        附加消息
     * @param status     审核状态
     * @return 操作是否成功
     */
    public Response<Boolean> systemApplyCreditQualify(Long uid, String msg, Integer status);

    /**
     * 供应商申诉被驳回的信用等级毕竟
     *
     * @param currentUser    当前登录用户
     * @param message        申诉消息
     * @return 当前记录的id
     */
    @Export(paramNames = {"currentUser", "message"})
    public Response<Long> appealCreditQualify(BaseUser currentUser, String message);

    /**
     * 更新一条用户信用等级核审记录
     *
     * @param user      当前登录用户
     * @param id        更新记录的 id
     * @param msg       更新的消息
     * @param status    更新记录的状态
     * @return
     */
    @Export(paramNames = {"user", "id", "msg", "status"})
    public Response<Boolean> updateCreditQualify(BaseUser user, Long id, String msg, Integer status);

    /**
     * 分页显示用户的信用等级核审记录
     *
     * @param currentUser    当前登录的用户
     * @param type           根据当前用户信用等级审核状态筛选，申诉、通过、未通过
     * @param pageNo         当前分页
     * @param size           分页大小
     * @return               分页后的用户的信用等级核审记录
     */
    @Export(paramNames = {"currentUser", "type", "pageNo", "size"})
    public Response<Paging<SupplierCreditQualifyDto>> pagingCreditQualify(
            BaseUser currentUser, Integer type, Integer pageNo, Integer size);

    /**
     * 二级财务驳回供应商应用等级验证
     *
     * @param user    当前登陆的二级财务
     * @param id      驳回的信用等级记录id
     * @return        操作是否成功
     */
    @Export(paramNames = {"user", "id"})
    public Response<Boolean> rejectQualify(User user, Long id);

    /**
     * 被定时任务调用，通知快要过期的供应商信用等级评价
     *
     * @param days    超过指定天数就超期
     * @return 操作是否成功
     */
    Response<Boolean> notifyUpcomingIn(Integer days);

    /**
     * 被定时任务调用，通知过期一天的供应商信用等级评价
     *
     * @return 操作是否成功
     */
    Response<Boolean> notifyDelayed(Integer days);
}
