package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.Deposit;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/16/14
 */
@Repository
public class DepositDao extends SqlSessionDaoSupport {

    public Long create(Deposit dp) {
        getSqlSession().insert("Deposit.create", dp);
        return dp.getId();
    }

    public Boolean update(Deposit dp) {
        return getSqlSession().update("Deposit.update", dp) == 1;
    }

    public Deposit findById(Long id) {
        return getSqlSession().selectOne("Deposit.findById", id);
    }

    public Deposit findBy(Deposit dp) {
        return getSqlSession().selectOne("Deposit.findBy", dp);
    }

    public Deposit findBy(Long supplierId, Long requirementId, Deposit.Type type) {
        Deposit dp = new Deposit();
        dp.setSupplierId(supplierId);
        dp.setRequirementId(requirementId);
        dp.setType(type.value());
        return findBy(dp);
    }

    public List<Deposit> findListBy(Deposit dp) {
        return getSqlSession().selectList("Deposit.findBy", dp);
    }
}
