package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dto.SupplierLevelCountDto;
import io.terminus.snz.user.manager.SupplierSummaryManager;
import io.terminus.snz.user.model.SupplierCountByIndustry;
import io.terminus.snz.user.model.SupplierCountByLevel;
import io.terminus.snz.user.model.SupplierCountByStatus;
import io.terminus.snz.user.model.SupplierCountBySupplyPark;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-3.
 */
public class SupplierSummaryServiceTest extends BaseServiceTest {

    @Mock
    private SupplierSummaryManager supplierSummaryManager;

    @Mock
    private SupplierCountBySupplyParkDao supplierCountBySupplyParkDao;

    @Mock
    private SupplierCountByStatusDao supplierCountByStatusDao;

    @Mock
    private SupplierCountByLevelDao supplierCountByLevelDao;

    @Mock
    private UserDao userDao;

    @Mock
    private CompanyDao companyDao;

    @Mock
    private SupplierTQRDCInfoService supplierTQRDCInfoService;

    @Mock
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Mock
    private FrontendCategoryService frontendCategoryService;

    @Mock
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Mock
    private SupplierCountByIndustryDao supplierCountByIndustryDao;

    @InjectMocks
    private SupplierSummaryServiceImpl supplierSummaryServiceImpl;

    @Test
    public void testSupplierSummaryBySupplyParks() {
        Assert.assertTrue(!supplierSummaryServiceImpl.supplierSummaryBySupplyParks(null).isSuccess());
        List<Long> parkIds = Lists.newArrayList(1L, 2L, 3L);
        Response<Boolean> result = supplierSummaryServiceImpl.supplierSummaryBySupplyParks(parkIds);
        Assert.assertTrue(result.isSuccess());
    }

    @Test
    public void testFindSupplierCountBySupplyParkIds() {
        when(supplierCountBySupplyParkDao.findLastBy(anyLong(), anyInt())).thenReturn(getSupplierCountBySupplyPark());
        Assert.assertTrue(supplierSummaryServiceImpl.findSupplierCountBySupplyParkIds(1L, 2L).isSuccess());
    }

    @Test
    public void testSupplierSummaryByStatus() {
        Assert.assertTrue(supplierSummaryServiceImpl.supplierSummaryByStatus().isSuccess());
    }

    @Test
    public void testFindSupplierCountByStatus() {
        when(supplierCountByStatusDao.findLastBy(anyInt(), anyInt())).thenReturn(getSupplierCountByStatus());
        Assert.assertTrue(supplierSummaryServiceImpl.findSupplierCountByStatus(1, 2).isSuccess());
    }

    @Test
    public void testSupplierSummaryByLevel() {
        when(supplierTQRDCInfoDao.findMaxMonth()).thenReturn("2014-04");
        when(supplierCountByLevelDao.countByMonth(anyString())).thenReturn(1);
        Assert.assertTrue(!supplierSummaryServiceImpl.supplierSummaryByLevel().isSuccess());
        when(supplierCountByLevelDao.countByMonth(anyString())).thenReturn(0);
        when(supplierTQRDCInfoService.countCompositeScoreOfMonth(anyString())).thenReturn(getSupplierLevelCount());
        Assert.assertTrue(supplierSummaryServiceImpl.supplierSummaryByLevel().isSuccess());
    }

    @Test
    public void testFindSupplierCountByLevel() {
        when(supplierCountByLevelDao.findLastBy(anyInt(), anyInt())).thenReturn(getSupplierCountByLevel());
        Assert.assertTrue(supplierSummaryServiceImpl.findSupplierCountByLevel(1, 2).isSuccess());
    }

    @Test
    public void testSupplierSummaryByIndustry() {
        when(frontendCategoryService.findByLevels(anyInt())).thenReturn(null);
        Assert.assertTrue(!supplierSummaryServiceImpl.supplierSummaryByIndustry().isSuccess());
        when(frontendCategoryService.findByLevels(anyInt())).thenReturn(getFrontendCategories());
        Assert.assertTrue(supplierSummaryServiceImpl.supplierSummaryByIndustry().isSuccess());
    }

    @Test
    public void testFindSupplierCountByIndustries() {
        when(supplierCountByIndustryDao.findLastBy(anyLong(), anyInt())).thenReturn(getSupplierCountByIndustries());
        Assert.assertTrue(supplierSummaryServiceImpl.findSupplierCountByIndustries(1L, 2L).isSuccess());
    }

    private List<SupplierCountBySupplyPark> getSupplierCountBySupplyPark() {
        List<SupplierCountBySupplyPark> supplierCountBySupplyParks = Lists.newArrayList();
        SupplierCountBySupplyPark supplierCountBySupplyPark = new SupplierCountBySupplyPark();
        supplierCountBySupplyPark.setSupplyParkId(1L);
        supplierCountBySupplyPark.setSupplierCount(30L);
        supplierCountBySupplyParks.add(supplierCountBySupplyPark);
        return supplierCountBySupplyParks;
    }

    private List<SupplierCountByStatus> getSupplierCountByStatus() {
        List<SupplierCountByStatus> supplierCountByStatuses = Lists.newArrayList();
        SupplierCountByStatus supplierCountByStatus = new SupplierCountByStatus();
        supplierCountByStatus.setStatus(1);
        supplierCountByStatus.setSupplierCount(30L);
        supplierCountByStatuses.add(supplierCountByStatus);
        return supplierCountByStatuses;
    }

    private List<SupplierCountByLevel> getSupplierCountByLevel() {
        List<SupplierCountByLevel> supplierCountByLevels = Lists.newArrayList();
        SupplierCountByLevel supplierCountByLevel = new SupplierCountByLevel();
        supplierCountByLevel.setLevel(1);
        supplierCountByLevel.setMonth("2014-12");
        supplierCountByLevel.setSupplierCount(30L);
        supplierCountByLevels.add(supplierCountByLevel);
        return supplierCountByLevels;
    }

    private Response<List<FrontendCategory>> getFrontendCategories() {
        Response<List<FrontendCategory>> result = new Response<List<FrontendCategory>>();
        List<FrontendCategory> frontendCategories = Lists.newArrayList();
        FrontendCategory fc = new FrontendCategory();
        fc.setId(11L);
        fc.setName("空调");

        frontendCategories.add(fc);
        result.setResult(frontendCategories);

        return result;

    }

    private List<SupplierCountByIndustry> getSupplierCountByIndustries() {
        List<SupplierCountByIndustry> supplierCountByIndustries = Lists.newArrayList();
        SupplierCountByIndustry supplierCountByIndustry = new SupplierCountByIndustry();
        supplierCountByIndustry.setIndustry(1L);
        supplierCountByIndustry.setSupplierCount(30L);
        supplierCountByIndustries.add(supplierCountByIndustry);
        return supplierCountByIndustries;
    }

    private Response<SupplierLevelCountDto> getSupplierLevelCount() {
        Response<SupplierLevelCountDto> result = new Response<SupplierLevelCountDto>();
        SupplierLevelCountDto supplierLevelCountDto = SupplierLevelCountDto.create(1);
        supplierLevelCountDto.setBadCount(23);
        supplierLevelCountDto.setBestCount(45);
        supplierLevelCountDto.setLimitedCount(54);
        supplierLevelCountDto.setStandardCount(66);
        result.setResult(supplierLevelCountDto);
        return result;
    }


}
