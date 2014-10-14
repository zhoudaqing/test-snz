package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.YzlCreditQualify;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by dream on 14-10-12.
 */
@Repository
public class YzlCreditQualifyDao extends SqlSessionDaoSupport{

    /**
     * 根据状态查询列表
     * @param status
     * @return  返回结果集
     */
    public List<YzlCreditQualify> selectByStatus(Integer status) {
        return getSqlSession().selectList("YzlCreditQualify.selectByStatus",status);
    }

    /**
     * 插入一条记录
     * @param yzlCreditQualify
     * @return 返回创建的编号
     */
    public Integer create(YzlCreditQualify yzlCreditQualify) {
        getSqlSession().insert("YzlCreditQualify.create", yzlCreditQualify);
        return yzlCreditQualify.getId();
    }

    /**
     * 更新一条记录
     * @param yzlCreditQualify
     * @return
     */
    public Integer update(YzlCreditQualify yzlCreditQualify) {
        return getSqlSession().update("YzlCreditQualify.update", yzlCreditQualify);
    }

    /**
     * 根据id删除一条记录
     * @param id
     * @return
     */
    public Boolean deleteById(Integer id) {
        return getSqlSession().delete("YzlCreditQualify.deleteById", id) == 0;
    }

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    public Integer deleteByIds(List<Integer> ids) {
        return getSqlSession().delete("YzlCreditQualify.deleteByIds", ids);
    }

    /**
     * 批量插入
     * @param yzlCreditQualifies
     * @return
     */
    public Integer creates(List<YzlCreditQualify> yzlCreditQualifies) {
        return getSqlSession().insert("YzlCreditQualify.creates", yzlCreditQualifies);
    }

    /**
     * 根据条件分页查询
     * @param params 查询条件
     * @return 分页查询结果
     */
    public Paging<YzlCreditQualify> pagingForWhat(Integer status, String message, Map<String, Object> params) {
        params.put("status", status);
        params.put("message", message);
        Long count = getSqlSession().selectOne("YzlCreditQualify.countBy", params);
        if (count == 0) {
            return new Paging<YzlCreditQualify>(0l, Collections.<YzlCreditQualify>emptyList());
        }
        List<YzlCreditQualify> yzlCreditQualifies = getSqlSession().selectList("YzlCreditQualify.findBy", params);
        return new Paging<YzlCreditQualify>(count, yzlCreditQualifies);
    }
}
