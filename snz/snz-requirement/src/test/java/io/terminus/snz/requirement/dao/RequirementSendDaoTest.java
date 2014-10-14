package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.RequirementSend;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class RequirementSendDaoTest extends BasicTest {

    @Autowired
    private RequirementSendDao requirementSendDao;

    @Test
    public void testCreate() throws Exception {
        assertNotNull(requirementSendDao.create(mock()));
    }

    @Test
    public void testUpdate() throws Exception {
        assertNotNull(requirementSendDao.update(mock()));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(requirementSendDao.findById(1l));
    }

    @Test
    public void testFindByRequirementId() throws Exception {
        assertNotNull(requirementSendDao.findByRequirementId(1l));
    }

    private RequirementSend mock(){
        RequirementSend requirementSend = new RequirementSend();
        requirementSend.setRequirementId(1l);
        requirementSend.setSendPLM(1);
        requirementSend.setReplyModuleNum(1);
        requirementSend.setBusinessNegotiate(1);
        requirementSend.setSupplierSign(1);
        requirementSend.setResultPublicity(1);
        requirementSend.setConfirmQuota(1);
        requirementSend.setSendVCode(1);
        requirementSend.setWriteDetailQuota(1);
        requirementSend.setSendSAP(1);

        return requirementSend;
    }
}