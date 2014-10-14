package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.BaseTest;
import io.terminus.snz.user.model.SupplierAppointed;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Author:Grancy Guo
 * Created on 14-9-16.
 */
public class SupplierAppointedDaoTest extends BaseTest{
    private SupplierAppointed supplierAppointed;

    @Autowired
    private SupplierAppointedDao supplierAppointedDao;

    private void mock() {
        supplierAppointed = new SupplierAppointed();
        supplierAppointed.setCompanyId(1L);
        supplierAppointed.setAdvice("test");
        supplierAppointed.setCorporation("testCorporation");
        supplierAppointed.setStatus(0);
    }

    @Before
    public void setUp() {
        mock();
        supplierAppointedDao.create(supplierAppointed);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(supplierAppointed.getId());
    }

    @Test
    public void countSupplierAppointedTest() {
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 10);
        params.put("status", 0);
        params.put("supplierName", "testCorporation");
        Long count = supplierAppointedDao.countSupplierAppointed(params);
        Assert.assertTrue(count != 0);
    }

    @Test
    public void findSupplierAppiontedByParamsTest() {
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("offset", 0);
        params.put("limit", 10);
        params.put("status", 0);
        params.put("supplierName", "testCorporation");

        List<SupplierAppointed> supplierAppointedList = supplierAppointedDao.findSupplierAppiontedByParams(params);
        Assert.assertNotNull(supplierAppointedList);
    }

    @Test
    public void updateStatusTest(){
        HashMap<String, Object> params = Maps.newHashMap();
        params.put("advice", "test");
        params.put("status", 1);
        params.put("id", 1);
        Assert.assertTrue(supplierAppointedDao.updateStatus(params));
    }

    @Test
    public void findCompanyTest(){
        Assert.assertEquals(supplierAppointedDao.findCompany(1L).getCompanyId().longValue(), 1);
    }


}
