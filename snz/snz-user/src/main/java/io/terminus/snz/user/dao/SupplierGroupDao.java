package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.user.model.SupplierGroupRelation;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static io.terminus.common.utils.Arguments.isNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
@Repository
public class SupplierGroupDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierGroupRelation.";

    public Long create(SupplierGroupRelation relation) {
        getSqlSession().insert(NAMESPACE + "create", relation);
        return relation.getId();
    }

    public Long create(Long supplierId, Long groupId) {
        SupplierGroupRelation relation = new SupplierGroupRelation();
        relation.setSupplierId(supplierId);
        relation.setGroupId(groupId);
        return create(relation);
    }

    public boolean update(SupplierGroupRelation relation) {
        return getSqlSession().update(NAMESPACE + "update", relation) == 1;
    }

    public boolean updateGroupId(Long from, Long to) {
        return getSqlSession().update(NAMESPACE + "updateGroupId", ImmutableMap.of("from", from, "to", to)) > 0;
    }

    public boolean delete(Long id) {
        return getSqlSession().delete(NAMESPACE + "delete", id) == 1;
    }

    public Long getMaxGroupId() {
        Long maxGroupId = getSqlSession().selectOne(NAMESPACE + "maxGroupId");
        if (isNull(maxGroupId)) {
            return 0L;
        }
        return maxGroupId;
    }

    public SupplierGroupRelation findOneBy(SupplierGroupRelation params) {
        return getSqlSession().selectOne(NAMESPACE + "findBy", params);
    }

    public SupplierGroupRelation findOneBySupplierId(Long supplierId) {
        SupplierGroupRelation params = new SupplierGroupRelation();
        params.setSupplierId(supplierId);
        return findOneBy(params);
    }

    public List<SupplierGroupRelation> findBy(SupplierGroupRelation params) {
        return getSqlSession().selectList(NAMESPACE + "findBy", params);
    }

    public List<SupplierGroupRelation> findByGroupId(Long groupId) {
        SupplierGroupRelation params = new SupplierGroupRelation();
        params.setGroupId(groupId);
        return findBy(params);
    }
}
