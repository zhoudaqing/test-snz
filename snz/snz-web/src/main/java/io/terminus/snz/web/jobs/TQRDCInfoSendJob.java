package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.user.service.TQRDCInfoSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 功能描述: 供应商绩效短信发送定时任务
 *
 * @author wanggen on 14-9-12.
 */
@Component
@Slf4j
@Profile(value = {"prod","prod-standalone"})
public class TQRDCInfoSendJob {

    @Autowired
    private TQRDCInfoSendService tqrdcInfoSendService;

    @Scheduled(cron = "0 30 13 ? * MON")
    @RedisLock(keyName = "TQRDCInfoSendJob:sendShortMessage", maxWait = 1000*60, expiredTime = 1000*60)
    public void sendShortMessage() {
        log.info("Start TQRDC information short message sending");
        tqrdcInfoSendService.sendTQRDCInfoToSuppliers();
        tqrdcInfoSendService.sendTQRDCSummaryToManagers();
        log.info("Finish TQRDC information short message send");
    }

}
