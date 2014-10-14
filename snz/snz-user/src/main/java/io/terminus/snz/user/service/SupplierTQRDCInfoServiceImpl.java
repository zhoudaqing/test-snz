package io.terminus.snz.user.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dao.CompanyMainBusinessDao;
import io.terminus.snz.user.dao.SupplierModuleCountDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoDao;
import io.terminus.snz.user.dto.SupplierLevelCountDto;
import io.terminus.snz.user.dto.SupplierModuleCountDto;
import io.terminus.snz.user.manager.SupplierTQRDCInfoManager;
import io.terminus.snz.user.model.SupplierModuleCount;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author wanggen on 14-7-24.
 */
@Service
@Slf4j
public class SupplierTQRDCInfoServiceImpl implements SupplierTQRDCInfoService {

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;

    @Autowired
    private SupplierTQRDCInfoManager supplierTQRDCInfoManager;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Autowired
    private CompanyMainBusinessDao companyMainBusinessDao;

    @Autowired
    private SupplierModuleCountDao supplierModuleCountDao;

    @Override
    public Response<SupplierLevelCountDto> countCompositeScoreOfMonth(String month) {

        Response<SupplierLevelCountDto> response = new Response<SupplierLevelCountDto>();
        SupplierLevelCountDto supplierLevelCountDto = SupplierLevelCountDto.create(0);

        try {
            //1. 查询数据
            List<SupplierTQRDCInfo> productLineScores = supplierTQRDCInfoDao.findCompositeScoreOfMonth(month);

            //2. 统计
            if (productLineScores != null && !productLineScores.isEmpty()) {
                Integer compositeScore;
                Object tmpVal;
                for (SupplierTQRDCInfo prodScore : productLineScores) {
                    tmpVal = prodScore.getCompositeScore();
                    if (tmpVal == null) {
                        continue;
                    }
                    //该供应商得分
                    compositeScore = ((Number) tmpVal).intValue();
                    //2.1 按分值进行分值区间统计
                    supplierLevelCountDto.incrCountByScore(compositeScore);
                }
            }
            response.setResult(supplierLevelCountDto);
        } catch (Exception e) {
            log.error("Error execute `countCompositeScoreOfMonth` caused by:" + e, e);
            response.setError("Failed to countCompositeScoreOfMonth");
        }

        return response;
    }

