/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.controller;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.net.MediaType;
import io.terminus.pampas.common.UserNotLoginException;
import io.terminus.pampas.engine.PageRender;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.FrontConfig;
import io.terminus.pampas.engine.exception.NotFound404Exception;
import io.terminus.pampas.engine.exception.Server500Exception;
import io.terminus.pampas.engine.exception.UnAuthorize401Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-09-18
 */
@Component
@Slf4j
public class ViewRender {
    @Autowired
    private PageRender pageRender;
    @Autowired
    private ConfigManager configManager;

    public void view(final String domain, final String path,
                     HttpServletResponse response,
                     final Map<String, Object> context) {
        Supplier<String> getHtml = new Supplier<String>() {
            @Override
            public String get() {
                return pageRender.render(domain, path, context);
            }
        };
        render(response, getHtml);
    }

    private void render(HttpServletResponse response, Supplier<String> getHtml) {
        String html = null;
        try {
            html = MoreObjects.firstNonNull(getHtml.get(), "");
        } catch (UserNotLoginException e) {
            try {
                response.sendRedirect(configManager.getFrontConfig().getCurrentHrefs(Setting.getCurrentDomain()).get(FrontConfig.HREF_LOGIN));
                return; //back
            } catch (IOException e1) {
                //ignore this fucking exception
            }
        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, NotFound404Exception.class);
            Throwables.propagateIfInstanceOf(e, Server500Exception.class);
            Throwables.propagateIfInstanceOf(e, UnAuthorize401Exception.class);
            log.error("render failed, cause:{}", Throwables.getStackTraceAsString(Throwables.getRootCause(e)));
            throw new Server500Exception(e.getMessage(), e);
        }
        response.setContentType(MediaType.HTML_UTF_8.toString());
        //response.setContentLength(html.getBytes(Charsets.UTF_8).length);
        try {
            response.getWriter().write(html);
        } catch (IOException e) {
            // ignore it
        }
    }
}
