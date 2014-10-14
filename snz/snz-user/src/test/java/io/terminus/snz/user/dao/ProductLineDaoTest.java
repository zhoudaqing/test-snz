package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.ProductLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-2.
 */
public class ProductLineDaoTest extends TestBaseDao {

    private ProductLine productLine;

    @Autowired
    private ProductLineDao productLineDao;

    private void mock() {
        productLine = new ProductLine();
        productLine.setName("测试");
    }

    @Before
    public void setUp() {
        mock();
        productLineDao.create(productLine);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(productLine.getId());
    }

    @Test
    public void testFindById() {
        ProductLine model = productLineDao.findById(productLine.getId());
        Assert.assertEquals(model.getId(), productLine.getId());
    }

    @Test
    public void testFindAll() {
        List<ProductLine> productLines = productLineDao.findAll();
        Assert.assertTrue(!productLines.isEmpty());
    }

}
