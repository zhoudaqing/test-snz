package io.terminus.snz.related.daos;

import com.google.common.collect.Lists;
import io.terminus.snz.related.BaseDaoTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
public class AddressFactoryDaoTest extends BaseDaoTest {
    @Autowired
    private AddressFactoryDao addressFactoryDao;

    @Test
    public void testFindByParkIds(){
        addressFactoryDao.findByFactoryIds(Lists.newArrayList(1l , 2l));
    }
}
