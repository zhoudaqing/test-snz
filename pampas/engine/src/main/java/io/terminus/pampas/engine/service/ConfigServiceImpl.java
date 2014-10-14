/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.service;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.BackConfig;
import io.terminus.pampas.engine.config.model.FrontConfig;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.model.AppWithConfigInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ConfigManager configManager;

    @Override
    public List<AppWithConfigInfo> listAllAppWithConfigInfo() {
        List<App> appList = configManager.listAllApp();
        List<AppWithConfigInfo> result = Lists.newArrayList();
        for (App app : appList) {
            AppWithConfigInfo appWithConfigInfo = new AppWithConfigInfo();
            appWithConfigInfo.setApp(app);
            FrontConfig frontConfig = configManager.getFrontConfig(app.getKey());
            if (frontConfig != null) {
                appWithConfigInfo.setFrontConfig(new AppWithConfigInfo.ConfigInfo(frontConfig.getSign(), frontConfig.getLoadedAt()));
            }
            BackConfig backConfig = configManager.getBackConfig(app.getKey());
            if (backConfig != null) {
                appWithConfigInfo.setBackConfig(new AppWithConfigInfo.ConfigInfo(backConfig.getSign(), backConfig.getLoadedAt()));
            }
            result.add(appWithConfigInfo);
        }
        return result;
    }

    @Override
    public App getApp(String appKey) {
        List<App> apps = configManager.listAllApp();
        for (App app : apps) {
            if (Objects.equal(app.getKey(), appKey)) {
                return app;
            }
        }
        return null;
    }

    @Override
    public FrontConfig getFrontConfig(String appKey) {
        return configManager.getFrontConfig(appKey);
    }

    @Override
    public BackConfig getBackConfig(String appKey) {
        return configManager.getBackConfig(appKey);
    }
}
