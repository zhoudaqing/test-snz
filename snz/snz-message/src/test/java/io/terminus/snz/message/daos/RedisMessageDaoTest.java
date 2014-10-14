package io.terminus.snz.message.daos;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.message.BaseRedisDaoTest;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Redis消息Dao测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-2
 */
public class RedisMessageDaoTest extends BaseRedisDaoTest{

    @Mock
    private JedisTemplate jedisTemplate;

    @InjectMocks
    private RedisMessageDao redisMessageDao;

    @Test
    public void testAddNewMsg(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(1L);
        assertEquals(1, redisMessageDao.addNewMsg(1L, 1L).intValue());
        assertEquals(1, redisMessageDao.addNewMsg(1L, Arrays.asList(1L, 2L)).intValue());
    }

    @Test
    public void testRmNewMsg(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(1L);
        assertEquals(1, redisMessageDao.rmNewMsg(1L, 1L).intValue());
        assertEquals(1, redisMessageDao.rmNewMsgs(1L, Arrays.asList(1L, 2L)).intValue());
    }

    @Test
    public void testGetNewMsgIds(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(Arrays.asList(1L, 2L));
        assertEquals(2, redisMessageDao.getAllMsgIds(1L, 0, 5).size());
    }

    @Test
    public void testGetAllMsgIds(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(Arrays.asList(1L, 2L));
        assertEquals(2, redisMessageDao.getNewMsgIds(1L).size());
        assertEquals(2, redisMessageDao.getNewMsgIds(1L, 0, 5).size());
    }

    @Test
    public void testGetAllMsgsCount(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(1L);
        assertEquals(1, redisMessageDao.getAllMsgsCount(1L).intValue());
    }

    @Test
    public void testRmNewAndAllMsg(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(2L);
        assertEquals(2L, redisMessageDao.rmNewAndAllMsg(Arrays.asList(1L, 2L), 1L).intValue());
    }

    @Test
    public void testRmMsgs(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(2L);
        assertEquals(2L, redisMessageDao.rmMsgs(1L, Arrays.asList(1L, 2L)).intValue());
    }

    @Test
    public void testRmNewMsgs(){
        when(jedisTemplate.execute(any(JedisTemplate.JedisAction.class))).thenReturn(2L);
        assertEquals(2L, redisMessageDao.rmNewMsgs(1L, Arrays.asList(1L, 2L)).intValue());
    }
}
