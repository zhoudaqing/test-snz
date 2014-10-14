package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierModuleCount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-28.
 */
public class SupplierModuleCountDaoTest extends TestBaseDao {

    private SupplierModuleCount supplierModuleCount;

    @Autowired
    private SupplierModuleCountDao supplierModuleCountDao;

    private void mock() {
        supplierModuleCount = new SupplierModuleCount();
        supplierModuleCount.setModuleId(1L);
        supplierModuleCount.setModuleName("test");
        supplierModuleCount.setSupplierCount(3);
        supplierModuleCount.setBadCount(3);
        supplierModuleCount.setBestCount(6);
        supplierModuleCount.setLimitedCount(8);
        supplierModuleCount.setStandardCount(9);
    }

    @Before
    public void setUp() {
        mock();
        supplierModuleCountDao.create(supplierModuleCount);
    }

    @Test
    public void testFindByModuleId() {
        SupplierModuleCount model = supplierModuleCountDao.findByModuleId(supplierModuleCount.getModuleId());
        Assert.assertNotNull(model);
    }

    @Test
    public void testFindAll() {
        List<SupplierModuleCount> supplierModuleCounts = supplierModuleCountDao.findAll();
        Assert.assertTrue(supplierModuleCounts.size() == 1);
    }

    @Test
    public void testUpdate() {
        SupplierModuleCount updated = new SupplierModuleCount();
        updated.setModuleId(supplierModuleCount.getModuleId());
        updated.setBestCount(3000);
        supplierModuleCountDao.update(updated);
        SupplierModuleCount model = supplierModuleCountDao.findByModuleId(supplierModuleCount.getModuleId());

        Assert.assertEquals(updated.getBestCount(), model.getBestCount());
    }


}
