package io.terminus.snz.user;

import com.google.common.collect.Maps;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.*;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class BaseTest {

    protected BaseUser loginer;
    protected BaseUser supplier;
    protected BaseUser purchaser;

    public BaseTest() {
        loginer = new BaseUser();
        loginer.setId(1L);
        loginer.setName("xxx");
        loginer.setNickName("ooo");
        loginer.setMobile("xxoo");

        supplier = new User();
        supplier.setId(2L);
        supplier.setName("张三");
        supplier.setNickName("zhangsan");
        supplier.setType(User.Type.SUPPLIER.value());

        purchaser = new User();
        purchaser.setId(3L);
        purchaser.setName("李四");
        purchaser.setNickName("lisi");
        purchaser.setType(User.Type.PURCHASER.value());
    }

    @Before
    public void init() {
        // NOTE: necessary
        MockitoAnnotations.initMocks(this);
    }

    protected User mockPurchaser(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.PURCHASER.value());
        u.setEncryptedPassword("eeeen");
        u.setMobile("123");
        u.setEmail("12312@qq.com");
        return u;
    }

    protected User mockSupplier(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.SUPPLIER.value());
        u.setTags("xx标签");
        u.setEncryptedPassword("eeeen");
        u.setMobile("123");
        u.setEmail("12312@qq.com");
        return u;
    }

    protected User mockAdmin(long id) {
        User u = new User();
        u.setId(id);
        u.setNick("xxx");
        u.setType(User.Type.ADMIN.value());
        u.setEncryptedPassword("eeeen");
        u.setMobile("123");
        u.setEmail("12312@qq.com");
        return u;
    }

    protected PurchaserDto mockPurchaserDto() {
        PurchaserDto pd = new PurchaserDto();
        pd.setUser(mockPurchaser(1L));
        pd.setPurchaserExtra(mockPurchaserExtra());
        return pd;
    }

    protected PurchaserExtra mockPurchaserExtra() {
        PurchaserExtra purchaserExtra = new PurchaserExtra();
        purchaserExtra.setUserId(1L);
        purchaserExtra.setEmployeeId("235353");
        purchaserExtra.setDepartment("财务部");
        purchaserExtra.setLeader("刘德华");
        purchaserExtra.setPosition("经理");
        return purchaserExtra;
    }

    protected Response<FrontendCategory> mockFrontendCategoryResp() {
        Response<FrontendCategory> resp = new Response<FrontendCategory>();
        resp.setResult(mockFrontendCategory());
        return resp;
    }

    protected FrontendCategory mockFrontendCategory() {
        FrontendCategory fc = new FrontendCategory();
        fc.setId(1L);
        fc.setName("xx");
        return fc;
    }

    protected MainBusinessApprover mockMainBussinessApprover() {
        MainBusinessApprover mainBusinessApprover = new MainBusinessApprover();
        mainBusinessApprover.setLeaderId("232");
        mainBusinessApprover.setLeaderName("rer");
        mainBusinessApprover.setMemberId("545");
        mainBusinessApprover.setMemberName("454");
        mainBusinessApprover.setMainBusinessId(3L);
        mainBusinessApprover.setMainBusinessName("dg");
        return mainBusinessApprover;
    }

    protected SupplierImportDto mockSupplierImportDto() {
        SupplierImportDto sid = new SupplierImportDto();
        User supplier = mockSupplier(1L);
        sid.setUser(supplier);
        Company mockCompany = mockCompany(supplier);
        sid.setCompany(mockCompany);
        sid.setContactInfo(mockContackInfo(supplier, mockCompany));
        sid.setCompanyRank(mockCompanyRank());
        return sid;
    }

    protected SupplierTQRDCInfo mockSupplierTQRDCInfo() {
        SupplierTQRDCInfo supplierTQRDCInfo = new SupplierTQRDCInfo();
        supplierTQRDCInfo.setCompositeScore(89);
        supplierTQRDCInfo.setCostScore(45);
        supplierTQRDCInfo.setDelayDays(67);
        supplierTQRDCInfo.setDeliverDiff(66);
        supplierTQRDCInfo.setIncrement(67);
        supplierTQRDCInfo.setLiveBad(67);
        supplierTQRDCInfo.setMarketBad(45);
        supplierTQRDCInfo.setDeliverScore(56);
        supplierTQRDCInfo.setMonth("2014-04");
        supplierTQRDCInfo.setNewProductPass(89);
        supplierTQRDCInfo.setSupplierName("海尔");
        supplierTQRDCInfo.setQualityScore(34);
        supplierTQRDCInfo.setSupplierCode("d434");
        supplierTQRDCInfo.setTechScore(89);
        supplierTQRDCInfo.setRequirementResp(54);
        supplierTQRDCInfo.setRespScore(99);
        supplierTQRDCInfo.setModule("aa");
        supplierTQRDCInfo.setProductLineId(1);
        supplierTQRDCInfo.setLocation("黄区");
        supplierTQRDCInfo.setRank(3);
        supplierTQRDCInfo.setUserId(1L);
        supplierTQRDCInfo.setCompanyId(2L);
        return supplierTQRDCInfo;
    }

    protected CompanyRank mockCompanyRank() {
        CompanyRank companyRank = new CompanyRank();
        companyRank.setUserId(1L);
        companyRank.setCompanyId(1L);
        companyRank.setInRank(10);
        companyRank.setInRankFile("/file/ds32.doc");
        companyRank.setInRankFileName("aa.doc");
        companyRank.setInRankOrg("国家商务部");
        companyRank.setOutRank(17);
        companyRank.setOutRankFile("/file/dff.doc");
        companyRank.setOutRankFileName("bb.doc");
        companyRank.setInRankOrg("国际商务部");
        return companyRank;
    }

    protected FinanceDto mockFinanceDto() {
        FinanceDto financeDto = new FinanceDto();
        financeDto.setFinance(mockFinance());
        return financeDto;
    }

    protected Finance mockFinance() {
        Finance finance = new Finance();
        finance.setCompanyId(1L);
        finance.setUserId(1L);
        finance.setCountry(1);
        finance.setBankAccount("张三");
        finance.setBankCode("344dfd");
        finance.setCoinType(Finance.CoinType.EUR.value());
        finance.setOpeningBank("农行");
        finance.setRecentFinance("wetdg");
        finance.setOpenLicense("www.upanyun.com/img/1.jpg");
        return finance;
    }

    protected CompanyDto mockCompanyDto() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCompany(mockCompany(loginer));
        companyDto.setCompanyMainBusinesses(Arrays.asList(mockMainBussiess(loginer.getId())));
        companyDto.setCompanySupplyParks(Arrays.asList(mockCompanySupplierPark(loginer.getId())));
        return companyDto;
    }

    protected RichSupplierDto mockRichSupplierDto(Long id) {
        RichSupplierDto richSupplierDto = new RichSupplierDto();
        Company mockCompany = mockCompany(loginer);
        ContactInfo mockContactInfo = mockContackInfo(loginer, mockCompany);
        List<CompanyMainBusiness> mainBusinesses = Arrays.asList(mockMainBussiess(loginer.getId()));
        List<CompanySupplyPark> companySupplyParks = Arrays.asList(mockCompanySupplierPark(1L));

        richSupplierDto.setUser(mockSupplier(2L));
        richSupplierDto.setCompany(mockCompany);
        richSupplierDto.setCompanyMainBusinesses(mainBusinesses);
        richSupplierDto.setCompanySupplyParks(companySupplyParks);
        richSupplierDto.setContactInfo(mockContactInfo);
        return richSupplierDto;
    }

    protected CompanySupplyPark mockCompanySupplierPark(Long id) {
        CompanySupplyPark csp = new CompanySupplyPark();
        csp.setCompanyId(id);
        csp.setName("xx");
        csp.setSupplyParkId(1L);
        return csp;
    }

    protected Company mockCompany(BaseUser user) {
        Company company = new Company();
        company.setUserId(user.getId());
        company.setActingBrand("xxoo");
        company.setCorporation("xxx公司");
        company.setInitAgent("xxxx");
        company.setDesc("yyyy");
        company.setRegCountry(1);
        company.setActingBrand("oooo");
        company.setBlDate(new Date());
        company.setZipcode("xxx");
        company.setWorldTop(111);
        company.setCustomers("yours");
        return company;
    }

    protected ContactInfo mockContackInfo(BaseUser user, Company company) {
        ContactInfo info = new ContactInfo();
        info.setCompanyId(company.getId());
        info.setUserId(user.getId());
        info.setMobile("1231123123");
        info.setEmail("11111@xx.com");
        return info;
    }

    protected CompanyMainBusiness mockMainBussiess(Long id) {
        CompanyMainBusiness cmb = new CompanyMainBusiness();
        cmb.setId(id);
        cmb.setUserId(id);
        cmb.setCompanyId(id);
        cmb.setMainBusinessId(id);
        cmb.setName("xx主营业务");
        return cmb;
    }

    protected SupplierCountByStatus mockSupplierCountByStatus() {
        SupplierCountByStatus supplierCountByStatus = new SupplierCountByStatus();
        supplierCountByStatus.setDate(new Date());
        supplierCountByStatus.setStatus(1);
        supplierCountByStatus.setSupplierCount(40L);
        return supplierCountByStatus;
    }

    protected SupplierCountByLevel mockSupplierCountByLevel() {
        SupplierCountByLevel supplierCountByLevel = new SupplierCountByLevel();
        supplierCountByLevel.setMonth("2014-04");
        supplierCountByLevel.setLevel(1);
        supplierCountByLevel.setSupplierCount(40L);
        return supplierCountByLevel;
    }

    protected SupplierCountByIndustry mockSupplierCountByIndustry() {
        SupplierCountByIndustry supplierCountByIndustry = new SupplierCountByIndustry();
        supplierCountByIndustry.setDate(new Date());
        supplierCountByIndustry.setIndustry(1L);
        supplierCountByIndustry.setSupplierCount(40L);
        return supplierCountByIndustry;
    }

    protected BlackList mockBlackList() {
        BlackList blackList = new BlackList();
        blackList.setName("jack");
        blackList.setKeywords("ja");
        blackList.setInitAgent("xx");
        return blackList;
    }

    protected SupplierInfoChangedDto mockSupplierChangedInfoDto() {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();
        supplierInfoChangedDto.setChanged(true);
        Map<String, String> changedInfo = Maps.newHashMap();
        supplierInfoChangedDto.setChangedInfo(changedInfo);
        return supplierInfoChangedDto;
    }

}
