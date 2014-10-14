package io.terminus.snz.user.dao;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.User;
import org.joda.time.DateTime;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午2:26
 */
@Repository
public class UserDao extends SqlSessionDaoSupport {

    /**
     * 创建用户
     */
    public Long create(User user) {
        getSqlSession().insert("User.insert", user);
        return user.getId();
    }

    /**
     * 删除用户
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("User.delete", id) == 1;
    }

    /**
     * 更新用户
     */
    public boolean update(User user) {
        return getSqlSession().update("User.update", user) == 1;
    }

    /**
     * 查询用户
     */
    public User findById(Long id) {
        return getSqlSession().selectOne("User.findById", id);
    }

    /**
     * 查询用户列表
     */
    public List<User> findByIds(List<Long> ids) {
        return getSqlSession().selectList("User.findByIds", ids);
    }

    /**
     * 通过用户名查询用户
     */
    public User findByNick(String nick) {
        return getSqlSession().selectOne("User.findByNick", nick);
    }

    /**
     * 通过roleStr模糊查询
     */
    public List<User> findByRoleStr(String role) {
        return getSqlSession().selectList("User.findByRoleStr", role);
    }

    /**
     * 分页查询用户
     */
    public Paging<User> findBy(Map<String, Object> criteria, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        if (criteria != null) {
            params.putAll(criteria);
        }

        Long count = getSqlSession().selectOne("User.countBy", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findBy", params);

        return new Paging<User>(count, users);
    }

    /**
     * 分页查询用户
     */
    public Paging<User> findQualifyingBy(Integer status, String nick, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("qualifyStatus", status);
        params.put("nick", Strings.emptyToNull(nick));

        Long count = getSqlSession().selectOne("User.countQualifyingBy", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findQualifyingBy", params);

        return new Paging<User>(count, users);
    }

    /**
     * 最大供应商id
     */
    public Long maxSupplierId() {
        return getSqlSession().selectOne("User.maxSupplierId");
    }

    /**
     * 查询需要dump的用户列表
     */
    public List<User> forDump(Long lastId, Integer limit) {
        return getSqlSession().selectList("User.forDump", ImmutableMap.of("lastId", lastId, "limit", limit));
    }

    /**
     * dump用户数据
     */
    public List<User> forDeltaDump(Long lastId, String compared, Integer limit) {
        return getSqlSession().selectList("User.forDeltaDump", ImmutableMap.of("lastId", lastId,
                "compared", compared, "limit", limit));
    }

    /**
     * 用户计数
     */
    public Long countBy(Integer type, Integer approveStatus, Integer refuseStatus, String nick) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("type", type);
        params.put("approveStatus", approveStatus);
        params.put("refuseStatus", refuseStatus);
        params.put("nick", nick);

        Long count = getSqlSession().selectOne("User.countBy", params);

        return count == null ? 0 : count;
    }

    /**
     * 认证过的用户计数
     */
    public Long countQualifiedSupplier() {
        Long count = getSqlSession().selectOne("User.countQualifiedSupplier");
        return count == null ? 0 : count;
    }

    /**
     * 通过外部用户id查找用户
     *
     * @param outerId 外部用户id
     * @return 用户对象
     */
    public User findByOuterId(Long outerId) {
        return getSqlSession().selectOne("User.findByOuterId", outerId);
    }

    /**
     * 在用户ids内查询特定审核状态的用户
     */
    public Long countByApproveAndUserIds(Integer type, Integer approveStatus, String nick, List<Long> userIds) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("type", type);
        params.put("approveStatus", approveStatus);
        params.put("nick", nick);
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countByApproveAndUserIds", params);

        return count == null ? 0 : count;
    }

    /**
     * 在用户ids内查询特定审核状态的用户
     */
    public Paging<User> findByApproveAndUserIds(Integer type, Integer approveStatus, String nick, List<Long> userIds, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("type", type);
        params.put("approveStatus", approveStatus);
        params.put("nick", Strings.emptyToNull(nick));
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countByApproveAndUserIds", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findByApproveAndUserIds", params);

