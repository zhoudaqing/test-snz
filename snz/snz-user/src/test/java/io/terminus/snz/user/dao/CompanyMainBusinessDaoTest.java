package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.user.model.CompanyMainBusiness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-8.
 */
public class CompanyMainBusinessDaoTest extends TestBaseDao {

    private CompanyMainBusiness companyMainBusiness;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    private void mock() {
        companyMainBusiness = new CompanyMainBusiness();
        companyMainBusiness.setFirstLevelId(2L);
        companyMainBusiness.setMainBusinessId(1L);
        companyMainBusiness.setCompanyId(2L);
        companyMainBusiness.setUserId(2L);
        companyMainBusiness.setName("冰箱制造");
    }

    @Before
    public void setUp() {
        mock();
        companyMainBusinessDao.create(companyMainBusiness);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(companyMainBusiness.getId());
    }

    @Test
    public void testFindByUserId() {
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(companyMainBusiness.getUserId());
        Assert.assertEquals(companyMainBusinesses.get(0).getId(), companyMainBusiness.getId());
    }

    @Test
    public void testFindByMainBusinessId() {
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByMainBusinessId(companyMainBusiness.getMainBusinessId());
        Assert.assertEquals(companyMainBusinesses.get(0).getId(), companyMainBusiness.getId());
    }

    @Test
    public void testFindByCompanyId() {
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByCompanyId(companyMainBusiness.getCompanyId());
        Assert.assertEquals(companyMainBusinesses.get(0).getId(), companyMainBusiness.getId());
    }

    @Test
    public void testDeleteByUserId() {
        companyMainBusinessDao.deleteByUserId(companyMainBusiness.getUserId());
        List<CompanyMainBusiness> companyMainBusinesses = companyMainBusinessDao.findByUserId(companyMainBusiness.getUserId());
        Assert.assertTrue(companyMainBusinesses.size() == 0);
    }

    @Test
    public void testFindCompanyIdsByMainBusinessIds() {
        List<CompanyMainBusiness> companyIds = companyMainBusinessDao.findCompanyIdsByMainBusinessIds(
                Arrays.asList(4L)
        );
        System.out.println(companyIds);
    }

    @Test
    public void testCountSupplierByFirstLevelId() {
        Long count = companyMainBusinessDao.countSupplierByFirstLevelId(companyMainBusiness.getFirstLevelId());
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void findUserIdsByMainBusinessIds() {
        List<Long> userIds = companyMainBusinessDao.findUserIdsByMainBusinessIds(Lists.newArrayList(companyMainBusiness.getMainBusinessId()));
        Assert.assertEquals(1, userIds.size());
        Assert.assertEquals(companyMainBusiness.getUserId(), userIds.get(0));
    }

    @Test
    public void testFindUserIdsByFirstLevelId() {
        List<Long> userIds = companyMainBusinessDao.findUserIdsByFirstLevelId(companyMainBusiness.getFirstLevelId());
        Assert.assertEquals(1, userIds.size());
    }

    @Test
    public void testFindUserIdsByFcIds() {
        List<Long> userIds = companyMainBusinessDao.findUserIdsByFcIds(companyMainBusiness.getFirstLevelId(), companyMainBusiness.getMainBusinessId(), Lists.newArrayList(companyMainBusiness.getMainBusinessId()));
        Assert.assertEquals(1, userIds.size());
    }

}
