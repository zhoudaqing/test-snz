package io.terminus.snz.message.managers;

/**
 * 消息管理
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-7
 */

import io.terminus.common.model.Paging;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.daos.redis.RedisMessageDao;
import io.terminus.snz.message.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 话题消息管理器
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 5/5/14.
 */
@Component
public class MessageManager {
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private RedisMessageDao redisMessageDao;

    /**
     * 获取用户所有的消息分页对象
     * @param uid 用户id
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 用户所有的消息分页对象
     */
    public Paging<Message> pagingAllMsgs(Long uid, Integer offset, Integer limit){
        Long total = redisMessageDao.getAllMsgsCount(uid);
        if (total <= 0){
            return new Paging<Message>(0L, Collections.<Message>emptyList());
        }
        List<Long> mids = redisMessageDao.getAllMsgIds(uid, offset, limit);
        List<Message> tms = messageDao.findByIds(mids);
        return new Paging<Message>(total, tms);
    }

    /**
     * 获取用户所有的消息分页对象
     * @param uid 用户id
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 用户所有的消息分页对象
     */
    public Paging<Message> pagingNewMsgs(Long uid, Integer offset, Integer limit){
        Long total = redisMessageDao.getNewMsgsCount(uid);
        if (total <= 0){
            return new Paging<Message>(0L, Collections.<Message>emptyList());
        }
        List<Long> mids = redisMessageDao.getNewMsgIds(uid, offset, limit);
        List<Message> tms = messageDao.findByIds(mids);
        return new Paging<Message>(total, tms);
    }
}
