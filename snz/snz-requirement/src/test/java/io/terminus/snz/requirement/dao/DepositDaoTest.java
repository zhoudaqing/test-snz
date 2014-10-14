package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.Deposit;
import org.elasticsearch.common.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.hamcrest.Matchers.is;

public class DepositDaoTest extends BasicTest {

    @Autowired
    private DepositDao depositDao;

    private Deposit mock(Long requirementId, Long supplierId,
                         String dealId, Date dealTime, String dealUrl,
                         Long amount, String bankInfo, Integer status) {
        Deposit dp = new Deposit();
        dp.setRequirementId(requirementId);
        dp.setSupplierId(supplierId);
        dp.setDealId(dealId);
        dp.setDealTime(dealTime);
        dp.setDealUrl(dealUrl);
        dp.setAmount(amount);
        dp.setBankInfo(bankInfo);
        dp.setType(1);
        dp.setStatus(status);
        dp.setSyncStatus(0);
        return dp;
    }

    private Deposit mockOne() {
        return mock(10L, 11L, "1000110101010", DateTime.now().toDate(), "dealUrl", 10000L, "bankInfo", 0);
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testCreate() throws Exception {
        Deposit dp = mockOne();
        Long id = depositDao.create(dp);
        Assert.assertNotNull(id);
        Assert.assertNotNull(dp.getId());
        Assert.assertEquals(dp.getId(), id);
    }

    @Test
    public void testUpdate() throws Exception {
        Deposit dp = mockOne();
        depositDao.create(dp);

        dp.setAmount(1L);
        depositDao.update(dp);

        Assert.assertThat(depositDao.findById(dp.getId()).getAmount(), is(1L));
    }

    @Test
    public void testFindById() throws Exception {
        Deposit dp = mockOne();
        depositDao.create(dp);

        Assert.assertThat(depositDao.findById(dp.getId()).getAmount(), is(dp.getAmount()));
    }

    @Test
    public void testFindBy() throws Exception {
        Deposit dp = mockOne();
        depositDao.create(dp);

        Deposit dp2 = depositDao.findBy(dp);
        Assert.assertThat(dp2.getId(), is(dp.getId()));
    }
}