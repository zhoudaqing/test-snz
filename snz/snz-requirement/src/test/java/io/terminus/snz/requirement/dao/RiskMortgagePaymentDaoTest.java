package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.RiskMortgagePayment;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class RiskMortgagePaymentDaoTest extends BasicTest {

    @Autowired
    private RiskMortgagePaymentDao riskMortgagePaymentDao;

    private RiskMortgagePayment exist;

    private RiskMortgagePayment mock() {
        RiskMortgagePayment payment = new RiskMortgagePayment();
        payment.setSupplierCode("V12359");
        payment.setSupplierDetail("河北科佳橡胶制品有限公司");
        payment.setPurchaserCode("8040");
        payment.setAmount(20000L);
        return payment;
    }

    @Before
    public void setUp() throws Exception {
        exist = mock();
        riskMortgagePaymentDao.create(exist);
    }

    @Test
    public void testCreate() throws Exception {
        RiskMortgagePayment payment = new RiskMortgagePayment();
        payment.setSupplierCode("V123456");
        payment.setSupplierDetail("heheh");
        payment.setPurchaserCode("123");
        payment.setAmount(123L);
        riskMortgagePaymentDao.create(payment);
    }

    @Test
    public void testUpdate() throws Exception {
        exist.setAmount(100L);
        riskMortgagePaymentDao.update(exist);
        assertThat(riskMortgagePaymentDao.findById(exist.getId()).getAmount(), is(100L));
    }

    @Test
    public void testDelete() throws Exception {
        riskMortgagePaymentDao.delete(exist.getId());
        assertNull(riskMortgagePaymentDao.findById(exist.getId()));
    }

    @Test
    public void testFindById() throws Exception {
        assertThat(riskMortgagePaymentDao.findById(exist.getId()).getAmount(), is(20000L));
    }

    @Test
    public void testFindBy() throws Exception {
        RiskMortgagePayment criteria = new RiskMortgagePayment();
        criteria.setSupplierCode("V12359");
        RiskMortgagePayment one = riskMortgagePaymentDao.findBy(criteria);
        assertNotNull(one);
        assertThat(20000L, is(one.getAmount()));
    }

    @Test
    public void testFindListBy() throws Exception {
        RiskMortgagePayment payment = new RiskMortgagePayment();
        payment.setSupplierCode("V12359");
        payment.setAmount(123L);
        payment.setPurchaserCode("purchaser");
        payment.setSupplierDetail("detail");
        riskMortgagePaymentDao.create(payment);

        RiskMortgagePayment criteria = new RiskMortgagePayment();
        criteria.setSupplierCode("V12359");
        List<RiskMortgagePayment> payments = riskMortgagePaymentDao.findListBy(criteria);
        assertEquals(2, payments.size());
    }
}