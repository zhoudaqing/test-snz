package io.terminus.snz.statistic.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.model.RequirementCountType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class RequirementCountDaoTest {
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private RequirementCountDao requirementCountDao;

    @Test
    public void testSetReqCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        requirementCountDao.setReqCount(1l, RequirementCountType.ANSWER_SU, 1);
    }

    @Test
    public void testFindReqCount() throws Exception {
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(ImmutableMap.of(RequirementCountType.ANSWER_SU , 10, RequirementCountType.SEND_SO, 10));
        assertNotNull(requirementCountDao.findReqCount(1l , RequirementCountType.ANSWER_SU.toString() , RequirementCountType.SEND_SO.toString()));
    }

    @Test
    public void testDeleteReqCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        requirementCountDao.deleteReqCount(1l);
    }

    @Test
    public void testIncTopicSuppliers() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        requirementCountDao.incTopicSuppliers(1l , 1l);
    }

    @Test
    public void testDeleteTopicCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        requirementCountDao.deleteTopicCount(1l);
    }
}