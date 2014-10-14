/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.user.model.SupplierContact;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对 `snz_supplier_contacts` CRUD 测试<BR>
 * created by wanggen 2014-09-11 21:18:48
 */
public class SupplierContactDaoTest extends TestBaseDao {

    @Autowired
    private SupplierContactDao supplierContactDao;

    private Long createdId = null;

    private SupplierContact supplierContact = null;

    @Before
    public void setUp() throws Exception {
        supplierContact = new SupplierContact();
        supplierContact.setId(1l); //主健
        supplierContact.setSupplierName("供应商名称-1"); //供应商名称
        supplierContact.setSupplierCode("供应商编码-1"); //供应商编码
        supplierContact.setPhone("联系电话-1"); //联系电话
        supplierContact.setCreatedAt(DateTime.now().toDate()); //创建时间
        supplierContact.setUpdatedAt(DateTime.now().toDate()); //更新时间
        createdId = supplierContactDao.create(supplierContact);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = supplierContactDao.create(supplierContact);
        Assert.assertTrue("One record must be created", createdId>=1);
    }


    @Test
    public void testUpdate() throws Exception {
        SupplierContact supplierContact = new SupplierContact();
        supplierContact.setId(createdId);
        
        supplierContact.setSupplierName("供应商名称-1-new"); //供应商名称
        supplierContact.setSupplierCode("供应商编码-1-new"); //供应商编码
        supplierContact.setPhone("联系电话-1-new"); //联系电话
        supplierContact.setCreatedAt(DateTime.now().toDate()); //创建时间
        supplierContact.setUpdatedAt(DateTime.now().toDate()); //更新时间
        int updated = supplierContactDao.update(supplierContact);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = supplierContactDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}