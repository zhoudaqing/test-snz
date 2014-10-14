package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.MainBusinessApprover;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-8.
 */
public class MainBusinessApproverDaoTest extends TestBaseDao {

    private MainBusinessApprover mainBusinessApprover;

    @Autowired
    private MainBusinessApproverDao mainBusinessApproverDao;

    private void mock() {
        mainBusinessApprover = new MainBusinessApprover();
        mainBusinessApprover.setLeaderId("232");
        mainBusinessApprover.setLeaderName("rer");
        mainBusinessApprover.setMemberId("545");
        mainBusinessApprover.setMemberName("454");
        mainBusinessApprover.setMainBusinessId(3L);
        mainBusinessApprover.setMainBusinessName("dg");

    }

    @Before
    public void setUp() {
        mock();
        mainBusinessApproverDao.create(mainBusinessApprover);
    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(mainBusinessApprover.getId());
    }

    @Test
    public void testFindByMemberId() {
        List<MainBusinessApprover> mainBusinessApprovers = mainBusinessApproverDao.findByMemberId(mainBusinessApprover.getMemberId());
        Assert.assertEquals(mainBusinessApprovers.get(0).getId(), mainBusinessApprover.getId());
    }

    @Test
    public void testFindMainBusinessIdsByLeaderId() {
        List<Long> ids = mainBusinessApproverDao.findMainBusinessIdsByLeaderId(mainBusinessApprover.getLeaderId());
        Assert.assertEquals(1, ids.size());
    }

    @Test
    public void testFindMainBusinessIdsByMemberIdOrLeaderId() {
        List<Long> ids = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(mainBusinessApprover.getMemberId());
        Assert.assertEquals(1, ids.size());

        ids = mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(mainBusinessApprover.getLeaderId());
        Assert.assertEquals(1, ids.size());
    }

    @Test
    public void testFindByBusinessIds() {
        List<MainBusinessApprover> mainBusinessApprovers = mainBusinessApproverDao.findByMainBusinessIds(Arrays.asList(mainBusinessApprover.getMainBusinessId()));
        Assert.assertEquals(mainBusinessApprovers.get(0).getId(), mainBusinessApprover.getId());
    }

}
