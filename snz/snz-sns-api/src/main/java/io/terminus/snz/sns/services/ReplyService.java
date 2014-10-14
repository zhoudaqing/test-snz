package io.terminus.snz.sns.services;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.sns.dtos.FatReply;
import io.terminus.snz.sns.models.Reply;
import io.terminus.snz.sns.models.Topic;

/**
 * 话题服务接口
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 30/4/14.
 */
public interface ReplyService {
    /**
     * 创建回复
     * @param r 回复对象
     * @return 创建记录数
     */
    @Export(paramNames = {"user", "r"})
    Response<Reply> create(BaseUser user, Reply r);

    /**
     * 简单分页查询回复, 不涉及子回复
     * @param user 当前用户
     * @param topicId 话题id
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @return 回复列表
     */
    @Export(paramNames = {"user", "topicId", "pageNo", "pageSize"})
    Response<Paging<FatReply>> pagingSimple(BaseUser user,
                                         Long topicId,
                                         Integer pageNo,
                                         Integer pageSize);

    /**
     * 判断需求和话题的状态是否一致，内部用
     * @param t 话题对象
     * @return 一致返回True, 反之False
     */
    public boolean validReqTopicStatus(Topic t);
}
