package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.service.SupplierCreditQualifyService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Date: 9/19/14
 * Time: 19:38
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Slf4j
@Component
public class SupplierCreditJob {

    // 最长等待时间(5s)
    private static final long MAX_WAIT = 5000;

    // 锁的最长持有时间(1小时)
    private static final int EXPIRED_TIME = 1000 * 60 * 60;

    // 提醒超期
    private static final String NOTIFY_DELAYED_LOCK = "notify-delayed-lock";

    // 提醒即将超期
    private static final String NOTIFY_UPCOMING_LOCK = "notify-upcoming-lock";

    @Autowired
    private MessageService messageService;

    @Autowired
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Scheduled(cron = "0 0 3 * * *")
    @RedisLock(keyName = NOTIFY_DELAYED_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void delayedNotifier() {
        log.info("NOTIFY DELAYED start");

        Response<Boolean> tryNotify = supplierCreditQualifyService.notifyDelayed(3);
        if (!tryNotify.isSuccess()) {
            log.error("notify delayed failed!");
        }

        log.info("NOTIFY DELAYED end");
    }


    @Scheduled(cron = "0 30 3 * * *")
    @RedisLock(keyName = NOTIFY_UPCOMING_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void upComingNotifier() {
        log.info("NOTIFY UPCOMING start");

        Response<Boolean> tryNotify = supplierCreditQualifyService.notifyUpcomingIn(2);
        if (!tryNotify.isSuccess()) {
            log.error("notify delayed failed!");
        }

        log.info("NOTIFY UPCOMING end");
    }
}
