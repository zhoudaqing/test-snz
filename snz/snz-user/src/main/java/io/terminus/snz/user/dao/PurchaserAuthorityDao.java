package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.user.model.PurchaserAuthority;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/21/14
 */
@Repository
public class PurchaserAuthorityDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "PurchaserAuthority.";

    public Long create(PurchaserAuthority authority) {
        getSqlSession().insert(NAMESPACE + "create", authority);
        return authority.getId();
    }

    public boolean delete(Long id) {
        return getSqlSession().delete(NAMESPACE + "delete", id) == 1;
    }

    public boolean update(PurchaserAuthority authority) {
        return getSqlSession().update(NAMESPACE + "update", authority) == 1;
    }

    public PurchaserAuthority findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }

    public List<PurchaserAuthority> findBy(PurchaserAuthority authority) {
        return getSqlSession().selectList(NAMESPACE + "findBy", authority);
    }

    public List<PurchaserAuthority> findByUserId(Long userId) {
        return getSqlSession().selectList(NAMESPACE + "findBy", ImmutableMap.of("userId", userId));
    }
}
