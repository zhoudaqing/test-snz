package io.terminus.snz.message.components;

import com.google.common.eventbus.EventBus;
import io.terminus.snz.message.dtos.MessageDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * 消息中心测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-2
 */
public class MessageCenterTest {
    @Mock
    private EventBus eventBus;

    @InjectMocks
    private MessageCenter messageCenter = new MessageCenter(1);

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPublish(){
        messageCenter.publish(new MessageDto());
    }

    @Test
    public void testSubcribe(){
        messageCenter.subcribe(new Object());
    }

    @Test
    public void testUnsubcribe(){
        messageCenter.unsubcribe(new Object());
    }
}
