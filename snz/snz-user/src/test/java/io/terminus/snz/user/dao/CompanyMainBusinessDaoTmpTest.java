package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.user.model.CompanyMainBusinessTmp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
public class CompanyMainBusinessDaoTmpTest extends TestBaseDao {

    private CompanyMainBusinessTmp companyMainBusinessTmp;

    @Autowired
    private CompanyMainBusinessTmpDao companyMainBusinessTmpDao;

    private void mock() {
        companyMainBusinessTmp = new CompanyMainBusinessTmp();
        companyMainBusinessTmp.setFirstLevelId(2L);
        companyMainBusinessTmp.setMainBusinessId(1L);
        companyMainBusinessTmp.setCompanyId(2L);
        companyMainBusinessTmp.setUserId(2L);
        companyMainBusinessTmp.setName("冰箱制造");
    }

    @Before
    public void setUp() {
        mock();
        companyMainBusinessTmpDao.create(companyMainBusinessTmp);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(companyMainBusinessTmp.getId());
    }

    @Test
    public void testFindByUserId() {
        List<CompanyMainBusinessTmp> companyMainBusinessTmpes = companyMainBusinessTmpDao.findByUserId(companyMainBusinessTmp.getUserId());
        Assert.assertEquals(companyMainBusinessTmpes.get(0).getId(), companyMainBusinessTmp.getId());
    }

    @Test
    public void testFindByMainBusinessId() {
        List<CompanyMainBusinessTmp> companyMainBusinessTmpes = companyMainBusinessTmpDao.findByMainBusinessId(companyMainBusinessTmp.getMainBusinessId());
        Assert.assertEquals(companyMainBusinessTmpes.get(0).getId(), companyMainBusinessTmp.getId());
    }

    @Test
    public void testFindByCompanyId() {
        List<CompanyMainBusinessTmp> companyMainBusinessTmpes = companyMainBusinessTmpDao.findByCompanyId(companyMainBusinessTmp.getCompanyId());
        Assert.assertEquals(companyMainBusinessTmpes.get(0).getId(), companyMainBusinessTmp.getId());
    }

    @Test
    public void testDeleteByUserId() {
        companyMainBusinessTmpDao.deleteByUserId(companyMainBusinessTmp.getUserId());
        List<CompanyMainBusinessTmp> companyMainBusinessTmpes = companyMainBusinessTmpDao.findByUserId(companyMainBusinessTmp.getUserId());
        Assert.assertTrue(companyMainBusinessTmpes.size() == 0);
    }

    @Test
    public void testFindCompanyIdsByMainBusinessIds() {
        List<CompanyMainBusinessTmp> companyIds = companyMainBusinessTmpDao.findCompanyIdsByMainBusinessIds(
                Arrays.asList(4L)
        );
        System.out.println(companyIds);
    }

    @Test
    public void testCountSupplierByFirstLevelId() {
        Long count = companyMainBusinessTmpDao.countSupplierByFirstLevelId(companyMainBusinessTmp.getFirstLevelId());
        Assert.assertEquals(1, count.intValue());
    }

    @Test
    public void findUserIdsByMainBusinessIds() {
        List<Long> userIds = companyMainBusinessTmpDao.findUserIdsByMainBusinessIds(Lists.newArrayList(companyMainBusinessTmp.getMainBusinessId()));
        Assert.assertEquals(1, userIds.size());
        Assert.assertEquals(companyMainBusinessTmp.getUserId(), userIds.get(0));
    }

}
