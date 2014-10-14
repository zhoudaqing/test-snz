package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.SupplierQualification;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Date: 7/22/14
 * Time: 16:33
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class SupplierQualificationDao extends SqlSessionDaoSupport {
    public Long create(SupplierQualification insert) {
        getSqlSession().insert("SupplierQualification.create", insert);
        return insert.getId();
    }

    public Boolean update(SupplierQualification update) {
        return getSqlSession().update("SupplierQualification.update", update) == 1;
    }

    public SupplierQualification findById(Long id) {
        return getSqlSession().selectOne("SupplierQualification.findById", id);
    }

    public SupplierQualification findBy(SupplierQualification params) {
        return getSqlSession().selectOne("SupplierQualification.findBy", params);
    }

    public SupplierQualification findBySupplierId(Long supplierId) {
        return getSqlSession().selectOne("SupplierQualification.findBySupplierId", supplierId);
    }
}
