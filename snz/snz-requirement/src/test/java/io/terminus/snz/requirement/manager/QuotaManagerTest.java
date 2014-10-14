package io.terminus.snz.requirement.manager;

import com.google.common.collect.Lists;
import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.requirement.service.BasicService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class QuotaManagerTest extends BasicService {

    @Autowired
    private QuotaManager quotaManager;

    @Test
    public void testCreateQuotaWithT() throws Exception {
        List<RequirementRank> rankList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            rankList.add(mockRank());
        }
        quotaManager.createQuotaWithT(mock(), 1, rankList);
        quotaManager.createQuotaWithT(mock(), 2, rankList);
        quotaManager.createQuotaWithT(mock(), 3, rankList);
    }

    @Test
    public void testCreateQuotaWithC() throws Exception {
        List<RequirementRank> rankList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            rankList.add(mockRank());
        }

        quotaManager.createQuotaWithC(mock() , 1, rankList);
        quotaManager.createQuotaWithC(mock() , 2, rankList);
        quotaManager.createQuotaWithC(mock() , 3, rankList);
    }

    @Test
    public void testFindDetailSolution() throws Exception {
        assertNotNull(quotaManager.findDetailSolution(1l));
    }

    @Test
    public void testCountSelectNum() throws Exception {
        Requirement requirement = mock();
        requirement.setId(5l);
        //quotaManager.countSelectNum(requirement);
    }

    @Test
    public void testCreateDetailQuota() throws Exception {
        List<ModuleQuota> quotaList = Lists.newArrayList();

        for(int i=0; i<5; i++){
            quotaList.add(mockQuota());
        }

        quotaManager.createDetailQuota(mock() , quotaList);
    }

    private Requirement mock(){
        Requirement requirement = new Requirement();
        requirement.setId(1l);
        requirement.setName("test");
        requirement.setPurchaserId(1l);
        requirement.setPurchaserName("purchaserName");
        requirement.setSeriesIds("{sids:[{id:1,name:冰箱把手},{id:21,name:冰箱门}]}");
        requirement.setCoinType(1);
        requirement.setMaterielType(1l);
        requirement.setModuleType(1);
        requirement.setDeliveryAddress("{ad:[{pa:101,fa:10},{pa:101,fa:20}]}");
        requirement.setDescription("describe");
        requirement.setAccessories("{file:[url1,url2]}");
        requirement.setSelectNum(3);
        requirement.setReplaceNum(2);
        requirement.setCompanyScope("[{id:10,name:AGH}]");
        requirement.setTacticsId(1);
        requirement.setHeadDrop("引领点");
        requirement.setModuleNum(10);
        requirement.setModuleTotal(10000);
        requirement.setCheckResult(0);
        requirement.setCreatorId(1l);
        requirement.setCreatorName("Michael");
        requirement.setCreatorPhone("18657327206");
        requirement.setCreatorEmail("MichaelZhaoZero@gmail.com");

        return requirement;
    }

    private RequirementRank mockRank(){
        RequirementRank requirementRank = new RequirementRank();
        requirementRank.setRequirementId(1l);
        requirementRank.setRank(1);
        requirementRank.setType(1);
        requirementRank.setSupplierId(1l);
        requirementRank.setSupplierName("Michael");
        requirementRank.setQuotaScale(50);

        return requirementRank;
    }

    private ModuleQuota mockQuota(){
        ModuleQuota moduleQuota = new ModuleQuota();
        moduleQuota.setRequirementId(1l);
        moduleQuota.setSolutionId(1l);
        moduleQuota.setModuleId(1l);
        moduleQuota.setModuleName("moduleName");
        moduleQuota.setSupplierId(1l);
        moduleQuota.setSupplierName("Michael");
        moduleQuota.setQuantity(50);
        moduleQuota.setScale(70);

        return moduleQuota;
    }
}