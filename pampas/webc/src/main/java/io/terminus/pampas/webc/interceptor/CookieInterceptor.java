/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.interceptor;

import com.google.common.collect.Maps;
import io.terminus.pampas.common.InnerCookie;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.webc.utils.CookieBuilder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-15
 */
public class CookieInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> cookieValues = Maps.newHashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieValues.put(cookie.getName(), cookie.getValue());
            }
        }
        InnerCookie innerCookie = new InnerCookie(cookieValues);
        UserUtil.putInnerCookie(innerCookie);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        InnerCookie innerCookie = UserUtil.getInnerCookie();
        for (InnerCookie.FakeCookie fakeCookie : innerCookie.getNewCookies()) {
            CookieBuilder cookieBuilder = CookieBuilder.from(fakeCookie.getName(), fakeCookie.getValue(), fakeCookie.getDomain())
                    .path(fakeCookie.getPath()).maxAge(fakeCookie.getAge());
            if (fakeCookie.isHttpOnly()) {
                cookieBuilder.httpOnly();
            }
            response.addCookie(cookieBuilder.build());
        }
        for (InnerCookie.FakeCookie fakeCookie : innerCookie.getDelCookies()) {
            CookieBuilder cookieBuilder = CookieBuilder.from(fakeCookie.getName(), null, fakeCookie.getDomain())
                    .path(fakeCookie.getPath()).maxAge(0);
            response.addCookie(cookieBuilder.build());
        }
        UserUtil.clearInnerCookie();
    }
}
