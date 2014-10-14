package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.SupplierChangedInfoRedisDao;
import io.terminus.snz.user.dto.SupplierImportDto;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.ExcelTransform;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-3.
 */
public class DataImportServiceTest extends BaseServiceTest {

    private static final String EXCEL_PATH = "/usr/file/test.xsl";

    @Mock
    private ExcelTransform excelTransform;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private UserDao userDao;

    @Mock
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Mock
    private FinanceDao financeDao;

    @Mock
    private AdditionalDocDao additionalDocDao;

    @Mock
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Mock
    private ContactInfoDao contactInfoDao;

    @Mock
    private CompanyRankDao companyRankDao;

    @Mock
    private CompanySupplyParkDao companySupplyParkDao;

    @Mock
    private SupplierIndexService supplierIndexService;

    @Mock
    private PurchaserExtraDao purchaserExtraDao;

    @Mock
    private TagService tagService;

    @Mock
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Mock
    private FrontendCategoryService frontendCategoryService;

    @Mock
    private SupplierApproveLogDao supplierApproveExtraDao;

    @Mock
    private SupplierChangedInfoRedisDao supplierRedisDao;

    @Mock
    private AccountManager accountManager;

    @InjectMocks
    private DataImportServiceImpl dataImportServiceImpl;

    @Test
    public void testBulkImportSupplierTQRDCInfo() {
        Assert.assertTrue(!dataImportServiceImpl.bulkImportSupplierTQRDCInfo("").isSuccess());
        when(excelTransform.getSupplierTQRDCInfo(EXCEL_PATH)).thenReturn(null);
        Assert.assertTrue(!dataImportServiceImpl.bulkImportSupplierTQRDCInfo(EXCEL_PATH).isSuccess());
        when(excelTransform.getSupplierTQRDCInfo(EXCEL_PATH)).thenReturn(getSupplierTQRDCInfos());
        Assert.assertTrue(dataImportServiceImpl.bulkImportSupplierTQRDCInfo(EXCEL_PATH).isSuccess());
    }

    @Test
    public void testBulkImportSupplier() {
        Assert.assertTrue(!dataImportServiceImpl.bulkImportSupplier("").isSuccess());
        when(excelTransform.getSupplier(EXCEL_PATH)).thenReturn(null);
        Assert.assertTrue(!dataImportServiceImpl.bulkImportSupplier(EXCEL_PATH).isSuccess());
        when(excelTransform.getSupplier(EXCEL_PATH)).thenReturn(getSupplierImportDtos());
        Assert.assertTrue(dataImportServiceImpl.bulkImportSupplier(EXCEL_PATH).isSuccess());
    }

    @Test
    public void testBulkImportMainBusinessApprover() {
        Assert.assertTrue(!dataImportServiceImpl.bulkImportMainBusinessApprover("").isSuccess());
        when(excelTransform.getMainBusinessApprover(EXCEL_PATH)).thenReturn(null);
        Assert.assertTrue(!dataImportServiceImpl.bulkImportMainBusinessApprover(EXCEL_PATH).isSuccess());
        when(excelTransform.getMainBusinessApprover(EXCEL_PATH)).thenReturn(getApprovers());
        when(frontendCategoryService.findFrontendCategoryByLevelAndName(anyInt(), anyString())).thenReturn(getFrontendCategory());
        Assert.assertTrue(dataImportServiceImpl.bulkImportMainBusinessApprover(EXCEL_PATH).isSuccess());
    }

    private List<SupplierTQRDCInfo> getSupplierTQRDCInfos() {
        List<SupplierTQRDCInfo> supplierTQRDCInfos = Lists.newArrayList();

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

        supplierTQRDCInfos.add(supplierTQRDCInfo);

        return supplierTQRDCInfos;

    }

    private List<SupplierImportDto> getSupplierImportDtos() {
        List<SupplierImportDto> supplierImportDtos = Lists.newArrayList();
        SupplierImportDto supplierImportDto = new SupplierImportDto();

        User user = new User();
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("h13@qq.com");
        user.setEncryptedPassword("mmewfdf543646");
        user.setMobile("18969973054");
        user.setPhone("66110");
        user.setName("fjack");
        user.setNick("mm");

        Company company = new Company();
        company.setIsComplete(0);
        company.setIncludeKeywords("大华");
        company.setProductLine("1,2");
        company.setBusinessLicense("/img/l123.jpg");
        company.setBlDate(new Date());
        company.setCorporation("娃哈哈");
        company.setCorpAddr("杭州");
        company.setGroupName("哈哈");
        company.setCorpAddr("北京");
        company.setDesc("公司很强");
        company.setFixedAssets(450000L);
        company.setFaCoinType(Company.CoinType.GBP.value());
        company.setFoundAt(new Date());
        company.setInitAgent("端点");
        company.setNature(1);

        CompanyRank companyRank = new CompanyRank();
        companyRank.setInRank(12);
        companyRank.setInRankFile("/file/ipm.doc");
        companyRank.setInRankFileName("ipm.doc");
        companyRank.setInRankOrg("国家商务部");
        companyRank.setOutRank(18);
        companyRank.setOutRankFile("/file/opm.doc");
        companyRank.setOutRankFileName("opm.doc");
        companyRank.setInRankOrg("国际商务部");

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("rose");
        contactInfo.setMobile("18969973058");
        contactInfo.setDepartment("development");
        contactInfo.setDuty("leader");
        contactInfo.setEmail("gg2@163.com");
        contactInfo.setOfficePhone("11120");

        supplierImportDto.setUser(user);
        supplierImportDto.setCompany(company);
        supplierImportDto.setCompanyRank(companyRank);
        supplierImportDto.setContactInfo(contactInfo);

        supplierImportDtos.add(supplierImportDto);

        return supplierImportDtos;

    }

    private List<MainBusinessApprover> getApprovers() {
        List<MainBusinessApprover> mainBusinessApprovers = Lists.newArrayList();
        MainBusinessApprover mainBusinessApprover = new MainBusinessApprover();
        mainBusinessApprover.setLeaderId("v232");
        mainBusinessApprover.setLeaderName("jack");
        mainBusinessApprover.setMemberId("v7yd");
        mainBusinessApprover.setMemberName("anson");
        mainBusinessApprover.setMainBusinessId(3L);
        mainBusinessApprover.setMainBusinessName("冰箱");

        mainBusinessApprovers.add(mainBusinessApprover);

        return mainBusinessApprovers;
    }

    private Response<FrontendCategory> getFrontendCategory() {
        Response<FrontendCategory> result = new Response<FrontendCategory>();
        FrontendCategory fc = new FrontendCategory();
        fc.setId(11L);
        fc.setName("空调");

        result.setResult(fc);

        return result;

    }

}
