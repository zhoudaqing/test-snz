package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.ImportGood;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.terminus.common.utils.Arguments.isNullOrEmpty;
import static org.junit.Assert.*;

/**
 * Date: 7/9/14
 * Time: 16:04
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodTest extends BasicTest {

    ImportGood init;

    @Autowired
    ImportGoodDao importGoodDao;

    private ImportGood genNew() {
        ImportGood ig = new ImportGood();
        ig.setRequirementId(1l);
        ig.setCompanyId(1l);
        ig.setCompanyName("Awesome");
        ig.setModuleId(1l);
        ig.setSupplierId(1l);

        return ig;
    }

    @Before
    public void setup() {
        init = genNew();
        importGoodDao.create(init);

        assertNotNull(init.getId());
    }

    @Test
    public void shouldFindById() {
        ImportGood found = importGoodDao.findById(init.getId());

        assertNotNull(found);
        assertEquals(init.getId(), found.getId());
    }

    @Test
    public void shouldFindOneByParams() {
        ImportGood param = new ImportGood();
        param.setCompanyName("Awesome");
        ImportGood found = importGoodDao.findOneBy(param);

        assertNotNull(found);
        assertEquals(init.getId(), found.getId());
    }

    @Test
    public void shouldUpdateRecord() {
        ImportGood update = importGoodDao.findById(init.getId());
        update.setCompanyName("sup dude");
        importGoodDao.update(update);

        ImportGood updated = importGoodDao.findById(init.getId());
        assertNotNull(updated);
        assertEquals("sup dude", updated.getCompanyName());
    }

    @Test
    public void shouldFindListByParams() {
        ImportGood another = genNew();
        another.setSupplierId(2l);
        another.setCompanyId(2l);
        importGoodDao.create(another);

        ImportGood param = new ImportGood();
        param.setModuleId(1l);
        List<ImportGood> importGoods = importGoodDao.findBy(param);

        assertFalse("Should not be empty", isNullOrEmpty(importGoods));
        assertEquals(2, importGoods.size());
    }
}
