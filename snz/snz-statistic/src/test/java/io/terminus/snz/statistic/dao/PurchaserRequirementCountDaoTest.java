package io.terminus.snz.statistic.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class PurchaserRequirementCountDaoTest {
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private PurchaserRequirementCountDao purchaserRequirementCountDao;

    @Test
    public void testSetPurCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        purchaserRequirementCountDao.setPurCount(mock());
    }

    @Test
    public void testUpdateReqCount() throws Exception {
        jedisTemplate.execute(any(JedisTemplate.JedisActionNoResult.class));
        purchaserRequirementCountDao.updateReqCount(1l , 1, 2);
    }

    @Test
    public void testFindReqCount() throws Exception {
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(mock());
        assertNotNull(purchaserRequirementCountDao.findReqCount(1l));
    }

    public PurchaserRequirementCount mock(){
        PurchaserRequirementCount purchaserRequirementCount = new PurchaserRequirementCount();
        purchaserRequirementCount.setUserId(1l);
        purchaserRequirementCount.setUserName("name");
        purchaserRequirementCount.setStatusCounts(ImmutableMap.of(1 , 1, 2, 1));

        return purchaserRequirementCount;
    }
}