package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierCountByStatus;
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
public class SupplierCountByStatusDaoTest extends TestBaseDao {

    private SupplierCountByStatus supplierCountByStatus;

    @Autowired
    private SupplierCountByStatusDao supplierCountByStatusDao;

    private void mock() {
        supplierCountByStatus = new SupplierCountByStatus();
        supplierCountByStatus.setDate(new Date());
        supplierCountByStatus.setStatus(1);
        supplierCountByStatus.setSupplierCount(40L);
    }

    @Before
    public void setUp() {
        mock();
        supplierCountByStatusDao.create(supplierCountByStatus);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierCountByStatus.getId());
    }

    @Test
    public void testFindLastBy() {
        List<SupplierCountByStatus> supplierCountByStatuss = supplierCountByStatusDao.findLastBy(supplierCountByStatus.getStatus(), 30);
        Assert.assertEquals(supplierCountByStatuss.size(), 1);
    }

}
