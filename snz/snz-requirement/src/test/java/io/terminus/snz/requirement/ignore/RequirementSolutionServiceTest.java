package io.terminus.snz.requirement.ignore;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.requirement.model.RequirementSolution;
import io.terminus.snz.requirement.service.BasicService;
import io.terminus.snz.requirement.service.RequirementSolutionService;
import io.terminus.snz.user.model.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class RequirementSolutionServiceTest extends BasicService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private RequirementSolutionService requirementSolutionService;

    BaseUser user = null;

    @Before
    public void init(){
        user = new User();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");
    }

    @Test
    public void testCreateSolution() throws Exception {
        assertNotNull(requirementSolutionService.createSolution(mock() , null));
        assertNotNull(requirementSolutionService.createSolution(mock() , user));
    }

    @Test
    public void testExistSolution() throws Exception {
        assertNotNull(requirementSolutionService.existSolution(null , 1l));
        assertNotNull(requirementSolutionService.existSolution(1l , null));
        assertNotNull(requirementSolutionService.existSolution(1l , 1l));
    }

    @Test
    public void testUpdateSolution() throws Exception {
        assertNotNull(requirementSolutionService.updateSolution(mock()));
    }

    @Test
    public void testSignSecrecy() throws Exception {
        assertNotNull(requirementSolutionService.signSecrecy(null , 1, user));
        assertNotNull(requirementSolutionService.signSecrecy(1l , null, user));
        assertNotNull(requirementSolutionService.signSecrecy(1l , 1, null));
        assertNotNull(requirementSolutionService.signSecrecy(1l , 1, user));
        assertNotNull(requirementSolutionService.signSecrecy(1l , 2, user));
    }

    @Test
    public void testUpdateSolutionAccept() throws Exception {
        assertNotNull(requirementSolutionService.updateSolutionAccept(null , "accept", user));
        assertNotNull(requirementSolutionService.updateSolutionAccept(1l , "accept", null));
        assertNotNull(requirementSolutionService.updateSolutionAccept(1l , "accept", user));
        assertNotNull(requirementSolutionService.updateSolutionAccept(1l , null, user));
    }

    @Test
    public void testDeleteSolution() throws Exception {
        assertNotNull(requirementSolutionService.deleteSolution(null));
        assertNotNull(requirementSolutionService.deleteSolution(1l));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(requirementSolutionService.findById(null));
        assertNotNull(requirementSolutionService.findById(1l));
    }

    @Test
    public void testFindSolutionBySupplier() throws Exception {
        assertNotNull(requirementSolutionService.findSolutionBySupplier(null , user));
        assertNotNull(requirementSolutionService.findSolutionBySupplier(1l , null));
        assertNotNull(requirementSolutionService.findSolutionBySupplier(1l , user));
    }

    @Test
    public void testCheckAcceptInfo() throws Exception {
        assertNotNull(requirementSolutionService.checkAcceptInfo(null , user));
        assertNotNull(requirementSolutionService.checkAcceptInfo(1l , null));
        assertNotNull(requirementSolutionService.checkAcceptInfo(1l , user));
    }

    @Test
    public void testSolutionEnd() throws Exception {
        assertNotNull(requirementSolutionService.solutionEnd(null , user));
        assertNotNull(requirementSolutionService.solutionEnd(1l , null));
        assertNotNull(requirementSolutionService.solutionEnd(1l , user));
    }

    @Test
    public void testFindAllSolution() throws Exception {
        assertNotNull(requirementSolutionService.findAllSolution(null , 1));
        assertNotNull(requirementSolutionService.findAllSolution(1l , null));
        assertNotNull(requirementSolutionService.findAllSolution(1l , 1));
        assertNotNull(requirementSolutionService.findAllSolution(1l , 2));
    }

    @Test
    public void testUpdateBatchTechnology() throws Exception {
        List<RequirementSolution> solutionList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            solutionList.add(mock());
        }
        assertNotNull(requirementSolutionService.updateBatchTechnology(JSON_MAPPER.toJson(solutionList)));
    }

    @Test
    public void testUpdateSolutionFile() throws Exception {
        assertNotNull(requirementSolutionService.updateSolutionFile(null , "filePath", user));
        assertNotNull(requirementSolutionService.updateSolutionFile(1l , "filePath", null));
        assertNotNull(requirementSolutionService.updateSolutionFile(1l , null, user));
        assertNotNull(requirementSolutionService.updateSolutionFile(1l , "filePath", user));
    }

    @Test
    public void testFindByParams() throws Exception {
        assertNotNull(requirementSolutionService.findByParams(null , 1, 1, null, null, null, null, null));
        assertNotNull(requirementSolutionService.findByParams(user , null, 1, null, null, null, null, null));
        assertNotNull(requirementSolutionService.findByParams(user , 1, null, null, null, null, null, null));
        assertNotNull(requirementSolutionService.findByParams(user , 1, 1, null, null, null, null, null));
    }

    @Test
    public void testFindByRequirementId() throws Exception {
        assertNotNull(requirementSolutionService.findByRequirementId(null , null, null, null));
        assertNotNull(requirementSolutionService.findByRequirementId(1l , "1,2", null, null));
    }

    private RequirementSolution mock(){
        RequirementSolution requirementSolution = new RequirementSolution();
        requirementSolution.setUserId(1l);
        requirementSolution.setRequirementId(1l);
        requirementSolution.setRequirementName("requirementName");
        requirementSolution.setSupplierId(1l);
        requirementSolution.setSupplierName("Michael");
        requirementSolution.setTechnology(92);
        requirementSolution.setQuality(1000);
        requirementSolution.setReaction(DateTime.now().toDate());
        requirementSolution.setDelivery(100);
        requirementSolution.setCost(100);
        requirementSolution.setStatus(0);

        return requirementSolution;
    }
}