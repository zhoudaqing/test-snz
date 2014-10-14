package io.terminus.snz.sns.daos.mysql;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.sns.models.Topic;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 话题dao
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
@Repository
public class TopicDao extends SqlSessionDaoSupport{

    /**
     * 创建话题
     * @param t 话题对象
     * @return 插入记录数
     */
    public int create(Topic t){
        return getSqlSession().insert("Topic.create", t);
    }

    /**
     * 软删除话题
     * @param id 话题主键
     * @return 软删除记录数
     */
    public int setDelete(Long id){
        return getSqlSession().update("Topic.setDelete", id);
    }

    /**
     * 批量软删除话题
     * @param ids 话题主键列表
     * @return 软删除记录数
     */
    public int setDeletes(List<Long> ids) {
        return getSqlSession().update("Topic.setDeletes", ids);
    }

    /**
     * 关闭话题
     * @param id 话题id
     * @return 关闭记录数
     */
    public int setClose(Long id){
        return getSqlSession().update("Topic.setClose", id);
    }

    /**
     * 批量关闭话题
     * @param ids 话题列表ids
     * @return 关闭记录数
     */
    public int setCloses(List<Long> ids){
        return getSqlSession().update("Topic.setCloses", ids);
    }

    /**
     * 删除话题
     * @param id 话题主键
     * @return 删除记录数
     */
    public int delete(Long id){
        return getSqlSession().delete("Topic.delete", id);
    }

    /**
     * 批量删除话题
     * @param ids 话题主键列表
     * @return 删除记录数
     */
    public int deletes(List<Long> ids) {
        return getSqlSession().delete("Topic.deletes", ids);
    }

    /**
     * 更新话题
     * @param t 话题对象
     * @return 更新记录数
     */
    public int update(Topic t){
        return getSqlSession().update("Topic.update", t);
    }

    /**
     * 获取话题
     * @param id 话题主键
     * @return 话题对象
     */
    public Topic findById(Long id){
        return getSqlSession().selectOne("Topic.findById", id);
    }

    /**
     * 获取话题列表
     * @param ids 话题主键列表
     * @return 话题列表
     */
    public List<Topic> findByIds(List<Long> ids){
        return getSqlSession().selectList("Topic.findByIds", ids);
    }

    /**
     * 分页查询话题
     * @param criteria 查询条件，如状态reqStatus
     * @param offset
     * @param limit
     * @return
     */
    public Paging<Topic> paging(Map<String, Object> criteria, Integer offset, Integer limit){
        Map<String, Object> pagingParams = Maps.newHashMap();
        if (criteria != null){
            pagingParams.putAll(criteria);
        }
        Long total = getSqlSession().selectOne("Topic.count", pagingParams);
        if(total == 0) {
            return new Paging<Topic>(0L, Collections.<Topic>emptyList());
        }
        pagingParams.put("offset", offset);
        pagingParams.put("limit", limit);
        List<Topic> data = getSqlSession().selectList("Topic.pagination", pagingParams);
        return new Paging<Topic>(total, data);
    }

    /**
     * 分页查询某需求下我可见的话题：公开话题和私有话题(在圈子内)
     * @param criteria 查询条件，如状态reqStatus
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 话题分页对象
     */
    public Paging<Topic> pagingOfReq(Map<String, Object> criteria, Integer offset, Integer limit){
        Map<String, Object> pagingParams = Maps.newHashMap();
        if (criteria != null){
            pagingParams.putAll(criteria);
        }
        Long total = getSqlSession().selectOne("Topic.countOfReq", pagingParams);
        if(total == 0) {
            return new Paging<Topic>(0L, Collections.<Topic>emptyList());
        }
        pagingParams.put("offset", offset);
        pagingParams.put("limit", limit);
        List<Topic> data = getSqlSession().selectList("Topic.paginationOfReq", pagingParams);
        return new Paging<Topic>(total, data);
    }

    /**
     * 获取某用户参与的话题分页对象
     * @param joinerId 参与用户id
     * @param offset   起始偏移
     * @param limit    分页大小
     * @return 用户参与的话题分页对象
     */
    public Paging<Topic> pagingByJoinerId(Long joinerId, Integer offset, Integer limit){
        Long total = getSqlSession().selectOne("Topic.countByJoinerId", joinerId);
        if(total == 0) {
            return new Paging<Topic>(0L, Collections.<Topic>emptyList());
        }
        List<Topic> data = getSqlSession().selectList("Topic.paginationByJoinerId",
                ImmutableMap.of("userId", joinerId, "offset", offset, "limit", limit));
        return new Paging<Topic>(total, data);
    }

    /**
     * 更新浏览量+1
     * @param id 话题id
     */
    public void viewed(Long id) {
        getSqlSession().update("Topic.viewed", id);
    }

    /**
     * 话题浏览量+1
     * @param id 话题id
     */
    public void replied(Long id) {
        getSqlSession().update("Topic.replied", id);
    }

    /**
     * 统计需求下的话题数，注意不能保证顺序, 按需求id降序
     * @param ids 需求id列表
     * @return 对应需求的话题数列表
     */
    public List<Long> countRequirementTopics(List<Long> ids){
        return getSqlSession().selectList("Topic.countRequirementTopics", ids);
    }
}
