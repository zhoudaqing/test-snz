/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.api.userEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-03-10
 */
public class LogoutEvent extends SessionEvent {

    public LogoutEvent(long userId, HttpServletRequest request, HttpServletResponse response) {
        super(userId, request, response);
    }
}
