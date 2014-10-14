package io.terminus.snz.web.jobs;

import io.terminus.common.annotation.RedisLock;
import io.terminus.snz.requirement.service.ModuleService;
import io.terminus.snz.requirement.service.RequirementQuotaService;
import io.terminus.snz.requirement.service.RequirementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Desc:需求任务的job
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-09.
 */
@Component
@Slf4j
public class RequirementJobs {
    //最长等待时间(1s)
    private static final long MAX_WAIT = 1000;

    //锁的最长持有时间(1小时)
    private static final int EXPIRED_TIME = 1000 * 60 * 60;

    //提前多少天预警
    private static final int WARNING_DAY = 2;

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private RequirementQuotaService requirementQuotaService;

    private final static String TRANSITION_STATUS_LOCK = "requirement-transition-lock";

    private final static String WARNING_LOCK = "warning_lock";

    private final static String SET_MODULE_NUM_LOCK = "set-module-num-lock";

    private final static String SET_COMPANY_V_CODE_LOCK = "set-company-v-code-lock";

    private static final String COUNT_MODULE_DUMP_LOCK = "count-module-dump-lock";

    /**
     * 自动的需求时间状态切换 todo（章鱼的需求关闭）
     * 每天晚上1点触发自动状态切换
     @Scheduled(cron = "0 0 1 * * *")
     @RedisLock(keyName = TRANSITION_STATUS_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
     public void autoTransitionStatus() {
     log.info("auto requirement transition status job begin");
     requirementService.transitionExpire();
     log.info("auto requirement transition status job end");
     }
     */

    /**
     * 自动的查询预警（场景：新品、老品、衍生号，适用卓越运营）
     * 当阶段处于->承诺底线时，提前WARNING_DAY天提醒当提交方案的供应商数量少于3个的情况下会发送消息给采购商
     */
    @Scheduled(cron = "0 0 0 * * *")
    @RedisLock(keyName = WARNING_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void warning() {
        log.info("auto query requirement status warning info begin");
        requirementService.warningExpire(WARNING_DAY);
        log.info("auto query requirement status warning info end");
    }

    /**
     * 自动的写入模块的专用号
     */
    @Scheduled(cron = "0 0 1 * * *")
    @RedisLock(keyName = SET_MODULE_NUM_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void autoSetModuleNum() {
        log.info("auto module number setting job begin");
        moduleService.setModuleNum();
        log.info("auto module number setting job end");
    }

    /**
     * 自动的写入供应商V码
     */
    @Scheduled(cron = "0 0 2 * * *")
    @RedisLock(keyName = SET_COMPANY_V_CODE_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void autoSetCompanyVCode() {
        log.info("auto company set to plm job begin");
        requirementQuotaService.plmCompanyVExpire();
        log.info("auto company set to plm job end");
    }

    /**
     * 自动修复redis统计数据的异常（真心没必要加这个）
     */
    @Scheduled(cron = "0 0 1 * * *")
    @RedisLock(keyName = COUNT_MODULE_DUMP_LOCK, maxWait = MAX_WAIT, expiredTime = EXPIRED_TIME)
    public void countModuleDump(){
        log.info("count module dump job started");
        requirementService.dumpCountModuleToRedis();
        log.info("count module dump job ended");
    }
}
