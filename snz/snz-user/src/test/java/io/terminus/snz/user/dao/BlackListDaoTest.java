package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.BlackList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-23
 */
public class BlackListDaoTest extends TestBaseDao {

    private BlackList blackList;

    @Autowired
    private BlackListDao blackListDao;

    private void mock() {
        blackList = new BlackList();
        blackList.setName("jack");
        blackList.setKeywords("ja");
        blackList.setInitAgent("xx");
    }

    @Before
    public void setUp() {
        mock();
        blackListDao.create(blackList);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(blackList.getId());
    }

    @Test
    public void testFindById() {
        BlackList model = blackListDao.findById(blackList.getId());
        Assert.assertTrue(model.getId() == blackList.getId());
    }

    @Test
    public void testFindAll() {
        List<BlackList> blackList = blackListDao.findAll();
        Assert.assertTrue(blackList.size() == 1);
    }


}
