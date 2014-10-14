package io.terminus.snz.sns.daos;

import io.terminus.snz.sns.BaseDaoTest;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.models.TopicUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * 话题参与者关联Dao测试
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
public class TopicUserDaoTest extends BaseDaoTest {

    @Autowired
    private TopicUserDao topicUserDao;

    @Test
    public void testCreate(){
        assertEquals(1, topicUserDao.create(mock()));
    }

    @Test
    public void testCreateBatch(){
        List<TopicUser> tus = new ArrayList<TopicUser>();
        for (int i=0; i<10; i++){
            TopicUser tu = new TopicUser();
            tu.setTopicId(i+1L);
            tu.setUserId(i+1L);
            tus.add(tu);
        }
        topicUserDao.createBatch(tus);
    }

    @Test
    public void testDelete(){
        TopicUser mock = mock();
        topicUserDao.create(mock);
        assertEquals(1, topicUserDao.delete(mock));
    }

    @Test
    public void testDeleteByTopicId(){
        TopicUser mock = mock();
        topicUserDao.create(mock);
        assertEquals(1, topicUserDao.deleteByTopicId(mock.getTopicId()));
    }

    @Test
    public void testDeleteByTopicIds(){
        TopicUser mock = mock();
        topicUserDao.create(mock);
        assertEquals(1, topicUserDao.deleteByTopicIds(Arrays.asList(mock.getTopicId(), 222L)));
    }

    @Test
    public void testExist(){
        testCreateBatch();
        assertEquals(Boolean.TRUE, topicUserDao.exist(1L, 1L));
        assertEquals(Boolean.FALSE, topicUserDao.exist(1L, 2L));
        assertEquals(Boolean.FALSE, topicUserDao.exist(1000L, 1000L));
    }

    @Test
    public void testFindUserIdsByTopicId(){
        TopicUser mock = mock();
        topicUserDao.create(mock);
        assertEquals(1, topicUserDao.findUserIdsByTopicId(mock.getTopicId()).size());
    }

    @Test
    public void testCountTopicUsers(){
        TopicUser mock = mock();
        topicUserDao.create(mock);
        assertEquals(1, topicUserDao.countTopicUsers(Arrays.asList(mock.getTopicId())).size());
    }

    private TopicUser mock(){
        TopicUser tu = new TopicUser();
        tu.setUserId(1L);
        tu.setTopicId(1L);
        return tu;
    }
}
