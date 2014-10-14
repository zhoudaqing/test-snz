/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.interceptor;

import com.google.common.net.HttpHeaders;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.SettingHelper;
import io.terminus.pampas.engine.exception.NotFound404Exception;
import io.terminus.pampas.engine.model.App;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应该是第一个 Interceptor
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-22
 */
@Slf4j
public class AppInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Setting setting;
    @Autowired
    private SettingHelper settingHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String domain = Splitters.COLON.splitToList(request.getHeader(HttpHeaders.HOST)).get(0).toLowerCase();
        App app = settingHelper.findByDomain(domain);
        if (app == null) {
            throw new NotFound404Exception("app not found");
        }
        Setting.setCurrentDomain(domain);
        Setting.setCurrentApp(app);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Setting.clearCurrentDomain();
        Setting.clearCurrentApp();
    }
}
