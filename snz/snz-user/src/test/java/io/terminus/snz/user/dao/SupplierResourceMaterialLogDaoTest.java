package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierResourceMaterialLog;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/26/14
 */
public class SupplierResourceMaterialLogDaoTest extends TestBaseDao {

    @Autowired
    private SupplierResourceMaterialLogDao supplierResourceMaterialLogDao;

    private SupplierResourceMaterialLog getLog(Long supplierId, Long checkerId, Long times, Integer type, Integer status, String content) {
        SupplierResourceMaterialLog log1 = new SupplierResourceMaterialLog();
        log1.setSupplierId(supplierId);
        log1.setCheckerId(checkerId);
        log1.setTimes(times);
        log1.setType(type);
        log1.setStatus(status);
        log1.setContent(content);
        return log1;
    }

    @Test
    public void testFindOneBy() throws Exception {
        SupplierResourceMaterialLog log1 = getLog(1L, 2L, 1L, 5, 1, "nothing");
        assertTrue(0 < supplierResourceMaterialLogDao.create(log1));
        SupplierResourceMaterialLog params = new SupplierResourceMaterialLog();
        params.setSupplierId(1L);
        params.setType(5); // 商户主动提交
        params.setTimes(1L);
        SupplierResourceMaterialLog exist = supplierResourceMaterialLogDao.findLastOneBy(params);
        assertNotNull(exist);
        assertThat(exist.getContent(), is("nothing"));
    }

    @Test
    public void testFindBy() throws Exception {
        SupplierResourceMaterialLog log1 = getLog(1L, 2L, 1L, 5, 1, "nothing");
        assertTrue(0 < supplierResourceMaterialLogDao.create(log1));

        SupplierResourceMaterialLog log2 = getLog(1L, 2L, 1L, 1, 0, "not pass");
        assertTrue(0 < supplierResourceMaterialLogDao.create(log2));

        SupplierResourceMaterialLog params = new SupplierResourceMaterialLog();
        params.setSupplierId(1L);
        params.setTimes(1L);
        List<SupplierResourceMaterialLog> logs = supplierResourceMaterialLogDao.findBy(params);
        assertTrue(logs.size() == 2);
        assertThat(logs.get(0).getContent(), is("not pass"));
        assertThat(logs.get(1).getContent(), is("nothing"));
    }
}