/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import com.google.common.html.HtmlEscapers;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.Component;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-11-22
 */
@org.springframework.stereotype.Component
@Slf4j
public class RenderHelpers {
    @Autowired
    private HandlebarEngine handlebarEngine;
    @Autowired
    private ConfigManager configManager;

    @PostConstruct
    private void init() {
        handlebarEngine.registerHelper("inject", new Helper<String>() {
            @Override
            public CharSequence apply(String compPath, Options options) throws IOException {
                boolean isDesignMode = options.get(RenderConstants.DESIGN_MODE) != null;
                // prepare context
                Map<String, Object> tempContext = Maps.newHashMap();
                if (options.context.model() instanceof Map) {
                    //noinspection unchecked
                    tempContext.putAll((Map<String, Object>) options.context.model());
                    // remove CDATA which may from parent inject
                    @SuppressWarnings("unchecked")
                    Set<String> cdataKeys = (Set<String>) tempContext.remove(RenderConstants.CDATA_KEYS);
                    if (cdataKeys != null) {
                        for (String key : cdataKeys) {
                            tempContext.remove(key);
                        }
                    }
                    if (isDesignMode) {
                        // remove COMPONENT_DATA which may from parent inject when designMode
                        tempContext.remove(RenderConstants.COMPONENT_DATA);
                    }
                }
                if (options.tagType == TagType.SECTION && StringUtils.isNotBlank(options.fn.text())) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> config = JsonMapper.nonEmptyMapper().fromJson(options.fn.text(), Map.class);
                    if (config != null && !config.isEmpty()) {
                        tempContext.put(RenderConstants.CDATA_KEYS, config.keySet());
                        tempContext.putAll(config);
                        if (isDesignMode) {
                            tempContext.put(RenderConstants.COMPONENT_DATA, HtmlEscapers.htmlEscaper().escape(options.fn.text().trim()));
                        }
                    }
                }
                Component component = new Component();
                component.setPath(compPath);
                Object firstParam = options.<Object>param(0, true);
                if (firstParam.equals(true)) {
                    Component result = configManager.findComponent(Setting.getCurrentAppKey(), compPath);
                    if (result == null) {
                        log.warn("can't find component config for path:{}", compPath);
                    } else {
                        component = result;
                    }
                } else if (firstParam instanceof String) {
                    component.setService((String) firstParam);
                }
                if (isDesignMode) {
                    String[] paths = compPath.split("/");
                    tempContext.put(RenderConstants.COMPONENT_NAME, MoreObjects.firstNonNull(component.getName(), paths[paths.length - 1]));
                }
                return new Handlebars.SafeString(handlebarEngine.execComponent(component, tempContext));
            }
        });

        handlebarEngine.registerHelper("component", new Helper<String>() {

            @Override
            public CharSequence apply(String className, Options options) throws IOException {
                @SuppressWarnings("unchecked")
                boolean isDesignMode = options.get(RenderConstants.DESIGN_MODE) != null;
                className = className + " eve-component";
                Object customClassName = options.context.get(RenderConstants.CLASS);
                StringBuilder compOpenTag = new StringBuilder("<div class=\"").append(className);
                if (customClassName != null) {
                    compOpenTag.append(" ").append(customClassName);
                }
                compOpenTag.append("\"");
                Object style = options.context.get(RenderConstants.STYLE);
                if (style != null) {
                    compOpenTag.append(" style=\"").append(style).append("\"");
                }
                Object compName = options.context.get(RenderConstants.COMPONENT_NAME);
                if (compName != null) {
                    compOpenTag.append(" data-comp-name=\"").append(compName).append("\"");
                }
                Object compData = options.context.get(RenderConstants.COMPONENT_DATA);
                if (compData != null) {
                    compOpenTag.append(" data-comp-data=\"").append(compData).append("\"");
                }
                Object compPath = options.context.get(RenderConstants.COMPONENT_PATH);
                if (compPath != null) {
                    compOpenTag.append(" data-comp-path=\"").append(compPath).append("\"");
                }
                if (isDesignMode) {
                    compOpenTag.append(" data-comp-class=\"").append(className).append("\"");
                }
                compOpenTag.append(" >");
                return new Handlebars.SafeString(compOpenTag.toString() + options.fn() + "</div>");
            }
        });
    }
}
