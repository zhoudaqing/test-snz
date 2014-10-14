package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierCountByLevel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-24.
 */
public class SupplierCountByLevelDaoTest extends TestBaseDao {

    private SupplierCountByLevel supplierCountByLevel;

    @Autowired
    private SupplierCountByLevelDao supplierCountByLevelDao;

    private void mock() {
        supplierCountByLevel = new SupplierCountByLevel();
        supplierCountByLevel.setMonth("2014-04");
        supplierCountByLevel.setLevel(1);
        supplierCountByLevel.setSupplierCount(40L);
    }

    @Before
    public void setUp() {
        mock();
        supplierCountByLevelDao.create(supplierCountByLevel);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierCountByLevel.getId());
    }

    @Test
    public void testFindLastBy() {
        List<SupplierCountByLevel> supplierCountByLevels = supplierCountByLevelDao.findLastBy(supplierCountByLevel.getLevel(), 30);
        Assert.assertEquals(supplierCountByLevels.size(), 1);
    }

    @Test
    public void testCountByMonth() {
        Integer count = supplierCountByLevelDao.countByMonth(supplierCountByLevel.getMonth());
        Assert.assertEquals(1, count.intValue());
    }

}
