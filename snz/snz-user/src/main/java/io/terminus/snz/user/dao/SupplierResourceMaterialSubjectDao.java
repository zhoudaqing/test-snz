package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.user.model.SupplierResourceMaterialSubject;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/26/14
 */
@Repository
public class SupplierResourceMaterialSubjectDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierResourceMaterialSubject.";

    public Long create(SupplierResourceMaterialSubject subject) {
        getSqlSession().insert(NAMESPACE + "create", subject);
        return subject.getId();
    }

    public boolean update(SupplierResourceMaterialSubject subject) {
        return getSqlSession().update(NAMESPACE + "update", subject) == 1;
    }

    public List<SupplierResourceMaterialSubject> findAllValid() {
        return getSqlSession().selectList(NAMESPACE + "findBy", ImmutableMap.of("status", 1));
    }

    public SupplierResourceMaterialSubject findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }
}
