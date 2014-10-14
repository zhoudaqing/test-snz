package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierResourceMaterialSubject;
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
public class SupplierResourceMaterialSubjectDaoTest extends TestBaseDao {

    @Autowired
    private SupplierResourceMaterialSubjectDao supplierResourceMaterialSubjectDao;

    private SupplierResourceMaterialSubject getSubject(String name, Integer type, String role, Integer status, Long version) {
        SupplierResourceMaterialSubject subject = new SupplierResourceMaterialSubject();
        subject.setName(name);
        subject.setType(type);
        subject.setRole(role);
        subject.setStatus(status);
        subject.setVersion(version);
        return subject;
    }

    @Test
    public void testCreate() throws Exception {
        SupplierResourceMaterialSubject subject = getSubject("条目1", 1, "resource", 1, 1L);
        assertTrue(supplierResourceMaterialSubjectDao.create(subject) > 0);
        List<SupplierResourceMaterialSubject> exists = supplierResourceMaterialSubjectDao.findAllValid();
        assertFalse(exists.isEmpty());
        assertThat(exists.get(0).getName(), is("条目1"));
        assertThat(exists.get(0).getType(), is(1));
        assertThat(exists.get(0).getRole(), is("resource"));
        assertThat(exists.get(0).getStatus(), is(1));
        assertThat(exists.get(0).getVersion(), is(1L));
    }

    @Test
    public void testUpdate() throws Exception {
        SupplierResourceMaterialSubject subject = getSubject("条目1", 1, "resource", 1, 1L);
        assertTrue(supplierResourceMaterialSubjectDao.create(subject) > 0);

        SupplierResourceMaterialSubject neoSubject = getSubject("条目2", 0, "big_data", 1, 2L);
        neoSubject.setId(subject.getId());
        assertTrue(supplierResourceMaterialSubjectDao.update(neoSubject));
        List<SupplierResourceMaterialSubject> exists = supplierResourceMaterialSubjectDao.findAllValid();
        assertFalse(exists.isEmpty());
        assertThat(exists.get(0).getName(), is("条目2"));
        assertThat(exists.get(0).getType(), is(0));
        assertThat(exists.get(0).getRole(), is("big_data"));
        assertThat(exists.get(0).getStatus(), is(1));
        assertThat(exists.get(0).getVersion(), is(2L));
    }

    @Test
    public void testFindAllValid() throws Exception {
        SupplierResourceMaterialSubject subject = getSubject("条目1", 1, "resource", 1, 1L);
        assertTrue(supplierResourceMaterialSubjectDao.create(subject) > 0);

        List<SupplierResourceMaterialSubject> exists = supplierResourceMaterialSubjectDao.findAllValid();
        assertFalse(exists.isEmpty());

        subject.setStatus(0);
        assertTrue(supplierResourceMaterialSubjectDao.update(subject));
        exists = supplierResourceMaterialSubjectDao.findAllValid();
        assertTrue(exists.isEmpty());
    }

    @Test
    public void testFindById() throws Exception {
        SupplierResourceMaterialSubject subject = getSubject("条目1", 1, "resource", 1, 1L);
        assertTrue(supplierResourceMaterialSubjectDao.create(subject) > 0);

        SupplierResourceMaterialSubject exist = supplierResourceMaterialSubjectDao.findById(subject.getId());
        assertNotNull(exist);
        assertThat(exist.getName(), is("条目1"));

        assertNull(supplierResourceMaterialSubjectDao.findById(100L));
    }
}