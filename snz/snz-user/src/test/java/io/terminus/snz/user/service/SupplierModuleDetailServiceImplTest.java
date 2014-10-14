/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.SupplierModuleDetailDao;
import io.terminus.snz.user.model.SupplierModuleDetail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 供应商物料明细 服务类测试<BR>
 * @author wanggen 2014-09-22 22:23:37
 */
public class SupplierModuleDetailServiceImplTest {

    @InjectMocks
    private SupplierModuleDetailServiceImpl supplierModuleDetailService;

    @Mock
    private SupplierModuleDetailDao supplierModuleDetailDao;

    @Mock
    private SupplierModuleDetail supplierModuleDetail;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        supplierModuleDetail = new SupplierModuleDetail();
        supplierModuleDetail.setId(1l); //主健
        supplierModuleDetail.setModuleNum("物料号-1"); //物料号
        supplierModuleDetail.setModuleName("物料名称-1"); //物料名称
        supplierModuleDetail.setSupplierCode("供应商编码-1"); //供应商编码
        supplierModuleDetail.setSupplierName("供应商名称-1"); //供应商名称
        supplierModuleDetail.setPurchOrg("采购组织-1"); //采购组织
        supplierModuleDetail.setPurchGroup("采购组-1"); //采购组
        supplierModuleDetail.setModuleGroup("物料组-1"); //物料组
        supplierModuleDetail.setModuleGroupDesc("物料组描述-1"); //物料组描述
        supplierModuleDetail.setTaxCode("税码-1"); //税码
        supplierModuleDetail.setValidityStart("有效期开始-1"); //有效期开始
        supplierModuleDetail.setValidityEnd("有效期结束-1"); //有效期结束
    }

    @Test
    public void testCreate() throws Exception {

        // Should be successfully
        when(supplierModuleDetailDao.create(supplierModuleDetail)).thenReturn(1L);
        Response<Long> response = supplierModuleDetailService.create(supplierModuleDetail);
        Assert.assertTrue("The new created id must be:[1]", response.getResult()==1);

        // Should be interrupted
        when(supplierModuleDetailDao.create(Matchers.<SupplierModuleDetail>anyObject())).thenThrow(new RuntimeException("Interruption"));
        response = supplierModuleDetailService.create(supplierModuleDetail);
        Assert.assertTrue("Should be interrupted", response.getError()!=null);

        // Should be fast failed
        response = supplierModuleDetailService.create(null);
        Assert.assertTrue("Should be fast failed", response.getError()!=null);

    }


    @Test
    public void testFindById() throws Exception {

        // Should be successfully
        when(supplierModuleDetailDao.findById(1L)).thenReturn(supplierModuleDetail);
        Response<SupplierModuleDetail> findByIdResp = supplierModuleDetailService.findById(1L);
        Assert.assertTrue("The found object's id must be 1" ,
                          findByIdResp.getResult().getId()==1);

        // Should be interrupted
        when(supplierModuleDetailDao.findById(Matchers.<Long>anyObject())).thenThrow(new RuntimeException("Interruption"));
        findByIdResp = supplierModuleDetailService.findById(1L);
        Assert.assertTrue("Should be interrupted", findByIdResp.getError()!=null);

        // Should be fast failed
        findByIdResp = supplierModuleDetailService.findById(null);
        Assert.assertTrue("Should be fast failed", findByIdResp.getError()!=null);

    }

    @Test
    public void testFindBySupplierCode(){

        String supplierCode = "供应商编码-1"; //供应商编码

        Response<List<SupplierModuleDetail>> resp;
        List<SupplierModuleDetail> supplierModuleDetails = Lists.newArrayList(supplierModuleDetail);

        // Should be successfully
        when(supplierModuleDetailDao.findBySupplierCode(supplierCode)).thenReturn(supplierModuleDetails);
        resp = supplierModuleDetailService.findBySupplierCode(supplierCode);
        Assert.assertTrue("At least one record must be found", resp.getResult().size()>=1);

        // Should be interrupted
        when(supplierModuleDetailDao.findBySupplierCode(anyString())).thenThrow(new RuntimeException("Interruption"));
        resp = supplierModuleDetailService.findBySupplierCode(supplierCode);
        Assert.assertTrue("Should be interrupted", resp.getError()!=null);

        // Should be fast failed
        //resp = supplierModuleDetailService.findByUserId(anyString());
        //Assert.assertTrue("Should be fast failed", resp.getError()!=null);

    }


    @Test
    public void testUpdate() throws Exception {

        // Should be successfully
        when(supplierModuleDetailDao.update(supplierModuleDetail)).thenReturn(1);
        supplierModuleDetail.setId(1l); //主健
        supplierModuleDetail.setModuleNum("物料号-1"); //物料号
        supplierModuleDetail.setModuleName("物料名称-1"); //物料名称
        supplierModuleDetail.setSupplierCode("供应商编码-1"); //供应商编码
        supplierModuleDetail.setSupplierName("供应商名称-1"); //供应商名称
        supplierModuleDetail.setPurchOrg("采购组织-1"); //采购组织
        supplierModuleDetail.setPurchGroup("采购组-1"); //采购组
        supplierModuleDetail.setModuleGroup("物料组-1"); //物料组
        supplierModuleDetail.setModuleGroupDesc("物料组描述-1"); //物料组描述
        supplierModuleDetail.setTaxCode("税码-1"); //税码
        supplierModuleDetail.setValidityStart("有效期开始-1"); //有效期开始
        supplierModuleDetail.setValidityEnd("有效期结束-1"); //有效期结束
        Response<Integer> updateResp = supplierModuleDetailService.update(supplierModuleDetail);
        Assert.assertTrue("At least one record must be updated", updateResp.getResult()==1);

        // Should be interrupted
        when(supplierModuleDetailDao.update(Matchers.<SupplierModuleDetail>anyObject())).thenThrow(new RuntimeException("Interruption"));
        updateResp = supplierModuleDetailService.update(supplierModuleDetail);
        Assert.assertTrue("Should be interrupted", updateResp.getError()!=null);

        // Should be fast failed
        updateResp = supplierModuleDetailService.update(null);
        Assert.assertTrue("Should be fast failed", updateResp.getError()!=null);

    }

    @Test
    public void testDeleteByIds() throws Exception {

        // Should be successfully
        when(supplierModuleDetailDao.deleteByIds(Lists.newArrayList(1l))).thenReturn(1);
        Response<Integer> deleteResponse = supplierModuleDetailService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("At least one record must be deleted", deleteResponse.getResult()==1);

        // Should be interrupted
        when(supplierModuleDetailDao.deleteByIds(Matchers.<List>anyObject())).thenThrow(new RuntimeException("Interruption"));
        deleteResponse = supplierModuleDetailService.deleteByIds(Lists.newArrayList(1l));
        Assert.assertTrue("Should be interrupted", deleteResponse.getError()!=null);

        // Should be fast failed
        deleteResponse = supplierModuleDetailService.deleteByIds(null);
        Assert.assertTrue("Should be fast failed", deleteResponse.getError()!=null);

    }
}