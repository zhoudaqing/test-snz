/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.console.ServiceHelper;
import io.terminus.pampas.design.model.DesignMetaInfo;
import io.terminus.pampas.design.service.DesignMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-17
 */
@Service
public class DesignMetaAgentService {
    @Autowired
    private ServiceHelper serviceHelper;

    @Export(paramNames = "group")
    public DesignMetaInfo getMetaInfo(String group) {
        DesignMetaService designMetaService = serviceHelper.getService(DesignMetaService.class, group);
        if (designMetaService == null) {
            return null;
        }
        return designMetaService.getMetaInfo();
    }

    public String renderTemplate(String group, String app, Long siteInstanceId, String path, Map<String, Object> context) {
        DesignMetaService designMetaService = serviceHelper.getService(DesignMetaService.class, group);
        return designMetaService.renderTemplate(app, siteInstanceId, path, context);
    }

    public String renderComponent(String group, String app, String template, Map<String, Object> context) {
        DesignMetaService designMetaService = serviceHelper.getService(DesignMetaService.class, group);
        return designMetaService.renderComponent(app, template, context);
    }
}
