package io.terminus.snz.user.event;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-27.
 */
@Component
public class SupplierEventBus {
    private final AsyncEventBus eventBus;

    public SupplierEventBus() {
        this.eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));
    }

    public void register(SupplierEventListener eventListener) {
        eventBus.register(eventListener);
    }

    public void post(ApproveEvent event) {
        eventBus.post(event);
    }

    public void unRegister(SupplierEventListener eventListener) {
        eventBus.unregister(eventListener);
    }
}
