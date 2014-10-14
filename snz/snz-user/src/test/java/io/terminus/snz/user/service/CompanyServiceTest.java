package io.terminus.snz.user.service;

import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.*;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.manager.AccountManager;
import io.terminus.snz.user.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-3.
 */
public class CompanyServiceTest extends BaseServiceTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private CompanyRankDao companyRankDao;

    @Mock
    private ContactInfoDao contactInfoDao;

    @Mock
    private FinanceDao financeDao;

    @Mock
    private AdditionalDocDao additionalDocDao;

    @Mock
    private UserDao userDao;

    @Mock
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Mock
    private AccountManager accountManager;

    @Mock
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Mock
    private CompanyExtraRDDao companyExtraRDDao;

    @Mock
    private CompanyExtraQualityDao companyExtraQualityDao;

    @Mock
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Mock
    private CompanyExtraDeliveryDao companyExtraDeliveryDao;

    @Mock
    private CompanyExtraScaleAndCostDao companyExtraScaleAndCostDao;

    @Mock
    private ProductLineDao productLineDao;

    @Mock
    private CompanySupplyParkDao companySupplyParkDao;

    @Mock
    private Validator validator;

    @Mock
    private SupplierApproveLogDao supplierApproveExtraDao;

    @Mock
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Mock
    private MainBusinessApproverDao mainBusinessApproverDao;

    @Mock
    private SupplierChangedInfoRedisDao supplierChangedInfoRedisDao;

    @Mock
    private LoadingCache<Long, Optional<Company>> companyCache;

    @Mock
    private SupplierInfoChangedServiceImpl supplierInfoChangedService;

    @Mock
    private CompanyMainBusinessTmpDao companyMainBusinessTmpDao;

    @Mock
    private CompanyRedisDao companyRedisDao;

    @Mock
    private PaperworkRedisDao paperworkRedisDao;

    @Mock
    private ContactInfoRedisDao contactInfoRedisDao;

    @Mock
    private CompanyRankRedisDao companyRankRedisDao;

    @Mock
    private FinanceRedisDao financeRedisDao;

    @InjectMocks
    private CompanyServiceImpl companyServiceImpl;

    @Test
    public void testUpdateCompany() {
        User user = getUser();
        user.setApproveStatus(User.ApproveStatus.OK.value());
        Assert.assertTrue(!companyServiceImpl.updateCompany(getNullIdBaseUser(), getCompanyDto()).isSuccess());
        when(companyCache.getUnchecked(anyLong())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.updateCompany(loginer, getCompanyDto()).isSuccess());
        when(companyCache.getUnchecked(anyLong())).thenReturn(getChachedCompany());
        when(userDao.findById(anyLong())).thenReturn(user);
        when(companyMainBusinessDao.findByUserId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(companySupplyParkDao.findByUserId(anyLong())).thenReturn(getCompanySupplyParks());

        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);

        SupplierCreditQualify supplierCreditQualify = new SupplierCreditQualify();
        Response<SupplierCreditQualify> rp = new Response<SupplierCreditQualify>();
        rp.setResult(supplierCreditQualify);
        when(supplierCreditQualifyService.findCreditQualifyByUserId(anyLong())).thenReturn(rp);
        //when(supplierInfoChangedService.companyBaseInfoChanged(any(CompanyDto.class), any(Company.class), anyListOf(CompanyMainBusiness.class), anyListOf(CompanySupplyPark.class))).thenReturn(getSupplierInfoChangeDto(1));
        Assert.assertTrue(companyServiceImpl.updateCompany(loginer, getCompanyDto()).isSuccess());
    }

    @Test
    public void testUpdatePaperwork() {
        Assert.assertTrue(!companyServiceImpl.updatePaperwork(loginer, null).isSuccess());

        cachedCompanyNotFound();
        Assert.assertTrue(!companyServiceImpl.updatePaperwork(loginer, getPaperworkDto()).isSuccess());

        when(companyCache.getUnchecked(loginer.getId())).thenReturn(getChachedCompany());

        Assert.assertTrue(!companyServiceImpl.updatePaperwork(getNullIdBaseUser(), getPaperworkDto()).isSuccess());

        Assert.assertTrue(companyServiceImpl.updatePaperwork(loginer, getPaperworkDto()).isSuccess());
    }

    @Test
    public void testFindBaseCompanyByUserId() {
        Assert.assertTrue(!companyServiceImpl.findBaseCompanyByUserId(getNullIdBaseUser()).isSuccess());

        when(companyRedisDao.getBaseCompanyInfo(anyLong())).thenReturn(getCompanyDto());
        Assert.assertTrue(companyServiceImpl.findBaseCompanyByUserId(loginer).isSuccess());

        when(companyRedisDao.getBaseCompanyInfo(anyLong())).thenReturn(null);
        cachedCompanyNotFound();
        Assert.assertFalse(companyServiceImpl.findBaseCompanyByUserId(loginer).isSuccess());

        when(companyCache.getUnchecked(loginer.getId())).thenReturn(getChachedCompany());
        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(companySupplyParkDao.findByCompanyId(anyLong())).thenReturn(getCompanySupplyParks());
        when(supplierInfoChangedService.getNewBaseCompany(anyLong(), any(Company.class), anyListOf(CompanyMainBusiness.class), anyListOf(CompanySupplyPark.class))).thenReturn(getSupplierUpdatedInfoDto(getCompanyDto()));
        Assert.assertTrue(companyServiceImpl.findBaseCompanyByUserId(loginer).isSuccess());
    }

    @Test
    public void testFindCompanyById() {
        Assert.assertTrue(!companyServiceImpl.findCompanyById(null).isSuccess());

        when(companyDao.findById(anyLong())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.findCompanyById(1L).isSuccess());

        when(companyDao.findById(anyLong())).thenReturn(getChachedCompany().get());
        Assert.assertTrue(companyServiceImpl.findCompanyById(1L).isSuccess());
    }

    @Test
    public void testFindCompanyByUserId() {
        Assert.assertTrue(!companyServiceImpl.findCompanyByUserId(null).isSuccess());

        cachedCompanyNotFound();
        Assert.assertTrue(!companyServiceImpl.findCompanyByUserId(loginer.getId()).isSuccess());

        when(companyCache.getUnchecked(loginer.getId())).thenReturn(getChachedCompany());
        Assert.assertTrue(companyServiceImpl.findCompanyByUserId(loginer.getId()).isSuccess());
    }

    @Test
    public void testFindCompanyMainBusiness() {
        Assert.assertTrue(!companyServiceImpl.findCompanyMainBusiness(null).isSuccess());

        when(companyMainBusinessDao.findByMainBusinessId(anyLong())).thenReturn(getCompanyMainUsinesses());
        Assert.assertTrue(companyServiceImpl.findCompanyMainBusiness(anyLong()).isSuccess());
    }

    @Test
    public void testFindCompanyIdsByMainBusinessIds() {
        when(companyMainBusinessDao.findCompanyIdsByMainBusinessIds(anyList())).thenReturn(getCompanyMainUsinesses());
        Assert.assertTrue(companyServiceImpl.findCompanyIdsByMainBusinessIds(Lists.newArrayList(1L, 2L)).isSuccess());
    }

    @Test
    public void testFindDetailSupplierByUserId() {
        Assert.assertTrue(!companyServiceImpl.findDetailSupplierByUserId(null).isSuccess());

        when(userDao.findById(loginer.getId())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.findDetailSupplierByUserId(loginer.getId()).isSuccess());

        when(companyDao.findByUserId(loginer.getId())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.findDetailSupplierByUserId(loginer.getId()).isSuccess());

        when(companyMainBusinessDao.findByUserId(loginer.getId())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.findDetailSupplierByUserId(loginer.getId()).isSuccess());

        when(userDao.findById(loginer.getId())).thenReturn(getUser());
        when(companyDao.findByUserId(loginer.getId())).thenReturn(getChachedCompany().get());
        when(companyMainBusinessDao.findByUserId(loginer.getId())).thenReturn(getCompanyMainUsinesses());
        when(companySupplyParkDao.findByUserId(loginer.getId())).thenReturn(getCompanySupplyParks());

        SupplierUpdatedInfoDto<CompanyDto> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<CompanyDto>();
        supplierUpdatedInfoDto.setSupplierInfo(getCompanyDto());

        SupplierUpdatedInfoDto<PaperworkDto> paperworkUpdatedInfoDto = new SupplierUpdatedInfoDto<PaperworkDto>();
        paperworkUpdatedInfoDto.setSupplierInfo(new PaperworkDto());

        when(supplierInfoChangedService.getNewPaperwork(anyLong(), any(Company.class))).thenReturn(paperworkUpdatedInfoDto);
        when(supplierInfoChangedService.getNewBaseCompany(anyLong(), any(Company.class), anyListOf(CompanyMainBusiness.class), anyListOf(CompanySupplyPark.class))).thenReturn(supplierUpdatedInfoDto);
        Assert.assertTrue(companyServiceImpl.findDetailSupplierByUserId(loginer.getId()).isSuccess());
    }

    @Test
    public void testFindCompaniesByIds() {
        Assert.assertTrue(!companyServiceImpl.findCompaniesByIds(null).isSuccess());

        when(companyDao.findByIds(anyList())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.findCompaniesByIds(Lists.newArrayList(1L, 2L)).isSuccess());

        when(companyDao.findByIds(anyList())).thenReturn(Lists.newArrayList(getChachedCompany().get()));
        Assert.assertTrue(companyServiceImpl.findCompaniesByIds(Lists.newArrayList(1L, 2L)).isSuccess());
    }

    @Test
    public void testUpdateParticipateCount() {
        Assert.assertTrue(!companyServiceImpl.updateParticipateCount(null).isSuccess());

        Assert.assertTrue(companyServiceImpl.updateParticipateCount(1L).isSuccess());
    }

    @Test
    public void testCreateOrUpdateCompanyRank() {
        Assert.assertFalse(companyServiceImpl.createOrUpdateCompanyRank(getNullIdBaseUser(), getCompanyRank()).isSuccess());

        Assert.assertFalse(companyServiceImpl.createOrUpdateCompanyRank(loginer, null).isSuccess());

        Assert.assertTrue(companyServiceImpl.createOrUpdateCompanyRank(loginer, getCompanyRank()).isSuccess());
    }

    @Test
    public void testFindCompanyRankByUserId() {
        Assert.assertFalse(companyServiceImpl.findCompanyRankByUserId(getNullIdBaseUser()).isSuccess());

        when(companyRankDao.findByUserId(loginer.getId())).thenReturn(getCompanyRank());
        Assert.assertTrue(companyServiceImpl.findCompanyRankByUserId(loginer).isSuccess());
    }

    @Test
    public void testCreateOrUpdateContactInfo() {
        Assert.assertTrue(!companyServiceImpl.createOrUpdateContactInfo(getNullIdBaseUser(), getContactInfo()).isSuccess());

        Assert.assertTrue(!companyServiceImpl.createOrUpdateContactInfo(loginer, null).isSuccess());

        when(contactInfoDao.findByUserId(loginer.getId())).thenReturn(getContactInfo());
        when(userDao.findById(loginer.getId())).thenReturn(getUser());
        Assert.assertTrue(companyServiceImpl.createOrUpdateContactInfo(loginer, getContactInfo()).isSuccess());
    }

    @Test
    public void testFindContactInfoByUserId() {
        Assert.assertFalse(companyServiceImpl.findContactInfoByUserId(getNullIdBaseUser()).isSuccess());

        when(contactInfoRedisDao.findByKey(anyString())).thenReturn(getContactInfo());
        Assert.assertTrue(companyServiceImpl.findContactInfoByUserId(loginer).isSuccess());

        when(contactInfoRedisDao.findByKey(anyString())).thenReturn(null);
        when(contactInfoDao.findByUserId(loginer.getId())).thenReturn(null);
        Assert.assertFalse(companyServiceImpl.findContactInfoByUserId(loginer).isSuccess());
    }

    @Test
    public void testCreateOrUpdateFinance() {
        Assert.assertTrue(!companyServiceImpl.createOrUpdateFinance(getNullIdBaseUser(), getFinanceDto()).isSuccess());

        FinanceDto financeDto = getFinanceDto();
        financeDto.getFinance().setOpenLicense("");
        Assert.assertTrue(!companyServiceImpl.createOrUpdateFinance(loginer, financeDto).isSuccess());

        financeDto.getFinance().setOpenLicense("/file/o.doc");
        financeDto.getFinance().setRecentFinance("");
        Assert.assertTrue(!companyServiceImpl.createOrUpdateFinance(loginer, financeDto).isSuccess());

        when(financeDao.findByUserId(loginer.getId())).thenReturn(getFinanceDto().getFinance());
        Assert.assertTrue(companyServiceImpl.createOrUpdateFinance(loginer, getFinanceDto()).isSuccess());
    }

    @Test
    public void testFindFinanceByUserId() {
        Assert.assertTrue(!companyServiceImpl.findFinanceByUserId(getNullIdBaseUser()).isSuccess());

        when(financeRedisDao.findByKey(anyString())).thenReturn(getFinanceDto().getFinance());
        Assert.assertTrue(companyServiceImpl.findFinanceByUserId(loginer).isSuccess());

        when(financeRedisDao.findByKey(anyString())).thenReturn(null);
        when(financeDao.findByUserId(loginer.getId())).thenReturn(null);
        Assert.assertTrue(companyServiceImpl.findFinanceByUserId(loginer).isSuccess());

    }

    @Test
    public void testDeleteAdditionalDoc() {
        Assert.assertTrue(!companyServiceImpl.deleteAdditionalDoc(null, loginer).isSuccess());

        Assert.assertTrue(!companyServiceImpl.deleteAdditionalDoc(1L, getNullIdBaseUser()).isSuccess());

        when(additionalDocDao.findById(anyLong())).thenReturn(null);
        Assert.assertTrue(!companyServiceImpl.deleteAdditionalDoc(1L, loginer).isSuccess());

        AdditionalDoc additionalDoc = getAdditionalDoc();
        additionalDoc.setUserId(2L);
        when(additionalDocDao.findById(anyLong())).thenReturn(additionalDoc);
        Assert.assertTrue(!companyServiceImpl.deleteAdditionalDoc(1L, loginer).isSuccess());

        when(additionalDocDao.findById(anyLong())).thenReturn(getAdditionalDoc());

        when(additionalDocDao.delete(anyLong())).thenReturn(false);
        Assert.assertTrue(!companyServiceImpl.deleteAdditionalDoc(1L, loginer).isSuccess());

        when(additionalDocDao.delete(anyLong())).thenReturn(true);
        Assert.assertTrue(companyServiceImpl.deleteAdditionalDoc(1L, loginer).isSuccess());
    }

    @Test
    public void testFindSupplierForMDMQualify() {
        loginer.setRoles(Lists.newArrayList(User.JobRole.RESOURCE.role(), User.JobRole.SHARING.role()));
        when(companyDao.findSuppliersForQualify(anyString(), anyInt(), anyInt(), anyInt())).thenReturn(new Paging<Company>(1L, Lists.newArrayList(getChachedCompany().get())));
        when(userDao.findById(anyLong())).thenReturn(getUser());
        when(contactInfoDao.findByUserId(anyLong())).thenReturn(getContactInfo());
        Assert.assertTrue(companyServiceImpl.findSupplierForMDMQualify(loginer, "test", 1, 20).isSuccess());
    }

    @Test
    public void testFindSupplierBy() {
        BaseUser baseUser = new BaseUser();
        baseUser.setNickName("23");
        baseUser.setId(1L);
        baseUser.setRoles(Lists.newArrayList(User.JobRole.RESOURCE.role()));
        when(mainBusinessApproverDao.findMainBusinessIdsByMemberIdOrLeaderId(loginer.getNickName())).thenReturn(Lists.newArrayList(1L, 2L));
        when(companyMainBusinessDao.findUserIdsByMainBusinessIds(anyList())).thenReturn(Lists.newArrayList(1L, 2L));
        when(userDao.findByApproveAndUserIds(anyInt(), anyInt(), anyString(), anyList(), anyInt(), anyInt())).thenReturn(new Paging<User>(1L, Lists.newArrayList(getUser())));
        when(companyDao.findByUserId(anyLong())).thenReturn(getChachedCompany().get());
        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(contactInfoDao.findByUserId(anyLong())).thenReturn(getContactInfo());
        Assert.assertTrue(companyServiceImpl.findSupplierBy(loginer, null, loginer.getNickName(), 1, 20).isSuccess());
        Assert.assertTrue(companyServiceImpl.findSupplierBy(loginer, 1, loginer.getNickName(), 1, 20).isSuccess());
        Assert.assertTrue(companyServiceImpl.findSupplierBy(loginer, 2, loginer.getNickName(), 1, 20).isSuccess());

    }

    @Test
    public void testFindSupplierQualifyBy() {
        when(userDao.findQualifyingBy(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(new Paging<User>(1L, Lists.newArrayList(getUser())));
        when(companyCache.getUnchecked(anyLong())).thenReturn(getChachedCompany());
        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(contactInfoDao.findByUserId(anyLong())).thenReturn(getContactInfo());
        Assert.assertTrue(companyServiceImpl.findSupplierQualifyBy(1, loginer.getNickName(), 1, 30).isSuccess());
    }

    @Test
    public void testCertificateExpired() {
        Assert.assertTrue(!companyServiceImpl.certificateExpired(getNullIdBaseUser()).isSuccess());

        cachedCompanyNotFound();
        Assert.assertTrue(!companyServiceImpl.certificateExpired(loginer).isSuccess());

        when(companyCache.getUnchecked(loginer.getId())).thenReturn(getChachedCompany());
        Assert.assertTrue(companyServiceImpl.certificateExpired(loginer).isSuccess());
    }

    @Test
    public void testFindSupplierTQRDCInfoBy() {
        when(supplierTQRDCInfoDao.findBy(anyString(), anyString(), anyString(), anyString(),anyString(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(new Paging<SupplierTQRDCInfo>(1L, Lists.newArrayList(getSupplierTQRDCInfos())));
        Assert.assertTrue(companyServiceImpl.findSupplierTQRDCInfoBy(loginer.getName(), loginer.getNickName(), "2014-04", "系统", "Q", 10, 80, 1, 20).isSuccess());
    }

    @Test
    public void testFindDetailSupplierTQRDCInfo() {
        Assert.assertTrue(!companyServiceImpl.findDetailSupplierTQRDCInfo("").isSuccess());

        when(supplierTQRDCInfoDao.findBySupplierCode(anyString(), anyString(), anyString())).thenReturn(getSupplierTQRDCInfos());
        Assert.assertTrue(companyServiceImpl.findDetailSupplierTQRDCInfo("v1234").isSuccess());
    }

    @Test
    public void testFindSupplierLastTQRDCInfoByUserId() {
        when(supplierTQRDCInfoDao.findLastByUserId(loginer.getId())).thenReturn(getSupplierTQRDCInfos().get(0));
        Assert.assertTrue(companyServiceImpl.findSupplierLastTQRDCInfoByUserId(loginer.getId()).isSuccess());
    }

    @Test
    public void testFindSupplierTQRDCInfos() {
        when(supplierTQRDCInfoDao.groupByMonthAndLocation()).thenReturn(getSupplierLocationInfos());
        Assert.assertTrue(companyServiceImpl.findSupplierTQRDCInfos().isSuccess());
    }

    @Test
    public void testSetTQRDCRank() {
        when(supplierTQRDCInfoDao.findByMonth(anyString())).thenReturn(getSupplierTQRDCInfos());
        Assert.assertTrue(companyServiceImpl.setTQRDCRank().isSuccess());
    }

    @Test
    public void testIsComplete() {
        Assert.assertTrue(!companyServiceImpl.isComplete(null).isSuccess());

        when(companyCache.getUnchecked(anyLong())).thenReturn(getChachedCompany());
        when(companyMainBusinessDao.findByUserId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(companySupplyParkDao.findByUserId(anyLong())).thenReturn(getCompanySupplyParks());
        when(contactInfoDao.findByUserId(anyLong())).thenReturn(getContactInfo());
        when(companyRankDao.findByUserId(anyLong())).thenReturn(getCompanyRank());
        when(financeDao.findByUserId(anyLong())).thenReturn(getFinanceDto().getFinance());
        Assert.assertTrue(companyServiceImpl.isComplete(loginer.getId()).isSuccess());
    }

    @Test
    public void testFindAllProductLine() {
        when(productLineDao.findAll()).thenReturn(getProductLines());
        Assert.assertTrue(companyServiceImpl.findAllProductLine().isSuccess());
    }

    @Test
    public void testGetSupplierStep() {
        cachedCompanyNotFound();
        Assert.assertTrue(!companyServiceImpl.getSupplierStep(loginer,null).isSuccess());

        when(companyCache.getUnchecked(loginer.getId())).thenReturn(getChachedCompany());
        when(userDao.findById(loginer.getId())).thenReturn(getUser());
        Assert.assertTrue(companyServiceImpl.getSupplierStep(loginer,null).isSuccess());
    }

    @Test
    public void testFindRefusedSuppliers() {
        when(userDao.findBy(anyMap(), anyInt(), anyInt())).thenReturn(new Paging<User>(1L, Lists.newArrayList(getUser())));
        when(companyCache.getUnchecked(anyLong())).thenReturn(getChachedCompany());
        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(getCompanyMainUsinesses());
        when(contactInfoDao.findByUserId(anyLong())).thenReturn(getContactInfo());
        Assert.assertTrue(companyServiceImpl.findRefusedSuppliers(loginer.getNickName(), 1, 30).isSuccess());
    }

    @Test
    public void testAddSupplierCode() {
        Assert.assertTrue(!companyServiceImpl.addSupplierCode(null, "v123").isSuccess());

        Assert.assertTrue(!companyServiceImpl.addSupplierCode(1L, null).isSuccess());

        when(companyDao.findById(anyLong())).thenReturn(getChachedCompany().get());
        Assert.assertTrue(companyServiceImpl.addSupplierCode(1L, "v1234").isSuccess());
    }

    @Test
    public void testFindContactInfoByUserIds() {
        when(contactInfoDao.findByUserIds(anyList())).thenReturn(Lists.newArrayList(getContactInfo()));
        Assert.assertTrue(companyServiceImpl.findContactInfoByUserIds(Lists.newArrayList(1L, 2L)).isSuccess());
    }

    @Test
    public void testFindMainBussinessByUserIds() {
        when(companyMainBusinessDao.findByUserIds(anyList())).thenReturn(getCompanyMainUsinesses());
        Assert.assertTrue(companyServiceImpl.findMainBussinessByUserIds(Lists.newArrayList(1L, 2L, 3L)).isSuccess());
    }

    @Test
    public void testFindMainBusinessByCompanyId() {
        Assert.assertFalse(companyServiceImpl.findMainBusinessByCompanyId(null).isSuccess());
        when(companyMainBusinessDao.findByCompanyId(anyLong())).thenReturn(getCompanyMainUsinesses());
        Assert.assertTrue(companyServiceImpl.findMainBusinessByCompanyId(1L).isSuccess());

    }

    private Optional<Company> getChachedCompany() {
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

        Optional<Company> oc = Optional.fromNullable(company);

        return oc;
    }

    private PaperworkDto getPaperworkDto() {
        PaperworkDto paperworkDto = new PaperworkDto();

        paperworkDto.setBusinessLicense("/img/bl2.jpg");
        paperworkDto.setBusinessLicenseId("364342");
        paperworkDto.setBlDate(new Date());

        paperworkDto.setOrgCert("/img/oc2.jpg");
        paperworkDto.setOrgCertId("633432");
        paperworkDto.setOcDate(new Date());

        paperworkDto.setTaxNo("/img/tn2.jpg");
        paperworkDto.setTnDate(new Date());
        paperworkDto.setTaxNoId("845452");

        return paperworkDto;

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

    private CompanyExtraDelivery getCompanyExtraDel() {
        CompanyExtraDelivery del = new CompanyExtraDelivery();
        del.setUserId(loginer.getId());
        return del;
    }

    private CompanyRank getCompanyRank() {
        CompanyRank companyRank = new CompanyRank();
        companyRank.setUserId(loginer.getId());
        companyRank.setCompanyId(1L);
        companyRank.setInRank(11);
        companyRank.setInRankFile("/file/if.doc");
        companyRank.setInRankFileName("in.doc");
        companyRank.setInRankOrg("国家商务部");
        companyRank.setOutRank(16);
        companyRank.setOutRankFile("/file/of.doc");
        companyRank.setOutRankFileName("on.doc");
        companyRank.setInRankOrg("国际商务部");
        return companyRank;
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

    private FinanceDto getFinanceDto() {
        FinanceDto financeDto = new FinanceDto();
        Finance finance = new Finance();
        finance.setCompanyId(1L);
        finance.setUserId(loginer.getId());
        finance.setCountry(1);
        finance.setBankAccount("李四");
        finance.setBankCode("h344dfd");
        finance.setCoinType(Finance.CoinType.EUR.value());
        finance.setOpeningBank("工行");
        finance.setRecentFinance("{}");
        finance.setOpenLicense("/img/op.jpg");
        financeDto.setFinance(finance);
        return financeDto;
    }

    private AdditionalDoc getAdditionalDoc() {
        AdditionalDoc additionalDoc = new AdditionalDoc();
        additionalDoc.setUserId(loginer.getId());
        additionalDoc.setFinanceId(1L);
        additionalDoc.setComment("");
        additionalDoc.setName("财务");
        additionalDoc.setFiles("/file/cw.doc");
        return additionalDoc;
    }

    private List<SupplierTQRDCInfo> getSupplierTQRDCInfos() {
        List<SupplierTQRDCInfo> supplierTQRDCInfos = Lists.newArrayList();
        SupplierTQRDCInfo supplierTQRDCInfo = new SupplierTQRDCInfo();
        supplierTQRDCInfo.setCompositeScore(59);
        supplierTQRDCInfo.setCostScore(75);
        supplierTQRDCInfo.setDelayDays(69);
        supplierTQRDCInfo.setDeliverDiff(66);
        supplierTQRDCInfo.setIncrement(64);
        supplierTQRDCInfo.setLiveBad(67);
        supplierTQRDCInfo.setMarketBad(85);
        supplierTQRDCInfo.setDeliverScore(56);
        supplierTQRDCInfo.setMonth("2014-05");
        supplierTQRDCInfo.setNewProductPass(90);
        supplierTQRDCInfo.setSupplierName("海尔");
        supplierTQRDCInfo.setQualityScore(34);
        supplierTQRDCInfo.setSupplierCode("fd434");
        supplierTQRDCInfo.setTechScore(88);
        supplierTQRDCInfo.setRequirementResp(54);
        supplierTQRDCInfo.setRespScore(99);
        supplierTQRDCInfo.setModule("bb");
        supplierTQRDCInfo.setProductLineId(3);
        supplierTQRDCInfo.setLocation("黄区");
        supplierTQRDCInfo.setRank(12);
        supplierTQRDCInfo.setUserId(loginer.getId());
        supplierTQRDCInfo.setCompanyId(1L);
        supplierTQRDCInfo.setTechScoreRank(1);
        supplierTQRDCInfo.setQualityScoreRank(2);
        supplierTQRDCInfo.setRespScoreRank(3);
        supplierTQRDCInfo.setDeliveryScoreRank(4);
        supplierTQRDCInfo.setCostScoreRank(5);

        supplierTQRDCInfos.add(supplierTQRDCInfo);
        return supplierTQRDCInfos;
    }

    private List<ProductLine> getProductLines() {
        List<ProductLine> productLines = Lists.newArrayList();
        ProductLine productLine = new ProductLine();
        productLine.setName("钢板");
        productLines.add(productLine);
        return productLines;
    }

    private CompanyDto getCompanyDto() {
        CompanyDto companyDto = new CompanyDto();

        companyDto.setCompany(getChachedCompany().get());
        companyDto.setCompanyMainBusinesses(getCompanyMainUsinesses());
        companyDto.setCompanySupplyParks(getCompanySupplyParks());

        return companyDto;
    }

    private BaseUser getNullIdBaseUser() {
        BaseUser baseUser = new BaseUser();
        baseUser.setId(null);
        return baseUser;
    }

    private void cachedCompanyNotFound() {
        Company company = null;
        when(companyCache.getUnchecked(loginer.getId())).thenReturn(Optional.fromNullable(company));
    }

    private List<SupplierLocationInfo> getSupplierLocationInfos() {
        SupplierLocationInfo supplierLocationInfo = new SupplierLocationInfo();
        supplierLocationInfo.setMonth("2014-04");
        supplierLocationInfo.setLocation("黄区");
        supplierLocationInfo.setCount(30);

        List<SupplierLocationInfo> supplierLocationInfos = Lists.newArrayList(supplierLocationInfo);
        return supplierLocationInfos;
    }

    private SupplierInfoChangedDto getSupplierInfoChangeDto(Integer type) {
        SupplierInfoChangedDto supplierInfoChangedDto = new SupplierInfoChangedDto();
        //supplierInfoChangedDto.setType(type);
        return supplierInfoChangedDto;
    }

    private <T> SupplierUpdatedInfoDto<T> getSupplierUpdatedInfoDto(T t) {
        SupplierUpdatedInfoDto<T> supplierUpdatedInfoDto = new SupplierUpdatedInfoDto<T>();
        supplierUpdatedInfoDto.setSupplierInfo(t);
        Map<String, Object> oldValues = Maps.newHashMap();
        supplierUpdatedInfoDto.setOldValues(oldValues);

        return supplierUpdatedInfoDto;
    }

}
