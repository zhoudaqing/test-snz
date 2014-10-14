/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;


import com.google.common.collect.Maps;
import io.terminus.pampas.client.Action;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by houly on 14-6-22.
 */
@Component
@Slf4j
public class ActionEngine {

    private final Map<Class<? extends Action>, ActionInvoker<? extends Action>> actions;

    public ActionEngine() {
        this.actions = Maps.newHashMap();
    }

    public <T extends Action> ActionEngine register(Class<T> clazz, ActionInvoker<T> action) {
        checkNotNull(clazz, "action name can not be null");
        checkNotNull(action, "action can not be null");

        ActionInvoker oldAction = actions.put(clazz, action);
        if (oldAction != null) {
            log.warn("action {} has been replaced by {}", clazz.getName(), oldAction);
        }
        return this;
    }


    public <T extends Action> ActionEngine registerOnce(Class<T> clazz, ActionInvoker<T> action) {
        checkNotNull(clazz, "action name can not be null");
        checkNotNull(action, "action can not be null");

        if (actions.get(clazz) != null) {
            log.warn("action {} can only be registered for one time ", clazz.getName());
            return this;
        }

        return register(clazz, action);
    }

    public <T extends Action> boolean handler(T action, HttpServletRequest request, HttpServletResponse response) {
        if (action == null) {
            log.warn("action is null, ignore this");
            return false;
        }
        //noinspection unchecked
        ActionInvoker<T> invoker = (ActionInvoker<T>) actions.get(action.getClass());
        if (invoker == null) {
            log.error("action invoker for class {} is null", action.getClass().getName());
            throw new NullPointerException("action invoker for class " + action.getClass().getName() + " is null");
        }
        try {
            return invoker.invoke(action, request, response);
        } catch (Exception e) {
            log.error("error when invoke ActionInvoker {} for action {}", invoker.getClass().getName(), action);
            invoker.onException(action, request, response, e);
            return false;
        }
    }
}
