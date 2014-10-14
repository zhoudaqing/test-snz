package io.terminus.snz.requirement.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Desc:认证数据测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-14.
 */
public class IdentifyNameDaoTest extends BasicTest {
    @Autowired
    private IdentifyNameDao identifyNameDao;

    @Test
    public void testFindAllName(){
        identifyNameDao.findAllName();
    }
}
