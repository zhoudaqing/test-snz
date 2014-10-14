package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.RiskMortgagePayment;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/21/14
 */
@Repository
public class RiskMortgagePaymentDao extends SqlSessionDaoSupport {

    public Long create(RiskMortgagePayment payment) {
        getSqlSession().insert("RiskMortgagePayment.create", payment);
        return payment.getId();
    }

    public boolean update(RiskMortgagePayment payment) {
        return getSqlSession().update("RiskMortgagePayment.update", payment) == 1;
    }

    public boolean delete(Long id) {
        return getSqlSession().delete("RiskMortgagePayment.delete", id) == 1;
    }

    public RiskMortgagePayment findById(Long id) {
        return getSqlSession().selectOne("RiskMortgagePayment.findById", id);
    }

    public RiskMortgagePayment findBy(RiskMortgagePayment payment) {
        return getSqlSession().selectOne("RiskMortgagePayment.findBy", payment);
    }

    public List<RiskMortgagePayment> findListBy(RiskMortgagePayment payment) {
        return getSqlSession().selectList("RiskMortgagePayment.findBy", payment);
    }
}
