package io.terminus.snz.haier.event;

import com.google.common.eventbus.Subscribe;
import io.terminus.snz.haier.manager.PLMModuleManager;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Desc:plm对接事务处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-09.
 */
@Slf4j
@Component
public class PLMEventHandler implements PLMEvents {

    private final PLMEventBus eventBus;

    private final PLMModuleManager plmModuleManager;

    @Autowired
    public PLMEventHandler(PLMEventBus eventBus , PLMModuleManager plmModuleManager){
        this.eventBus = eventBus;
        this.plmModuleManager = plmModuleManager;
    }

    @PostConstruct
    public void init() {
        eventBus.register(this);
    }

    @Subscribe
    @Override
    public void sendModuleToPLM(Requirement requirement) {
        //向plm中间表写入数据
        plmModuleManager.sendToPLM(requirement);
    }
}
