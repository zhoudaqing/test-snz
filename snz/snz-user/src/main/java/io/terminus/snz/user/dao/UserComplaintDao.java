package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.UserComplaint;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 提供对表 `snz_user_complaints`  的增删改查操作<P>
 * <p/>
 * Created by wanggen 2014-08-13 14:34:57
 */
@Repository
public class UserComplaintDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "UserComplaint.";

    /**
     * 新增
     *
     * @param userComplaint add bean
     * @return 新增后的自增序列号
     */
    public long create(UserComplaint userComplaint) {
        getSqlSession().insert(NAMESPACE + "create", userComplaint);
        return userComplaint.getId();
    }


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 userComplaint
     */
    public UserComplaint findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 分页查询结果
     */
    public Paging<UserComplaint> findByPaging(Map<String, Object> param) {
        Integer total = getSqlSession().selectOne(NAMESPACE + "countBy", param);
        if (total == null || total == 0)
            return new Paging<UserComplaint>(0L, Collections.<UserComplaint>emptyList());
        if (param.get("offset") == null)
            param.put("offset", 0);
        if (param.get("limit") == null)
            param.put("limit", total);
        List<UserComplaint> userComplaintList = getSqlSession().selectList(NAMESPACE + "findBy", param);
        return new Paging<UserComplaint>(total.longValue(), userComplaintList);
    }


    /**
     * 根据条件无分页查询
     *
     * @param param 查询条件
     * @return 结果集
     */
    public List<UserComplaint> findAllBy(Map<String, Object> param) {
        return getSqlSession().selectList(NAMESPACE + "findBy", param);
    }

    /**
     * 根据 userId 查询 UserComplaint 列表
     *
     * @param userId 抱怨人ID
     * @return 结果列
     */
    public List<UserComplaint> findByUserId(Long userId) {
        return getSqlSession().selectList(NAMESPACE + "findByUserId", userId);
    }


    /**
     * 更新操作
     *
     * @param userComplaint 更新操作参数
     * @return 影响行数
     */
    public int update(UserComplaint userComplaint) {
        return getSqlSession().update(NAMESPACE + "update", userComplaint);
    }


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public int deleteByIds(List<Long> ids) {
        return getSqlSession().delete(NAMESPACE + "deleteByIds", ids);
    }


}
