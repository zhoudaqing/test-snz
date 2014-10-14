/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.user.model.SupplierModuleDetail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 对 `snz_supplier_module_details` CRUD 测试<BR>
 * created by wanggen 2014-09-22 22:23:37
 */
public class SupplierModuleDetailDaoTest extends TestBaseDao {

    @Autowired
    private SupplierModuleDetailDao supplierModuleDetailDao;

    private Long createdId = null;

    private SupplierModuleDetail supplierModuleDetail = null;

    @Before
    public void setUp() throws Exception {
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
        createdId = supplierModuleDetailDao.create(supplierModuleDetail);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = supplierModuleDetailDao.create(supplierModuleDetail);
        Assert.assertTrue("One record must be created", createdId>=1);
    }

    @Test
    public void testFindById() throws Exception {
        SupplierModuleDetail byId = supplierModuleDetailDao.findById(createdId);
        Assert.assertTrue("The requery result's id must equals:"+createdId, byId!=null && byId.getId().equals(createdId));
    }

    @Test
    public void testFindBySupplierCode(){
        String supplierCode = "供应商编码-1"; //供应商编码
        List<SupplierModuleDetail> supplierModuleDetails = supplierModuleDetailDao.findBySupplierCode(supplierCode);
        Assert.assertTrue("At least one record must be found", supplierModuleDetails.size()>=1);
    }


    @Test
    public void testCountBySupplierCode(){
        String supplierCode = "供应商编码-1"; //供应商编码
        int count = supplierModuleDetailDao.countBySupplierCode(supplierCode);
        Assert.assertTrue("At least one record must be found", count>=1);
    }


    @Test
    public void testUpdate() throws Exception {
        SupplierModuleDetail supplierModuleDetail = new SupplierModuleDetail();
        supplierModuleDetail.setId(createdId);
        
        supplierModuleDetail.setModuleNum("物料号-1-new"); //物料号
        supplierModuleDetail.setModuleName("物料名称-1-new"); //物料名称
        supplierModuleDetail.setSupplierCode("供应商编码-1-new"); //供应商编码
        supplierModuleDetail.setSupplierName("供应商名称-1-new"); //供应商名称
        supplierModuleDetail.setPurchOrg("采购组织-1-new"); //采购组织
        supplierModuleDetail.setPurchGroup("采购组-1-new"); //采购组
        supplierModuleDetail.setModuleGroup("物料组-1-new"); //物料组
        supplierModuleDetail.setModuleGroupDesc("物料组描述-1-new"); //物料组描述
        supplierModuleDetail.setTaxCode("税码-1-new"); //税码
        supplierModuleDetail.setValidityStart("有效期开始-1-new"); //有效期开始
        supplierModuleDetail.setValidityEnd("有效期结束-1-new"); //有效期结束
        int updated = supplierModuleDetailDao.update(supplierModuleDetail);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = supplierModuleDetailDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}