package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierGroupRelation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
public class SupplierGroupDaoTest extends TestBaseDao {

    @Autowired
    private SupplierGroupDao supplierGroupDao;

    @Test
    public void testCreate() throws Exception {
        SupplierGroupRelation relation = new SupplierGroupRelation();
        relation.setSupplierId(1L);
        relation.setGroupId(2L);
        assertTrue(supplierGroupDao.create(relation) > 0);
        assertNotNull(supplierGroupDao.findOneBySupplierId(1L));
        assertNull(supplierGroupDao.findOneBySupplierId(2L));
    }

    @Test
    public void testUpdate() {
        Long id = supplierGroupDao.create(1L, 1L);
        assertTrue(id > 0);
        SupplierGroupRelation existRelation = supplierGroupDao.findOneBySupplierId(1L);
        assertNotNull(existRelation);
        existRelation.setSupplierId(2L);
        existRelation.setGroupId(2L);
        supplierGroupDao.update(existRelation);
        SupplierGroupRelation updatedRelation = supplierGroupDao.findOneBySupplierId(2L);
        assertNotNull(updatedRelation);
        assertThat(updatedRelation.getSupplierId(), is(2L));
        assertThat(updatedRelation.getGroupId(), is(2L));
    }

    @Test
    public void testUpdateGroupId() {
        assertTrue(supplierGroupDao.create(1L, 1L) > 0);
        assertTrue(supplierGroupDao.create(2L, 1L) > 0);
        assertTrue(supplierGroupDao.create(3L, 1L) > 0);
        assertTrue(supplierGroupDao.create(4L, 1L) > 0);
        List<SupplierGroupRelation> existRelations = supplierGroupDao.findByGroupId(1L);
        assertThat(existRelations.size(), is(4));

        supplierGroupDao.updateGroupId(1L, 2L);
        assertThat(supplierGroupDao.findByGroupId(1L).size(), is(0));
        assertThat(supplierGroupDao.findByGroupId(2L).size(), is(4));
    }

    @Test
    public void testDelete() {
        Long id = supplierGroupDao.create(1L, 1L);
        assertTrue(id > 0);
        supplierGroupDao.delete(id);
        assertNull(supplierGroupDao.findOneBySupplierId(1L));
    }

    @Test
    public void testGetMaxGroupId() {
        assertTrue(supplierGroupDao.create(1L, 1L) > 0);
        assertTrue(supplierGroupDao.create(2L, 1L) > 0);
        assertTrue(supplierGroupDao.create(3L, 1L) > 0);
        assertTrue(supplierGroupDao.create(4L, 1L) > 0);
        assertTrue(supplierGroupDao.create(5L, 2L) > 0);
        // 当前最大2
        assertThat(supplierGroupDao.getMaxGroupId(), is(2L));

        assertTrue(supplierGroupDao.create(6L, 10L) > 0);
        // 当前最大10
        assertThat(supplierGroupDao.getMaxGroupId(), is(10L));
    }

    @Test
    public void testFindOneBy() throws Exception {
        SupplierGroupRelation relation = new SupplierGroupRelation();
        relation.setSupplierId(1L);
        relation.setGroupId(2L);
        assertTrue(supplierGroupDao.create(relation) > 0);

        SupplierGroupRelation params = new SupplierGroupRelation();
        params.setSupplierId(1L);
        SupplierGroupRelation exist = supplierGroupDao.findOneBy(params);
        assertNotNull(exist);
        assertThat(exist.getSupplierId(), is(1L));
        assertThat(exist.getGroupId(), is(2L));
    }

    @Test
    public void testFindBy() throws Exception {
        SupplierGroupRelation relation = new SupplierGroupRelation();
        relation.setSupplierId(1L);
        relation.setGroupId(1L);
        assertTrue(supplierGroupDao.create(relation) > 0);

        SupplierGroupRelation relation2 = new SupplierGroupRelation();
        relation2.setSupplierId(2L);
        relation2.setGroupId(1L);
        assertTrue(supplierGroupDao.create(relation2) > 0);

        SupplierGroupRelation params = new SupplierGroupRelation();
        List<SupplierGroupRelation> existRelations = supplierGroupDao.findBy(params);
        assertThat(existRelations.size(), is(2));
        assertThat(existRelations.get(0).getSupplierId(), is(1L));
        assertThat(existRelations.get(1).getSupplierId(), is(2L));

        params.setGroupId(1L);
        assertThat(supplierGroupDao.findBy(params).size(), is(2));
    }
}