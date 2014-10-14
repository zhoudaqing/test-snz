/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import io.terminus.pampas.client.Action;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by houly on 14-6-22.
 */
@Slf4j
public abstract class ActionInvoker<T extends Action> {

    /**
     *
     * @param action Action
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return 如果处理完成不需要继续返回数据，则返回 true ；否则返回 false
     */
    public abstract boolean invoke(T action, HttpServletRequest request, HttpServletResponse response);

    public void onException(T action, HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("action {} invoke has exception", action, e);
    }
}
