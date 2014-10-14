package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ImportGoodList;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Date: 7/11/14
 * Time: 11:15
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodListDaoTest extends BasicTest {

    @Autowired
    ImportGoodListDao importGoodListDao;

    private ImportGoodList init;

    private ImportGoodList genNew() {
        ImportGoodList igl = new ImportGoodList();
        igl.setRequirementId(1l);
        igl.setRequirementName("hehe");
        igl.setSeriesId(1l);
        igl.setSeriesName("haha");
        igl.setInCharge("John Doe");
        igl.setModuleNum("10086");
        igl.setProductLine("Battle Ship");
        igl.setModuleName("soso");
        igl.setModuleId(1l);

        return igl;
    }

    @Before
    public void setup() {
        init = genNew();
        init.setInCharge("Thor");
        importGoodListDao.create(init);

        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindById() {
        ImportGoodList found = importGoodListDao.findById(init.getId());

        assertNotNull(found);
        assertEquals("Thor", found.getInCharge());
    }

    @Test
    public void shouldFindBy() {
        ImportGoodList params = new ImportGoodList();
        params.setInCharge("Thor");
        ImportGoodList found = importGoodListDao.findBy(params);

        assertNotNull(found);
        assertEquals(init.getId(), found.getId());
    }

    @Test
    public void shouldUpdateRecord() {
        init.setInCharge("Loki");
        importGoodListDao.update(init);
        ImportGoodList found = importGoodListDao.findById(init.getId());

        assertNotNull(found);
        assertEquals("Loki", found.getInCharge());
    }
}