    @Override
    public Response<Boolean> create(SupplierTQRDCInfo supplierTQRDCInfo) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            supplierTQRDCInfoDao.create(supplierTQRDCInfo);
            resp.setResult(true);
            return resp;
        } catch (Exception e) {
            resp.setError("import.supplier.tqrdc.info.fail");
            log.error("Faild to create `SupplierTQRDCInfo` with param:{}", supplierTQRDCInfo, e);
            return resp;
        }
    }


    @Override
    public Response<Map<Integer, SupplierLevelCountDto>> countCompositeScoreByProductLineOfMonth(String month) {

        //0. 构造返回结果
        Response<Map<Integer, SupplierLevelCountDto>> response = new Response<Map<Integer, SupplierLevelCountDto>>();
        Map<Integer, SupplierLevelCountDto> productLineSupplierCount = Maps.newHashMap();

        try {

            //1. 查询数据
            List<SupplierTQRDCInfo> productLineScores = supplierTQRDCInfoDao.findCompositeScoreOfMonth(month);

            if (productLineScores != null && !productLineScores.isEmpty()) {
                Integer productLineId;
                Integer compositeScore;
                Object tmpVal;

                //1.1 map 产品线，reduce 供应商数量
                for (SupplierTQRDCInfo prodScore : productLineScores) {
                    tmpVal = prodScore.getProductLineId();
                    if (tmpVal == null) {
                        continue;
                    }
                    productLineId = ((Number) tmpVal).intValue();
                    SupplierLevelCountDto supplierCountOfProduct = productLineSupplierCount.get(productLineId);

                    if (supplierCountOfProduct == null) {
                        supplierCountOfProduct = SupplierLevelCountDto.create(productLineId);
                        productLineSupplierCount.put(productLineId, supplierCountOfProduct);
                    }
                    tmpVal = prodScore.getCompositeScore();
                    if (tmpVal == null) {
                        continue;
                    }
                    //该产品线的得分
                    compositeScore = ((Number) tmpVal).intValue();
                    //当前产品线的
                    supplierCountOfProduct.incrCountByScore(compositeScore);
                }

            }
            response.setResult(productLineSupplierCount);
        } catch (Exception e) {
            log.error("Error execute `countCompositeScoreByProductLineOfMonth` caused by:" + e, e);
            response.setError("Failed to countCompositeScoreByProductLineOfMonth");
        }

        return response;
    }


    @Override
    public Response<Integer> execTransfer(Map<String, Object> params) {
        Response<Integer> resp = new Response<Integer>();
        try {
            resp.setResult(supplierTQRDCInfoManager.batchImportTQRDCInfos(params));
            return resp;
        } catch (Exception e) {
            if (e instanceof NoSuchFieldException || e instanceof IllegalAccessException) {
                log.error("Illegal access fileds or SupplierTQRDCInfo, please check the reflection section.");
            }
            log.error("Faild to transfer data of `snz_supplier_tqrdc_infos_tmp` with param:{}", params, e);
            resp.setError("transfer snz_supplier_tqrdc_infos_tmp failed");
            return resp;
        }
    }

    @Override
    public Response<Boolean> updateSupplierStatus(String month) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            SupplierTQRDCInfo supplierTQRDCInfo = supplierTQRDCInfoDao.findByMonthOne(month);
            if (supplierTQRDCInfo.getCompositeScore() < 70) {
                Boolean result = supplierTQRDCInfoDao.updateSupplierStatusSmall(month);
                resp.setResult(result);
                return resp;
            }
            if (supplierTQRDCInfo.getCompositeScore() > 80) {
                Boolean result = supplierTQRDCInfoDao.updateSupplierStatusBig(month);
                resp.setResult(result);
                return resp;
            }
            resp.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("failed to update supplier status , cause:{}", e);
            resp.setError("supplier.status.update.failed");
        }
        return resp;
    }

    @Override
    public Response<Boolean> supplierSummaryByModule() {
        Response<Boolean> result = new Response<Boolean>();

        try {

            List<FrontendCategory> frontendCategories = frontendCategoryService.findByLevels(1).getResult();
            if (frontendCategories == null || frontendCategories.isEmpty()) {
                log.error("frontendCategory not found where level=1");
                result.setError("frontendCategory.not.found");
                return result;
            }

            String lastMonth = supplierTQRDCInfoDao.findMaxMonth();

            List<SupplierModuleCount> supplierModuleCounts = Lists.newArrayList();

            for (FrontendCategory frontendCategory : frontendCategories) {

                SupplierModuleCount supplierModuleCount = new SupplierModuleCount();
                supplierModuleCount.setModuleId(frontendCategory.getId());
                supplierModuleCount.setModuleName(frontendCategory.getName());

                List<SupplierTQRDCInfo> supplierTQRDCInfos = supplierTQRDCInfoDao.findByMonthAndModule(lastMonth, frontendCategory.getName());

                if (supplierTQRDCInfos != null && !supplierTQRDCInfos.isEmpty()) {
                    supplierModuleCount.setSupplierCount(supplierTQRDCInfos.size());

                    for (SupplierTQRDCInfo supplierTQRDCInfo : supplierTQRDCInfos) {
                        supplierModuleCount.incrCountByScore(supplierTQRDCInfo.getCompositeScore());
                    }
                }

                supplierModuleCounts.add(supplierModuleCount);
            }

            supplierTQRDCInfoManager.createOrUpdateSupplierSummaryByModule(supplierModuleCounts);
            result.setResult(Boolean.TRUE);
        } catch (Exception e) {
            log.error("fail to summary supplier by module,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("summary.supplier.fail");
        }

        return result;
    }

    @Override
    public Response<SupplierModuleCountDto> getSupplierSummaryByModule() {
        Response<SupplierModuleCountDto> result = new Response<SupplierModuleCountDto>();

        try {

            SupplierModuleCountDto supplierModuleCountDto = new SupplierModuleCountDto();

            List<SupplierModuleCount> supplierModuleCounts = supplierModuleCountDao.findAll();
            if (supplierModuleCounts == null) {
                supplierModuleCounts = Collections.emptyList();
            } else {
                int supplierTotal = 0;
                int bestTotal = 0;
                int standardTotal = 0;
                int limitedTotal = 0;
                int badTotal = 0;

                for (SupplierModuleCount supplierModuleCount : supplierModuleCounts) {
                    supplierTotal += supplierModuleCount.getSupplierCount();
                    bestTotal += supplierModuleCount.getBestCount();
                    standardTotal += supplierModuleCount.getStandardCount();
                    limitedTotal += supplierModuleCount.getLimitedCount();
                    badTotal += supplierModuleCount.getBadCount();
                }
                supplierModuleCountDto.setSupplierTotal(supplierTotal);
                supplierModuleCountDto.setBestTotal(bestTotal);
                supplierModuleCountDto.setStandardTotal(standardTotal);
                supplierModuleCountDto.setLimitedTotal(limitedTotal);
                supplierModuleCountDto.setBadTotal(badTotal);
            }

            supplierModuleCountDto.setSupplierModuleCounts(supplierModuleCounts);
            result.setResult(supplierModuleCountDto);

        } catch (Exception e) {
            log.error("fail to get supplier summary by module,cause:{}", Throwables.getStackTraceAsString(e));
            result.setError("get.supplier.summary.fail");
        }

        return result;
    }
}