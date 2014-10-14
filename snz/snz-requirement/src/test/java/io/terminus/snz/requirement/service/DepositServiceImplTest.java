package io.terminus.snz.requirement.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.snz.eai.service.HGVSService;
import io.terminus.snz.requirement.dao.DepositDao;
import io.terminus.snz.requirement.dto.DepositPayment;
import io.terminus.snz.requirement.dto.KjtTransDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.model.Deposit;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.CompanyService;
import org.elasticsearch.common.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/4/14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ UserUtil.class })
public class DepositServiceImplTest {

    @InjectMocks
    private DepositServiceImpl depositService;

    @Mock
    private DepositDao depositDao;

    @Mock
    private CompanyService companyService;

    @Mock
    private RequirementService requirementService;

    @Mock
    private RequirementSolutionService requirementSolutionService;

    @Mock
    private RiskMortgagePaymentService riskMortgagePaymentService;

    @Mock
    private HGVSService hgvsService;

    BaseUser user = null;

    @Before
    public void init(){
        user = new User();
        user.setId(1l);
        user.setName("Michale");
        user.setMobile("18657327206");

        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(UserUtil.class);
        PowerMockito.when(UserUtil.getCurrentUser()).thenReturn(user);
    }

    @Test
    public void testFindPaidListByRequirement() throws Exception {
        assertNotNull(depositService.findPaidListByRequirement(null));

        when(depositDao.findListBy(any(Deposit.class))).thenReturn(Lists.newArrayList(mockOne()));
        assertNotNull(depositService.findPaidListByRequirement(1l));
    }

    @Test
    public void testCheckPaid() throws Exception {
        assertNotNull(depositService.checkPaid(null , 1l));
        assertNotNull(depositService.checkPaid(1l , null));
        assertNotNull(depositService.checkPaid(1l , 1l));
    }

    @Test
    public void testGetPayment() throws Exception {
        Response<Company> companyRes = new Response<Company>();
        Company company = new Company();
        company.setId(1L); // supplierId
        company.setSupplierCode("V1234");
        companyRes.setResult(company);
        when(companyService.findCompanyByUserId(anyLong())).thenReturn(companyRes);

        Response<RequirementDto> requirementDtoRes = new Response<RequirementDto>();
        RequirementDto requirementDto = new RequirementDto();
        Requirement requirement = new Requirement();
        // 需要支付 20000块钱
        requirement.setModuleAmount(2000000L);
        requirementDto.setRequirement(requirement);
        requirementDtoRes.setResult(requirementDto);
        when(requirementService.findById(1L)).thenReturn(requirementDtoRes);

        when(depositDao.findBy(any(Deposit.class))).thenReturn(mockOne());

        // 10块钱 风险抵押金
        Response<Long> riskRes = new Response<Long>();
        riskRes.setResult(1000L);
        when(riskMortgagePaymentService.getRiskMortgageAmountOfSupplier("V1234")).thenReturn(riskRes);
        // 10000块钱 余额

        Response<Long> hgvsRes = new Response<Long>();
        hgvsRes.setResult(1000000L);
        when(hgvsService.getBalanceBySupplierCode("V1234")).thenReturn(hgvsRes);

        Response<DepositPayment> depositRes = depositService.getPayment(user, 1L);
        assertTrue(depositRes.isSuccess());

        Response<Paging<Requirement>> reqRes = new Response<Paging<Requirement>>();
        requirement.setId(1L);
        reqRes.setResult(new Paging<Requirement>(1L, Lists.newArrayList(requirement)));
        when(requirementSolutionService.findByParams(user, 1, 1, "req", "", "", 1, 20)).thenReturn(reqRes);
        assertNotNull(depositService.pagingSolutionAndDeposit(user, 1, 1, "req", "", "", 1, 20));
    }

    @Test
    public void testSubmit() throws Exception {
        Response<Company> companyRes = new Response<Company>();
        companyRes.setResult(new Company());
        when(companyService.findCompanyByUserId(1L)).thenReturn(companyRes);
//        Response<String> depositRes = depositService.submit(1l, 1l, new KjtTransDto());
    }

    @Test
    public void testRevoke() throws Exception {
        assertNotNull(depositService.revoke(null));
        assertNotNull(depositService.revoke(1l));
    }

    @Test
    public void testRevokeFull() throws Exception {
        assertNotNull(depositService.revokeFull(null , 1l));
        assertNotNull(depositService.revokeFull(1l , null));
        assertNotNull(depositService.revokeFull(1l , 1l));
    }

    @Test
    public void testDealResponse() throws Exception {
        assertNotNull(depositService.dealResponse(ImmutableMap.of("name" , "name")));
    }

    @Test
    public void testDealTransResponse() throws Exception {
        assertNotNull(depositService.dealTransResponse(ImmutableMap.of("name" , "name")));
    }

    private Deposit mock(Long requirementId, Long supplierId,
                         String dealId, Date dealTime, String dealUrl,
                         Long amount, String bankInfo, Integer status) {
        Deposit dp = new Deposit();
        dp.setRequirementId(requirementId);
        dp.setSupplierId(supplierId);
        dp.setDealId(dealId);
        dp.setDealTime(dealTime);
        dp.setDealUrl(dealUrl);
        dp.setAmount(amount);
        dp.setBankInfo(bankInfo);
        dp.setType(1);
        dp.setStatus(status);
        dp.setSyncStatus(0);
        return dp;
    }

    private Deposit mockOne() {
        return mock(10L, 11L, "1000110101010", DateTime.now().toDate(), "dealUrl", 10000L, "bankInfo", 0);
    }
}