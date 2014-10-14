package io.terminus.snz.user.service;

import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.services.SmsService;
import io.terminus.snz.user.dao.SupplierContactDao;
import io.terminus.snz.user.dao.SupplierModuleCountDao;
import io.terminus.snz.user.dao.SupplierTQRDCInfoDao;
import io.terminus.snz.user.dao.TQRDCManagerContactDao;
import io.terminus.snz.user.model.SupplierContact;
import io.terminus.snz.user.model.SupplierModuleCount;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import io.terminus.snz.user.model.TQRDCManagerContact;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述: 供应商绩效信息短信发送接口
 *
 * @author wanggen on 14-9-12.
 */
@Service
@Slf4j
public class TQRDCInfoSendServiceImpl implements TQRDCInfoSendService {


    @Autowired
    private SmsService smsService;                              // 短信发送服务

    @Autowired
    private SupplierContactDao supplierContactDao;              // 供应商联系方式DAO

    @Autowired
    private TQRDCManagerContactDao tqrdcManagerContactDao;      // 供应商绩效海尔管理者联系方式DAO

    @Autowired
    private SupplierTQRDCInfoDao supplierTQRDCInfoDao;          // 供应商绩效数据DAO

    @Autowired
    private SupplierModuleCountDao supplierModuleCountDao;      // 供应商TQRDC区位统计DAO


    final DateTimeFormatter M_FMT = DateTimeFormat.forPattern("YYYY-MM");

