package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.user.service.SupplierIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by yangzefeng on 14-7-5
 */
@Component @Slf4j
public class SupplierSearchDumper {

    @Autowired
    private SupplierIndexService supplierIndexService;

    //todo:zookeeper 做一发同步控制
    /**
     * run every midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @RedisLock(keyName = "SUPPLIER_FULL_DUMP", maxWait = 1000, expiredTime = 10000)
    public void fullDump() {
        log.info("[CRON-JOB]search supplier refresh job begin");
        supplierIndexService.fullDump();
        log.info("[CRON-JOB]search supplier refresh job ends");
    }

    /**
     * run every 15 minutes;
     */
    @Scheduled(cron = "0 0/15 * * * ?")  //每隔15分钟触发一次
    @RedisLock(keyName = "SUPPLIER_DELTA_DUMP", maxWait = 1000, expiredTime = 10000)
    public void deltaDump() {
        log.info("[DELTA_DUMP_ITEM] supplier delta dump start");
        supplierIndexService.deltaDump(15);
        log.info("[DELTA_DUMP_ITEM] supplier delta finished");
    }
}
