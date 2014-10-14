package io.terminus.snz.message.managers;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.snz.message.BaseManagerTest;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.models.Message;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * 消息管理器测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-2
 */
public class MessageManagerTest extends BaseManagerTest{

    @Mock
    private MessageDao messageDao;

    @Mock
    private RedisMessageDao redisMessageDao;

    @InjectMocks
    private MessageManager messageManager;

    @Test
    public void testPagingAllMsgs(){
        when(redisMessageDao.getAllMsgsCount(anyLong())).thenReturn(100L);
        when(redisMessageDao.getAllMsgIds(anyLong(), anyInt(), anyInt())).thenReturn(Arrays.asList(1L, 2L, 3L));
        when(messageDao.findByIds(anyList())).thenReturn(createMessages(Arrays.asList(1L, 2L, 3L)));
        Paging<Message> pm = messageManager.pagingAllMsgs(1L, 0, 10);
        assertEquals(100, pm.getTotal().intValue());
        assertEquals(3, pm.getData().size());
    }

    @Test
    public void testPagingNewMsgs(){
        when(redisMessageDao.getNewMsgsCount(anyLong())).thenReturn(100L);
        when(redisMessageDao.getNewMsgIds(anyLong(), anyInt(), anyInt())).thenReturn(Arrays.asList(1L, 2L, 3L));
        when(messageDao.findByIds(anyList())).thenReturn(createMessages(Arrays.asList(1L, 2L, 3L)));
        Paging<Message> pm = messageManager.pagingNewMsgs(1L, 0, 10);
        assertEquals(100, pm.getTotal().intValue());
        assertEquals(3, pm.getData().size());
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


