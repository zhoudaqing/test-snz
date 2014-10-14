package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.user.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class RequirementQuotaServiceTest extends BasicService{

    @Autowired
    private RequirementQuotaService requirementQuotaService;

    BaseUser user = null;

    @Before
    public void init(){
        user = new User();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");
    }

    @Test
    public void testFindEndSolutions() throws Exception {
        assertNotNull(requirementQuotaService.findEndSolutions(null , 1, user));
        assertNotNull(requirementQuotaService.findEndSolutions(1l , 1, null));
        assertNotNull(requirementQuotaService.findEndSolutions(1l , 1, user));
        assertNotNull(requirementQuotaService.findEndSolutions(1l , 2, user));
        assertNotNull(requirementQuotaService.findEndSolutions(1l , 5, user));
    }

    @Test
    public void testCreateRanks() throws Exception {
        assertNotNull(requirementQuotaService.createRanks(null , 1, user));
        assertNotNull(requirementQuotaService.createRanks(1l , 1, null));
        assertNotNull(requirementQuotaService.createRanks(1l , 1, user));
        assertNotNull(requirementQuotaService.createRanks(1l , 2, user));
        assertNotNull(requirementQuotaService.createRanks(1l , 5, user));
    }

    @Test
    public void testCreateRankByT() throws Exception{
        List<RequirementRank> rankList = Lists.newArrayList();
        for(int i=0; i<5; i++){
            rankList.add(mock());
        }
        assertNotNull(requirementQuotaService.createRankByT(JsonMapper.JSON_NON_DEFAULT_MAPPER.toJson(rankList), null));
        assertNotNull(requirementQuotaService.createRankByT(JsonMapper.JSON_NON_DEFAULT_MAPPER.toJson(rankList), user));
    }

    @Test
    public void testSelectEndStatus() throws Exception{
        assertNotNull(requirementQuotaService.selectEndStatus(null , 1, user));
        assertNotNull(requirementQuotaService.selectEndStatus(1l , 1, null));
        assertNotNull(requirementQuotaService.selectEndStatus(1l , 1, user));
        assertNotNull(requirementQuotaService.selectEndStatus(1l , 2, user));
        assertNotNull(requirementQuotaService.selectEndStatus(1l , 3, user));
        assertNotNull(requirementQuotaService.selectEndStatus(1l , 4, user));

    }

    @Test
    public void testSignSolution() throws Exception {
        assertNotNull(requirementQuotaService.signSolution(null , 1, user));
        assertNotNull(requirementQuotaService.signSolution(1l , 1, null));
        assertNotNull(requirementQuotaService.signSolution(1l , 1, user));
        assertNotNull(requirementQuotaService.signSolution(1l , 2, user));
    }

    @Test
    public void testFindDetailSolution() throws Exception {
        assertNotNull(requirementQuotaService.findDetailSolution(null));
        assertNotNull(requirementQuotaService.findDetailSolution(1l));
    }

    @Test
    public void testFindRequirementRanks() throws Exception {
        assertNotNull(requirementQuotaService.findRequirementRanks(null , 1));
        assertNotNull(requirementQuotaService.findRequirementRanks(1l , null));
        assertNotNull(requirementQuotaService.findRequirementRanks(1l , 1));
        assertNotNull(requirementQuotaService.findRequirementRanks(1l , 2));
        assertNotNull(requirementQuotaService.findRequirementRanks(1l , 3));
    }

    @Test
    public void testFindByRequirementId() throws Exception {
        assertNotNull(requirementQuotaService.findByRequirementId(null , null, null));
        assertNotNull(requirementQuotaService.findByRequirementId(1l , null, null));
    }

    @Test
    public void testFindQuotasBySupplier() throws Exception {
        assertNotNull(requirementQuotaService.findQuotasBySupplier(null , 1l , null, null));
        assertNotNull(requirementQuotaService.findQuotasBySupplier(user , null , null, null));
        assertNotNull(requirementQuotaService.findQuotasBySupplier(user , 1l , null, null));
    }

    @Test
    public void testPlmCompanyVExpire() throws Exception {
        //requirementQuotaService.plmCompanyVExpire();
    }

    private RequirementRank mock(){
        RequirementRank requirementRank = new RequirementRank();
        requirementRank.setRequirementId(1l);
        requirementRank.setRank(1);
        requirementRank.setType(1);
        requirementRank.setSupplierId(1l);
        requirementRank.setSupplierName("Michael");
        requirementRank.setQuotaScale(50);

        return requirementRank;
    }
}