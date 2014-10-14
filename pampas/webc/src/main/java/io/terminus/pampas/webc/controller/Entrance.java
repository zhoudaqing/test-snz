/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.controller;

import com.google.common.base.Strings;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.RenderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-29
 */
@Controller
@Slf4j
public class Entrance {
    @Autowired
    private ViewRender viewRender;
    @Autowired
    private AssetsHandler assetsHandler;
    @Autowired
    private MappingHandler mappingHandler;

    @RequestMapping
    public void doRequest(@RequestHeader("Host") String host,
                          HttpServletRequest request,
                          HttpServletResponse response,
                          Map<String, Object> context) {
        String domain = Splitters.COLON.splitToList(host).get(0);
        String path = request.getRequestURI().substring(request.getContextPath().length() + 1);
        if (Strings.isNullOrEmpty(path)) path = "index";
        context = prepareContext(request, context);
        boolean isAssets = assetsHandler.handle(path, response);
        if (isAssets) return;
        boolean hasMapping = mappingHandler.handle(path, request, response, context);
        if (hasMapping) return;
        viewRender.view(domain, path, response, context);
    }

    private Map<String, Object> prepareContext(HttpServletRequest request, Map<String, Object> context) {
        if (request != null) {
            for (Object name : request.getParameterMap().keySet()) {
                context.put((String) name, request.getParameter((String) name));
            }
        }
        context.put(RenderConstants.USER, UserUtil.getCurrentUser());
        return context;
    }
}
