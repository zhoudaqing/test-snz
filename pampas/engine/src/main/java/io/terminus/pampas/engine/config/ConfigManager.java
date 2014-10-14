/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.model.*;
import io.terminus.pampas.engine.dao.AppDao;
import io.terminus.pampas.engine.model.App;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-25
 */
@org.springframework.stereotype.Component
@Slf4j
public class ConfigManager implements Runnable {
    private final ConcurrentMap<String, FrontConfig> frontConfigMap = Maps.newConcurrentMap();
    private final ConcurrentMap<String, BackConfig> backConfigMap = Maps.newConcurrentMap();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private Setting setting;
    @Autowired
    private AppDao appDao;
    @Autowired
    private FileLoaderHelper fileLoaderHelper;

    @PostConstruct
    private void init() {
        load();
        // 每 5 分钟 check 一次，如果是 devMode 则改为 5 秒一次
        if (setting.isDevMode()) {
            executorService.scheduleAtFixedRate(this, 5, 5, TimeUnit.SECONDS);
        } else {
            executorService.scheduleAtFixedRate(this, 5, 5, TimeUnit.MINUTES);
        }
    }

    public void reloadAll() {
        load();
    }

    public void reload(String appKey) {
        List<App> apps = listAllApp();
        for (App app : apps) {
            if (app.getKey().equals(appKey)) {
                loadByApp(app);
            }
        }
    }

    public List<App> listAllApp() {
        List<App> apps;
        if (setting.getMode() == Setting.Mode.IMPLANT) {
            apps = ImmutableList.of(setting.getImplantApp());
        } else {
            apps = appDao.listAll();
            for (App app : apps) {
                if (Strings.isNullOrEmpty(app.getAssetsHome())) {
                    app.setAssetsHome(setting.getRootPath() + app.getKey() + "/");
                }
                if (Strings.isNullOrEmpty(app.getConfigPath())) {
                    app.setConfigPath(setting.getRootPath() + app.getKey() + "/" + App.BACK_CONFIG_FILE);
                }
                if (Strings.isNullOrEmpty(app.getProxyRoot())) {
                    app.setProxyRoot("http://" + app.getDomain() + "/");
                }
                if (Strings.isNullOrEmpty(app.getConfigJsFile())) {
                    app.setConfigJsFile("assets/scripts/config.js");
                }
            }
        }
        return apps;
    }

    public FrontConfig getFrontConfig(String appKey) {
        return frontConfigMap.get(appKey);
    }

    public FrontConfig getFrontConfig() {
        return getFrontConfig(Setting.getCurrentAppKey());
    }

    public BackConfig getBackConfig(String appKey) {
        return backConfigMap.get(appKey);
    }

    public BackConfig getBackConfig() {
        return getBackConfig(Setting.getCurrentAppKey());
    }

    public Service findService(String appKey, String serviceKey) {
        BackConfig backConfig = backConfigMap.get(appKey);
        if (backConfig == null || backConfig.getServices() == null) {
            return null;
        }
        return backConfig.getServices().get(serviceKey);
    }

    public List<Mapping> findMappings(String appKey) {
        FrontConfig frontConfig = frontConfigMap.get(appKey);
        if (frontConfig == null) {
            return null;
        }
        return frontConfig.getMappings();
    }

    public List<Component> findComponents(String appKey) {
        FrontConfig frontConfig = frontConfigMap.get(appKey);
        if (frontConfig == null) {
            return Lists.newArrayList();
        }
        Map<String, Component> components = frontConfig.getComponents();
        if (components == null || components.isEmpty()) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(components.values());
    }

    public Component findComponent(String appKey, String componentPath) {
        FrontConfig frontConfig = frontConfigMap.get(appKey);
        if (frontConfig == null || frontConfig.getComponents() == null) {
            return null;
        }
        return frontConfig.getComponents().get(componentPath);
    }

    public List<Component> findComponentsByCategory(String appKey, Component.ComponentCategory category) {
        FrontConfig frontConfig = frontConfigMap.get(appKey);
        if (frontConfig == null || frontConfig.getComponentCategoryListMap() == null) {
            return Lists.newArrayList();
        }
        return frontConfig.getComponentCategoryListMap().get(category);
    }

    public Render findRender(String appKey) {
        FrontConfig frontConfig = frontConfigMap.get(appKey);
        if (frontConfig == null) {
            return null;
        }
        return frontConfig.getRender();
    }

    private synchronized void load() {
        List<App> apps = listAllApp();
        for (App app : apps) {
            loadByApp(app);
        }
    }

    private void loadByApp(App app) {
        try {
            String appKey = app.getKey();
            if (Strings.isNullOrEmpty(app.getAssetsHome())) {
                log.warn("app [{}] have no assetsHome", appKey);
            } else {
                String frontConfigPath = app.getAssetsHome() + App.FRONT_CONFIG_FILE;
                FrontConfig frontConfig = frontConfigMap.get(appKey);
                FileLoader.Resp frontConfigResp;
                if (frontConfig == null) {
                    frontConfigResp = fileLoaderHelper.load(frontConfigPath);
                } else {
                    frontConfigResp = fileLoaderHelper.load(frontConfigPath, frontConfig.getSign());
                }
                if (frontConfigResp.isNotFound()) {
                    log.warn("front config not found for app [{}] and path [{}]", app.getKey(), frontConfigPath);
                } else if (!frontConfigResp.isNotModified()) {
                    frontConfig = ConfigParser.parseFrontConfig(app, frontConfigResp.asString());
                    frontConfig.setLocation(frontConfigPath);
                    frontConfig.setSign(frontConfigResp.getSign());
                    frontConfig.setLoadedAt(DateTime.now().toDate());
                    frontConfigMap.put(app.getKey(), frontConfig);
                    log.info("load app [{}] front config success, config: {}", app, frontConfig);
                }
            }
            if (Strings.isNullOrEmpty(app.getConfigPath())) {
                log.warn("app [{}] have no configPath", appKey);
            } else {
                BackConfig backConfig = backConfigMap.get(appKey);
                FileLoader.Resp backConfigResp;
                if (backConfig == null) {
                    backConfigResp = fileLoaderHelper.load(app.getConfigPath());
                } else {
                    backConfigResp = fileLoaderHelper.load(app.getConfigPath(), backConfig.getSign());
                }
                if (backConfigResp.isNotFound()) {
                    log.warn("back config not found for app [{}] and path [{}]", app.getKey(), app.getConfigPath());
                } else if (!backConfigResp.isNotModified()) {
                    backConfig = ConfigParser.parseBackConfig(app, backConfigResp.asString());
                    backConfig.setLocation(app.getConfigPath());
                    backConfig.setSign(backConfigResp.getSign());
                    backConfig.setLoadedAt(DateTime.now().toDate());
                    backConfigMap.put(app.getKey(), backConfig);
                    log.info("load app [{}] back config success, config: {}", app, backConfig);
                }
            }
        } catch (RuntimeException e) {
            log.error("error when load config for app [{}]", app);
            // 如果是嵌入模式就把错误抛出去
            if (setting.getMode() == Setting.Mode.IMPLANT) {
                throw e;
            }
        }
    }

    @Override
    public void run() {
        try {
            load();
        } catch (Exception e) {
            log.error("error happened when config load heartbeat", e);
        }
    }
}
