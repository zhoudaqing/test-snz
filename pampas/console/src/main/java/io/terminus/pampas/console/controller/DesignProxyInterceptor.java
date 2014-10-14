/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import io.terminus.pampas.console.service.ConfigAgentService;
import io.terminus.pampas.engine.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-20
 */
public class DesignProxyInterceptor implements HandlerInterceptor {
    private static final Pattern pattern = Pattern.compile("/design/pages/group/(.+?)/app/(.+?)\\?");

    @Autowired
    private ConfigAgentService configAgentService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (Strings.isNullOrEmpty(referer)) {
            return true;
        }
        Matcher match = pattern.matcher(referer);
        if (!match.find()) {
            return true;
        }
        String group = match.group(1);
        String appKey = match.group(2);
        String path = request.getRequestURI().substring(request.getContextPath().length() + 1);
        Map<String, Object> context = prepareContext(request);
        App app = configAgentService.getApp(group, appKey);
        String proxyUrl = app.getProxyRoot() + path;
        HttpRequest httpRequest = new HttpRequest(HttpRequest.append(proxyUrl, context), request.getMethod().toUpperCase());
        copyRequestHeader(httpRequest, request,
                HttpHeaders.ACCEPT, HttpHeaders.ACCEPT_ENCODING, HttpHeaders.ACCEPT_LANGUAGE, HttpHeaders.CONNECTION, HttpHeaders.USER_AGENT);
        response.setStatus(httpRequest.code());
        response.setContentLength(httpRequest.contentLength());
        response.setContentType(httpRequest.contentType());
        response.setHeader(HttpHeaders.CONTENT_ENCODING, httpRequest.contentEncoding());
        response.setHeader(HttpHeaders.SERVER, httpRequest.server());
        try {
            response.getOutputStream().write(httpRequest.bytes());
        } catch (IOException e) {
            // ignore this fucking error
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void copyRequestHeader(HttpRequest httpRequest, HttpServletRequest request, String... headers) {
        for (String header : headers) {
            httpRequest.header(header, request.getHeader(header));
        }
    }

    private Map<String, Object> prepareContext(HttpServletRequest request) {
        Map<String, Object> context = Maps.newHashMap();
        if (request != null) {
            for (Object name : request.getParameterMap().keySet()) {
                context.put((String) name, request.getParameter((String) name));
            }
        }
        return context;
    }
}
