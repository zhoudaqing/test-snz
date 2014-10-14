package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.user.model.ProductOwner;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 对 `snz_product_owners` CRUD 测试<P>
 * created by wanggen 2014-08-13 22:17:08
 */
public class ProductOwnerDaoTest extends TestBaseDao {

    @Autowired
    private ProductOwnerDao productOwnerDao;

    private Long createdId = null;

    private ProductOwner productOwner = null;

    @Before
    public void setUp() throws Exception {
        productOwner = new ProductOwner();
        productOwner.setId(1l); //自增主键
        productOwner.setFactoryNum(""); //工厂ID
        productOwner.setFactoryName("工程名称-1"); //工程名称
        productOwner.setProductLineId(1); //产品线ID
        productOwner.setProductLineName("产品线名称-1"); //产品线名称
        productOwner.setOwnerName("产品负责人-1"); //产品负责人
        productOwner.setOwnerId(1l); //产品负责人ID
        productOwner.setCreatedAt(DateTime.now().toDate()); //创建时间
        productOwner.setUpdatedAt(DateTime.now().toDate()); //更新时间
        createdId = productOwnerDao.create(productOwner);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = productOwnerDao.create(productOwner);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        ProductOwner byId = productOwnerDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }


    @Test
    public void testFindByIds() throws Exception {
        List<ProductOwner> byIds = productOwnerDao.findByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be selectd"+createdId, byIds!=null && byIds.size()==1);
    }

    @Test
    public void testFindByProductLineId(){

        Integer productLineId = 1; //产品线ID

        List<ProductOwner> productOwnerList = productOwnerDao.findByProductLineId(productLineId);

        Assert.assertTrue("At least one record must be found", productOwnerList.size()>=1);

    }


    @Test
    public void testFindByProductLineName(){

        String productLineName = "产品线名称-1"; //产品线名称

        List<ProductOwner> productOwnerList = productOwnerDao.findByProductLineName(productLineName);

        Assert.assertTrue("At least one record must be found", productOwnerList.size()>=1);

    }


    @Test
    public void testUpdate() throws Exception {
        ProductOwner productOwner = new ProductOwner();
        productOwner.setId(createdId);
        productOwner.setFactoryNum("工厂ID-1001"); //工厂ID
        productOwner.setFactoryName("工程名称-1-new"); //工程名称
        productOwner.setProductLineId(1001); //产品线ID
        productOwner.setProductLineName("产品线名称-1-new"); //产品线名称
        productOwner.setOwnerName("产品负责人-1-new"); //产品负责人
        productOwner.setOwnerId(1001l); //产品负责人ID
        productOwner.setCreatedAt(DateTime.now().toDate()); //创建时间
        productOwner.setUpdatedAt(DateTime.now().toDate()); //更新时间
        int updated = productOwnerDao.update(productOwner);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = productOwnerDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}