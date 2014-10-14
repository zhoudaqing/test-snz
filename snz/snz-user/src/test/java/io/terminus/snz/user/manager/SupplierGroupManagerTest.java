package io.terminus.snz.user.manager;

import io.terminus.snz.user.dao.SupplierGroupDao;
import io.terminus.snz.user.model.SupplierGroupRelation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class SupplierGroupManagerTest {

    @InjectMocks
    private SupplierGroupManager supplierGroupManager;

    @Mock
    private SupplierGroupDao supplierGroupDao;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHasRelation() throws Exception {
        SupplierGroupRelation relationA = new SupplierGroupRelation();
        relationA.setSupplierId(1L);
        relationA.setGroupId(1L);
        SupplierGroupRelation relationB = new SupplierGroupRelation();
        relationB.setSupplierId(2L);
        relationB.setGroupId(1L);
        SupplierGroupRelation relationC = new SupplierGroupRelation();
        relationC.setSupplierId(3L);
        relationC.setGroupId(2L);

        when(supplierGroupDao.findOneBySupplierId(1L)).thenReturn(relationA);
        when(supplierGroupDao.findOneBySupplierId(2L)).thenReturn(relationB);
        when(supplierGroupDao.findOneBySupplierId(3L)).thenReturn(relationC);

        assertTrue(supplierGroupManager.hasRelation(1L, 2L));
        assertFalse(supplierGroupManager.hasRelation(1L, 3L));
        assertFalse(supplierGroupManager.hasRelation(2L, 3L));
    }

    @Test
    public void testCreateRelation() throws Exception {

    }

    @Test
    public void testDeleteRelation() throws Exception {

    }

    @Test
    public void testGetRelatedSupplierIds() throws Exception {

    }
}