package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.snz.requirement.model.ModuleQuotation;
import io.terminus.snz.requirement.model.ModuleSolution;
import io.terminus.snz.user.model.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class ModuleSolutionServiceTest extends BasicService {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private ModuleSolutionService moduleSolutionService;

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
        assertNotNull(moduleSolutionService.createSolution(mock()));
    }

    @Test
    public void testBatchSolution() throws Exception {
        List<ModuleSolution> solutionList = Lists.newArrayList();

        ModuleSolution solution;
        for(long i=0; i<5; i++){
            solution = mock();
            solution.setModuleId(i);
            solutionList.add(solution);
        }

        assertNotNull(moduleSolutionService.batchSolution(JSON_MAPPER.toJson(solutionList)));
    }

    @Test
    public void testExistSolution() throws Exception {
        assertNotNull(moduleSolutionService.existSolution(null , 1l));
        assertNotNull(moduleSolutionService.existSolution(1l , null));
        assertNotNull(moduleSolutionService.existSolution(1l , 1l));
    }

    @Test
    public void testUpdateSolution() throws Exception {
        assertNotNull(moduleSolutionService.updateSolution(mock()));
    }

    @Test
    public void testBatchUpdateSolution() throws Exception {
        List<ModuleSolution> solutionList = Lists.newArrayList();

        ModuleSolution solution;
        for(long i=0; i<5; i++){
            solution = mock();
            solution.setModuleId(i);
            solutionList.add(solution);
        }

        assertNotNull(moduleSolutionService.batchUpdateSolution(JSON_MAPPER.toJson(solutionList)));
    }

    @Test
    public void testFindById() throws Exception {
        assertNotNull(moduleSolutionService.findById(null));
        assertNotNull(moduleSolutionService.findById(1l));
    }

    @Test
    public void testFindModules() throws Exception {
        assertNotNull(moduleSolutionService.findModules(null , 1l, 1, null, null));
        assertNotNull(moduleSolutionService.findModules(user , null, 1, null, null));
        assertNotNull(moduleSolutionService.findModules(user , 1l, null, null, null));
        assertNotNull(moduleSolutionService.findModules(user , 1l, 1, null, null));
        assertNotNull(moduleSolutionService.findModules(user , 1l, 2, 1, 20));
    }

    @Test
    public void testFindSolutionByPurchaser() throws Exception {
        assertNotNull(moduleSolutionService.findSolutionByPurchaser(null, null, null));
        assertNotNull(moduleSolutionService.findSolutionByPurchaser(1l, null, null));
    }

    @Test
    public void testCreateQuotation() throws Exception {
        assertNotNull(moduleSolutionService.createQuotation(mockQuotation()));
    }

    @Test
    public void testBatchQuotation() throws Exception {
        List<ModuleQuotation> quotationList = Lists.newArrayList();

        ModuleQuotation quotation;
        for(long i=0; i<5; i++){
            quotation = mockQuotation();
            quotation.setModuleId(i);
            quotationList.add(quotation);
        }

        assertNotNull(moduleSolutionService.batchQuotation(JSON_MAPPER.toJson(quotationList) , user));
    }

    @Test
    public void testExistQuotation() throws Exception {
        assertNotNull(moduleSolutionService.existQuotation(null , 1l));
        assertNotNull(moduleSolutionService.existQuotation(1l , null));
        assertNotNull(moduleSolutionService.existQuotation(1l , 1l));
    }

    @Test
    public void testUpdateQuotation() throws Exception {
        assertNotNull(moduleSolutionService.updateQuotation(mockQuotation()));
    }

    @Test
    public void testBatchUpdateQuotation() throws Exception {
        List<ModuleQuotation> quotationList = Lists.newArrayList();

        ModuleQuotation quotation;
        for(long i=0; i<5; i++){
            quotation = mockQuotation();
            quotation.setModuleId(i);
            quotationList.add(quotation);
        }

        assertNotNull(moduleSolutionService.batchUpdateQuotation(JSON_MAPPER.toJson(quotationList)));
    }

    @Test
    public void testFindByQuotationId() throws Exception {
        assertNotNull(moduleSolutionService.findByQuotationId(null));
        assertNotNull(moduleSolutionService.findByQuotationId(1l));
    }

    @Test
    public void testFindQuotations() throws Exception {
        assertNotNull(moduleSolutionService.findQuotations(null , 1l, 1, null, null));
        assertNotNull(moduleSolutionService.findQuotations(user , null, 1, null, null));
        assertNotNull(moduleSolutionService.findQuotations(user , 1l, null, null, null));
        assertNotNull(moduleSolutionService.findQuotations(user , 1l, 1, null, null));
        assertNotNull(moduleSolutionService.findQuotations(user , 1l, 2, 1, 20));
    }

    @Test
    public void testFindQuotationByPurchaser() throws Exception {
        assertNotNull(moduleSolutionService.findQuotationByPurchaser(null , null, null));
        assertNotNull(moduleSolutionService.findQuotationByPurchaser(1l , null, null));
    }

    private ModuleSolution mock(){
        ModuleSolution moduleSolution = new ModuleSolution();
        moduleSolution.setSolutionId(1l);
        moduleSolution.setModuleId(1l);
        moduleSolution.setModuleName("moduleName");
        moduleSolution.setTechnology("technology");
        moduleSolution.setQuality(100);
        moduleSolution.setReaction(DateTime.now().toDate());
        moduleSolution.setDelivery(10);
        moduleSolution.setCost(100);

        return moduleSolution;
    }

    private ModuleQuotation mockQuotation(){
        ModuleQuotation quotation = new ModuleQuotation();
        quotation.setSolutionId(1l);
        quotation.setModuleId(1l);
        quotation.setModuleName("moduleName");
        quotation.setSupplierId(1l);
        quotation.setSupplierName("MichaelZhao");
        quotation.setTotal(1300);
        quotation.setPrice(350);
        quotation.setCoinType("CMN");
        quotation.setExchangeRate(70);

        return quotation;
    }
}