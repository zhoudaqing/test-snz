package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierCountByIndustry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-24.
 */
public class SupplierCountByIndustryDaoTest extends TestBaseDao {

    private SupplierCountByIndustry supplierCountByIndustry;

    @Autowired
    private SupplierCountByIndustryDao supplierCountByIndustryDao;

    private void mock() {
        supplierCountByIndustry = new SupplierCountByIndustry();
        supplierCountByIndustry.setDate(new Date());
        supplierCountByIndustry.setIndustry(1L);
        supplierCountByIndustry.setSupplierCount(40L);
    }

    @Before
    public void setUp() {
        mock();
        supplierCountByIndustryDao.create(supplierCountByIndustry);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierCountByIndustry.getId());
    }

    @Test
    public void testFindLastBy() {
        List<SupplierCountByIndustry> supplierCountByIndustrys = supplierCountByIndustryDao.findLastBy(supplierCountByIndustry.getIndustry(), 30);
        Assert.assertEquals(supplierCountByIndustrys.size(), 1);
    }

}
