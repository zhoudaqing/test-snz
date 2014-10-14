package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierResourceMaterialLog;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/26/14
 */
@Repository
public class SupplierResourceMaterialLogDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierResourceMaterialLog.";

    public Long create(SupplierResourceMaterialLog log1) {
        getSqlSession().insert(NAMESPACE + "create", log1);
        return log1.getId();
    }

    public SupplierResourceMaterialLog findLastOneBy(SupplierResourceMaterialLog params) {
        return getSqlSession().selectOne(NAMESPACE + "findLastOneBy", params);
    }

    public List<SupplierResourceMaterialLog> findBy(SupplierResourceMaterialLog params) {
        return getSqlSession().selectList(NAMESPACE + "findBy", params);
    }
}
