/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.dao;

import io.terminus.pampas.console.model.UserPermission;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserPermissionDaoTest extends BaseDaoTest {

    @Autowired
    private UserPermissionDao userPermissionDao;

    private UserPermission up;

    @Before
    public void setUp() throws Exception {
        up = new UserPermission();
        up.setUserId(1L);
        up.setPermissions("[\"app:pampas\", \"site:2\", \"group:wtf\"]");
        userPermissionDao.create(up);
    }

    @Test
    public void testFindByUserId() throws Exception {
        List<String> actual = userPermissionDao.findByUserId(up.getUserId());
        assertThat(actual, contains("app:pampas","site:2", "group:wtf"));
    }

    @Test
    public void testDeleteByUserId() throws Exception {
        assertThat(userPermissionDao.deleteByUserId(up.getUserId()),is(true));
        assertThat(userPermissionDao.findByUserId(up.getUserId()).size(),is(0));
    }
}