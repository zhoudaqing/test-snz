package io.terminus.snz.user.service;

import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-3.
 */
public class TagServiceTest extends BaseServiceTest {

    private User user;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    @Before
    public void mockUser() {
        user = new User();
        user.setId(1L);
        user.setNick("rose");
        user.setTags("合作");
    }

    @Test
    public void testAddTag() {
        Assert.assertTrue(!tagServiceImpl.addTag(null, User.SupplierTag.IN_SUPPLIER).isSuccess());
        Assert.assertTrue(!tagServiceImpl.addTag(user.getId(), null).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(null);
        Assert.assertTrue(!tagServiceImpl.addTag(user.getId(), User.SupplierTag.IN_SUPPLIER).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(user);
        Assert.assertTrue(tagServiceImpl.addTag(user.getId(), User.SupplierTag.IN_SUPPLIER).getResult());
    }

    @Test
    public void testAddTQRDCTag() {
        Assert.assertTrue(!tagServiceImpl.addTQRDCTag(null, 60).isSuccess());
        Assert.assertTrue(!tagServiceImpl.addTQRDCTag(user.getId(), null).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(null);
        Assert.assertTrue(!tagServiceImpl.addTQRDCTag(user.getId(), 60).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(user);
        Assert.assertTrue(tagServiceImpl.addTQRDCTag(user.getId(), 60).getResult());
    }

    @Test
    public void testUpdateTagsByCompany() {
        Assert.assertTrue(!tagServiceImpl.updateTagsByCompany(null, createCompany()).isSuccess());
        Assert.assertTrue(!tagServiceImpl.updateTagsByCompany(user.getId(), null).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(null);
        Assert.assertTrue(!tagServiceImpl.updateTagsByCompany(user.getId(), createCompany()).isSuccess());
        when(userDao.findById(user.getId())).thenReturn(user);
        Assert.assertTrue(tagServiceImpl.updateTagsByCompany(user.getId(), createCompany()).getResult());
    }

    private Company createCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setUserId(user.getId());
        company.setWorldTop(1);
        company.setSupplierCode("v234");

        return company;
    }


}
