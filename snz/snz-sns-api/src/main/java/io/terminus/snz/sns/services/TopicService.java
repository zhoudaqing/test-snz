package io.terminus.snz.sns.services;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.sns.dtos.RequirementCount;
import io.terminus.snz.sns.dtos.TopicDetail;
import io.terminus.snz.sns.models.Topic;

import java.util.List;

/**
 * 话题服务接口
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 30/4/14.
 */
public interface TopicService {
    /**
     * 创建话题
     * @param user 当前用户
     * @param t 话题对象
     * @param joinerIds 参与者id列表
     * @return 创建记录数目
     */
    @Export(paramNames = {"user", "t", "joinerIds"})
    Response<Integer> create(BaseUser user, Topic t, String joinerIds);

    /**
     * 创建默认的话题
     * @param user
     * @return 创建记录数
     */
    @Export(paramNames = {"user", "t"})
    Response<Integer> createDefault(BaseUser user, Topic t);

    /**
     * 获取话题
     * @param user 当前用户
     * @param id 话题主键
     * @param isView 是否增加浏览量, 默认增加 Boolean.FALSE不增加
     * @return 话题对象
     */
    @Export(paramNames = {"user", "id", "isView"})
    Response<TopicDetail> findById(BaseUser user, Long id, Boolean isView);

    /**
     * 分页获取公开的话题
     * @param user 当前登录用户
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @return 话题分页对象
     */
    @Export(paramNames = {"user", "reqId", "reqStatus", "pageNo", "pageSize"})
    Response<Paging<Topic>> pagingPublics(BaseUser user, Long reqId, String reqStatus,
                                          Integer pageNo, Integer pageSize);

    /**
     * 获取某用户参与的话题分页对象
     * @param user 当前登录用户
     * @param joinerId 参与用户id
     * @param pageNo 页号
     * @param pageSize 分页大小
     * @return 用户参与的话题分页对象
     */
    @Export(paramNames = {"user", "joinerId", "pageNo", "pageSize"})
    Response<Paging<Topic>> pagingByJoinerId(BaseUser user, Long joinerId, Integer pageNo, Integer pageSize);

    /**
     * 浏览话题, 浏览量+1
     * @param id 话题id
     * @return 更新成功TRUE, 反之FALSE
     */
    @Export(paramNames = {"id"})
    Response<Boolean> viewed(Long id);

    /**
     * 回复了话题, 回复量+1
     * @param id 话题id
     * @return 更新成功TRUE, 反之FALSE
     */
    @Export(paramNames = {"id"})
    Response<Boolean> replied(Long id);

    /**
     * 获取话题的统计信息，如话题数，参与供应商人数
     * @param ids 话题id列表, 以逗号隔开
     * @return 话题列表统计信息
     */
    @Export(paramNames = {"ids"})
    Response<List<RequirementCount>> getRequirementsCountInfo(String ids);
}
