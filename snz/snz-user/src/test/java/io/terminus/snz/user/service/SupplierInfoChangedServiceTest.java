package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dao.redis.SupplierChangedInfoRedisDao;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.dto.PaperworkDto;
import io.terminus.snz.user.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-15.
 */
public class SupplierInfoChangedServiceTest extends BaseServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Mock
    private SupplierChangedInfoRedisDao supplierChangedInfoRedisDao;

    @InjectMocks
    private SupplierInfoChangedServiceImpl supplierInfoChangedServiceImpl;

    @Test
    public void testNeedCheckChange() {
        supplierInfoChangedServiceImpl.needCheckChanged(User.ApproveStatus.INIT.value());
    }

    @Test
    public void testCompanyBaseInfoChanged() {
        when(supplierCreditQualifyService.findCreditQualifyByUserId(anyLong())).thenReturn(mockCreditQualifyResult());
        supplierInfoChangedServiceImpl.baseCompanyChanged(loginer.getId(), getCompanyDto(), new Company(), getCompanyMainUsinesses(), getCompanySupplyParks());
    }

    @Test
    public void testGetNewBaseCompany() {
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(false);
        supplierInfoChangedServiceImpl.getNewBaseCompany(loginer.getId(), getCompany(), getCompanyMainUsinesses(), getCompanySupplyParks());

        Map<String, String> changedInfo = Maps.newHashMap();
        when(supplierCreditQualifyService.findCreditQualifyByUserId(anyLong())).thenReturn(mockCreditQualifyResult());
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        when(supplierChangedInfoRedisDao.getChangedInfos(anyLong())).thenReturn(changedInfo);
        supplierInfoChangedServiceImpl.getNewBaseCompany(loginer.getId(), getCompany(), getCompanyMainUsinesses(), getCompanySupplyParks());

    }

    @Test
    public void testPaperworkChanged() {
        PaperworkDto paperworkDto = new PaperworkDto();
        BeanMapper.copy(getCompany(), paperworkDto);
        supplierInfoChangedServiceImpl.paperworkChanged(loginer.getId(), paperworkDto, getCompany());

        getCompany().setRegCountry(15);
        supplierInfoChangedServiceImpl.paperworkChanged(loginer.getId(), paperworkDto, getCompany());

        getCompany().setRegCountry(6);
        supplierInfoChangedServiceImpl.paperworkChanged(loginer.getId(), paperworkDto, getCompany());

        getCompany().setRegCountry(2);
        supplierInfoChangedServiceImpl.paperworkChanged(loginer.getId(), paperworkDto, getCompany());

    }

    @Test
    public void testGetNewPaperwork() {
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(false);
        supplierInfoChangedServiceImpl.getNewPaperwork(loginer.getId(), getCompany());

        Map<String, String> changedInfo = Maps.newHashMap();
        when(supplierChangedInfoRedisDao.getChangedInfos(anyLong())).thenReturn(changedInfo);
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        supplierInfoChangedServiceImpl.getNewPaperwork(loginer.getId(), getCompany());
    }

    @Test
    public void testContactInfoChanged() {
        when(companyDao.findByUserId(anyLong())).thenReturn(getCompany());
        supplierInfoChangedServiceImpl.contactInfoChanged(loginer.getId(), getContactInfo(), new ContactInfo());
    }

    @Test
    public void testGetNewContactInfo() {
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(false);
        supplierInfoChangedServiceImpl.getNewContactInfo(loginer.getId(), getContactInfo());

        Map<String, String> changedInfo = Maps.newHashMap();
        when(supplierChangedInfoRedisDao.getChangedInfos(anyLong())).thenReturn(changedInfo);
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        supplierInfoChangedServiceImpl.getNewContactInfo(loginer.getId(), getContactInfo());
    }

    @Test
    public void testQualityChanged() {
        supplierInfoChangedServiceImpl.qualityChanged(loginer.getId(), mockQuality(), new CompanyExtraQuality());
    }

    @Test
    public void testGetNewCompanyExtraQuality() {
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(false);
        supplierInfoChangedServiceImpl.getNewCompanyExtraQuality(loginer.getId(), mockQuality());

        Map<String, String> changedInfo = Maps.newHashMap();
        when(supplierChangedInfoRedisDao.getChangedInfos(anyLong())).thenReturn(changedInfo);
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        supplierInfoChangedServiceImpl.getNewCompanyExtraQuality(loginer.getId(), mockQuality());
    }

    @Test
    public void testFinanceChanged() {
        supplierInfoChangedServiceImpl.financeChanged(loginer.getId(), mockFinance(), new Finance());
    }

    @Test
    public void testGetNewFinance() {
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(false);
        supplierInfoChangedServiceImpl.getNewFinance(loginer.getId(), mockFinance());

        Map<String, String> changedInfo = Maps.newHashMap();
        when(supplierChangedInfoRedisDao.getChangedInfos(anyLong())).thenReturn(changedInfo);
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        supplierInfoChangedServiceImpl.getNewFinance(loginer.getId(), mockFinance());
    }

    @Test
    public void testUpdateModifyInfoWaitingForApprove() {
        supplierInfoChangedServiceImpl.updateModifyInfoWaitingForApprove(loginer.getId(), User.ApproveStatus.OK.value());
    }

    @Test
    public void testCheckSupplierInfoChanged() {
        Assert.assertFalse(supplierInfoChangedServiceImpl.checkSupplierInfoChanged(null).isSuccess());
        when(supplierChangedInfoRedisDao.tabInfoChanged(anyLong(), anyString())).thenReturn(true);
        Assert.assertTrue(supplierInfoChangedServiceImpl.checkSupplierInfoChanged(loginer.getId()).isSuccess());
    }

    private Company getCompany() {
        Company company = new Company();
        company.setId(1L);
        company.setUserId(loginer.getId());
        company.setRegCountry(1);
        company.setCorporation("阿里巴巴");
        company.setCustomers("{}");
        company.setInitAgent("gg");
        company.setCorpAddr("杭州");
        company.setDesc("hello world");

        company.setBusinessLicense("/img/bl.jpg");
        company.setBusinessLicenseId("36434");
        company.setBlDate(new Date());

        company.setOrgCert("/img/oc.jpg");
        company.setOrgCertId("63343");
        company.setOcDate(new Date());

        company.setTaxNo("/img/tn.jpg");
        company.setTnDate(new Date());
        company.setTaxNoId("84545");

        return company;
    }

    private List<CompanyMainBusiness> getCompanyMainUsinesses() {
        List<CompanyMainBusiness> companyMainBusinesses = Lists.newArrayList();
        CompanyMainBusiness companyMainBusiness = new CompanyMainBusiness();
        companyMainBusiness.setFirstLevelId(2L);
        companyMainBusiness.setMainBusinessId(1L);
        companyMainBusiness.setCompanyId(1L);
        companyMainBusiness.setUserId(loginer.getId());
        companyMainBusiness.setName("冰箱");

        companyMainBusinesses.add(companyMainBusiness);
        return companyMainBusinesses;

    }

    private List<CompanySupplyPark> getCompanySupplyParks() {
        List<CompanySupplyPark> companySupplyParks = Lists.newArrayList();
        CompanySupplyPark companySupplyPark = new CompanySupplyPark();
        companySupplyPark.setSupplyParkId(1L);
        companySupplyPark.setCompanyId(1L);
        companySupplyPark.setUserId(loginer.getId());
        companySupplyPark.setName("青岛");
        companySupplyParks.add(companySupplyPark);
        return companySupplyParks;
    }

    private User getUser() {
        User user = new User();
        user.setOrigin(User.Origin.NORMAL.value());
        user.setEmail("8123@qq.com");
        user.setEncryptedPassword("oewfdf543646");
        user.setMobile("18969973056");
        user.setPhone("9110");
        user.setName("jack");
        user.setNick("hello");
        user.setRoleStr("supplier_init");
        user.setType(User.Type.SUPPLIER.value());
        user.setStatus(User.Status.OK.value());
        user.setApproveStatus(User.ApproveStatus.ENTER_WAITING_FOR_APPROVE.value());
        user.setRefuseStatus(User.RefuseStatus.NOT_REFUSED.value());
        user.setTags("500强");
        user.setQualifyStatus(User.QualifyStatus.QUALIFIED.value());

        return user;
    }

    private ContactInfo getContactInfo() {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setName("baby");
        contactInfo.setMobile("18969973090");
        contactInfo.setCompanyId(1L);
        contactInfo.setUserId(loginer.getId());
        contactInfo.setDepartment("development");
        contactInfo.setDuty("leader");
        contactInfo.setEmail("8g2@163.com");
        contactInfo.setOfficePhone("99120");
        return contactInfo;
    }

    private CompanyDto getCompanyDto() {
        CompanyDto companyDto = new CompanyDto();

        companyDto.setCompany(getCompany());
        companyDto.setCompanyMainBusinesses(getCompanyMainUsinesses());
        companyDto.setCompanySupplyParks(getCompanySupplyParks());

        return companyDto;
    }

    private Response<Boolean> mockQualityResult() {
        Response<Boolean> result = new Response<Boolean>();
        result.setResult(Boolean.TRUE);
        return result;
    }

    private Response<SupplierCreditQualify> mockCreditQualifyResult() {
        Response<SupplierCreditQualify> result = new Response<SupplierCreditQualify>();
        SupplierCreditQualify supplierCreditQualify = new SupplierCreditQualify();
        supplierCreditQualify.setStatus(SupplierCreditQualify.STATUS.A.toValue());
        result.setResult(supplierCreditQualify);
        return result;
    }

    private CompanyExtraQuality mockQuality() {
        CompanyExtraQuality qua = new CompanyExtraQuality();
        qua.setUserId(1L);
        qua.setTs16949ValidDate(new Date());
        qua.setTs16949AttachUrl("dfs");
        qua.setTs16949Id("fgfh");
        qua.setIso14001ValidDate(new Date());
        qua.setIso14001AttachUrl("fgh");
        qua.setIso9001Id("ee");
        qua.setIso9001ValidDate(new Date());
        qua.setIso9001AttachUrl("fgh");
        qua.setIso14001Id("ee");
        qua.setRohsValidDate(new Date());
        qua.setRohsAttachUrl("fgh");
        qua.setRohsId("ee");

        return qua;
    }

    private Finance mockFinance() {
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

}
