package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.RiskMortgagePaymentDao;
import io.terminus.snz.requirement.model.RiskMortgagePayment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/4/14
 */
public class RiskMortgagePaymentServiceImplTest {

    @InjectMocks
    private RiskMortgagePaymentServiceImpl riskMortgagePaymentService;

    @Mock
    private RiskMortgagePaymentDao riskMortgagePaymentDao;

    private RiskMortgagePayment getPayment(String vCode, String detail, String purchaserCode, Long amount) {
        RiskMortgagePayment payment = new RiskMortgagePayment();
        payment.setSupplierCode(vCode);
        payment.setSupplierDetail(detail);
        payment.setPurchaserCode(purchaserCode);
        payment.setAmount(amount);
        return payment;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCRUD() throws Exception {
        RiskMortgagePayment riskMoney = getPayment("v1234", "公司信息", "123", 2000000L);

        // create test
        when(riskMortgagePaymentDao.create(riskMoney)).thenReturn(1L);

        Response<Long> createRes = riskMortgagePaymentService.create(riskMoney);
        assertTrue(createRes.isSuccess());
        assertNotNull(createRes.getResult());
        assertThat(createRes.getResult(), is(1L));

        // update test
        riskMoney.setPurchaserCode("321");

        when(riskMortgagePaymentDao.update(riskMoney)).thenReturn(true);
        Response<Boolean> updateRes = riskMortgagePaymentService.update(riskMoney);
        assertTrue(updateRes.isSuccess());
        assertNotNull(updateRes.getResult());
        assertThat(updateRes.getResult(), is(true));

        // delete test
        when(riskMortgagePaymentDao.delete(1L)).thenReturn(true);
        Response<Boolean> deleteRes = riskMortgagePaymentService.delete(1L);
        assertTrue(deleteRes.isSuccess());
        assertNotNull(deleteRes.getResult());
        assertThat(deleteRes.getResult(), is(true));

        // findBy test
        when(riskMortgagePaymentDao.findById(1L)).thenReturn(riskMoney);
        when(riskMortgagePaymentDao.findBy(riskMoney)).thenReturn(riskMoney);

        Response<RiskMortgagePayment> findRes = riskMortgagePaymentService.findBy(riskMoney);
        assertTrue(findRes.isSuccess());
        assertThat(findRes.getResult().getPurchaserCode(), is("321"));

        findRes = riskMortgagePaymentService.findById(1L);
        assertTrue(findRes.isSuccess());
        assertThat(findRes.getResult().getPurchaserCode(), is("321"));

        // amount test
        RiskMortgagePayment amountQuery = new RiskMortgagePayment();
        amountQuery.setSupplierCode("v1234");

        when(riskMortgagePaymentDao.findListBy(any(RiskMortgagePayment.class)))
                .thenReturn(Lists.newArrayList(riskMoney));

        Response<Long> amountRes = riskMortgagePaymentService.getRiskMortgageAmountOfSupplier("v1234");
        assertTrue(amountRes.isSuccess());
        assertThat(amountRes.getResult(), is(2000000L));
    }
}