package io.terminus.snz.requirement.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.RequirementQuestionnaire;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-22.
 */
public class RequirementQuestionnaireTest extends BasicTest {

    private RequirementQuestionnaire requirementQuestionnaire;

    @Autowired
    private RequirementQuestionnaireDao requirementQuestionnaireDao;

    private void mock() {
        requirementQuestionnaire = new RequirementQuestionnaire();
        requirementQuestionnaire.setUserId(1L);
        requirementQuestionnaire.setCompanyId(1L);
        requirementQuestionnaire.setCorporation("海尔");
        requirementQuestionnaire.setRequirementId(2L);
        requirementQuestionnaire.setRequirementName("钢材");
        requirementQuestionnaire.setNoClearInterfaceOrPackage("aa");
        requirementQuestionnaire.setNoInterfaceOrPackage("bb");
        requirementQuestionnaire.setLessOrderCount("cc");
        requirementQuestionnaire.setNoStandardInterfaceOrPackage("dd");
        requirementQuestionnaire.setParksNotMatch("ee");
        requirementQuestionnaire.setShortPeriod("ff");
        requirementQuestionnaire.setOtherReason("gg");
    }

    @Before
    public void testCreate() {
        mock();
        Long id = requirementQuestionnaireDao.create(requirementQuestionnaire);
        Assert.assertNotNull(id);
    }

    @Test
    public void testFindByUserIdAndRequirementId() {
        RequirementQuestionnaire model = requirementQuestionnaireDao.findByUserIdAndRequirementId(requirementQuestionnaire.getUserId(), requirementQuestionnaire.getRequirementId());
        Assert.assertNotNull(model);
    }

    @Test
    public void testFindById() {
        RequirementQuestionnaire model = requirementQuestionnaireDao.findById(requirementQuestionnaire.getId());
        Assert.assertNotNull(model);
    }

    @Test
    public void testFindByPaging() {
        Paging<RequirementQuestionnaire> paging = requirementQuestionnaireDao.findByPaging("海", "钢", 0, 30);
        Assert.assertEquals(1, paging.getTotal().intValue());
    }

}
