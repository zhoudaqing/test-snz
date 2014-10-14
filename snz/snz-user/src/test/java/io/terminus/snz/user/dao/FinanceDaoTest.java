package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.Finance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午9:03
 */
public class FinanceDaoTest extends TestBaseDao {

    private Finance finance;

    @Autowired
    private FinanceDao financeDao;

    private void mock() {
        finance = new Finance();
        finance.setCompanyId(1L);
        finance.setUserId(1L);
        finance.setCountry(1);
        finance.setBankAccount("张三");
        finance.setBankCode("344dfd");
        finance.setCoinType(Finance.CoinType.EUR.value());
        finance.setOpeningBank("农行");
        finance.setRecentFinance("wetdg");
        finance.setOpenLicense("www.upanyun.com/img/1.jpg");
    }

    @Before
    public void setUp() {
        mock();
        financeDao.create(finance);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(finance.getId());
    }

    @Test
    public void testFindById() {
        Finance model = financeDao.findById(finance.getId());
        Assert.assertEquals(model.getId(), finance.getId());
    }

    @Test
    public void testFindByUserId() {
        Finance model = financeDao.findByUserId(finance.getUserId());
        Assert.assertEquals(model.getId(), finance.getId());
    }

    @Test
    public void testDelete() {
        financeDao.delete(finance.getId());
        Finance model = financeDao.findById(finance.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        Finance updatedModel = new Finance();
        updatedModel.setId(finance.getId());
        updatedModel.setRecentFinance("sdfsdf");
        updatedModel.setOpenLicense("/2.jpg");
        financeDao.update(updatedModel);

        Finance model = financeDao.findById(finance.getId());

        Assert.assertEquals(model.getRecentFinance(), updatedModel.getRecentFinance());
        Assert.assertEquals(model.getOpenLicense(), updatedModel.getOpenLicense());
    }

}
