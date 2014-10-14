/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.api.userEvent;

import com.google.common.base.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-03-10
 */
public abstract class SessionEvent {
    protected final long userId;
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;

    public SessionEvent(long userId, HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        this.userId = userId;
        this.request = request;
    }

    public long getUserId() {
        return userId;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getCookie(String name) {
        for (Cookie cookie : request.getCookies()) {
            if (Objects.equal(cookie.getName(), name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
