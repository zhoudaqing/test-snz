package io.terminus.snz.user.dao;

import com.google.common.collect.Lists;
import io.terminus.snz.user.model.ContactInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午3:35
 */
public class ContactInfoTest extends TestBaseDao {

    private ContactInfo contactInfo;

    @Autowired
    private ContactInfoDao contactInfoDao;

    private void mock() {
        contactInfo = new ContactInfo();
        contactInfo.setName("rose");
        contactInfo.setMobile("18969973054");
        contactInfo.setCompanyId(1L);
        contactInfo.setUserId(2L);
        contactInfo.setDepartment("development");
        contactInfo.setDuty("leader");
        contactInfo.setEmail("g2@163.com");
        contactInfo.setOfficePhone("120");
    }

    @Before
    public void setUp() {
        mock();
        contactInfoDao.create(contactInfo);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(contactInfo.getId());
    }

    @Test
    public void testFindById() {
        ContactInfo model = contactInfoDao.findById(contactInfo.getId());
        Assert.assertTrue(model.getId() == contactInfo.getId());
    }

    @Test
    public void testFindByUserId() {
        ContactInfo model = contactInfoDao.findByUserId(contactInfo.getUserId());
        Assert.assertTrue(model.getId() == contactInfo.getId());
    }

    @Test
    public void testFindByUserIds() {
        List<ContactInfo> contactInfos = contactInfoDao.findByUserIds(Lists.newArrayList(contactInfo.getUserId()));
        Assert.assertTrue(contactInfos.size() == 1);
    }

    @Test
    public void testDelete() {
        contactInfoDao.delete(contactInfo.getId());
        ContactInfo model = contactInfoDao.findById(contactInfo.getId());
        Assert.assertNull(model);
    }

    @Test
    public void testUpdate() {
        ContactInfo updatedModel = new ContactInfo();
        updatedModel.setId(contactInfo.getId());
        updatedModel.setName("guochaopeng");
        contactInfoDao.update(updatedModel);

        ContactInfo model = contactInfoDao.findById(contactInfo.getId());
        Assert.assertTrue(model.getName().equals(updatedModel.getName()));
    }
}
