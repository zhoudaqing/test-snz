package io.terminus.snz.user.service;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.MapBuilder;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.*;
import io.terminus.snz.user.dto.SupplierCountDto;
import io.terminus.snz.user.dto.SupplierLevelCountDto;
import io.terminus.snz.user.dto.SupplierReparationSummariesDto;
import io.terminus.snz.user.manager.SupplierSummaryManager;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.tool.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
@Slf4j
@Service
public class SupplierSummaryServiceImpl implements SupplierSummaryService {

    private static final Integer LAST_LIMIT = 30;

    private static final Integer MONTH_NUM = 12;

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormat.forPattern("yyyy-MM");

    @Autowired
    private SupplierSummaryManager supplierSummaryManager;

    @Autowired
    private SupplierCountBySupplyParkDao supplierCountBySupplyParkDao;

    @Autowired
    private SupplierCountByStatusDao supplierCountByStatusDao;

    @Autowired
    private SupplierCountByLevelDao supplierCountByLevelDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private SupplierTQRDCInfoService supplierTQRDCInfoService;

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private SupplierCountByIndustryDao supplierCountByIndustryDao;

    @Autowired
    private SupplierReparationSumariesDao supplierReparationSumariesDao;

    @Autowired
    private SupplierResourceDao supplierResourceDao;

