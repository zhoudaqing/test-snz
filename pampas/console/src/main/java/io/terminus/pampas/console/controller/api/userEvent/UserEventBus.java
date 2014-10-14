/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.api.userEvent;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-03-10
 */
@Component
public class UserEventBus {

    private final AsyncEventBus eventBus;

    public UserEventBus() {
        this.eventBus = new AsyncEventBus(Executors.newFixedThreadPool(4));
    }


    public void register(Object object) {
        eventBus.register(object);
    }


    public void post(Object event) {
        eventBus.post(event);
    }


    public void unregister(Object object) {
        eventBus.unregister(object);
    }
}
