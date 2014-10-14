package io.terminus.snz.user.manager;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.BaseTest;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dao.redis.*;
import io.terminus.snz.user.dto.CompanyDto;
import io.terminus.snz.user.dto.PaperworkDto;
import io.terminus.snz.user.dto.SupplierApproveDto;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.*;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-8-4
 */
public class AccountManagerTest extends BaseTest {
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
    private SupplierApproveLogDao supplierApproveLogDao;

    @Mock
    private SupplierChangedInfoRedisDao supplierRedisDao;

    @Mock
    private SupplierInfoChangedService supplierInfoChangedService;

    @Mock
    private SupplierChangedInfoRedisDao supplierChangedInfoRedisDao;

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

    @Mock
    private CompanyExtraRDRedisDao companyExtraRDRedisDao;

    @Mock
    private CompanyExtraResponseRedisDao companyExtraResponseRedisDao;

    @Mock
    private CompanyExtraQualityRedisDao companyExtraQualityRedisDao;

    @Mock
    private CompanyExtraRDDao companyExtraRDDao;

    @Mock
    private CompanyExtraResponseDao companyExtraResponseDao;

    @Mock
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @InjectMocks
    private AccountManager accountManager;

    @Test
    public void testCreateSupplier() {
        when(userDao.create(any(User.class))).thenReturn(1L);
        when(companyDao.create(any(Company.class))).thenReturn(1L);
        assertEquals(1L, accountManager.createSupplier(mockRichSupplierDto(1L)).intValue());
    }

    @Test
    public void testUpdateCompany() {

        User user = new User();
        user.setId(1L);
        user.setApproveStatus(User.ApproveStatus.INIT.value());
        when(userDao.findById(anyLong())).thenReturn(user);
        when(supplierInfoChangedService.needCheckChanged(anyInt())).thenReturn(true);
        when(companyMainBusinessDao.findByUserId(anyLong())).thenReturn(Lists.newArrayList(mockMainBussiess(1L)));
        when(companySupplyParkDao.findByUserId(anyLong())).thenReturn(Lists.newArrayList(mockCompanySupplierPark(1L)));
        when(supplierInfoChangedService.baseCompanyChanged(anyLong(), any(CompanyDto.class), any(Company.class), anyListOf(CompanyMainBusiness.class), anyListOf(CompanySupplyPark.class))).thenReturn(mockSupplierChangedInfoDto());
        CompanyDto companyDto = mockCompanyDto();
        Response<Boolean> resp = new Response<Boolean>();
        resp.setSuccess(Boolean.TRUE);
        when(supplierIndexService.realTimeIndex(anyList(), any((User.SearchStatus.class)))).thenReturn(resp);
        accountManager.updateCompany(companyDto, mockCompany(loginer));

        when(supplierInfoChangedService.needCheckChanged(anyInt())).thenReturn(false);
        accountManager.updateCompany(companyDto, mockCompany(loginer));
    }

    @Test
    public void testApproveSupplier() {
        SupplierApproveLog supplierApproveLog = new SupplierApproveLog();
        supplierApproveLog.setId(1L);
        supplierApproveLog.setUserId(1L);

        SupplierApproveDto supplierApproveDto = new SupplierApproveDto();
        supplierApproveDto.setUserId(1L);
        supplierApproveDto.setApproverId(1L);
        supplierApproveDto.setApproverName("aa");
        supplierApproveDto.setOperation(1);
        accountManager.approveSupplier(supplierApproveDto);
    }

    @Test
    public void testAddSupplierCodeAndTag() {
        accountManager.addSupplierCodeAndTag(1L, 1L, "123");
    }

    @Test
    public void testUpdatePaperwork() {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setSuccess(Boolean.TRUE);
        User user = new User();
        user.setId(1L);
        user.setApproveStatus(User.ApproveStatus.INIT.value());
        when(userDao.findById(anyLong())).thenReturn(user);
        accountManager.updatePaperwork(user.getId(), new PaperworkDto(), mockCompany(loginer));

        when(supplierInfoChangedService.needCheckChanged(anyInt())).thenReturn(true);
        when(supplierInfoChangedService.paperworkChanged(anyLong(), any(PaperworkDto.class), any(Company.class))).thenReturn(mockSupplierChangedInfoDto());
        when(userDao.findById(anyLong())).thenReturn(user);
        when(supplierIndexService.realTimeIndex(anyList(), any((User.SearchStatus.class)))).thenReturn(resp);
        accountManager.updatePaperwork(user.getId(), new PaperworkDto(), mockCompany(loginer));
    }

    @Test
    public void testCreateOrUpdateFinance() {

        mockIsQualified();
        when(companyDao.findByUserId(anyLong())).thenReturn(mockCompany(loginer));
        when(userDao.findById(anyLong())).thenReturn(new User());
        when(financeDao.findByUserId(anyLong())).thenReturn(mockFinance());
        when(supplierInfoChangedService.financeChanged(anyLong(), any(Finance.class), any(Finance.class))).thenReturn(mockSupplierChangedInfoDto());
        accountManager.createOrUpdateFinance(mockFinanceDto());

        when(financeDao.findByUserId(anyLong())).thenReturn(null);
        when(companyDao.findByUserId(anyLong())).thenReturn(mockCompany(loginer));
        accountManager.createOrUpdateFinance(mockFinanceDto());
    }

    @Test
    public void testBulkCreateSupplierQTRDCInfo() {
        accountManager.bulkCreateSupplierQTRDCInfo(Arrays.asList(mockSupplierTQRDCInfo()));
    }

    @Test
    public void testBulkUpdateSupplierQTRDCInfo() {
        accountManager.bulkUpdateSupplierQTRDCInfo(Arrays.asList(mockSupplierTQRDCInfo()));
    }

    @Test
    public void testBulkCreateSupplier() {
        accountManager.bulkCreateSupplier(Arrays.asList(mockSupplierImportDto()));
    }

    @Test
    public void testBulkCreateMainBusinessApprover() {
        when(frontendCategoryService.findFrontendCategoryByLevelAndName(anyInt(), anyString())).thenReturn(mockFrontendCategoryResp());
        accountManager.bulkCreateMainBusinessApprover(Arrays.asList(mockMainBussinessApprover()));
    }

    @Test
    public void testCreatePurcharser() {
        accountManager.createPurcharser(mockPurchaserDto());
    }

    @Test
    public void testBulkCreatePurchaser() {
        accountManager.bulkCreatePurchaser(Arrays.asList(mockPurchaserDto()));
    }

    private void mockIsQualified() {
        Response<Boolean> resp = new Response<Boolean>();
        resp.setResult(Boolean.TRUE);

        SupplierCreditQualify supplierCreditQualify = new SupplierCreditQualify();
        supplierCreditQualify.setStatus(SupplierCreditQualify.STATUS.A.toValue());
        Response<SupplierCreditQualify> rp = new Response<SupplierCreditQualify>();
        rp.setResult(supplierCreditQualify);
        when(supplierCreditQualifyService.findCreditQualifyByUserId(anyLong())).thenReturn(rp);
    }

}
