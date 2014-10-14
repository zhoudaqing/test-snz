package io.terminus.snz.message.services;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.message.BaseServiceTest;
import io.terminus.snz.message.components.MessageCenter;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.dtos.MessagePagingView;
import io.terminus.snz.message.managers.MessageManager;
import io.terminus.snz.message.models.Message;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * 消息服务测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-1
 */
public class MessageServiceTest extends BaseServiceTest{

    @Mock
    private RedisMessageDao redisMessageDao;

    @Mock
    private MessageDao messageDao;

    @Mock
    private MessageManager messageManager;

    @Mock
    private MessageCenter messageCenter;

    @Mock
    private LoadingCache<Long, Message> messageCache;

    @InjectMocks
    private MessageServiceImpl messageServiceIml;

    @Test
    public void testNewMsgCount(){
        when(redisMessageDao.getNewMsgsCount(anyLong())).thenReturn(10L);
        assertEquals(10, messageServiceIml.newMsgCount(loginer).getResult().intValue());

        // user not login
        assertFalse(messageServiceIml.newMsgCount(null).isSuccess());
    }

    @Test
    public void testFindById(){
        when(messageCache.getUnchecked(anyLong())).thenReturn(createMessage(2L));
        assertEquals(2L, messageServiceIml.findById(loginer, 2L).getResult().getId().longValue());

        // user not login
        assertFalse(messageServiceIml.findById(null, 2L).isSuccess());
    }

    @Test
    public void testLastest(){
        List<Long> newMsgIds = Arrays.asList(1L, 2L, 3L, 4L);
        when(redisMessageDao.getNewMsgIds(anyLong(), anyInt(), anyInt())).thenReturn(newMsgIds);
        when(messageDao.findByIds(anyList())).thenReturn(createMessages(newMsgIds));
        assertEquals(newMsgIds.size(), messageServiceIml.lastest(loginer, 5).getResult().size());

        // user not login
        assertFalse(messageServiceIml.lastest(null, 5).isSuccess());
    }

    @Test
    public void testRmNewMsgId(){
        when(redisMessageDao.rmNewMsg(anyLong(), anyLong())).thenReturn(10L);
        assertEquals(10L, messageServiceIml.rmNewMsgId(loginer, 2L).getResult().longValue());

        // user not login
        assertFalse(messageServiceIml.rmNewMsgId(null, 2L).isSuccess());
        // id invalid
        assertFalse(messageServiceIml.rmNewMsgId(loginer, -2L).isSuccess());
    }

    @Test
    public void testRmMsgs(){
        when(redisMessageDao.rmNewMsgs(anyLong(), anyList())).thenReturn(10L);
        assertTrue(messageServiceIml.rmMsgs(loginer, "1,2,3").getResult());

        // user not login
        assertFalse(messageServiceIml.rmMsgs(null, "1,2,3").isSuccess());
        // id invalid
        assertFalse(messageServiceIml.rmMsgs(loginer, "").isSuccess());
    }

    @Test
    public void testRead(){
        when(redisMessageDao.rmNewMsgs(anyLong(), anyList())).thenReturn(10L);
        assertTrue(messageServiceIml.read(loginer, "1,2,3").getResult());

        // user not login
        assertFalse(messageServiceIml.read(null, "1,2,3").isSuccess());
        // id invalid
        assertFalse(messageServiceIml.read(loginer, "").isSuccess());
    }

    @Test
    public void testDelete(){
        when(messageDao.findById(anyLong())).thenReturn(createMessage(1L));
        when(messageDao.delete(anyLong())).thenReturn(1);
        assertEquals(1L, messageServiceIml.delete(loginer, 1L).getResult().longValue());

        // user not login
        assertFalse(messageServiceIml.delete(null, 1L).isSuccess());
        // id invalid
        assertFalse(messageServiceIml.delete(loginer, -1L).isSuccess());
        // message not exist
        when(messageDao.findById(anyLong())).thenReturn(null);
        assertFalse(messageServiceIml.delete(loginer, 1L).isSuccess());
        // user not owner
        when(messageDao.findById(anyLong())).thenReturn(createMessage(1L));
        loginer.setId(100L);
        assertFalse(messageServiceIml.delete(loginer, 1L).isSuccess());
    }

    @Test
    public void testPaging(){
        // new msg paging
        List<Long> newIds = Arrays.asList(1L, 2L, 3L, 4L);
        Paging<Message> mockNewPage = new Paging<Message>(20L, createMessages(newIds));
        when(messageManager.pagingNewMsgs(anyLong(), anyInt(), anyInt())).thenReturn(mockNewPage);
        when(redisMessageDao.getAllMsgsCount(anyLong())).thenReturn(100L);
        MessagePagingView mpv = messageServiceIml.paging(loginer, 10, 10, 1).getResult();
        assertEquals(100, mpv.getTotalNum().intValue());
        assertEquals(mockNewPage.getTotal(), mpv.getNewNum());
        assertEquals(newIds.size(), mockNewPage.getData().size());

        // all msg paging
        List<Long> allIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        Paging<Message> mockAllPage = new Paging<Message>(120L, createMessages(allIds));
        when(messageManager.pagingAllMsgs(anyLong(), anyInt(), anyInt())).thenReturn(mockAllPage);
        when(redisMessageDao.getNewMsgIds(anyLong())).thenReturn(newIds);
        mpv = messageServiceIml.paging(loginer, 10, 10, 0).getResult();
        assertEquals(mockAllPage.getTotal().intValue(), mpv.getTotalNum().intValue());
        assertEquals(newIds.size(), mpv.getNewNum().intValue());
        assertEquals(allIds.size(), mockAllPage.getData().size());

        // user not login
        assertFalse(messageServiceIml.paging(null, 10, 10, 1).isSuccess());
    }

    @Test
    public void testPush(){
        assertTrue(messageServiceIml.push(Message.Type.TOPIC_CREATE, 1L, 1L, null).getResult());
        assertTrue(messageServiceIml.push(Message.Type.TOPIC_CREATE, 1L, Arrays.asList(1L, 2L), null).getResult());
    }

    private List<Message> createMessages(List<Long> ids){
        List<Message> ms = Lists.newArrayList();
        for (Long id : ids){
            ms.add(createMessage(id));
        }
        return ms;
    }

    private Message createMessage(Long id){
        Message m = new Message();
        m.setId(id);
        m.setContent("message"+id);
        m.setUserId(id);
        m.setMtype(Message.Type.REPLY_CREATE.value());
        return m;
    }
}
