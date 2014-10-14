/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.console.ServiceHelper;
import io.terminus.pampas.engine.config.model.BackConfig;
import io.terminus.pampas.engine.config.model.FrontConfig;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.model.AppWithConfigInfo;
import io.terminus.pampas.engine.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Service
public class ConfigAgentService {
    @Autowired
    private ServiceHelper serviceHelper;

    @Export(paramNames = {"group", "cell"})
    public List<AppWithConfigInfo> listAppWithConfigInfoByCell(String group, String cell) {
        ConfigService configService = serviceHelper.getService(ConfigService.class, group, cell);
        return configService.listAllAppWithConfigInfo();
    }

    public App getApp(String group, String appKey) {
        ConfigService configService = serviceHelper.getService(ConfigService.class, group);
        return configService.getApp(appKey);
    }

    @Export(paramNames = {"group", "cell", "app"})
    public FrontConfig getFrontConfig(String group, String cell, String app) {
        ConfigService configService = serviceHelper.getService(ConfigService.class, group, cell);
        return configService.getFrontConfig(app);
    }

    public FrontConfig getFrontConfig(String group, String app) {
        ConfigService configService = serviceHelper.getService(ConfigService.class, group);
        return configService.getFrontConfig(app);
    }

    @Export(paramNames = {"group", "cell", "app"})
    public BackConfig getBackConfig(String group, String cell, String app) {
        ConfigService configService = serviceHelper.getService(ConfigService.class, group, cell);
        return configService.getBackConfig(app);
    }
}
