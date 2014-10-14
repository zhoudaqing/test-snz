package io.terminus.snz.message.components;

import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.dtos.MessageDto;
import io.terminus.snz.message.models.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-2
 */
public class MessageListenerTest {

    @Mock
    private MessageDao messageDao;

    @Mock
    private RedisMessageDao redisMessageDao;

    @InjectMocks
    private MessageListener messageListener;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListen(){
        when(messageDao.create(any(Message.class))).thenReturn(1);
        when(redisMessageDao.addNewMsg(anyLong(), anyList())).thenReturn(1L);
        messageListener.listen(new MessageDto(createMessage(1L), Arrays.asList(1L), null));
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
