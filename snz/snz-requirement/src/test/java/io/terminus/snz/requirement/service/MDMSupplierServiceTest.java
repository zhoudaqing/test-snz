package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.model.MDMConfigure;
import io.terminus.snz.eai.service.MdmConfigureService;
import io.terminus.snz.requirement.dao.SupplierQualificationDao;
import io.terminus.snz.requirement.dto.MDMBaseCompanyDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.requirement.model.SupplierQualification;
import io.terminus.snz.user.dto.FinanceDto;
import io.terminus.snz.user.model.Address;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.ContactInfo;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.AddressService;
import io.terminus.snz.user.service.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static io.terminus.snz.user.model.User.JobRole;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Date: 8/3/14
 * Time: 19:26
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class MDMSupplierServiceTest extends BasicService {

    @InjectMocks
    MDMSupplierServiceImpl mdmSupplierService;

    @Mock
    SupplierQualificationDao supplierQualificationDao;

    @Mock
    MdmConfigureService mdmConfigureService;

    @Mock
    AddressService addressService;

    @Mock
    CompanyService companyService;

    @Mock
    AccountService<BaseUser> accountService;

    @Mock
    RequirementService requirementService;

    @Mock
    RequirementQuotaService requirementQuotaService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldPaigingBy() {
        // mock requirement page
        Response<Paging<Requirement>> requirementPageGetMock = new Response<Paging<Requirement>>();
        List<Requirement> requirementListMock = Lists.newArrayList();
        Paging<Requirement> requrimentPageMock = new Paging<Requirement>(1l, requirementListMock);
        Requirement req = new Requirement();
        req.setName("xxx");
        requirementListMock.add(req);
        requirementPageGetMock.setResult(requrimentPageMock);
        when(requirementService.findByPurchaser(
                any(BaseUser.class), anyInt(), anyInt(), anyString(),
                anyString(), anyString(), eq(0), anyInt()))
                .thenReturn(requirementPageGetMock);
        // jump out loop
        Paging<Requirement> empty = new Paging<Requirement>(0l, Collections.<Requirement>emptyList());
        Response<Paging<Requirement>> emptyMock = new Response<Paging<Requirement>>();
        emptyMock.setResult(empty);
        when(requirementService.findByPurchaser(
                any(BaseUser.class), anyInt(), anyInt(), anyString(),
                anyString(), anyString(), eq(1), anyInt()))
                .thenReturn(emptyMock);

        // 查找用户信息
        Response<BaseUser> userGetMock = new Response<BaseUser>();
        BaseUser user = new BaseUser();
        user.setId(1l);
        user.setNickName("xxx");
        userGetMock.setResult(user);
        when(accountService.findUserById(anyLong())).thenReturn(userGetMock);

        // Address
        Response<Address> addressGet = new Response<Address>();
        Address address = new Address();
        address.setName("any");
        when(addressService.findById(anyInt())).thenReturn(addressGet);

        // MDMConfigure
        Response<MDMConfigure> configure = new Response<MDMConfigure>();
        MDMConfigure configureMock = new MDMConfigure();
        configureMock.setCode("CN");
        configure.setResult(configureMock);
        when(mdmConfigureService.findBy(any(MDMConfigure.class))).thenReturn(configure);

        // contact info mock
        Response<ContactInfo> contactGetMock = new Response<ContactInfo>();
        ContactInfo infoMock = new ContactInfo();
        contactGetMock.setResult(infoMock);
        when(companyService.findContactInfoByUserId(any(BaseUser.class))).thenReturn(contactGetMock);

        // mock
        when(supplierQualificationDao.findBySupplierId(anyLong())).thenReturn(null);

        // mock requirement quota
        Response<List<RequirementRank>> reqListGetMock = new Response<List<RequirementRank>>();
        List<RequirementRank> reqListMock = Lists.newArrayList();
        RequirementRank rankMock = new RequirementRank();
        rankMock.setSupplierId(1l);
        reqListMock.add(rankMock);
        reqListGetMock.setResult(reqListMock);
        when(requirementQuotaService.findRequirementRanks(anyLong(), anyInt())).thenReturn(reqListGetMock);

        // mock Company
        Response<List<Company>> companiesGetMock = new Response<List<Company>>();
        List<Company> companiesMock = Lists.newArrayList();
        Company companyMock = new Company();
        companiesMock.add(companyMock);
        companiesGetMock.setResult(companiesMock);
        when(companyService.findCompaniesByIds(anyList())).thenReturn(companiesGetMock);


        BaseUser loginer = new BaseUser();
        loginer.setId(1l);
        loginer.setNickName("xxx");
        loginer.setRoles(Lists.newArrayList(JobRole.RESOURCE.role()));
        Response<Paging<MDMBaseCompanyDto>> pageGet = mdmSupplierService.findBaseSupplierInfoBy(loginer, null, null, null);
        assertTrue(pageGet.isSuccess());

        resetAll();
    }

    @Test
    public void shouldFindBaseSupplierInfoById() {
        commonMockSetup();

        Response<MDMBaseCompanyDto> dtoGet = mdmSupplierService.findBaseSupplierInfoById(1l);
        assertTrue(dtoGet.isSuccess());
        assertNotNull(dtoGet.getResult());

        resetAll();
    }

    @Test
    public void shouldfindQualification() {
        when(supplierQualificationDao.findBySupplierId(anyLong())).thenReturn(new SupplierQualification());

        Response<SupplierQualification> qaGet = mdmSupplierService.findQualificationBySupplierId(1l);
        assertTrue(qaGet.isSuccess());

        resetAll();
    }

    @Test
    public void updateSupplierInfoById() {
        commonMockSetup();
        BaseUser user = new BaseUser();
        user.setId(1l);
        user.setNickName("xxx");
        user.setRoles(Lists.newArrayList(JobRole.RESOURCE.role()));

        when(supplierQualificationDao.update(any(SupplierQualification.class))).thenReturn(true);

        SupplierQualification sq = new SupplierQualification();
        sq.setStage(1);
        Response<Boolean> tryUpdate = mdmSupplierService.updateSupplierInfoById(user, 1l, sq);
        assertTrue(tryUpdate.isSuccess());

        resetAll();
    }

    private void resetAll() {
        reset(supplierQualificationDao);
        reset(mdmConfigureService);
        reset(addressService);
        reset(companyService);
        reset(accountService);
        reset(requirementService);
        reset(requirementQuotaService);
    }

    private void commonMockSetup() {
        // 查找公司
        Response<Company> companyReultMock = new Response<Company>();
        Company companyMock = new Company();
        companyMock.setId(1l);
        companyMock.setUserId(1l);
        companyMock.setRegCountry(1);
        companyReultMock.setResult(companyMock);
        when(companyService.findCompanyById(anyLong())).thenReturn(companyReultMock);

        // 查找财务信息
        Response<FinanceDto> financeGetMock = new Response<FinanceDto>();
        financeGetMock.setResult(new FinanceDto());
        when(companyService.findFinanceByUserId(any(BaseUser.class))).thenReturn(financeGetMock);

        // 查找验证信息
        SupplierQualification sqMock = new SupplierQualification();
        sqMock.setStage(1);
        when(supplierQualificationDao.findBySupplierId(anyLong())).thenReturn(sqMock);

        // 查找用户信息
        Response<BaseUser> userGetMock = new Response<BaseUser>();
        BaseUser user = new BaseUser();
        user.setId(1l);
        user.setNickName("xxx");
        userGetMock.setResult(user);
        when(accountService.findUserById(anyLong())).thenReturn(userGetMock);

        // Address
        Response<Address> addressGet = new Response<Address>();
        Address address = new Address();
        address.setName("any");
        when(addressService.findById(anyInt())).thenReturn(addressGet);

        // MDMConfigure
        Response<MDMConfigure> configure = new Response<MDMConfigure>();
        MDMConfigure configureMock = new MDMConfigure();
        configureMock.setCode("CN");
        configure.setResult(configureMock);
        when(mdmConfigureService.findBy(any(MDMConfigure.class))).thenReturn(configure);

        // contact info mock
        Response<ContactInfo> contactGetMock = new Response<ContactInfo>();
        ContactInfo infoMock = new ContactInfo();
        contactGetMock.setResult(infoMock);
        when(companyService.findContactInfoByUserId(any(BaseUser.class))).thenReturn(contactGetMock);
    }
}
