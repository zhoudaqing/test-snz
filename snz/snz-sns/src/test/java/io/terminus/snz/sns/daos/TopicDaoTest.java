package io.terminus.snz.sns.daos;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.RequirementStatus;
import io.terminus.snz.sns.BaseDaoTest;
import io.terminus.snz.sns.daos.mysql.TopicDao;
import io.terminus.snz.sns.daos.mysql.TopicUserDao;
import io.terminus.snz.sns.models.Topic;
import io.terminus.snz.sns.models.TopicUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 话题Dao测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
public class TopicDaoTest extends BaseDaoTest {

    @Autowired
    private TopicDao topicDao;

    @Autowired
    private TopicUserDao topicUserDao;

    @Test
    public void testCreate(){
        assertEquals(1L, topicDao.create(mock()));
    }

    @Test
    public void testSetDelete(){
        Topic mock = mock();
        topicDao.create(mock);
        assertEquals(1, topicDao.setDelete(mock.getId()));
    }

    @Test
    public void testSetDeletes(){
        List<Topic> mocks = mocks(5);
        for (Topic t : mocks){
            topicDao.create(t);
        }
        List<Long> ids = Lists.transform(mocks, new Function<Topic, Long>() {
            @Override
            public Long apply(Topic t) {
                return t.getId();
            }
        });
        assertEquals(ids.size(), topicDao.setDeletes(ids));
    }

    @Test
    public void testSetClose(){
        Topic mock = mock();
        topicDao.create(mock);
        assertEquals(1, topicDao.setClose(mock.getId()));
    }

    @Test
    public void testSetCloses(){
        List<Topic> mocks = mocks(5);
        for (Topic t : mocks){
            topicDao.create(t);
        }
        List<Long> ids = Lists.transform(mocks, new Function<Topic, Long>() {
            @Override
            public Long apply(Topic t) {
                return t.getId();
            }
        });
        assertEquals(ids.size(), topicDao.setCloses(ids));
    }

    @Test
    public void testDelete(){
        Topic mock = mock();
        topicDao.create(mock);
        assertEquals(1, topicDao.delete(mock.getId()));
    }

    @Test
    public void testDeletes() {
        List<Topic> mocks = mocks(5);
        for (Topic t : mocks){
            topicDao.create(t);
        }
        List<Long> ids = Lists.transform(mocks, new Function<Topic, Long>() {
            @Override
            public Long apply(Topic t) {
                return t.getId();
            }
        });
        assertEquals(ids.size(), topicDao.deletes(ids));
    }

    @Test
    public void testUpdate(){
        Topic mock = mock();
        topicDao.create(mock);
        mock.setTotalViews(100L);
        mock.setTotalReplies(101L);
        topicDao.update(mock);
        mock = topicDao.findById(mock.getId());
        assertEquals(100, mock.getTotalViews().intValue());
        assertEquals(101, mock.getTotalReplies().intValue());
    }

    @Test
    public void testFindById(){
        Topic mock = mock();
        topicDao.create(mock);
        assertNotNull(topicDao.findById(mock.getId()));
    }

    @Test
    public void testFindByIds(){
        List<Topic> mocks = mocks(5);
        for (Topic t : mocks){
            topicDao.create(t);
        }
        List<Long> ids = Lists.transform(mocks, new Function<Topic, Long>() {
            @Override
            public Long apply(Topic t) {
                return t.getId();
            }
        });
        assertEquals(ids.size(), topicDao.findByIds(ids).size());
    }

    @Test
    public void testPaging(){
        List<Topic> mocks = mocks(21);
        for (Topic m : mocks){
            topicDao.create(m);
        }
        Paging<Topic> topicPage = topicDao.paging(null, 0, 11);
        assertEquals(21, topicPage.getTotal().intValue());
        assertEquals(11, topicPage.getData().size());
    }

    @Test
    public void testPagingOfReq(){
        List<Topic> mocks = mocks(21);
        List<TopicUser> topicUsers = new ArrayList<TopicUser>();
        for (Topic m : mocks){
            topicDao.create(m);
            TopicUser tu = new TopicUser();
            tu.setTopicId(m.getId());
            tu.setUserId(1L);
            topicUsers.add(tu);
        }
        topicUserDao.createBatch(topicUsers);
        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("reqId", 1L);
        criteria.put("userId", 1L);
        criteria.put("reqStatus", Lists.newArrayList(1,2,3,4));
        Paging<Topic> topicPage = topicDao.pagingOfReq(criteria, 0, 11);
        System.out.println(topicPage.getTotal());
    }

    @Test
    public void testPagingByJoinerId(){
        List<Topic> mocks = mocks(21);
        for (Topic m : mocks){
            topicDao.create(m);
        }
        Paging<Topic> topicPage = topicDao.pagingByJoinerId(1L, 0 ,11);
        assertEquals(0, topicPage.getTotal().intValue());
        assertEquals(0, topicPage.getData().size());
    }

    @Test
    public void testViewed(){
        Topic mock = mock();
        topicDao.create(mock);
        topicDao.viewed(mock.getId());
    }

    @Test
    public void testReplied(){
        Topic mock = mock();
        topicDao.create(mock);
        topicDao.replied(mock.getId());
    }

    @Test
    public void testCountRequirementTopics(){
        List<Topic> mocks = mocks(5);
        for (Topic m : mocks){
            topicDao.create(m);
        }
        List<Long> ids = Lists.transform(mocks, new Function<Topic, Long>() {
            @Override
            public Long apply(Topic t) {
                return t.getReqId();
            }
        });
        assertEquals(5, topicDao.countRequirementTopics(ids).size());
    }

    public Topic mock(){
        Topic t = new Topic();
        t.setTitle("oo");
        t.setContent("xx");
        t.setUserId(1L);
        t.setUserName("xxoo");
        t.setCompanyName("xx公司");
        t.setJoiners(2);
        t.setReqId(1L);
        t.setReqName("xx需求");
        t.setReqStatus(RequirementStatus.RES_INTERACTIVE.value());
        t.setScope(Topic.Scope.PUBLIC.value());
        return t;
    }

    public List<Topic> mocks(int count){
        List<Topic> ts = Lists.newArrayList();
        for (int i=0; i<count; i++){
            Topic t = new Topic();
            t.setTitle("oo"+i);
            t.setContent("xx");
            t.setUserId(1L);
            t.setUserName("xxoo");
            t.setCompanyName("xx公司");
            t.setJoiners(2);
            t.setReqId(i+1L);
            t.setReqName("xx需求");
            t.setReqStatus(RequirementStatus.RES_INTERACTIVE.value());
            t.setScope(Topic.Scope.PUBLIC.value());
            ts.add(t);
        }
        return ts;
    }
}
