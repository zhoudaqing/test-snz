package io.terminus.snz.haier.event;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Desc:haier对接事件注册处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-16.
 */
@Component
public class PLMEventBus {

    private final AsyncEventBus asyncEventBus;

    public PLMEventBus() {
        //初始化线程池
        this.asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(3));
    }


    public void register(PLMEvents eventObj) {
        asyncEventBus.register(eventObj);
    }


    public void post(Object eventValue) {
        asyncEventBus.post(eventValue);
    }


    public void unregister(Object eventObj) {
        asyncEventBus.unregister(eventObj);
    }
}
