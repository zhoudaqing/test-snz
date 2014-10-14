package io.terminus.snz.message.daos;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.message.BaseDaoTest;
import io.terminus.snz.message.daos.mysql.MessageDao;
import io.terminus.snz.message.models.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 消息Dao测试
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-1
 */
public class MessageDaoTest extends BaseDaoTest {

    @Autowired
    private MessageDao messageDao;

    @Test
    public void testCreate(){
        Message m = new Message();
        m.setUserId(1L);
        m.setContent("This is a test message content.");
        m.setMtype(Message.Type.REPLY_CREATE.value());
        Integer created = messageDao.create(m);
        assertEquals(created.intValue(), 1);
    }

    @Test
    public void testDelete(){
        Message m = createMessage();
        Integer deleted = messageDao.delete(m.getId());
        assertEquals(deleted.intValue(), 1);
    }



    @Test
    public void testDeletes(){
        List<Message> msgs = createMessages();
        List<Long> ids = ids(msgs);
        // add some not existed ids
        List<Long> nonExisted = Arrays.asList(100000L, 5201314L);
        ids.addAll(nonExisted);

        Integer deleted = messageDao.deletes(ids);
        assertEquals(deleted.intValue(), ids.size() - nonExisted.size());
    }

    @Test
    public void testFindById(){
        Message m = createMessage();
        assertNotNull(messageDao.findById(m.getId()));
        assertNull(messageDao.findById(654321L));
    }

    @Test
    public void testFindByIds(){
        List<Message> msgs = createMessages();
        List<Long> ids = ids(msgs);

        assertEquals(msgs.size(), messageDao.findByIds(ids).size());

        // add some not existed ids
        List<Long> nonExisted = Arrays.asList(100000L, 5201314L);
        ids.addAll(nonExisted);

        assertEquals(ids.size() - nonExisted.size(), messageDao.findByIds(ids).size());
    }

    @Test
    public void testPaging(){
        List<Message> msgs = createMessages();
        Map<String, Object> criteria = Maps.newHashMap();

        Integer offset = 0;
        Integer limit = 5;
        Paging<Message> pm = messageDao.paging(criteria, offset, limit);
        assertEquals(msgs.size(), pm.getTotal().intValue());
        assertEquals(limit.intValue(), pm.getData().size());

        offset = 2;
        limit = 10;
        pm = messageDao.paging(criteria, offset, limit);
        assertEquals(msgs.size(), pm.getTotal().intValue());
        assertEquals(limit.intValue(), pm.getData().size());

        Integer exceed = 5;
        offset = msgs.size() - exceed;
        limit = 10;
        pm = messageDao.paging(criteria, offset, limit);
        assertEquals(msgs.size(), pm.getTotal().intValue());
        assertEquals(exceed.intValue(), pm.getData().size());

        criteria.put("userId", 1000L);
        offset = 0;
        limit = 10;
        pm = messageDao.paging(criteria, offset, limit);
        assertEquals(0, pm.getTotal().intValue());
        assertEquals(0, pm.getData().size());

        criteria.put("userId", 1L);
        offset = 0;
        limit = 10;
        pm = messageDao.paging(criteria, offset, limit);
        assertEquals(msgs.size(), pm.getTotal().intValue());
        assertEquals(limit.intValue(), pm.getData().size());
    }

    private Message createMessage() {
        Message m = new Message();
        m.setUserId(1L);
        m.setContent("This is a test message content.");
        m.setMtype(Message.Type.REPLY_CREATE.value());
        messageDao.create(m);
        return m;
    }

    private List<Long> ids(List<Message> msgs) {
        List<Long> ids = Lists.newArrayList();
        for (Message m : msgs){
            ids.add(m.getId());
        }
        return ids;
    }

    private List<Message> createMessages() {
        List<Message> msgs = Lists.newArrayList();
        for (int i=0; i < 21; i++){
            Message m = createMessage();
            msgs.add(m);
        }
        return msgs;
    }

}
