package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.SupplierQualification;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
* Date: 7/23/14
* Time: 10:20
* Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
*/
public class SupplierQualificationDaoTest extends BasicTest {

    private SupplierQualification init = genNew();

    @Autowired
    SupplierQualificationDao dao;

    private SupplierQualification genNew() {
        SupplierQualification gen = new SupplierQualification();
        gen.setSupplierId(1l);
        gen.setVCode("1100");
        gen.setStage(1);

        return gen;
    }

    @Before
    public void setup() {
        dao.create(init);
        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindById() {
        SupplierQualification found = dao.findById(init.getId());
        assertNotNull(found);
    }

    @Test
    public void shouldUpdate() {
        SupplierQualification found = dao.findById(init.getId());
        found.setVCode("23333");
        dao.update(found);

        found = dao.findById(init.getId());
        assertEquals("23333", found.getVCode());
    }

    @Test
    public void shouldFindByParams() {
        SupplierQualification params = new SupplierQualification();
        params.setSupplierId(1l);
        SupplierQualification found = dao.findBy(params);

        assertNotNull(found);
    }

    @Test
    public void shouldFindBySupplierId() {
        SupplierQualification found = dao.findBySupplierId(1l);

        assertNotNull(found);
        assertEquals(init.getId(), found.getId());
    }
}
