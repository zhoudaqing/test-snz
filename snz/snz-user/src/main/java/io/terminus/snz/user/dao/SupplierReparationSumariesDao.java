package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierReparationSumaries;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-9-1.
 */
@Repository
public class SupplierReparationSumariesDao extends SqlSessionDaoSupport {

    public SupplierReparationSumaries findSupplierReparationSumariesBySupplierUid(Long supplierId){
        return getSqlSession().selectOne("SupplierReparationSumaries.selectBySupplierUid", supplierId);
    }

    public Long create(SupplierReparationSumaries create) {
        getSqlSession().insert("SupplierReparationSumaries.insert", create);
        return create.getId();
    }

    public Integer update(SupplierReparationSumaries update) {
        return getSqlSession().update("SupplierReparationSumaries.updateByPrimaryKeySelective", update);
    }
}
