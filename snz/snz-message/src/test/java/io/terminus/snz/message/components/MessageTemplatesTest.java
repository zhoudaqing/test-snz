package io.terminus.snz.message.components;

import io.terminus.snz.message.models.Message;
import org.junit.Test;

/**
 * 消息模版测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-2
 */
public class MessageTemplatesTest {

    @Test
    public void testBuildMessageContent(){
        try {
            MessageTemplates.buildMessageContent(Message.Type.TOPIC_CREATE, new Object());
        } catch (Exception e) {
        }
    }
}
