package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanySupplyPark;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-4.
 */
public class CompanySupplyParkDaoTest extends TestBaseDao {

    private CompanySupplyPark companySupplyPark;

    @Autowired
    private CompanySupplyParkDao companySupplyParkDao;

    private void mock() {
        companySupplyPark = new CompanySupplyPark();
        companySupplyPark.setSupplyParkId(1L);
        companySupplyPark.setCompanyId(2L);
        companySupplyPark.setUserId(2L);
        companySupplyPark.setName("青岛");
    }

    @Before
    public void setUp() {
        mock();
        companySupplyParkDao.create(companySupplyPark);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(companySupplyPark.getId());
    }

    @Test
    public void testFindByUserId() {
        List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(companySupplyPark.getUserId());
        Assert.assertEquals(companySupplyParks.get(0).getId(), companySupplyPark.getId());
    }

    @Test
    public void testFindByCompanyId() {
        List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByCompanyId(companySupplyPark.getCompanyId());
        Assert.assertEquals(companySupplyParks.get(0).getId(), companySupplyPark.getId());
    }

    @Test
    public void testDeleteByUserId() {
        companySupplyParkDao.deleteByUserId(companySupplyPark.getUserId());
        List<CompanySupplyPark> companySupplyParks = companySupplyParkDao.findByUserId(companySupplyPark.getUserId());
        Assert.assertTrue(companySupplyParks.size() == 0);
    }

    @Test
    public void testCountBySupplyParkId() {
        Long count = companySupplyParkDao.countBySupplyParkId(companySupplyPark.getSupplyParkId());
        Assert.assertEquals(count.intValue(), 1);
    }

    @Test
    public void testFindUserIdsBySupplyParkId() {
        List<Long> userIds = companySupplyParkDao.findUserIdsBySupplyParkId(companySupplyPark.getSupplyParkId(), Arrays.asList(companySupplyPark.getUserId()));
        Assert.assertTrue(userIds.size() == 1);
    }

}
