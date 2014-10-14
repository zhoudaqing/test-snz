package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.user.service.SupplierResourceMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/14/14
 */
@Slf4j
@Component
public class SupplierQualifyJobs {

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    /**
     * 每天凌晨3点
     */
//    @Scheduled(cron = "0 0 3 * * ?")
//    @RedisLock(keyName = "SUPPLIER_QUALIFY_MAIL_SENDING", maxWait = 1000, expiredTime = 10000)
    public void autoSending() {
        log.info("[CRON-JOB]supplier qualify auto sending mail & message begin");
        supplierResourceMaterialService.bulkSendMessages();
        log.info("[CRON-JOB]supplier qualify auto sending mail & message end");
    }

    /**
     * 每天凌晨3点14分
     */
//    @Scheduled(cron = "0 14 3 * * ?")
//    @RedisLock(keyName = "SUPPLIER_QUALIFY_AUTO_FAIL", maxWait = 1000, expiredTime = 10000)
    public void autoFail() {
        log.info("[CRON-JOB]supplier qualify auto fail begin");
        supplierResourceMaterialService.bulkFailSuppliers();
        log.info("[CRON-JOB]supplier qualify auto fail end");
    }
}