    LoadingCache<Long,Optional<SupplierReparationSumaries>> sumariesCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, Optional<SupplierReparationSumaries>>() {
                @Override
                public Optional<SupplierReparationSumaries> load(Long uid) throws Exception {
                    return Optional.fromNullable(supplierReparationSumariesDao.findSupplierReparationSumariesBySupplierUid(uid));
                }
            });

    @Override
    public Response<Boolean> supplierSummaryBySupplyParks(List<Long> supplyParkIds) {

        Response<Boolean> result = new Response<Boolean>();

        try {

            if (supplyParkIds == null || supplyParkIds.isEmpty()) {
                log.error("supply park ids can not be null");
                result.setError("supply.park.ids.not.null.fail");
                return result;
            }

            supplierSummaryManager.createSupplierSummaryBySupplyParkIds(supplyParkIds);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to count supplier where supplyParkIds={},cause:{}", supplyParkIds, Throwables.getStackTraceAsString(e));
            result.setError("count.supplier.fail");
        }

        return result;
    }

    @Override
    public Response<SupplierCountDto> findSupplierCountBySupplyParkIds(Long supplyParkId1, Long supplyParkId2) {

        Response<SupplierCountDto> result = new Response<SupplierCountDto>();

        try {

            List<SupplierCountBySupplyPark> supplierCountBySupplyParks1 = supplierCountBySupplyParkDao.findLastBy(supplyParkId1, LAST_LIMIT);
            List<SupplierCountBySupplyPark> supplierCountBySupplyParks2 = supplierCountBySupplyParkDao.findLastBy(supplyParkId2, LAST_LIMIT);

            //List<Date> dates = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result1 = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result2 = Lists.newArrayListWithCapacity(LAST_LIMIT);

            if (supplierCountBySupplyParks1 == null || supplierCountBySupplyParks1.isEmpty()) {
                fillNulls(result1, LAST_LIMIT);
            } else {
                fillNulls(result1, LAST_LIMIT - supplierCountBySupplyParks1.size());
                Collections.reverse(supplierCountBySupplyParks1);
                for (SupplierCountBySupplyPark supplierCountBySupplyPark : supplierCountBySupplyParks1) {
                    result1.add(supplierCountBySupplyPark.getSupplierCount());
                }
            }

            if (supplierCountBySupplyParks2 == null || supplierCountBySupplyParks2.isEmpty()) {
                fillNulls(result2, LAST_LIMIT);
            } else {
                fillNulls(result2, LAST_LIMIT - supplierCountBySupplyParks2.size());
                Collections.reverse(supplierCountBySupplyParks2);
                for (SupplierCountBySupplyPark supplierCountBySupplyPark : supplierCountBySupplyParks2) {
                    result2.add(supplierCountBySupplyPark.getSupplierCount());
                }
            }

            SupplierCountDto supplierCountDto = new SupplierCountDto();
            //supplierCountDto.setDates(dates);
            supplierCountDto.setResult1(result1);
            supplierCountDto.setResult2(result2);

            result.setResult(supplierCountDto);

        } catch (Exception e) {
            log.error("fail to find supplier count where supply park id=({},{}),cause:{}", supplyParkId1, supplyParkId2, Throwables.getStackTraceAsString(e));
            result.setError("find.supplier.count.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> supplierSummaryByStatus() {
        Response<Boolean> result = new Response<Boolean>();

        try {

            Date date = DateUtil.getYesterdayStart();

            List<SupplierCountByStatus> supplierCountByStatuses = Lists.newArrayList();

            //统计注册的供应商数量
            SupplierCountByStatus countRegistered = new SupplierCountByStatus();
            countRegistered.setDate(date);
            countRegistered.setStatus(SupplierCountByStatus.Status.REGISTERED.value());
            countRegistered.setSupplierCount(companyDao.countSupplier());

            //统计参与交互的供应商数量
            SupplierCountByStatus countParticipated = new SupplierCountByStatus();
            countParticipated.setDate(date);
            countParticipated.setStatus(SupplierCountByStatus.Status.PARTICIPATED.value());
            countParticipated.setSupplierCount(companyDao.countParticipatedSupplier());

            //统计入围的供应商数量
            SupplierCountByStatus countStandard = new SupplierCountByStatus();
            countStandard.setDate(date);
            countStandard.setStatus(SupplierCountByStatus.Status.STANDARD.value());
            countStandard.setSupplierCount(userDao.countQualifiedSupplier());

            //统计合作供应商数量
            SupplierCountByStatus countPartner = new SupplierCountByStatus();
            countPartner.setDate(date);
            countPartner.setStatus(SupplierCountByStatus.Status.PARTNER.value());
            countPartner.setSupplierCount(companyDao.countSupplierHasSupplierCode());

            supplierCountByStatuses.add(countRegistered);
            supplierCountByStatuses.add(countParticipated);
            supplierCountByStatuses.add(countStandard);
            supplierCountByStatuses.add(countPartner);

            supplierSummaryManager.createSupplierSummaryByStatus(supplierCountByStatuses);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to count supplier by status,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("count.supplier.fail");
        }
        return result;
    }

    @Override
    public Response<SupplierCountDto> findSupplierCountByStatus(Integer status1, Integer status2) {

        Response<SupplierCountDto> result = new Response<SupplierCountDto>();

        try {

            List<SupplierCountByStatus> supplierCountByStatus1 = supplierCountByStatusDao.findLastBy(status1, LAST_LIMIT);
            List<SupplierCountByStatus> supplierCountByStatus2 = supplierCountByStatusDao.findLastBy(status2, LAST_LIMIT);

            //List<Date> dates = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result1 = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result2 = Lists.newArrayListWithCapacity(LAST_LIMIT);

            if (supplierCountByStatus1 == null || supplierCountByStatus1.isEmpty()) {
                fillNulls(result1, LAST_LIMIT);
            } else {
                fillNulls(result1, LAST_LIMIT - supplierCountByStatus1.size());
                Collections.reverse(supplierCountByStatus1);
                for (SupplierCountByStatus supplierCountByStatus : supplierCountByStatus1) {
                    result1.add(supplierCountByStatus.getSupplierCount());
                }
            }

            if (supplierCountByStatus2 == null || supplierCountByStatus2.isEmpty()) {
                fillNulls(result2, LAST_LIMIT);
            } else {
                fillNulls(result2, LAST_LIMIT - supplierCountByStatus2.size());
                Collections.reverse(supplierCountByStatus2);
                for (SupplierCountByStatus supplierCountByStatus : supplierCountByStatus2) {
                    result2.add(supplierCountByStatus.getSupplierCount());
                }

            }

            SupplierCountDto supplierCountDto = new SupplierCountDto();
            supplierCountDto.setResult1(result1);
            supplierCountDto.setResult2(result2);

            result.setResult(supplierCountDto);

        } catch (Exception e) {
            log.error("fail to find supplier count where status=({},{}),cause:{}", status1, status2, Throwables.getStackTraceAsString(e));
            result.setError("find.supplier.count.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> supplierSummaryByLevel() {

        Response<Boolean> result = new Response<Boolean>();

        try {

            String lastMonth = supplierTQRDCInfoDao.findMaxMonth();

            //该月已统计过
            if (hasSummary(lastMonth)) {
                log.error("it has summary already where month={}", lastMonth);
                result.setError("supplier.already.summary");
                return result;
            }

            supplierSummaryByLevelWithMonth(lastMonth);
            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to count supplier by level,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("count.supplier.fail");
        }

        return result;
    }

    private void supplierSummaryByLevelWithMonth(String month) {

        SupplierLevelCountDto supplierLevelCountDto = supplierTQRDCInfoService.countCompositeScoreOfMonth(month).getResult();

        List<SupplierCountByLevel> supplierCountByLevels = Lists.newArrayList();

        //优选供应商数量
        SupplierCountByLevel countBest = new SupplierCountByLevel();
        countBest.setMonth(month);
        countBest.setLevel(SupplierCountByLevel.Level.BEST.value());
        countBest.setSupplierCount(Long.valueOf(supplierLevelCountDto.getBestCount()));

        //合格供应商数量
        SupplierCountByLevel countStandard = new SupplierCountByLevel();
        countStandard.setMonth(month);
        countStandard.setLevel(SupplierCountByLevel.Level.STANDARD.value());
        countStandard.setSupplierCount(Long.valueOf(supplierLevelCountDto.getStandardCount()));

        //受限制供应商数量
        SupplierCountByLevel countLimited = new SupplierCountByLevel();
        countLimited.setMonth(month);
        countLimited.setLevel(SupplierCountByLevel.Level.LIMITED.value());
        countLimited.setSupplierCount(Long.valueOf(supplierLevelCountDto.getLimitedCount()));

        //淘汰供应商数量
        SupplierCountByLevel countBad = new SupplierCountByLevel();
        countBad.setMonth(month);
        countBad.setLevel(SupplierCountByLevel.Level.BAD.value());
        countBad.setSupplierCount(Long.valueOf(supplierLevelCountDto.getBadCount()));

        supplierCountByLevels.add(countBest);
        supplierCountByLevels.add(countStandard);
        supplierCountByLevels.add(countLimited);
        supplierCountByLevels.add(countBad);

        supplierSummaryManager.createSupplierSummaryByLevel(supplierCountByLevels);
    }

    @Override
    public Response<SupplierCountDto> findSupplierCountByLevel(Integer level1, Integer level2) {

        Response<SupplierCountDto> result = new Response<SupplierCountDto>();

        try {

            List<SupplierCountByLevel> supplierCountByLevel1 = supplierCountByLevelDao.findLastBy(level1, MONTH_NUM);
            List<SupplierCountByLevel> supplierCountByLevel2 = supplierCountByLevelDao.findLastBy(level2, MONTH_NUM);

            //List<String> months = Lists.newArrayListWithCapacity(MONTH_NUM);
            List<Long> result1 = Lists.newArrayListWithCapacity(MONTH_NUM);
            List<Long> result2 = Lists.newArrayListWithCapacity(MONTH_NUM);

            if (supplierCountByLevel1 == null || supplierCountByLevel1.isEmpty()) {
                fillNulls(result1, MONTH_NUM);
            } else {
                setSupplierCountByLevelResult(result1, supplierCountByLevel1);
            }

            if (supplierCountByLevel2 == null || supplierCountByLevel2.isEmpty()) {
                fillNulls(result2, MONTH_NUM);
            } else {
                setSupplierCountByLevelResult(result2, supplierCountByLevel2);
            }

            SupplierCountDto supplierCountDto = new SupplierCountDto();
            //supplierCountDto.setMonths(months);
            supplierCountDto.setResult1(result1);
            supplierCountDto.setResult2(result2);

            result.setResult(supplierCountDto);

        } catch (Exception e) {
            log.error("fail to find supplier count where level=({},{}),cause:{}", level1, level2, Throwables.getStackTraceAsString(e));
            result.setError("find.supplier.count.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> supplierSummaryByIndustry() {

        Response<Boolean> result = new Response<Boolean>();

        try {
            Date date = DateUtil.getYesterdayStart();

            List<FrontendCategory> frontendCategories = frontendCategoryService.findByLevels(1).getResult();
            if (frontendCategories == null || frontendCategories.isEmpty()) {
                log.error("frontendCategory not found where level=1");
                result.setError("frontendCategory.not.found");
                return result;
            }

            List<SupplierCountByIndustry> supplierCountByIndustries = Lists.newArrayListWithCapacity(frontendCategories.size());
            for (FrontendCategory frontendCategory : frontendCategories) {
                Long count = companyMainBusinessDao.countSupplierByFirstLevelId(frontendCategory.getId());
                SupplierCountByIndustry supplierCountByIndustry = new SupplierCountByIndustry();
                supplierCountByIndustry.setDate(date);
                supplierCountByIndustry.setIndustry(frontendCategory.getId());
                supplierCountByIndustry.setSupplierCount(count);
                supplierCountByIndustries.add(supplierCountByIndustry);
            }

            supplierSummaryManager.createSupplierSummaryByIndustry(supplierCountByIndustries);

            result.setResult(Boolean.TRUE);

        } catch (Exception e) {
            log.error("fail to count supplier by industry,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("count.supplier.fail");
        }

        return result;
    }

    @Override
    public Response<SupplierCountDto> findSupplierCountByIndustries(Long industryId1, Long industryId2) {

        Response<SupplierCountDto> result = new Response<SupplierCountDto>();

        try {

            List<SupplierCountByIndustry> supplierCountByIndustrys1 = supplierCountByIndustryDao.findLastBy(industryId1, LAST_LIMIT);
            List<SupplierCountByIndustry> supplierCountByIndustrys2 = supplierCountByIndustryDao.findLastBy(industryId2, LAST_LIMIT);

            //List<Date> dates = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result1 = Lists.newArrayListWithCapacity(LAST_LIMIT);
            List<Long> result2 = Lists.newArrayListWithCapacity(LAST_LIMIT);

            if (supplierCountByIndustrys1 == null || supplierCountByIndustrys1.isEmpty()) {
                fillNulls(result1, LAST_LIMIT);
            } else {
                fillNulls(result1, LAST_LIMIT - supplierCountByIndustrys1.size());
                Collections.reverse(supplierCountByIndustrys1);
                for (SupplierCountByIndustry supplierCountByIndustry : supplierCountByIndustrys1) {
                    result1.add(supplierCountByIndustry.getSupplierCount());
                }
            }

            if (supplierCountByIndustrys2 == null || supplierCountByIndustrys2.isEmpty()) {
                fillNulls(result2, LAST_LIMIT);
            } else {
                fillNulls(result2, LAST_LIMIT - supplierCountByIndustrys2.size());
                Collections.reverse(supplierCountByIndustrys2);
                for (SupplierCountByIndustry supplierCountByIndustry : supplierCountByIndustrys2) {
                    result2.add(supplierCountByIndustry.getSupplierCount());
                }
            }

            SupplierCountDto supplierCountDto = new SupplierCountDto();
            //supplierCountDto.setDates(dates);
            supplierCountDto.setResult1(result1);
            supplierCountDto.setResult2(result2);

            result.setResult(supplierCountDto);

        } catch (Exception e) {
            log.error("fail to find supplier count where industry id=({},{}),cause:{}", industryId1, industryId2, Throwables.getStackTraceAsString(e));
            result.setError("find.supplier.count.fail");
        }

        return result;
    }

    @Override
    public Response<Paging<SupplierReparationSummariesDto>> findSupplierReparationSumaries(BaseUser baseUser, String supplinerName, Integer pageNo, Integer size) {

        Response<Paging<SupplierReparationSummariesDto>> result = new Response<Paging<SupplierReparationSummariesDto>>();

        try{
            Map<String, Object> param = MapBuilder.<String, Object>of().map();
            if(!Strings.isNullOrEmpty(supplinerName)){
                param.put("supplierName", supplinerName);
            }
            PageInfo pageInfo = new PageInfo(pageNo, size);
            param.put("offset", pageInfo.getOffset());
            param.put("limit", pageInfo.getLimit());

            Paging<SupplierResource> resourcePaging = supplierResourceDao.findByPaging(param);
            if(resourcePaging==null || resourcePaging.getData() == null || resourcePaging.getData().size()==0){
                result.setResult(new Paging<SupplierReparationSummariesDto>(0L, Lists.<SupplierReparationSummariesDto>newArrayList()));
                return result;
            }
            List<SupplierReparationSummariesDto> list = Lists.newArrayList();
            for(SupplierResource supplierResource : resourcePaging.getData()){
                SupplierReparationSummariesDto dto = new SupplierReparationSummariesDto();
                dto.setSupplierResource(supplierResource);
                Optional<SupplierReparationSumaries> summaries = sumariesCache.getUnchecked(supplierResource.getSupplierUid());
                if (summaries.isPresent()) {
                    dto.setSupplierReparationSumaries(summaries.get());
                }
                list.add(dto);
            }
            Paging<SupplierReparationSummariesDto> paging = new Paging<SupplierReparationSummariesDto>(resourcePaging.getTotal(), list);
            result.setResult(paging);
        }catch (Exception e){
            log.error("fail to find supplier reparationSumaries by supplinerName,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("count.supplier.fail");
        }
        return result;
    }

    @Override
    public Response<Long> insertOrUpdateReparationSummary(SupplierReparationSumaries reparationSumaries) {
        Response<Long> result = new Response<Long>();

        try {
            SupplierReparationSumaries found =
                    supplierReparationSumariesDao.findSupplierReparationSumariesBySupplierUid(
                            reparationSumaries.getSupplierUid());
            if (found==null) {
                supplierReparationSumariesDao.create(reparationSumaries);
            } else {
                supplierReparationSumariesDao.update(reparationSumaries);
            }

            result.setResult(reparationSumaries.getId());
        } catch (Exception e) {
            log.error("`insertOrUpdateReparationSummary` invoke fail. with reparation summary:{}, e:{}", reparationSumaries, e);
            result.setError("insert.or.update.reparation.fail");
            return result;
        }

        return result;
    }

    private void fillNulls(List<Long> target, int count) {
        if (target == null || count == 0) {
            return;
        }
        for (int i = 1; i <= count; i++) {
            target.add(null);
        }

    }

    private boolean hasSummary(String month) {
        Integer count = supplierCountByLevelDao.countByMonth(month);
        return count == 0 ? false : true;
    }

    private void setSupplierCountByLevelResult(List<Long> result, List<SupplierCountByLevel> supplierCountByLevels) {

        DateTime recentMonth = DateTime.now().minusMonths(13);

        for (int i = 1; i <= 12; i++) {

            String month = MONTH_FORMAT.print(recentMonth.plusMonths(i));

            boolean existed = false;
            for (SupplierCountByLevel supplierCountByLevel : supplierCountByLevels) {
                if (supplierCountByLevel.getMonth().equals(month)) {
                    result.add(supplierCountByLevel.getSupplierCount());
                    existed = true;
                    break;
                }
            }

            if (!existed) {
                result.add(null);
            }

        }

    }


}
