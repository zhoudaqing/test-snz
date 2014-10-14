package io.terminus.snz.web.jobs;

import io.terminus.snz.requirement.service.ModuleService;
import io.terminus.snz.requirement.service.RequirementQuotaService;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.web.BaseTest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-5
 */
public class RequirementJosTest extends BaseTest {
    @Mock
    private RequirementService requirementService;

    @Mock
    private ModuleService moduleService;

    @Mock
    private RequirementQuotaService requirementQuotaService;

    @InjectMocks
    private RequirementJobs requirementJobs;

    @Test
    public void testWarning(){
        requirementJobs.warning();
    }

    @Test
    public void testAutoSetCompanyVCode(){
        requirementJobs.autoSetCompanyVCode();
    }

    @Test
    public void testAutoSetModuleNum(){
        requirementJobs.autoSetModuleNum();
    }

}
