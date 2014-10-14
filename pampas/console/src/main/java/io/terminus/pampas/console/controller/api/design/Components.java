/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.api.design;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.pampas.console.service.ConfigAgentService;
import io.terminus.pampas.console.service.DesignMetaAgentService;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.config.model.Component;
import io.terminus.pampas.engine.config.model.FrontConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* User: AnsonChan
* Date: 13-11-28
*/
@Controller
@RequestMapping("/api/design/components")
public class Components {
    @Autowired
    private ConfigAgentService configAgentService;
    @Autowired
    private DesignMetaAgentService designMetaAgentService;

    @RequestMapping(value = "render", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String render(@RequestParam String group, @RequestParam String app,
                         @RequestParam("template") String template, @RequestParam Map<String, Object> context) {
        context.remove("template");
        context.put(RenderConstants.DESIGN_MODE, true);
        String html = designMetaAgentService.renderComponent(group, app, template, context);
        if (Strings.isNullOrEmpty(html)) {
            throw new JsonResponseException(404, "组件未找到或者渲染出错");
        }
        return html;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> listCategory(@RequestParam("mode") String modeName) {
        modeName = modeName.toUpperCase();
        Component.Mode mode = Component.Mode.valueOf(modeName);
        Map<String, Object> categoryMap = Maps.newHashMap();
        for (Component.ComponentCategory category : mode.getCategories()) {
            categoryMap.put(category.name(), category.getName());
        }
        return categoryMap;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Component> list(@RequestParam String group, @RequestParam String app,
                                @RequestParam("category") String categoryName) {
        categoryName = categoryName.toUpperCase();
        Component.ComponentCategory category = Component.ComponentCategory.valueOf(categoryName);
        if (category == null) {
            throw new JsonResponseException(400, "category not found");
        }
        if (!category.isDesignable()) {
            throw new JsonResponseException(400, "category isnt designable");
        }
        FrontConfig frontConfig = configAgentService.getFrontConfig(group, app);
        if (frontConfig == null || frontConfig.getComponentCategoryListMap() == null) {
            return Collections.emptyList();
        }
        return frontConfig.getComponentCategoryListMap().get(category);
    }
}
