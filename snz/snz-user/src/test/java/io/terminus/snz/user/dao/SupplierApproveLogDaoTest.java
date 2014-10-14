package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierApproveLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-22.
 */
public class SupplierApproveLogDaoTest extends TestBaseDao {

    private SupplierApproveLog supplierApproveLog;

    @Autowired
    private SupplierApproveLogDao supplierApproveLogDao;

    private void mock() {
        supplierApproveLog = new SupplierApproveLog();
        supplierApproveLog.setUserId(1L);
        supplierApproveLog.setApproveType(SupplierApproveLog.ApproveType.ENTER.value());
        supplierApproveLog.setApprovedAt(new Date());
        supplierApproveLog.setApproverId(3L);
        supplierApproveLog.setApproverName("kk");
        supplierApproveLog.setApproveResult(SupplierApproveLog.ApproveResult.OK.value());
    }

    @Before
    public void setUp() {
        mock();
        supplierApproveLogDao.create(supplierApproveLog);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierApproveLog.getId());
    }

    @Test
    public void testFindByUserId() {
        SupplierApproveLog model = supplierApproveLogDao.findByUserId(supplierApproveLog.getUserId());
        Assert.assertNotNull(model);
    }

    @Test
    public void testUpdateByUserId() {
        SupplierApproveLog updated = new SupplierApproveLog();
        updated.setId(supplierApproveLog.getId());
        updated.setUserId(supplierApproveLog.getUserId());
        updated.setApprovedAt(new Date());
        updated.setApproverId(1L);
        updated.setApproverName("ll");
        supplierApproveLogDao.update(updated);

        SupplierApproveLog model = supplierApproveLogDao.findByUserId(supplierApproveLog.getUserId());
        Assert.assertEquals(updated.getUserId(), model.getUserId());
    }

    @Test
    public void testDelete() {
        Assert.assertTrue(supplierApproveLogDao.delete(supplierApproveLog.getId()));
    }

    @Test
    public void testFindLastByUserIdAndApproveType() {
        SupplierApproveLog model = supplierApproveLogDao.findLastByUserIdAndApproveType(supplierApproveLog.getUserId(), supplierApproveLog.getApproveType());
        Assert.assertNotNull(model);
    }

}
