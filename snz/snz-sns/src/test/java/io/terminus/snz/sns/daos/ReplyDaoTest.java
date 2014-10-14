package io.terminus.snz.sns.daos;

import io.terminus.common.model.Paging;
import io.terminus.snz.sns.BaseDaoTest;
import io.terminus.snz.sns.daos.mysql.ReplyDao;
import io.terminus.snz.sns.models.Reply;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 话题回复Dao测试
 * MADE IN IntelliJ IDEA
 * AUTHOR: haolin
 * AT 4/5/14.
 */
public class ReplyDaoTest extends BaseDaoTest {

    @Autowired
    private ReplyDao replyDao;

    @Test
    public void testCreate(){
        assertEquals(1, replyDao.create(mock()));
    }

    @Test
    public void testFindById(){
        Reply r = mock();
        replyDao.create(r);
        assertNotNull(replyDao.findById(r.getId()));
    }

    @Test
    public void testDeleteByTopicId(){
        Reply r = mock();
        replyDao.create(r);
        assertEquals(1, replyDao.deleteByTopicId(r.getTopicId()));
    }

    @Test
    public void testDeleteByTopicIds(){
        Reply r = mock();
        replyDao.create(r);
        assertEquals(1, replyDao.deleteByTopicIds(Arrays.asList(r.getTopicId(), 22L)));
    }

    @Test
    public void testPagingByTopicId(){
        Reply r = mock();
        replyDao.create(r);
        Paging<Reply> replyPage = replyDao.pagingTopByTopicId(r.getTopicId(), 0, 10);
        assertEquals(1, replyPage.getTotal().intValue());
        assertEquals(1, replyPage.getData().size());
    }

    @Test
    public void testListAllByTopId(){
        Reply r = mock();
        replyDao.create(r);
        assertEquals(1, replyDao.listAllByTopId(r.getTid()).size());
    }

    @Test
    public void testPagingForAll(){
        Reply r = mock();
        replyDao.create(r);
        Paging<Reply> replyPage = replyDao.pagingForAll(r.getTopicId(), 0, 10);
        assertEquals(1, replyPage.getTotal().intValue());
        assertEquals(1, replyPage.getData().size());
    }

    @Test
    public void testPagingForPrivate(){
        Reply r = mock();
        replyDao.create(r);
        Paging<Reply> replyPage = replyDao.pagingForPrivate(r.getTopicId(), 1L, 1L, 0, 10);
        assertEquals(1, replyPage.getTotal().intValue());
        assertEquals(1, replyPage.getData().size());

        Paging<Reply> replyPaging;
        replyPaging = replyDao.pagingForPrivate(79l, 2961l, 2961l, 0, 20);
        replyPaging = replyDao.pagingForPrivate(79l, 2962l, 2961l, 0, 20);

    }

    @Test
    public void testListTopicSuppliers(){
        Reply r = mock();
        replyDao.create(r);
        assertEquals(0, replyDao.listTopicSuppliers(1L).size());
    }

    @Test
    public void testUsersReplyCounts(){
        Reply r = mock();
        replyDao.create(r);
        assertEquals(1, replyDao.usersReplyCounts(r.getTopicId(), Arrays.asList(1L, 2L), ",1,2,").size());
    }


    @Test
    public void testFindByUserId(){
        Reply r = mock();
        replyDao.create(r);
        Reply oneByCreatorId = replyDao.findOneByCreatorId(1l);
    }


    private Reply mock(){
        Reply r = new Reply();
        r.setUserId(1L);
        r.setReqId(1L);
        r.setTopicId(1L);
        r.setContent("xxx");
        r.setCompanyName("ooo");
        r.setPid(0L);
        r.setTid(0L);
        return r;
    }
}
