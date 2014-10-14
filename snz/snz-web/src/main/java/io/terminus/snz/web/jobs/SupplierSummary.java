package io.terminus.snz.web.jobs;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.related.models.AddressPark;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.SupplierReportService;
import io.terminus.snz.user.service.SupplierSummaryService;
import io.terminus.snz.user.service.SupplierTQRDCInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-24.
 */
@Component
@Slf4j
public class SupplierSummary {

    //最长等待时间(1s)
    private static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1小时)
    private static final int EXPIRED_TIME = 1000 * 60 * 60;

    private final static String COUNT_SUPPLIER_LOCK = "count-supplier-lock";

    private final static String COUNT_SUPPLIER_BY_LEVEL_LOCK = "count-supplier-by-level-lock";

    private final static String COUNT_SUPPLIER_BY_MODULE_LOCK = "count-supplier-by-module-lock";

    private final static String COUNT_SUPPLIER_BY_DIMENSIONS_LOCK = "count-supplier-by-dimensions-lock";

    private final static String REMIND_OF_APPROVING_LOCK = "remind-of-approving-lock";

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private SupplierSummaryService supplierSummaryService;

    @Autowired
    private SupplierTQRDCInfoService supplierTQRDCInfoService;

    @Autowired
    private SupplierReportService supplierReportService;

    @Autowired
    private AccountService<User> accountService;

    /**
     * run every midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @RedisLock(keyName = COUNT_SUPPLIER_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void countSupplier() {

        log.info("[CRON-JOB]count supplier job begin");

        countSupplierBySupplyPark();
        supplierSummaryService.supplierSummaryByStatus();
        supplierSummaryService.supplierSummaryByIndustry();

        log.info("[CRON-JOB]count supplier job end");
    }

    @Scheduled(cron = "0 0 4 * * ?")
    @RedisLock(keyName = COUNT_SUPPLIER_BY_DIMENSIONS_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void countSupplierByDimensions() {

        log.info("[CRON-JOB]count supplier by dimensions job begin");

        supplierReportService.countSupplierByDimensions();

        log.info("[CRON-JOB]count supplier by dimensions job end");
    }

    /**
     * 每月5号0点
     */
    @Scheduled(cron = "0 0 0 5 * ?")
    @RedisLock(keyName = COUNT_SUPPLIER_BY_LEVEL_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void countSupplierByLevel() {

        log.info("[CRON-JOB]count supplier by level job begin");

        supplierSummaryService.supplierSummaryByLevel();

        log.info("[CRON-JOB]count supplier by level job end");
    }

    /**
     * 凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @RedisLock(keyName = COUNT_SUPPLIER_BY_MODULE_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void countSupplierByModule() {

        log.info("[CRON-JOB]count supplier by module job begin");

        supplierTQRDCInfoService.supplierSummaryByModule();

        log.info("[CRON-JOB]count supplier by module job end");
    }

    @Scheduled(cron = "0 0 6 * * ?")
    @RedisLock(keyName = REMIND_OF_APPROVING_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void remindOfApproving() {

        log.info("[CRON-JOB]remind of approving job begin");

        accountService.remindOfApproving();

        log.info("[CRON-JOB]remind of approving job end");
    }

    private void countSupplierBySupplyPark() {
        List<AddressPark> addressParks = deliveryService.findAllPark().getResult();
        List<Long> supplyParkIds = Lists.transform(addressParks, new Function<AddressPark, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable AddressPark addressPark) {
                return addressPark.getId();
            }
        });

        supplierSummaryService.supplierSummaryBySupplyParks(supplyParkIds);

    }

}
