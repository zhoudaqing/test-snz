package io.terminus.snz.message.services;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.dtos.MessagePagingView;
import io.terminus.snz.message.models.Message;

import java.util.List;

/**
 * 消息服务接口
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-26
 */
public interface MessageService {
    /**
     * 获取用户未读消息数
     * @param uid 用户id
     * @return 未读消息数
     */
    @Export(paramNames = {"user"})
    Response<Long> newMsgCount(BaseUser user);

    /**
     * 获取消息对象
     * @param user 当前登录用户
     * @param id 消息id
     * @return 消息对象
     */
    @Export(paramNames = {"user","id"})
    Response<Message> findById(BaseUser user, Long id);

    /**
     * 获取最近消息列表
     * @param  uid 用户id
     * @param limit 限制大小
     * @return 最近limit个消息列表
     */
    @Export(paramNames = {"user", "limit"})
    Response<List<Message>> lastest(BaseUser user, Integer limit);

    /**
     * 从未读消息队列中移除消息id
     * @param user 当前用户
     * @param mid 消息id
     * @return 移除记录数
     */
    @Export(paramNames = {"user", "mid"})
    Response<Long> rmNewMsgId(BaseUser user, Long mid);

    /**
     * 从用户的消息列表中移除消息
     * @param user 当前用户
     * @param ids 消息id列表, 逗号隔开
     * @return 移除成功返回TRUE, 反之FALSE
     */
    @Export(paramNames = {"user", "ids"})
    Response<Boolean> rmMsgs(BaseUser user, String ids);

    /**
     * 标记用户消息已读
     * @param user 当前用户
     * @param ids 消息id列表, 逗号隔开
     * @return 移除成功返回TRUE, 反之FALSE
     */
    @Export(paramNames = {"user", "ids"})
    Response<Boolean> read(BaseUser user, String ids);

    /**
     * 物理删除消息
     * @param  user 当前用户
     * @param mid 消息id
     * @return 删除记录数
     */
    @Export(paramNames = {"user", "mid"})
    Response<Integer> delete(BaseUser user, Long mid);

    /**
     * 分页获取消息
     * @param user 当前登录用户
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @return 话题消息分页对象
     */
    @Export(paramNames = {"user","pageNo","pageSize", "status"})
    Response<MessagePagingView> paging(BaseUser user, Integer pageNo, Integer pageSize, Integer status);

    /**
     * 推送消息
     * @param type 消息类型
     * @param uid 当前用户id
     * @param receiverId 消息接收用户id
     * @param datas 消息模版数据，参照templates/对应的模版, 可以是Map或对象
     * @return 推送成功返回TRUE, 反之FALSE
     */
    Response<Boolean> push(Message.Type type, Long uid, Long receiverId, Object datas);

    /**
     * 推送消息
     * @param type 消息类型
     * @param uid 当前用户id
     * @param receiverIds 消息接收用户id列表
     * @param datas 消息模版数据，参照templates/对应的模版, 可以是Map或对象
     * @return 推送成功返回TRUE, 反之FALSE
     */
    Response<Boolean> push(Message.Type type, Long uid, List<Long> receiverIds, Object datas);
}

