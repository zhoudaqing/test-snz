package io.terminus.snz.eai.dao;

import io.terminus.snz.eai.model.MDMConfigure;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Date: 7/25/14
 * Time: 10:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class MDMConfigureDao extends SqlSessionDaoSupport {
    public Long create(MDMConfigure insert) {
        getSqlSession().insert("MDMConfigure.create", insert);
        return insert.getId();
    }

    public Boolean update(MDMConfigure update) {
        return getSqlSession().update("MDMConfigure.update", update) == 1;
    }

    public MDMConfigure findById(Long id) {
        return getSqlSession().selectOne("MDMConfigure.findById", id);
    }

    public MDMConfigure findBy(MDMConfigure params) {
        return getSqlSession().selectOne("MDMConfigure.findBy", params);
    }

    public List<MDMConfigure> findListBy(MDMConfigure params) {
        return getSqlSession().selectList("MDMConfigure.findListBy", params);
    }

    public List<MDMConfigure> findListByTypes(List<Integer> types) {
        return getSqlSession().selectList("MDMConfigure.findListByTypes", types);
    }
}
