/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.dao;

import io.terminus.common.model.Paging;
import io.terminus.pampas.console.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest  extends BaseDaoTest{

    @Autowired
    private UserDao userDao;

    private User u;

    @Before
    public void setUp() throws Exception {
        u = new User();
        u.setName("jlchen");
        u.setPassword("123456");
        u.setStatus(1);
        u.setRealName("JinLiang Chen");
        userDao.create(u);
    }

    @Test
    public void testFindByName() throws Exception {
        User actual = userDao.findByName(u.getName());
        assertThat(actual, is(u));
    }

    @Test
    public void update() throws Exception {
        User updated =new User();
        updated.setId(u.getId());
        updated.setPassword("hello");
        updated.setStatus(-1);
        updated.setRealName("dadu");
        userDao.update(updated);

        User actual = userDao.findByName(u.getName());
        assertThat(actual.getPassword(), is(updated.getPassword()));
        assertThat(actual.getRealName(), is(updated.getRealName()));
        assertThat(actual.getStatus(), is(updated.getStatus()));
    }

    @Test
    public void testPaging() throws Exception{
        Paging<User> result = userDao.paging(0,10);
        assertThat(result.getTotal(), is(1L));
    }
}