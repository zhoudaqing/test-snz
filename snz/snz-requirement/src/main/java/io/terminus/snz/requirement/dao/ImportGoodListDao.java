package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ImportGoodList;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Date: 7/11/14
 * Time: 10:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class ImportGoodListDao extends SqlSessionDaoSupport {
    public Long create(ImportGoodList insert) {
        getSqlSession().insert("ImportGoodList.create", insert);
        return insert.getId();
    }

    public Boolean update(ImportGoodList update) {
        return getSqlSession().update("ImportGoodList.update", update) == 1;
    }

    public ImportGoodList findById(Long id) {
        return getSqlSession().selectOne("ImportGoodList.findById", id);
    }

    public ImportGoodList findBy(ImportGoodList params) {
        return getSqlSession().selectOne("ImportGoodList.findBy", params);
    }
}
