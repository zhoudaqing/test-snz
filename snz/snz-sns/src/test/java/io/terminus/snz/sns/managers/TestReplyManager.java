package io.terminus.snz.sns.managers;

import io.terminus.snz.sns.BaseManagerTest;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.daos.redis.RedisReplyDao;
import io.terminus.snz.sns.models.Reply;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class TestReplyManager extends BaseManagerTest {

    @Mock
    private ReplyDao replyDao;

    @Mock
    private RedisReplyDao redisReplyDao;

    @InjectMocks
    private ReplyManager replyManager = new ReplyManager();

    @Test
    public void testCreate(){
        when(replyDao.create(any(Reply.class))).thenReturn(1);
        assertEquals(1, replyManager.create(mockReply(1L)));
    }

    private Reply mockReply(Long id){
        Reply r = new Reply();
        r.setId(id);
        r.setTid(0L);
        r.setTopicId(1L);
        r.setPid(0L);
        r.setUserId(1L);
        return r;
    }
}
