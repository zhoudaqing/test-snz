package io.terminus.snz.sns.daos.mysql;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.sns.dtos.TopicFriend;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.sns.models.TopicUser;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 话题_用户中间表Dao
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
@Repository
public class TopicUserDao extends SqlSessionDaoSupport {

    /**
     * 创建
     * @param tu 话题用户关联对象
     * @return 删除记录数
     */
    public int create(TopicUser tu){
        return getSqlSession().delete("TopicUser.create",tu);
    }

    /**
     * 批量创建话题参与者关联对象
     * @param tus 话题参与者关联对象列表
     * @return 创建记录数
     */
    public int createBatch(List<TopicUser> tus) {
        return getSqlSession().insert("TopicUser.createBatch", tus);
    }

    /**
     * 删除
     * @param tu 话题用户关联对象
     * @return 删除记录数
     */
    public int delete(TopicUser tu){
        return getSqlSession().delete("TopicUser.delete", tu);
    }

    /**
     * 根据topicId删除
     * @param topicId 话题id
     * @return 删除记录数
     */
    public int deleteByTopicId(Long topicId) {
        return getSqlSession().delete("TopicUser.deleteByTopicId", topicId);
    }

    /**
     * 根据topicIds批量删除
     * @param topicIds 话题id
     * @return 删除记录数
     */
    public int deleteByTopicIds(List<Long> topicIds) {
        return getSqlSession().delete("TopicUser.deleteByTopicIds", topicIds);
    }

    /**
     * 查看某用户是否在话题圈子内
     * @param uid 用户id
     * @param tid 话题id
     * @return 若存在返回true, 反之false
     */
    public boolean exist(Long uid, Long tid) {
        return getSqlSession().selectOne("TopicUser.exist",
                ImmutableMap.of("userId", uid, "topicId", tid)) != null;
    }

    /**
     * 获取参与话题的用户id列表
     * @param topicId 话题id
     * @return 用户id列表
     */
    public List<Long> findUserIdsByTopicId(Long topicId) {
        return getSqlSession().selectList("TopicUser.findUserIdsByTopicId", topicId);
    }

    /**
     * 获取话题的圈子人数, 注意不保证次序, 按topic id降序
     * @param ids 话题id列表，降序
     * @return 依次话题列表的圈子内的人数
     */
    public List<Long> countTopicUsers(List<Long> ids){
        return getSqlSession().selectList("TopicUser.countTopicUsers", ids);
    }

    /**
     * 查找该话题的朋友圈
     * @param topic 当前话题
     */
    public List<TopicFriend> findFriends(Topic topic) {
        return getSqlSession().selectList("TopicUser.findFriends", topic);
    }
}
