package io.terminus.snz.message.components;

import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.dtos.MessageDto;
import io.terminus.snz.message.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-9
 */
@Component @Slf4j
public class MessageListener {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private RedisMessageDao redisMessageDao;

    @Subscribe
    public void listen(MessageDto md){
        try{
            Message m = md.getMessage();
            // 1.构建消息内容
            String content = MessageTemplates.buildMessageContent(
                 Message.Type.from(m.getMtype()), md.getDatas()
            );
            m.setContent(content);
            // 2. 保存消息对象
            messageDao.create(m);
            // 3. 推送消息给用户
            redisMessageDao.addNewMsg(m.getId(), md.getReceiverIds());
        } catch (Exception e){
            log.error("failed to handle message, MessageDto={}, cause:{}",
                    md, Throwables.getStackTraceAsString(e));
        }
    }
}
