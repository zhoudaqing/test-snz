package io.terminus.snz.related.daos;

import com.google.common.collect.Lists;
import io.terminus.snz.related.BaseDaoTest;
import io.terminus.snz.related.models.AddressPark;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Desc:园区数据测试
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
public class AddressParkDaoTest extends BaseDaoTest {
    @Autowired
    private AddressParkDao addressParkDao;

    @Test
    public void testFindPark() {
        addressParkDao.findAllPark();
    }

    @Test
    public void testFindByProductId() {
        addressParkDao.findParkByProductId(1l);
    }

    @Test
    public void testFindByIds() {
        List<AddressPark> addressParks = addressParkDao.findByIds(Lists.newArrayList(1L, 2L, 3l));
        List<AddressPark> addressParks1 = addressParkDao.findByIds(null);
        Assert.assertEquals(addressParks.size(), 0);
        Assert.assertEquals(addressParks1.size(), 0);
    }
}
