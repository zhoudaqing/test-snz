package io.terminus.snz.web.jobs;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.user.service.SupplierTQRDCInfoService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 导数据：From: snz_supplier_tqrdc_infos_tmp  To:snz_supplier_tqrdc_infos<BR>
 */
@Component
@Slf4j
public class SupplierTQRDCInfoTransJob {

    private static DateTimeFormatter MONTH_FMT = DateTimeFormat.forPattern("YYYY-MM");
    private static DateTimeFormatter DATE_FMT = DateTimeFormat.forPattern("YYYYMMdd");

    @Autowired
    private SupplierTQRDCInfoService supplierTQRDCInfoService;


    /**
     * 每日 23 点执行定时任务
     */
    @Scheduled(cron = "0 0 23 * * ?")
    @RedisLock(keyName = "SupplierTQRDCInfoTransJob:transTQRDCInfoFromTmpTable:", maxWait = 1000*60, expiredTime = 1000*60*60)
    public void transTQRDCInfoFromTmpTable() {

        DateTime today = DateTime.now();
        String month = MONTH_FMT.print(today);
        String date = DATE_FMT.print(today);

        log.info("Start transfer rows of:[{}] from table `snz_supplier_tqrdc_infos_tmp` to table `snz_supplier_tqrdc_infos`", date);
        Map<String, Object> params = Maps.newHashMap();
        params.put("date", date);
        params.put("month", month);
        supplierTQRDCInfoService.execTransfer(params);
        log.info("Finish transfer rows of:[{}] from table `snz_supplier_tqrdc_infos_tmp` to table `snz_supplier_tqrdc_infos`", date);
    }


    /**
     *  手工任务执行，在数据不稳定期间需手工执行任务导入数据，根据系统时间调整
     */
    //@Scheduled(cron = "0 37 16 * * ?")
    public void transTQRDCInfoFromTmpTable_TestJOB() {
		//List<String> dates = Lists.newArrayList("20140430", "20140531", "20140630", "20140731", "20140807");
        //List<String> dates = Lists.newArrayList("20140228", "20140331", "20140430", "20140531", "20140630", "20140731", "20140807");
        List<String> dates = Lists.newArrayList("20140913");
        for(String date: dates){
            log.info("Start transfer rows of:[{}] from table `snz_supplier_tqrdc_infos_tmp` to table `snz_supplier_tqrdc_infos`", date);
            Map<String, Object> params = Maps.newHashMap();
            String month = date.substring(0, 6);
            month = month.substring(0, 4)+"-"+month.substring(4);
            params.put("date", date);
            params.put("month", month);
            supplierTQRDCInfoService.execTransfer(params);
            log.info("Finish transfer rows of:[{}] from table `snz_supplier_tqrdc_infos_tmp` to table `snz_supplier_tqrdc_infos`", date);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @RedisLock(keyName = "SupplierTQRDCInfoTransJob:moduleManageStrategy:", maxWait = 1000*5, expiredTime = 1000*60*60)
    public void moduleManageStrategy(){

        DateTime today = DateTime.now();
        String month = MONTH_FMT.print(today);

        if (!(today.plusDays(1).monthOfYear().get() == today.plusMonths(1).monthOfYear().get())) {
            return;
        }
        log.info("Start to update supplier status");
        if(supplierTQRDCInfoService.updateSupplierStatus(month).getResult()){
            log.info("update supplier status success");
        }else {
            log.info("update supplier status failed");
        }

    }

}
