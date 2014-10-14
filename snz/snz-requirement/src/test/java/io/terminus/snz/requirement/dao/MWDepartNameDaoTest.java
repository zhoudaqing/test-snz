package io.terminus.snz.requirement.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

/**
 * Date: 8/20/14
 * Time: 11:59
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */


public class MWDepartNameDaoTest extends BasicTest {

    @Autowired
    MWDepartNameDao mwDepartNameDao;

    @Test
    public void shouldGetDepartName() {
        assertNotNull(mwDepartNameDao.getDepartName("anykey"));
    }
}
