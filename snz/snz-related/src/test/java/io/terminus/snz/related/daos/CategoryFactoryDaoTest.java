package io.terminus.snz.related.daos;

import io.terminus.snz.related.BaseDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class CategoryFactoryDaoTest extends BaseDaoTest {

    @Autowired
    private CategoryFactoryDao categoryFactoryDao;

    @Test
    public void testFindByProductId() throws Exception {
        assertNotNull(categoryFactoryDao.findByProductId(1l , 1l));
    }
}