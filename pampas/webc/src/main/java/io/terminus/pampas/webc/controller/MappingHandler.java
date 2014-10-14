/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.controller;

import com.google.common.net.MediaType;
import io.terminus.common.utils.Joiners;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.client.Action;
import io.terminus.pampas.client.action.LoginAction;
import io.terminus.pampas.client.action.LogoutAction;
import io.terminus.pampas.engine.ActionEngine;
import io.terminus.pampas.engine.ActionInvoker;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.model.Mapping;
import io.terminus.pampas.engine.mapping.Invoker;
import io.terminus.pampas.engine.utils.Domains;
import io.terminus.pampas.webc.utils.CookieBuilder;
import io.terminus.pampas.webc.utils.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-29
 */
@Component
public class MappingHandler {
    @Autowired
    private Invoker invoker;
    @Autowired
    private ActionEngine actionEngine;

    @PostConstruct
    public void init() {
        this.actionEngine.registerOnce(LoginAction.class, new ActionInvoker<LoginAction>() {
            @Override
            public boolean invoke(LoginAction loginAction, HttpServletRequest request, HttpServletResponse response) {
                if (loginAction.getUserId() == null) {
                    return false;
                }
                LoginInfo loginInfo = new LoginInfo(loginAction.getUserId(), request.getRemoteAddr());
                // 默认放到移除第一个子域的根域下
                String domain = Setting.getCurrentDomain().equals("localhost") ? "localhost" : "." + Domains.removeSubDomain(Setting.getCurrentDomain());
                Cookie cookie = CookieBuilder.from(LoginInfo.SESSION_ID, loginInfo.toCookieKey(), domain)
                        .httpOnly()
                        .maxAge(loginAction.getMaxAge())
                        .build();
                response.addCookie(cookie);
                return false;
            }
        });
        this.actionEngine.registerOnce(LogoutAction.class, new ActionInvoker<LogoutAction>() {
            @Override
            public boolean invoke(LogoutAction action, HttpServletRequest request, HttpServletResponse response) {
                // 默认放到移除第一个子域的根域下
                String domain = Setting.getCurrentDomain().equals("localhost") ? "localhost" : "." + Domains.removeSubDomain(Setting.getCurrentDomain());
                Cookie cookie = CookieBuilder.from(LoginInfo.SESSION_ID, null, domain)
                        .httpOnly()
                        .maxAge(0)
                        .build();
                response.addCookie(cookie);
                return false;
            }
        });
    }

    public boolean handle(String path, HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        String method = request.getMethod().toUpperCase();
        Mapping mapping = invoker.mappingMatch(Setting.getCurrentAppKey(), Joiners.COLON.join(method, path));
        if (mapping == null) {
            return false;
        }
        response.setContentType(MediaType.JSON_UTF_8.toString());
        Object result = invoker.mappingInvoke(mapping, path, context);
        if (result instanceof Action) {
            if (actionEngine.handler((Action) result, request, response)) {
                return true;
            }
        }

        Object realResult = result instanceof Action ? ((Action) result).getData() : result;
        try {
            response.getWriter().write(JsonMapper.JSON_NON_EMPTY_MAPPER.toJson(realResult));
        } catch (IOException e) {
            // ignore this
        }
        return true;
    }
}
