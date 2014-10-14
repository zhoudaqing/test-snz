package io.terminus.snz.related.daos;

import io.terminus.snz.related.models.FactoryProductionDirector;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/17/14
 */
@Repository
public class FactoryProductionDirectorDao extends SqlSessionDaoSupport {

    public Long create(FactoryProductionDirector director) {
        getSqlSession().insert("FactoryProductionDirector.create", director);
        return director.getId();
    }

    public List<FactoryProductionDirector> findBy(FactoryProductionDirector params) {
        return getSqlSession().selectList("FactoryProductionDirector.findBy", params);
    }
}
