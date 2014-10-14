package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.ModuleQuotationDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dao.RequirementSolutionDao;
import io.terminus.snz.requirement.dto.CheckSolEndDto;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.manager.SolutionManager;
import io.terminus.snz.requirement.model.ModuleQuotation;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementSolution;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-5.
 */
public class RequirementSolutionServiceTest  {
    @Mock
    private RequirementSolutionDao requirementSolutionDao;

    @Mock
    private RequirementDao requirementDao;

    @Mock
    private SolutionManager solutionManager;

    @Mock
    private ModuleQuotationDao moduleQuotationDao;

    @Mock
    private CompanyService companyService;

    @Mock
    private CountManager countManager;

    @Mock
    private RequirementCountService requirementCountService;

    @Mock
    private DepositService depositService;

    @InjectMocks
    private RequirementSolutionServiceImpl requirementSolutionServiceImpl;

    @Before
    public void init(){
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void createSolution() {
        Response<Boolean> response = requirementSolutionServiceImpl.createSolution(null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        when(companyService.findCompanyByUserId(anyLong())).thenReturn(new Response<Company>());
        response = requirementSolutionServiceImpl.createSolution(null, createBaseUser());
        assertFalse(response.isSuccess());

        Response<Company> r = new Response<Company>();
        r.setResult(createCompany());
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByRequirementId(anyLong(),anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.createSolution(createRequirementSolution(), createBaseUser());
        assertFalse(response.isSuccess());

        r = new Response<Company>();
        r.setResult(createCompany());
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(r);
        reset(requirementSolutionDao);
        when(requirementSolutionDao.findByRequirementId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        response = requirementSolutionServiceImpl.createSolution(createRequirementSolution(), createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.existed.error");

        r = new Response<Company>();
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByRequirementId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        Requirement requirement = createRequirement();
        requirement.setStatus(1);
        when(requirementDao.findById(anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.createSolution(createRequirementSolution(), createBaseUser());
        assertFalse(response.isSuccess());

        r = new Response<Company>();
        r.setResult(createCompany());
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        requirement = createRequirement();
        requirement.setStatus(4);
        reset(requirementDao);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        response = requirementSolutionServiceImpl.createSolution(createRequirementSolution(), createBaseUser());
        assertTrue(response.isSuccess());


        r = new Response<Company>();
        r.setResult(createCompany());
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        requirement = createRequirement();
        requirement.setStatus(1);

        response = requirementSolutionServiceImpl.createSolution(createRequirementSolution(), createBaseUser());
        assertEquals(response.getError(),null);


    }

    @Test
    public void existSolution() {
        Response<RequirementSolution> response = requirementSolutionServiceImpl.existSolution(null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        response = requirementSolutionServiceImpl.existSolution(1L,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.supplierId.null");

        when(requirementSolutionDao.findByRequirementId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        response = requirementSolutionServiceImpl.existSolution(1L,1L);
        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findByRequirementId(anyLong(),anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.existSolution(1L,1L);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.find.failed");
    }

    @Test
    public void updateSolution() {
        RequirementSolution requirementSolution = createRequirementSolution();
        requirementSolution.setId(null);
        Response<Boolean> response = requirementSolutionServiceImpl.updateSolution(requirementSolution);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.id.null");

        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        Requirement requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        response = requirementSolutionServiceImpl.updateSolution(requirementSolution);
        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        requirement = createRequirement();
        requirement.setStatus(1);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        response = requirementSolutionServiceImpl.updateSolution(requirementSolution);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.status.notUpdate");

        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.updateSolution(requirementSolution);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(), "solution.update.failed");
    }

    @Test
    public void signSecrecy() {
        Response<Boolean> response = requirementSolutionServiceImpl.signSecrecy(null,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        response = requirementSolutionServiceImpl.signSecrecy(1L,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        when(companyService.isComplete(anyLong())).thenReturn(new Response<Boolean>());
        response = requirementSolutionServiceImpl.signSecrecy(1L,1,createBaseUser());
        assertTrue(response.isSuccess());

        Response<Boolean> r = new Response<Boolean>();
        r.setResult(false);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        response = requirementSolutionServiceImpl.signSecrecy(1L,1,createBaseUser());
        assertTrue(response.isSuccess());
        //assertEquals(response.getError(),"requirement.company.noComplete");

        r = new Response<Boolean>();
        r.setResult(true);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        response = requirementSolutionServiceImpl.signSecrecy(1L,1,createBaseUser());
        assertTrue(response.isSuccess());

        r = new Response<Boolean>();
        r.setResult(true);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(null);
        when(countManager.existSign(anyLong(), anyLong())).thenReturn(true);
        response = requirementSolutionServiceImpl.signSecrecy(1L,null,createBaseUser());
        assertTrue(response.isSuccess());

        r = new Response<Boolean>();
        r.setResult(true);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        countManager.removeSign(anyLong(),anyLong());
        response = requirementSolutionServiceImpl.signSecrecy(1L,null,createBaseUser());
        assertTrue(response.isSuccess());

        r = new Response<Boolean>();
        r.setResult(true);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.signSecrecy(1L,null,createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.find.failed");

    }

    @Test
    public void updateSolutionAccept() {
        Response<Boolean> response = requirementSolutionServiceImpl.updateSolutionAccept(null,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        when(companyService.isComplete(anyLong())).thenReturn(new Response<Boolean>());
        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,"1",createBaseUser());
        assertFalse(response.isSuccess());

        Response<Boolean> r = new Response<Boolean>();
        r.setResult(false);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findById(anyLong())).thenReturn(createRequirementSolution());
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenReturn(true);
        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,"1",createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.status.notUpdate");

        r = new Response<Boolean>();
        r.setResult(false);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        when(requirementSolutionDao.findById(anyLong())).thenReturn(createRequirementSolution());
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenReturn(true);
        Requirement requirement = createRequirement();
        requirement.setId(1L);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,"1",createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.update.failed");


        r = new Response<Boolean>();
        r.setResult(false);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        RequirementSolution requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        when(requirementSolutionDao.findById(anyLong())).thenReturn(requirementSolution);
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenReturn(true);
        requirement = createRequirement();
        requirement.setId(1L);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,"1",createBaseUser());
        assertTrue(response.isSuccess());

        r = new Response<Boolean>();
        r.setResult(false);
        when(companyService.isComplete(anyLong())).thenReturn(r);
        requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        when(requirementSolutionDao.findById(anyLong())).thenReturn(requirementSolution);
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenReturn(true);
        requirement = createRequirement();
        requirement.setId(1L);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirementCountService.setReqCountInfo(anyLong(),any(RequirementCountType.class),anyInt());
        response = requirementSolutionServiceImpl.updateSolutionAccept(1L,null,createBaseUser());
        assertTrue(response.isSuccess());

    }

    @Test
    public void deleteSolution() {
        RequirementSolution requirementSolution = createRequirementSolution();
        requirementSolution.setId(null);
        Response<Boolean> response = requirementSolutionServiceImpl.deleteSolution(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.id.null");

        requirementSolution = createRequirementSolution();
        requirementSolution.setId(null);
        when(requirementSolutionDao.findById(anyLong())).thenReturn(requirementSolution);
        when(requirementSolutionDao.findByRequirementId(anyLong(), anyLong())).thenReturn(null);
        Requirement requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        requirementSolution = createRequirementSolution();
        requirementSolution.setId(1L);
        response = requirementSolutionServiceImpl.deleteSolution(1L);
        assertTrue(response.isSuccess());

    }

    @Test
    public void  findById() {
        Response<RequirementSolution> response = requirementSolutionServiceImpl.findById(null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.id.null");

        RequirementSolution requirementSolution = createRequirementSolution();
        when(requirementSolutionDao.findById(anyLong())).thenReturn(requirementSolution);
        response = requirementSolutionServiceImpl.findById(1L);
        assertTrue(response.isSuccess());

    }

    @Test
    public void findSolutionBySupplier() {
        Response<RequirementSolution> response = requirementSolutionServiceImpl.findSolutionBySupplier(null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        response = requirementSolutionServiceImpl.findSolutionBySupplier(null,createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        response = requirementSolutionServiceImpl.findSolutionBySupplier(1L,createBaseUser());
        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.findSolutionBySupplier(1L,createBaseUser());
        assertFalse(response.isSuccess());

    }

    @Test
    public void checkAcceptInfo() {
        Response<String> response = requirementSolutionServiceImpl.checkAcceptInfo(null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        response = requirementSolutionServiceImpl.checkAcceptInfo(null, createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        response = requirementSolutionServiceImpl.checkAcceptInfo(1L, createBaseUser());
        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(null);
        response = requirementSolutionServiceImpl.checkAcceptInfo(1L,createBaseUser());
        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.checkAcceptInfo(1L, createBaseUser());
        assertFalse(response.isSuccess());
    }

    @Test
    public void solutionEnd() {
        Response<CheckSolEndDto> response = requirementSolutionServiceImpl.solutionEnd(null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        response = requirementSolutionServiceImpl.solutionEnd(null, createBaseUser());
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirementId.id.null");

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        when(requirementDao.findById(anyLong())).thenReturn(createRequirement());
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        Requirement requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        Response<Integer> integerResponse = new Response<Integer>();
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(1);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        RequirementSolution requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(3);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(3);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(4);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(2);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(4);
        requirementSolution.setSolutionFile(null);
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        requirement.setTacticsId(1);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(2);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(4);
        requirementSolution.setSolutionFile("123213213");
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        requirement.setTacticsId(1);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(2);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        when(moduleQuotationDao.findAllQuotations(anyLong())).thenReturn(new ArrayList<ModuleQuotation>());
        when(requirementSolutionDao.findByUserId(anyLong(), anyLong())).thenReturn(createRequirementSolution());
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertFalse(response.isSuccess());

        requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(4);
        requirementSolution.setSolutionFile("123213213");
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        requirement.setTacticsId(1);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(2);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        when(moduleQuotationDao.findAllQuotations(anyLong())).thenReturn(Lists.newArrayList(createModuleQuotation()));
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertNull(response);

        requirementSolution = createRequirementSolution();
        requirementSolution.setStatus(4);
        requirementSolution.setSolutionFile("123213213");
        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(requirementSolution);
        requirement = createRequirement();
        requirement.setStatus(4);
        requirement.setTacticsId(1);
        when(requirementDao.findById(anyLong())).thenReturn(requirement);
        integerResponse = new Response<Integer>();
        integerResponse.setResult(2);
        when(depositService.checkPaid(anyLong(),anyLong())).thenReturn(integerResponse);
        when(moduleQuotationDao.findAllQuotations(anyLong())).thenReturn(Lists.newArrayList(new ModuleQuotation()));
        response = requirementSolutionServiceImpl.solutionEnd(1L,createBaseUser());
        assertNull(response);
    }

    @Test
    public void findAllSolution() {
        Response<List<RequirementSolution>> response = requirementSolutionServiceImpl.findAllSolution(null,null);
        assertFalse(response.isSuccess());

        response = requirementSolutionServiceImpl.findAllSolution(1L,null);
        assertFalse(response.isSuccess());

        when(requirementSolutionDao.findSolutionEnds(anyLong(),anyList(),anyInt(), null)).thenReturn(new ArrayList<RequirementSolution>());
        when(requirementSolutionDao.findAllSolution(anyLong())).thenReturn(new ArrayList<RequirementSolution>());
        response = requirementSolutionServiceImpl.findAllSolution(1L,1);

        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findSolutionEnds(anyLong(),anyList(),anyInt(), null)).thenReturn(new ArrayList<RequirementSolution>());
        when(requirementSolutionDao.findAllSolution(anyLong())).thenReturn(new ArrayList<RequirementSolution>());
        response = requirementSolutionServiceImpl.findAllSolution(1L,5);

        assertTrue(response.isSuccess());

        when(requirementSolutionDao.findSolutionEnds(anyLong(),anyList(),anyInt(), null)).thenReturn(new ArrayList<RequirementSolution>());
        when(requirementSolutionDao.findAllSolution(anyLong())).thenThrow(Exception.class);
        response = requirementSolutionServiceImpl.findAllSolution(1L,5);

        assertFalse(response.isSuccess());
    }

    @Test
    public void updateBatchTechnology() {
        Response<Boolean> response = requirementSolutionServiceImpl.updateBatchTechnology("[]");
        assertFalse(response.isSuccess());
    }

    @Test
    public void updateSolutionFile() {
        Response<Boolean> response = requirementSolutionServiceImpl.updateSolutionFile(null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"requirement.id.null");

        response = requirementSolutionServiceImpl.updateSolutionFile(1L, null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.file.null");

        response = requirementSolutionServiceImpl.updateSolutionFile(1L, "[]",null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        when(requirementSolutionDao.findByUserId(anyLong(),anyLong())).thenReturn(createRequirementSolution());
        when(requirementSolutionDao.update(any(RequirementSolution.class))).thenReturn(true);
        response = requirementSolutionServiceImpl.updateSolutionFile(1L, "[]",createBaseUser());

        assertTrue(response.isSuccess());

    }

    @Test
    public void findByParams() {
        Response<Paging<Requirement>> response = requirementSolutionServiceImpl.findByParams(null, null, null, null, null, null, null, null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"user.not.login");

        when(companyService.findCompanyByUserId(anyLong())).thenReturn(new Response<Company>());
        response = requirementSolutionServiceImpl.findByParams(createBaseUser(), null, null, null, null, null, null, null);
        assertFalse(response.isSuccess());

        Response<Company> companyRes = new Response<Company>();
        companyRes.setResult(createCompany());

        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyRes);
        when(requirementDao.findBySupplier(anyLong(), anyMap())).thenReturn(new Paging<Requirement>());
        response = requirementSolutionServiceImpl.findByParams(createBaseUser(), null, null, null, null, null, null, null);
        assertTrue(response.isSuccess());
    }

    @Test
    public void findByRequirementId() {
        Response<Paging<RequirementSolution>>  response = requirementSolutionServiceImpl.findByRequirementId(null, null, null,null);
        assertFalse(response.isSuccess());
        assertEquals(response.getError(),"solution.requirementId.null");

        when(requirementSolutionDao.findByParams(anyLong(),anyMap())).thenReturn(new Paging<RequirementSolution>());
        response = requirementSolutionServiceImpl.findByRequirementId(1L,"1,2", null,null);
        assertTrue(response.isSuccess());
    }

    
    public Response<List<Long>> qualifyBackendCategories(Long requirementId) {
        return null;
    }

    
    public Response<Boolean> updateSolQualify(Long requirementId, Integer qualifyStatus, BaseUser user) {
        return null;
    }

    
    public Response<List<RequirementSolution>> findSolutionsByQualifyStatusOfSupplier(Integer qualifyStatus, BaseUser user) {
        return null;
    }

    
    public Response<Paging<RequirementSolution>> findSolutionsBySupplierNameAndQualifyStatus(String supplierName, Integer qualifyStatus, Integer pageNo, Integer size) {
        return null;
    }

    private RequirementSolution createRequirementSolution(){
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

    private BaseUser createBaseUser(){
        BaseUser loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("xxx");
        loginer.setNickName("ooo");
        loginer.setMobile("xxoo");
        return loginer;
    }

    private Company createCompany(){
        Company company = new Company();
        company.setId(1L);
        company.setCustomers("11");
        return company;
    }

    private Requirement createRequirement(){
        Requirement requirement = new Requirement();
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
        requirement.setModuleNum(1);
        requirement.setModuleTotal(10000);
        requirement.setCheckResult(0);
        requirement.setCreatorId(1l);
        requirement.setCreatorName("Michael");
        requirement.setCreatorPhone("18657327206");
        requirement.setCreatorEmail("MichaelZhaoZero@gmail.com");
        requirement.setStatus(3);
        return requirement;
    }
    private ModuleQuotation createModuleQuotation(){
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
