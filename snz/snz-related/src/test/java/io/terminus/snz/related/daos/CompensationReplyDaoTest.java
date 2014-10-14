package io.terminus.snz.related.daos;

import io.terminus.common.model.Paging;
import io.terminus.snz.related.BaseDaoTest;
import io.terminus.snz.related.models.CompensationReply;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Author:  wenhaoli
 * Date: 2014-08-12
 */
public class CompensationReplyDaoTest extends BaseDaoTest {

    @Autowired
    private CompensationReplyDao compensationReplyDao;

    @Test
    public void testcreate() {
        CompensationReply test = new CompensationReply();
        test.setCompanyName("abcd");
        assertNotNull(compensationReplyDao.create(test));
    }

    @Test
    public void testfindById() {
        CompensationReply test = new CompensationReply();
        test.setCompanyName("abcdefg");
        compensationReplyDao.create(test);
        assertNotNull(test.getId());
        assertNotNull(compensationReplyDao.findById(test.getId()));
    }

    @Test
    public void testfindDetailById() {
        CompensationReply test = new CompensationReply();
        compensationReplyDao.create(test);
        assertNull(compensationReplyDao.findDetailById(test));
    }

    @Test
    public void testallPagingForDid() {
        CompensationReply compensationReply = new CompensationReply();
        compensationReply.setListId(1L);
        compensationReply.setCompanyName("abc");
        compensationReply.setContent("abc");
        compensationReplyDao.create(compensationReply);
        compensationReply.setListId(2L);
        compensationReplyDao.create(compensationReply);
        Paging<CompensationReply> resp = compensationReplyDao.allPagingForDid(1L,0,2);
        assertNotNull(resp);
    }

}
