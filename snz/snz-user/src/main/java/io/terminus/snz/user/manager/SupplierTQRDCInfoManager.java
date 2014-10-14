package io.terminus.snz.user.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.user.dao.SupplierModuleCountDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoTmpDao;
import io.terminus.snz.user.model.*;
import io.terminus.snz.user.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author wanggen on 14-8-8.
 */
@Service
@Slf4j
public class SupplierTQRDCInfoManager {

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;          //供应商绩效信息 DAO

    @Autowired
    private SupplierTQRDCInfoTmpDao supplierTQRDCInfoTmpDao;    //供应商临时表数据 DAO

    @Autowired
    private SupplierModuleCountDao supplierModuleCountDao;      //供应商按模块统计 DAO

    @Autowired
    TagService tagService;

    private List<String> locations = Lists.newArrayList("绿区", "蓝区", "黄区", "红区");

    private static final String SPL_INFO_KEY = "supplier_base_infos_key";
    private final LoadingCache<String, Map<String, SupplierBaseInfo>> supplierBaseInfoCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<String, Map<String, SupplierBaseInfo>>() {
        public Map<String, SupplierBaseInfo> load(String key) throws Exception {
            Map<String, SupplierBaseInfo> supplierBaseInfoMap = Maps.newHashMap();
            if (SPL_INFO_KEY.equals(key)) {
                List<SupplierBaseInfo> supplierBaseInfos = supplierTQRDCInfoDao.loadSupplierBaseInfos();
                if (supplierBaseInfos != null) {
                    for (SupplierBaseInfo info : supplierBaseInfos) {
                        if (!supplierBaseInfoMap.containsKey(info.getSupplierName())) {
                            supplierBaseInfoMap.put(info.getSupplierName(), info);
                        }
                    }
                    return supplierBaseInfoMap;
                }
            }
            return supplierBaseInfoMap;
        }
    });


    /**
     * 1. 若有模块分类（module不为空） 则按模块分组
     *    将模块内的供应商的绩效数据进行排名以及 ‘红区’ ‘黄区’ ‘蓝区’ ‘绿区’ 分区
     * 2. 若无模块类则不进行分组，直接将数据新增
     */
    @Transactional
    public int batchImportTQRDCInfos(Map<String, Object> params) throws InterruptedException, NoSuchFieldException, IllegalAccessException, ExecutionException {
        int count = 0;

        // NO.1 按日期查询临时表中供应商绩效数据
        List<SupplierTQRDCInfoTmp> tmpTqrdcList = supplierTQRDCInfoTmpDao.findByDate((String) params.get("date"));
        log.info("[{}] records found with param:[{}]", tmpTqrdcList == null ? 0 : tmpTqrdcList.size(), params);

        Map<String, List<SupplierTQRDCInfo>> tqrdcInfosOfModule = Maps.newHashMap();
        if (tmpTqrdcList != null && tmpTqrdcList.size()>0) {
            // NO.2 按模块将供应商绩效数据分组
            String month = (String) params.get("month");
            supplierTQRDCInfoDao.deleteByMonth(month);
            convertTQRDCInfos(tmpTqrdcList, tqrdcInfosOfModule, month);

            // NO.3 将按模块分组后的供应商按 T/Q/R/D/C 排序，并根据排名设置区域
            for (Map.Entry<String, List<SupplierTQRDCInfo>> entry : tqrdcInfosOfModule.entrySet()) {
                List<SupplierTQRDCInfo> infos = entry.getValue();
                if(infos==null || infos.size()==0)
                    continue;
                rankAndLocate(infos, "tech");
                rankAndLocate(infos, "quality");
                rankAndLocate(infos, "resp");
                rankAndLocate(infos, "delivery");
                rankAndLocate(infos, "cost");
                rankAndLocate(infos, "composite");
                count += infos.size();
                supplierTQRDCInfoDao.batchCreate(infos);    //将数据落地到数据库
                TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        log.info("Totally [{}] records were import into `snz_supplier_tqrdc_infos`", count);
        return count;
    }


    /**
     * 将临时表中的数据转换到本地对应POJO
     * @param tmpTqrdcList       临时TQRDC信息
     * @param tqrdcInfosOfModule 按模块划分的TQRDC信息
     * @param month              月份
     */
    private void convertTQRDCInfos(List<SupplierTQRDCInfoTmp> tmpTqrdcList, Map<String, List<SupplierTQRDCInfo>> tqrdcInfosOfModule, String month) {
        List<SupplierTQRDCInfo> tmpList;
        for (SupplierTQRDCInfoTmp supplierTQRDCInfoTmp : tmpTqrdcList) {
            SupplierTQRDCInfo tqrdc = new SupplierTQRDCInfo();
            supplierTQRDCInfoTmp.mapTo(tqrdc);
            setBaseInfo(tqrdc);
            tqrdc.setMonth(month);
            tmpList = tqrdcInfosOfModule.get(tqrdc.getModule());
            if (tmpList == null) {
                tqrdcInfosOfModule.put(tqrdc.getModule(), Lists.newArrayList(tqrdc));
            } else {
                tmpList.add(tqrdc);
            }
        }
    }


    /**
     * 为供应商绩效数据设置基础信息
     * @param tqrdc                落地表行实例
     */
    private void setBaseInfo(SupplierTQRDCInfo tqrdc) {
        try {
            String supplierName = tqrdc.getSupplierName();
            SupplierBaseInfo supplierBaseInfo = supplierBaseInfoCache.get(SPL_INFO_KEY).get(supplierName);
            if (supplierBaseInfo != null) {
                tqrdc.setUserId(supplierBaseInfo.getUserId());          //设置注册用户ID
                tqrdc.setCompanyId(supplierBaseInfo.getCompanyId());    //设置注册公司ID
                tqrdc.setModule(supplierBaseInfo.getModule());          //设置模块名称
                if (tqrdc.getCompositeScore() != null) {
                    if (tqrdc.getCompositeScore() >= 60) {
                        tagService.addSupplierStatusTag(tqrdc.getUserId(), User.SupplierTag.ALTERNATIVE);
                    } else {
                        tagService.addSupplierStatusTag(tqrdc.getUserId(), User.SupplierTag.DIE_OUT);
                    }
                } else {
                    tagService.addSupplierStatusTag(tqrdc.getUserId(), User.SupplierTag.DIE_OUT);
                }
            }
        } catch (ExecutionException e) {
            log.error("Failed to load company id, user id and module info for supplier:[{}]", tqrdc.getSupplierCode(), e);
        }
    }


    /**
     * <P>排名，按 TQRDC 各个得分值排名的同时设置各个供应商在排名上的区域</P>
     *
     * @param infos qtrdc List
     * @param field 当前排序字段，例如 field=tech，将按 techScore 降序排序，然后设置排名及区位
     */
    private void rankAndLocate(List<SupplierTQRDCInfo> infos, final String field) throws NoSuchFieldException, IllegalAccessException {
        String rightScoreFieldName = ("delivery".equals(field) ? "deliver" : field) + "Score";
        final Field scoreField = SupplierTQRDCInfo.class.getDeclaredField(rightScoreFieldName);
        scoreField.setAccessible(true);

        Collections.sort(infos, new Comparator<SupplierTQRDCInfo>() {
            public int compare(SupplierTQRDCInfo o1, SupplierTQRDCInfo o2) {
                try {
                    Integer o1Val = (Integer) scoreField.get(o1);
                    Integer o2Val = (Integer) scoreField.get(o2);
                    if (o1Val == null) {
                        scoreField.set(o1, 0);
                        o1Val = 0;
                    }
                    if (o2Val == null) {
                        scoreField.set(o2, 0);
                        o2Val = 0;
                    }
                    return o2Val - o1Val;
                } catch (IllegalAccessException e) {
                    log.error("Failed to get value of field:[{}] from SupplierTQRDCInfo", field, e);
                    return 0;
                }
            }
        });
        setLocations(infos, field);
    }


    /**
     * 根据供应商绩效得分排名划分区域
     * <PRE>
     *   [0%~10%)  绿区
     *   (10%~50%] 蓝区
     *   (50%~90]  黄区
     *   (90%~~~)  红区
     * </PRE>
     *
     * @param infos 供应商绩效
     * @param field 当前属性，将当前属性按得分降序排序，同时设置其得分区位
     */
    private void setLocations(List<SupplierTQRDCInfo> infos, String field) throws NoSuchFieldException, IllegalAccessException {
        // compositeScore 字段特殊
        String rightRankFieldName = "composite".equals(field) ? "rank" : (field + "ScoreRank");
        Field rankField = SupplierTQRDCInfo.class.getDeclaredField(rightRankFieldName);
        rankField.setAccessible(true);
        String rightLocationFieldName = "composite".equals(field) ? "location" : (field + "Location");
        Field locationField = SupplierTQRDCInfo.class.getDeclaredField(rightLocationFieldName);
        locationField.setAccessible(true);

        if (infos.size() <= 4) {
            for (int i = 0; i < infos.size(); i++) {
                locationField.set(infos.get(i), locations.get(i));
                rankField.set(infos.get(i), i + 1);
            }
        } else {
            int rank = 1, size = infos.size();
            double ratio;
            for (SupplierTQRDCInfo curr : infos) {
                if (rank == 1) {
                    locationField.set(curr, "绿区");
                    rankField.set(curr, rank++);
                } else {
                    ratio = (double) rank / (double) size;
                    if (ratio >= 0 && ratio <= 0.1) {
                        locationField.set(curr, "绿区");
                    } else if (ratio > 0.1 && ratio <= 0.5) {
                        locationField.set(curr, "蓝区");
                    } else if (ratio > 0.5 && ratio <= 0.9) {
                        locationField.set(curr, "黄区");
                    } else if (ratio > 0.9) {
                        locationField.set(curr, "红区");
                    }
                    rankField.set(curr, rank++);
                }
            }
        }
    }


    @Transactional
    public void createOrUpdateSupplierSummaryByModule(List<SupplierModuleCount> supplierModuleCounts) {
        if (supplierModuleCounts == null || supplierModuleCounts.isEmpty()) {
            return;
        }
        for (SupplierModuleCount supplierModuleCount : supplierModuleCounts) {
            SupplierModuleCount existed = supplierModuleCountDao.findByModuleId(supplierModuleCount.getModuleId());
            if (existed != null) {
                supplierModuleCountDao.update(supplierModuleCount);
            } else {
                supplierModuleCountDao.create(supplierModuleCount);
            }
        }
    }


}