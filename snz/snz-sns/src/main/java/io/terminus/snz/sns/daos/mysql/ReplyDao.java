package io.terminus.snz.sns.daos.mysql;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.Paging;
import io.terminus.snz.sns.models.Reply;
import io.terminus.snz.user.model.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * 话题回复Dao
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
@Repository
public class ReplyDao extends SqlSessionDaoSupport {

    /**
     * 创建回复
     *
     * @param r 回复对象
     * @return 创建记录数
     */
    public int create(Reply r) {
        return getSqlSession().insert("Reply.create", r);
    }

    /**
     * 根据id获取回复
     *
     * @param rid 回复id
     * @return 回复对象
     */
    public Reply findById(Long rid) {
        return getSqlSession().selectOne("Reply.findById", rid);
    }

    /**
     * 根据话题id删除回复
     *
     * @param topicId 话题id
     * @return 删除记录数
     */
    public int deleteByTopicId(Long topicId) {
        return getSqlSession().delete("Reply.deleteByTopicId", topicId);
    }

    /**
     * 批量删除话题回复
     *
     * @param topicIds 话题id列表
     * @return 删除记录数
     */
    public int deleteByTopicIds(List<Long> topicIds) {
        return getSqlSession().delete("Reply.deleteByTopicIds", topicIds);
    }

    /**
     * 根据话题id分页查询顶级回复
     *
     * @param topicId 话题id
     * @param offset  起始偏移
     * @param limit   分页大小
     * @return 回复分页对象
     */
    public Paging<Reply> pagingTopByTopicId(Long topicId, Integer offset, Integer limit) {
        Long total = getSqlSession().selectOne("Reply.countTopByTopicId", topicId);
        if (total == 0) {
            return new Paging<Reply>(0L, Collections.<Reply>emptyList());
        }
        List<Reply> data = getSqlSession().selectList("Reply.paginationTopByTopicId", ImmutableMap.of("topicId", topicId, "offset", offset, "limit", limit));
        return new Paging<Reply>(total, data);
    }

    /**
     * 根据顶级回复id，获取其下所有子回复列表
     *
     * @param topId 顶级回复id
     * @return 子回复列表
     */
    public List<Reply> listAllByTopId(Long topId) {
        return getSqlSession().selectList("Reply.listAllByTopId", topId);
    }

    /**
     * 分页获取所有回复信息
     *
     * @param tid    话题id
     * @param offset 起始偏移
     * @param limit  分页大小
     * @return
     */
    public Paging<Reply> pagingForAll(Long tid, Integer offset, Integer limit) {
        Long total = getSqlSession().selectOne("Reply.countForAll", ImmutableMap.of("topicId", tid));
        if (total == 0) {
            return new Paging<Reply>(0L, Collections.<Reply>emptyList());
        }
        List<Reply> data = getSqlSession().selectList("Reply.paginationForAll", ImmutableMap.of("topicId", tid, "offset", offset, "limit", limit));

        return new Paging<Reply>(total, data);
    }

    /**
     * 分页获取私有的信息: 用户只能看见自己和话题发起者的回复信息
     *
     * @param tid            话题id
     * @param topicCreatorId 话题创建者
     * @param userId         当前用户id
     * @param offset         起始偏移
     * @param limit          分页大小
     * @return 回复分页列表
     */
    public Paging<Reply> pagingForPrivate(Long tid, Long topicCreatorId, Long userId, Integer offset, Integer limit) {
        Long total = getSqlSession().selectOne("Reply.countForPrivate", ImmutableMap.of("topicId", tid, "topicCreatorId", topicCreatorId, "userId", userId));
        if (total == 0) {
            return new Paging<Reply>(0L, Collections.<Reply>emptyList());
        }
        List<Reply> data = getSqlSession().selectList("Reply.paginationForPrivate", ImmutableMap.of("topicId", tid, "topicCreatorId", topicCreatorId, "userId", userId, "offset", offset, "limit", limit));
        return new Paging<Reply>(total, data);
    }

    /**
     * 查询所有回复过该话题的供应商列表, 咱不分页
     *
     * @param topicId 话题id
     * @return 回复过该话题的供应商列表
     */
    public List<User> listTopicSuppliers(Long topicId) {
        return getSqlSession().selectList("listTopicSuppliers", topicId);
    }


    /**
     * 查询所有回复过该话题的采购商列表, 咱不分页
     *
     * @param topicId 话题id
     * @return 回复过该话题的采购商列表
     */
    public List<User> listTopicPurchasers(Long topicId) {
        return getSqlSession().selectList("listTopicPurchasers", topicId);
    }

    /**
     * 统计供应商回复计数
     *
     * @param topicId    话题id
     * @param userIds    用户id列表
     * @param userIdsStr 用户id列表组成字符串，形如  ,1,2,3, (注意: 首尾的","不能少)
     * @return 对应的用户回复数列表(顺序对应userIds)
     */
    public List<Long> usersReplyCounts(Long topicId, List<Long> userIds, String userIdsStr) {
        return getSqlSession().selectList("usersReplyCounts", ImmutableMap.of("topicId", topicId, "userIds", userIds, "userIdsStr", userIdsStr));
    }


    /**
     * <pre>
     * 查询回复过 id 是 #{pid} 话题的消息<br>
     *     1.对于圈内私密话题顶级回复的回复：
     *          当前用户可见话题创建者对顶级回复的回复;
     *          当前用户可见顶级回复创建者对顶级回复的回复;
     *          当前用户可见自己对顶级回复的回复
     * </pre>
     * @param pid            父级回复ID
     * @param topicCreatorId 话题创建者ID
     * @param firstReplyerId 顶级回复创建者ID
     * @param currUserId     被回复信息ID
     * @return 回复信息列表
     */
    public List<Reply> findByPid(Long pid, int scope, Long topicCreatorId, Long firstReplyerId, Long currUserId) {
        return getSqlSession().selectList("Reply.findByPid",
                ImmutableMap.of(
                        "pid", pid,
                        "scope", scope,
                        "topicCreatorId", topicCreatorId,
                        "firstReplyerId", firstReplyerId,
                        "currUserId", currUserId));
    }


    /**
     * 根据话题创建者ID查询一条回复记录
     * @param creatorId 话题创建者ID
     * @return  回复信息
     */
    public Reply findOneByCreatorId(Long creatorId) {
        return getSqlSession().selectOne("Reply.findOneByCreatorId", creatorId);
    }
}
