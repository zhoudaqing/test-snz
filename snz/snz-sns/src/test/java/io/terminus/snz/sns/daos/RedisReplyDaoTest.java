package io.terminus.snz.sns.daos;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.sns.daos.redis.RedisReplyDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * redis话题回复Dao测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-3
 */
public class RedisReplyDaoTest {

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private RedisReplyDao redisReplyDao;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIncrRequirementSuppliers(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(1L);
        assertEquals(1, redisReplyDao.incrRequirementSuppliers(1L, 1L).intValue());
    }

    @Test
    public void testCountRequirementSuppliers(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(Arrays.asList(1L, 2L));
        assertEquals(2, redisReplyDao.countRequirementSuppliers(Arrays.asList(1L, 2L)).size());
    }
}
