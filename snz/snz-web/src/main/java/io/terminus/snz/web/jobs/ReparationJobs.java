package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.service.ReparationService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 定时从中间表获取供应商绩效数据：现场不良，T-1不良，市场不良
 *
 * Date: 8/19/14
 * Time: 10:31
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Component
public class ReparationJobs {

    // 最长等待时间(5s)
    private static final long MAX_WAIT = 5000;

    // 锁的最长持有时间(1小时)
    private static final int EXPIRED_TIME = 1000 * 60 * 60;

    // 现场不良数据
    private static final String SCENE_BAD_RECORDS = "scene-bad-records-lock";

    // 市场不良数据
    private static final String MARKET_BAD_RECORDS = "market-bad-records-lock";

    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-MM-dd");

    @Autowired
    ReparationService reparationService;

    @Scheduled(cron = "0 0 4 * * *")
    @RedisLock(keyName = SCENE_BAD_RECORDS, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void syncOnSceneData() {
        log.info("SCENE_BAD_RECORDS started");

        DateTime now = DateTime.now().withTimeAtStartOfDay();
        String today = dtf.withLocale(Locale.CHINA).print(now.plusDays(1));
        String yesterDay = dtf.withLocale(Locale.CHINA).print(now.minusDays(1));

        Response<Boolean> trySync = reparationService.deltaDumpScene(yesterDay, today);
        if (trySync.isSuccess()) {
            log.info("SCENE_BAD_RECORDS invoke success.");
        } else {
            log.info("SCENE_BAD_RECORDS invoke fail");
        }

        log.info("SCENE_BAD_RECORDS ended");
    }

    @Scheduled(cron = "0 0 5 * * *")
    @RedisLock(keyName = MARKET_BAD_RECORDS, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void syncMarketDate() {
        log.info("MARKET_BAD_RECORDS started");

        DateTime now = DateTime.now().withTimeAtStartOfDay();
        String today = dtf.withLocale(Locale.CHINA).print(now.plusDays(1));
        String yesterDay = dtf.withLocale(Locale.CHINA).print(now.minusDays(1));

        Response<Boolean> trySync = reparationService.deltaSyncMarketData(yesterDay, today);
        if (trySync.isSuccess()) {
            log.info("MARKET_BAD_RECORDS invoke success.");
        } else {
            log.info("MARKET_BAD_RECORDS invoke fail");
        }

        log.info("MARKET_BAD_RECORDS ends");
    }
}
