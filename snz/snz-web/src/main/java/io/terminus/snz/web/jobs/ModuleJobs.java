package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.eai.service.MdmConfigureService;
import io.terminus.snz.requirement.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 模块相关的定时任务
 *
 * Date: 8/13/14
 * Time: 17:35
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Component
public class ModuleJobs {
    // 最长等待时间(5s)
    private static final long MAX_WAIT = 5000;

    // 锁的最长持有时间(1小时)
    private static final int EXPIRED_TIME = 1000 * 60 * 60;

    // 写入老品信息锁
    private static final String WRITE_OLD_MODULE_LOCK = "write-old-module-lock";

    // 从 MDM 同步老品信息
    private static final String SYNC_OLE_MODULE_LOCK = "sync-old-module-info";

    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-MM-dd");

    @Autowired
    ModuleService moduleService;

    @Autowired
    MdmConfigureService mdmConfigureService;

//    @Scheduled(cron = "0 0 0 * * *")
    @RedisLock(keyName = WRITE_OLD_MODULE_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void writeOldModuleLock() {
        log.info("write old module job started");
        moduleService.writeOldModule();
        log.info("write old module job ended");
    }

    @Scheduled(cron = "30 0 0 * * *")
    @RedisLock(keyName = SYNC_OLE_MODULE_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void syncOldModuleInfo() {
        log.info("sync ole module begin");
        DateTime now = DateTime.now().withTimeAtStartOfDay();
        String today = dtf.withLocale(Locale.CHINA).print(now.plusDays(1));
        String yesterDay = dtf.withLocale(Locale.CHINA).print(now.minusDays(1));

        mdmConfigureService.syncOldModuleFromMDM(yesterDay, today);
        log.info("sync ole module end");
    }
}
