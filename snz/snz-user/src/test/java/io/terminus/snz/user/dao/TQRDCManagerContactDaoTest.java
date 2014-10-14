/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableList;
import io.terminus.snz.user.model.TQRDCManagerContact;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 对 `snz_tqrdc_manager_contacts` CRUD 测试<BR>
 * created by wanggen 2014-09-11 21:09:51
 */
public class TQRDCManagerContactDaoTest extends TestBaseDao {

    @Autowired
    private TQRDCManagerContactDao tQRDCManagerContactDao;

    private Long createdId = null;

    private TQRDCManagerContact tQRDCManagerContact = null;

    @Before
    public void setUp() throws Exception {
        tQRDCManagerContact = new TQRDCManagerContact();
        tQRDCManagerContact.setId(1l); //主健
        tQRDCManagerContact.setName("发送人姓名-1"); //发送人姓名
        tQRDCManagerContact.setPhone("联系方式-1"); //联系方式
        tQRDCManagerContact.setTemplate("短信模板-1"); //短信模板
        tQRDCManagerContact.setRemark("备注-1"); //备注
        tQRDCManagerContact.setCreatedAt(DateTime.now().toDate()); //创建时间
        tQRDCManagerContact.setUpdatedAt(DateTime.now().toDate()); //更新时间
        createdId = tQRDCManagerContactDao.create(tQRDCManagerContact);
    }

    @Test
    public void testCreate() throws Exception {
        createdId = tQRDCManagerContactDao.create(tQRDCManagerContact);
        Assert.assertTrue("One record must be created", createdId>=1);
    }


    @Test
    public void testUpdate() throws Exception {
        TQRDCManagerContact tQRDCManagerContact = new TQRDCManagerContact();
        tQRDCManagerContact.setId(createdId);
        
        tQRDCManagerContact.setName("发送人姓名-1-new"); //发送人姓名
        tQRDCManagerContact.setPhone("联系方式-1-new"); //联系方式
        tQRDCManagerContact.setTemplate("短信模板-1-new"); //短信模板
        tQRDCManagerContact.setRemark("备注-1-new"); //备注
        tQRDCManagerContact.setCreatedAt(DateTime.now().toDate()); //创建时间
        tQRDCManagerContact.setUpdatedAt(DateTime.now().toDate()); //更新时间
        int updated = tQRDCManagerContactDao.update(tQRDCManagerContact);
        Assert.assertTrue("One record must be updated", updated==1);
    }


    @Test
    public void testDeleteByIds() throws Exception {
        int deleted = tQRDCManagerContactDao.deleteByIds(ImmutableList.of(createdId));
        Assert.assertTrue("One record must be deleted", deleted==1);
    }

}