        return new Paging<User>(count, users);
    }

    /**
     * 在用户ids内统计待审核的供应商
     */
    public Long countApprovingSupplierByUserIds(String nick, List<Long> userIds) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("nick", nick);
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countApprovingSupplierByUserIds", params);

        return count == null ? 0 : count;
    }

    /**
     * 在用户ids内查询待审核的供应商
     */
    public Paging<User> findApprovingSupplierByUserIds(String nick, List<Long> userIds, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("nick", Strings.emptyToNull(nick));
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countApprovingSupplierByUserIds", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findApprovingSupplierByUserIds", params);

        return new Paging<User>(count, users);
    }

    /**
     * 从外部id中选择已经存在的outerIds
     *
     * @param outerIds 所给的outerIds
     * @return 存在的outerId 列表
     */
    public List<Long> findExistedOuterIds(List<Long> outerIds) {
        return getSqlSession().selectList("User.findExistedOuterIds", outerIds);
    }

    /**
     * 从nick中选择已经存在的nick
     *
     * @param nicks 所给的nicks
     * @return 存在的nick列表
     */
    public List<String> findExistedNicks(List<String> nicks) {
        return getSqlSession().selectList("User.findExistedNicks", nicks);
    }

    /**
     * 从nick中选择已经存在的nick
     *
     * @param nicks 所给的nicks
     * @return 存在的nick列表
     */
    public List<User> findByNicks(List<String> nicks) {
        return getSqlSession().selectList("User.findByNicks", nicks);
    }

    /**
     * 在用户ids内统计n天之内审核通过的供应商
     */
    public Long countLastEnterPassByUserIds(int days, String nick, List<Long> userIds) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("date", DateTime.now().minusDays(days).toDate());
        params.put("nick", nick);
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countLastEnterPassByUserIds", params);

        return count == null ? 0 : count;
    }

    /**
     * 在用户ids内查询n天之内审核通过的供应商
     */
    public Paging<User> findLastEnterPassByUserIds(int days, String nick, List<Long> userIds, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("date", DateTime.now().minusDays(days).toDate());
        params.put("nick", Strings.emptyToNull(nick));
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countLastEnterPassByUserIds", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findLastEnterPassByUserIds", params);

        return new Paging<User>(count, users);
    }

    /**
     * 统计入驻审核通过的供应商
     */
    public Long countEnterPassSupplier(String nick, List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("nick", Strings.emptyToNull(nick));
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countEnterPassSupplier", params);
        return count == null ? 0 : count;
    }

    /**
     * 查询入驻审核通过的供应商
     */
    public Paging<User> findEnterPassSupplier(String nick, List<Long> userIds, Integer offset, Integer limit) {

        Map<String, Object> params = Maps.newHashMap();
        params.put("nick", Strings.emptyToNull(nick));
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countEnterPassSupplier", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findEnterPassSupplier", params);

        return new Paging<User>(count, users);

    }

    /**
     * 根据注册时间和所处阶段统计供应商
     */
    public Long countSupplierByCreatedAtAndStepAndIds(Date createdAtStart, Date createdAtEnd, Integer step, List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("createdAtStart", createdAtStart);
        params.put("createdAtEnd", createdAtEnd);
        params.put("step", step);
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countSupplierByCreatedAtAndStepAndIds", params);
        return count == null ? 0 : count;
    }

    /**
     * 根据注册时间和所处阶段统计查询供应商
     */
    public Paging<User> findSupplierByCreatedAtAndStepAndIds(Date createdAtStart, Date createdAtEnd, Integer step, List<Long> userIds, Integer offset, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("createdAtStart", createdAtStart);
        params.put("createdAtEnd", createdAtEnd);
        params.put("step", step);
        params.put("list", userIds);

        Long count = getSqlSession().selectOne("User.countSupplierByCreatedAtAndStepAndIds", params);
        if (count == 0) {
            return new Paging<User>(0L, Collections.<User>emptyList());
        }

        params.put("offset", offset);
        params.put("limit", limit);

        List<User> users = getSqlSession().selectList("User.findSupplierByCreatedAtAndStepAndIds", params);

        return new Paging<User>(count, users);

    }

    /**
     * 查询指定提交审核日期之前所有待审核供应商的user id
     */
    public List<Long> findApprovingUserIdsWithSubmitAt(Date date) {
        return getSqlSession().selectList("User.findApprovingUserIdsWithSubmitAt", date);
    }

}
