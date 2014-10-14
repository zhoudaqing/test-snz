/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.UserNotLoginException;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.Component;
import io.terminus.pampas.engine.mapping.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author: Anson Chan
 */
@org.springframework.stereotype.Component
@Slf4j
public class HandlebarEngine {

    private Handlebars handlebars;

    private Invoker invoker;

    private final LoadingCache<String, Optional<Template>> caches;

    private ConfigManager configManager;

    @Autowired
    public HandlebarEngine(Invoker invoker,
                           Setting setting,
                           ConfigManager configManager,
                           ServletContext servletContext) {
        this.invoker = invoker;
        TemplateLoader templateLoader = new GreatTemplateLoader(servletContext, "/views", ".hbs");
        this.handlebars = new Handlebars(templateLoader);
        this.caches = initCache(!setting.isDevMode());
        this.configManager = configManager;
    }

    private LoadingCache<String, Optional<Template>> initCache(boolean buildCache) {
        if (buildCache) {
            return CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(new CacheLoader<String, Optional<Template>>() {
                @Override
                public Optional<Template> load(String path) throws Exception {
                    Template t = null;
                    try {
                        t = handlebars.compile(path);
                    } catch (Exception e) {
                        log.error("failed to compile template(path={}), cause:{}",path, e.getMessage());
                    }
                    return Optional.fromNullable(t);
                }
            });
        }
        return null;
    }

    public <T> void registerHelper(String name, Helper<T> helper) {
        handlebars.registerHelper(name, helper);
    }

    public String execInline(String templateStr, Map<String, Object> params) {
        return execInline(templateStr, params, null);
    }

    public String execInline(String templateStr, Map<String, Object> params, String cacheKey) {
        try {
            if (params == null) {
                params = Maps.newHashMap();
            }
            Template template;
            if (caches == null || cacheKey == null) {
                template = handlebars.compileInline(templateStr);
            } else {
                template = caches.getUnchecked("inline/" + cacheKey).orNull();
            }
            if(template == null){
                log.error("failed to exec handlebars' template:{}", templateStr);
                return "";
            }
            return template.apply(params);
        } catch (Exception e) {
            log.error("exec handlebars' template failed: {},cause:{}", templateStr, Throwables.getStackTraceAsString(e));
            return "";
        }

    }

    @SuppressWarnings("unchecked")
    public String execPath(String path, Map<String, Object> params, boolean isComponent) throws FileNotFoundException {
        try {
            if (params == null) {
                params = Maps.newHashMap();
            }

            Template template;
            if (isComponent) {
                if (caches == null) {
                    template = handlebars.compile("component:" + path);
                } else {
                    template = caches.getUnchecked("component:" + path).orNull();
                }
                params.put(RenderConstants.COMPONENT_PATH, path);
            } else {
                if (caches == null) {
                    template = handlebars.compile(path);
                } else {
                    template = caches.getUnchecked(path).orNull();
                }
            }

            params.put(RenderConstants.USER, UserUtil.getCurrentUser()); //user
            params.put(RenderConstants.HREF, configManager.getFrontConfig().getCurrentHrefs(Setting.getCurrentDomain()));
            if(template == null){
                log.error("failed to exec handlebars' template:path={}", path);
                return "";
            }
            return template.apply(params);

        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, FileNotFoundException.class);
            if (e instanceof HandlebarsException) {
                Throwables.propagateIfInstanceOf(e.getCause(), UserNotLoginException.class);
            }
            log.error("failed to execute handlebars' template(path={}),cause:{} ",
                    path, Throwables.getStackTraceAsString(e));
        }

        return "";
    }

    public String execComponent(final Component component, final Map<String, Object> context) {
        if (!Strings.isNullOrEmpty(component.getService())) {
            Object object = null;
            try {
                object = invoker.invoke(component.getService(), context);
            } catch (UserNotLoginException e) {
                if (context.get(RenderConstants.DESIGN_MODE) == null) {
                    throw e; // 非 DESIGN_MODE 时未登录需要抛出
                }
            } catch (Exception e) {
                log.error("error when invoke component, component: {}", component, e);
                context.put(RenderConstants.ERROR, e.getMessage());
            }
            context.put(RenderConstants.DATA, object);
        }
        try {
            return execPath(component.getPath(), context, true);
        } catch (Exception e) {
            log.error("failed to execute handlebars' template(path={}),cause:{} ",
                    component.getPath(), Throwables.getStackTraceAsString(e));
        }
        return "";
    }

}