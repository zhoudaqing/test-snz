package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.ProductOwnerDao;
import io.terminus.snz.user.model.ProductOwner;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author wanggen 2014-08-18 10:50:18
 */
public class ProductOwnerServiceImplTest {

    @InjectMocks
    private ProductOwnerServiceImpl productOwnerService;

    @Mock
    private ProductOwnerDao productOwnerDao;

    @Mock
    private ProductOwner productOwner;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        productOwner = new ProductOwner();
        productOwner.setId(1l); //自增主键
        productOwner.setFactoryNum("工厂ID-1"); //工厂ID
        productOwner.setFactoryName("工程名称-1"); //工程名称
        productOwner.setProductLineId(1); //产品线ID
        productOwner.setProductLineName("产品线名称-1"); //产品线名称
        productOwner.setOwnerId(1l); //产品负责人ID
        productOwner.setOwnerName("产品负责人-1"); //产品负责人
        productOwner.setCreatedAt(DateTime.now().toDate()); //创建时间
        productOwner.setUpdatedAt(DateTime.now().toDate()); //更新时间
    }

    @Test
    public void testCreate() throws Exception {

        // Should be successfully
        when(productOwnerDao.create(productOwner)).thenReturn(1L);
        Response<Long> response = productOwnerService.create(productOwner);
        Assert.assertTrue("The new created id must be:[1]", response.getResult()==1);

        // Should be interrupted
        when(productOwnerDao.create(Matchers.<ProductOwner>anyObject())).thenThrow(new RuntimeException("Interruption"));
        response = productOwnerService.create(productOwner);
        Assert.assertTrue("Should be interrupted", response.getError()!=null);

        // Should be fast failed
        response = productOwnerService.create(null);
        Assert.assertTrue("Should be fast failed", response.getError()!=null);

    }


    @Test
    public void testFindById() throws Exception {

        // Should be successfully
        when(productOwnerDao.findById(1L)).thenReturn(productOwner);
        Response<ProductOwner> findByIdResp = productOwnerService.findById(1L);
        Assert.assertTrue("The found object's id must be 1" ,
                          findByIdResp.getResult().getId()==1);

        // Should be interrupted
        when(productOwnerDao.findById(Matchers.<Long>anyObject())).thenThrow(new RuntimeException("Interruption"));
        findByIdResp = productOwnerService.findById(1L);
        Assert.assertTrue("Should be interrupted", findByIdResp.getError()!=null);

        // Should be fast failed
        findByIdResp = productOwnerService.findById(null);
        Assert.assertTrue("Should be fast failed", findByIdResp.getError()!=null);

    }


    @Test
    public void testFindByIds() throws Exception {

        // Should be successfully
        when(productOwnerDao.findByIds(Lists.newArrayList(1l)))
            .thenReturn(Lists.newArrayList(productOwner));
        Response<List<ProductOwner>> findByIdsResp = productOwnerService.findByIds(Lists.newArrayList(1l));
        Assert.assertTrue("The found list must by at least one record",
                findByIdsResp.getResult().size()==1 &&
                findByIdsResp.getResult().get(0).getId()==1);

        // Should be interrupted
        when(productOwnerDao.findByIds(Lists.newArrayList(Matchers.<Long>anyObject()))).thenThrow(new RuntimeException("Interruption"));
        findByIdsResp = productOwnerService.findByIds(Lists.newArrayList(1l));
        Assert.assertTrue("Should be interrupted", findByIdsResp.getError()!=null);

        // Should be fast failed
        findByIdsResp = productOwnerService.findByIds(Lists.<Long>newArrayList());
        Assert.assertTrue("Should be fast failed", findByIdsResp.getError()!=null);

    }


    @Test
    public void testFindByProductLineId(){

        Integer productLineId = 1; //产品线ID

        List<ProductOwner> productOwnerList = productOwnerDao.findByProductLineId(productLineId);

        Response<List<ProductOwner>> resp;
        List<ProductOwner> productOwners = Lists.newArrayList(productOwner);

        // Should be successfully
        when(productOwnerDao.findByProductLineId(productLineId)).thenReturn(productOwners);
        resp = productOwnerService.findByProductLineId(productLineId);
        Assert.assertTrue("At least one record must be found", resp.getResult().size()>=1);

        // Should be interrupted
        when(productOwnerDao.findByProductLineId(anyInt())).thenThrow(new RuntimeException("Interruption"));
        resp = productOwnerService.findByProductLineId(productLineId);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);

        // Should be fast failed
        //resp = productOwnerService.findByUserId(anyInteger());
        //Assert.assertTrue("Should be fast failed", resp.getError()!=null);

    }

    @Test
    public void testFindByProductLineName(){

        String productLineName = "产品线名称-1"; //产品线名称

        List<ProductOwner> productOwnerList = productOwnerDao.findByProductLineName(productLineName);

        Response<List<ProductOwner>> resp;
        List<ProductOwner> productOwners = Lists.newArrayList(productOwner);

        // Should be successfully
        when(productOwnerDao.findByProductLineName(productLineName)).thenReturn(productOwners);
        resp = productOwnerService.findByProductLineName(productLineName);
        Assert.assertTrue("At least one record must be found", resp.getResult().size()>=1);

        // Should be interrupted
        when(productOwnerDao.findByProductLineName(anyString())).thenThrow(new RuntimeException("Interruption"));
        resp = productOwnerService.findByProductLineName(productLineName);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);

        // Should be fast failed
        //resp = productOwnerService.findByUserId(anyString());
        //Assert.assertTrue("Should be fast failed", resp.getError()!=null);

    }


    @Test
    public void testUpdate() throws Exception {

        // Should be successfully
        when(productOwnerDao.update(productOwner)).thenReturn(1);
        productOwner.setId(1l); //自增主键
        productOwner.setFactoryNum("工厂ID-1"); //工厂ID
        productOwner.setFactoryName("工程名称-1"); //工程名称
        productOwner.setProductLineId(1); //产品线ID
        productOwner.setProductLineName("产品线名称-1"); //产品线名称
        productOwner.setOwnerId(1l); //产品负责人ID
        productOwner.setOwnerName("产品负责人-1"); //产品负责人
        productOwner.setCreatedAt(DateTime.now().toDate()); //创建时间
        productOwner.setUpdatedAt(DateTime.now().toDate()); //更新时间
        Response<Integer> updateResp = productOwnerService.update(productOwner);
        Assert.assertTrue("At least one record must be updated", updateResp.getResult()==1);

        // Should be interrupted
        when(productOwnerDao.update(Matchers.<ProductOwner>anyObject())).thenThrow(new RuntimeException("Interruption"));
        updateResp = productOwnerService.update(productOwner);
        Assert.assertTrue("Should be interrupted", updateResp.getError()!=null);

        // Should be fast failed
        updateResp = productOwnerService.update(null);
        Assert.assertTrue("Should be fast failed", updateResp.getError()!=null);

    }

    @Test
    public void testDeleteByIds() throws Exception {

        // Should be successfully
        when(productOwnerDao.deleteByIds(Lists.newArrayList(1l))).thenReturn(1);
        Response<Integer> deleteResponse = productOwnerService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("At least one record must be deleted", deleteResponse.getResult()==1);

        // Should be interrupted
        when(productOwnerDao.deleteByIds(Matchers.<List>anyObject())).thenThrow(new RuntimeException("Interruption"));
        deleteResponse = productOwnerService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("Should be interrupted", deleteResponse.getError()!=null);

        // Should be fast failed
        deleteResponse = productOwnerService.deleteByIds(null);
        Assert.assertTrue("Should be fast failed", deleteResponse.getError()!=null);

    }
}