    /**
     * 发送供应商绩效信息给所有供应商
     *
     * @return 发送短信响应结果
     */
    @Override
    public Response<Boolean> sendTQRDCInfoToSuppliers() {
        List<SupplierContact> supplierContacts = supplierContactDao.listAll();
        String month = M_FMT.print(DateTime.now());
        for (SupplierContact supplierContact : supplierContacts) {

            // NO.1 根据供应商联系人中的 supplierCode 查询供应商绩效
            String vCode = supplierContact.getSupplierCode();
            List<SupplierTQRDCInfo> tqrdcInfos = supplierTQRDCInfoDao.findBySupplierCode(vCode, month, month);
            if (tqrdcInfos.size() > 0) {

                // NO.2 根据查询得到的供应商绩效构造短信数据
                Map context = buildTQRDCContext(tqrdcInfos);
                try {
                    String phone = supplierContact.getPhone();
                    smsService.sendWithTemplate("000000", phone, "supplier_tqrdc_notify", context);
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (Exception e) {
                    log.error("Error occurred for phone:[{}], context:[{}]", supplierContact.getPhone(), context, e);
                }
            }
        }
        return new Response<Boolean>();
    }


    /**
     * 发送供应商绩效数据给某个供应商
     *
     * @param supplierCode 供应商 V码
     * @param supplierName 供应商名称
     * @return 发送结果
     */
    @Override
    public Response<Boolean> sendTQRDCInfoToSupplier(String supplierCode, String supplierName) {
        return null;
    }


    /**
     * 发送供应商绩效汇总信息给领导者
     *
     * @return 发送结果
     */
    @Override
    public Response<Boolean> sendTQRDCSummaryToManagers() {
        log.info("Start send short messages to all tqrdc managers");

        // NO.1 加载所有按模块划分的供应商绩效在各分值区间的供应商数量
        Map<String, SupplierModuleCount> moduleSummaryCount = loadTQRDCSummaryInfos();

        // NO.2 加载所有供应商绩效管理者联系方式，并发送短信给他们
        List<TQRDCManagerContact> tqrdcManagerContacts = tqrdcManagerContactDao.listAll();
        for (TQRDCManagerContact tqrdcManagerContact : tqrdcManagerContacts) {
            sendShortMsgToManager(moduleSummaryCount, tqrdcManagerContact);
        }

        log.info("Finished to send short messages to all tqrdc managers:[{}]", tqrdcManagerContacts.size());
        return new Response<Boolean>();
    }


    /**
     * 加载供应商绩效按模块统计信息
     *
     * @return 模块名称 - 该模块的供应商统计数量按绩效统计
     */
    private Map<String, SupplierModuleCount> loadTQRDCSummaryInfos() {
        List<SupplierModuleCount> moduleCounts = supplierModuleCountDao.findAll();
        if (moduleCounts.isEmpty()) {
            log.error("There has no manager contact info");
        }
        Map<String, SupplierModuleCount> moduleSummaryCount = Maps.newHashMap();
        for (SupplierModuleCount moduleCount : moduleCounts) {
            moduleSummaryCount.put(moduleCount.getModuleName().replaceAll("(^发送)|(类$)|(模块$)", ""), moduleCount);
        }
        return moduleSummaryCount;
    }

    /**
     * 发生供应商绩效数据给供应商
     *
     * @param moduleSupplierCounts 模块-供应商统计 {"钣金模块"={bestCount=345,standardCount=34}, "xxx模块":{...}}
     * @param tqrdcManagerContact  该供应商的联系方式
     */
    private void sendShortMsgToManager(Map<String, SupplierModuleCount> moduleSupplierCounts, TQRDCManagerContact tqrdcManagerContact) {

        Map context;
        String phone = tqrdcManagerContact.getPhone();
        String template;

        // NO.1 根据当前联系人是【汇总】还是【模块】设置不同的模板和数据上下文
        String remark = tqrdcManagerContact.getRemark();
        if (remark != null && remark.contains("汇总")) {
            template = "tqrdc_manager_summary";
            context = buildAllSummaryContext(moduleSupplierCounts);
        } else {
            template = "tqrdc_manager_product";
            context = buildModuleSummaryContext(moduleSupplierCounts, remark);
        }
        if (context.isEmpty()) {
            log.warn("Has no summary tqrdc info for module manager:[{}]", tqrdcManagerContact);
            return;
        }

        // NO.2 发送短信消息
        try {
            smsService.sendWithTemplate("000000", phone, template, context);
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (Exception e) {
            log.error("Error occurred for phone:[{}], template:[{}], context:[{}]", phone, template, context, e);
        }

    }


    /**
     * 构建供应商绩效分布汇总上下文
     *
     * @param moduleCounts 模块-供应商数量统计
     * @return map hold 供应商绩效汇总信息
     */
    private Map buildAllSummaryContext(Map<String, SupplierModuleCount> moduleCounts) {
        Map<String, Object> context = Maps.newHashMap();
        int bestCount = 0,       //优选数量
                qualifiedCount = 0,  //合格数量
                restrictedCount = 0, //限制级数量
                rejectedCount = 0;   //淘汰级数量
        for (Map.Entry<String, SupplierModuleCount> entry : moduleCounts.entrySet()) {
            bestCount += entry.getValue().getBestCount();
            qualifiedCount += entry.getValue().getStandardCount();
            restrictedCount += entry.getValue().getLimitedCount();
            rejectedCount += entry.getValue().getBadCount();
        }
        if(bestCount==0 && qualifiedCount==0 && restrictedCount==0 && rejectedCount==0)
            return context;
        context.put("bestCount", bestCount);
        context.put("qualifiedCount", qualifiedCount);
        context.put("restrictedCount", restrictedCount);
        context.put("rejectedCount", rejectedCount);
        return context;
    }


    /**
     * 构造模块-供应商绩效得分统计汇总，模块绩效有别于总汇总：总汇总是所有模块在某个得分区间的供应商和；
     * 模块汇总只是指该模块的供应商的数量
     *
     * @param moduleSupplierCounts 模块-供应商分值区间统计
     * @param remark               用于区分该联系人时汇总还是【模块】还是【汇总】，例如：塑料类
     * @return 构造的统计信息上下文
     */
    private Map buildModuleSummaryContext(Map<String, SupplierModuleCount> moduleSupplierCounts, String remark) {
        Map<String, Object> context = Maps.newHashMap();
        String module = remark.replaceAll("(^发送)|(类$)|(模块$)", "");
        SupplierModuleCount moduleCount = moduleSupplierCounts.get(module);
        if (moduleCount == null) return context;
        context.put("bestCount", moduleCount.getBestCount());
        context.put("qualifiedCount", moduleCount.getStandardCount());
        context.put("restrictedCount", moduleCount.getLimitedCount());
        context.put("rejectedCount", moduleCount.getBadCount());
        context.put("product", remark.replaceAll("(^发送)", ""));
        return context;
    }


    /**
     * 将供应商绩效信息构建 put to map
     *
     * @param tqrdcInfos 供应商绩效数据
     * @return map 存储供应商绩效
     */
    private Map buildTQRDCContext(List<SupplierTQRDCInfo> tqrdcInfos) {
        SupplierTQRDCInfo tqrdcInfo = tqrdcInfos.get(0);
        Map<String, Object> context = Maps.newHashMap();
        context.put("supplierName", tqrdcInfo.getSupplierName());
        context.put("supplierCode", tqrdcInfo.getSupplierCode());
        context.put("month", tqrdcInfo.getMonth());
        context.put("day", DateTime.now().dayOfMonth().get());
        context.put("compositeScore", tqrdcInfo.getCompositeScore());
        context.put("tectScore", tqrdcInfo.getTechScore());
        context.put("qualityScore", tqrdcInfo.getQualityScore());
        context.put("deliverScore", tqrdcInfo.getDeliverScore());
        context.put("costScore", tqrdcInfo.getCostScore());
        context.put("respScore", tqrdcInfo.getRespScore());
        return context;
    }

}
