package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierCountBySupplyPark;
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
public class SupplierCountBySupplyParkDaoTest extends TestBaseDao {

    private SupplierCountBySupplyPark supplierCountBySupplyPark;

    @Autowired
    private SupplierCountBySupplyParkDao supplierCountBySupplyParkDao;

    private void mock() {
        supplierCountBySupplyPark = new SupplierCountBySupplyPark();
        supplierCountBySupplyPark.setDate(new Date());
        supplierCountBySupplyPark.setSupplyParkId(1L);
        supplierCountBySupplyPark.setSupplierCount(40L);
    }

    @Before
    public void setUp() {
        mock();
        supplierCountBySupplyParkDao.create(supplierCountBySupplyPark);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierCountBySupplyPark.getId());
    }

    @Test
    public void testFindLastBy() {
        List<SupplierCountBySupplyPark> supplierCountBySupplyParks = supplierCountBySupplyParkDao.findLastBy(supplierCountBySupplyPark.getSupplyParkId(), 30);
        Assert.assertEquals(supplierCountBySupplyParks.size(), 1);
    }

}
