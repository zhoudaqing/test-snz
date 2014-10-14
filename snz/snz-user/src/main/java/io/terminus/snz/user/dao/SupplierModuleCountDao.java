package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierModuleCount;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-28.
 */
@Repository
public class SupplierModuleCountDao extends SqlSessionDaoSupport {

    public Long create(SupplierModuleCount supplierModuleCount) {
        getSqlSession().insert("SupplierModuleCount.insert", supplierModuleCount);
        return supplierModuleCount.getId();
    }

    public boolean update(SupplierModuleCount supplierModuleCount) {
        return getSqlSession().update("SupplierModuleCount.update", supplierModuleCount) == 1;
    }

    public SupplierModuleCount findByModuleId(Long moduleId) {
        return getSqlSession().selectOne("SupplierModuleCount.findByModuleId", moduleId);
    }

    public List<SupplierModuleCount> findAll() {
        return getSqlSession().selectList("SupplierModuleCount.findAll");
    }


    public SupplierModuleCount findByModuleName(String moduleName) {
        return getSqlSession().selectOne("SupplierModuleCount.findByModuleName", moduleName);
    }

}
