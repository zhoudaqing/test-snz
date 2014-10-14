package io.terminus.snz.requirement.event;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Desc:事件注册处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-09.
 */
@Component
public class RequirementEventBus {

    private final AsyncEventBus asyncEventBus;

    public RequirementEventBus() {
        //初始化线程池
        this.asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(3));
    }


    public void register(RequirementEvents eventObj) {
        asyncEventBus.register(eventObj);
    }


    public void post(Object eventValue) {
        asyncEventBus.post(eventValue);
    }


    public void unregister(Object eventObj) {
        asyncEventBus.unregister(eventObj);
    }
}
