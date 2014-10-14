package io.terminus.snz.sns.managers;

import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.redis.RedisReplyDao;
import io.terminus.snz.sns.models.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 6/5/14.
 */
@Component
public class ReplyManager {
    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private RedisReplyDao redisReplyDao;

    /**
     * 创建回复并增加需求的参与供应商
     * @param r 回复对象
     * @return 插入记录数
     */
    @Transactional
    public int create(Reply r){
        int created = replyDao.create(r);
        redisReplyDao.incrRequirementSuppliers(r.getReqId(), r.getUserId());
        return created;
    }
}
