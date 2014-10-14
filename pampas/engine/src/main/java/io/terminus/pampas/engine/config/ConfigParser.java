/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.engine.config.model.*;
import io.terminus.pampas.engine.model.App;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@org.springframework.stereotype.Component
@Slf4j
public class ConfigParser {
    private static final Yaml yaml = new Yaml();

    @Autowired
    private FileLoaderHelper fileLoaderHelper;

    private final Map<String, BaseConfig> atomConfigCache = Maps.newHashMap();

    public <T extends BaseConfig> T parseConfig(App app, Class<T> configClass) {
        boolean isFront = configClass == FrontConfig.class;
        String configFilePath;
        if (isFront) {
            configFilePath = app.getAssetsHome() + App.FRONT_CONFIG_FILE;
        } else {
            configFilePath = app.getConfigPath();
        }
        return null;
    }

    private <T extends BaseConfig> T parseConfig0(App app, String path, Class<T> configClass) {
        FileLoader.Resp resp;
        BaseConfig config = atomConfigCache.get(path);
        if (config == null) {
            resp = fileLoaderHelper.load(path);
        } else {
            resp = fileLoaderHelper.load(path, config.getSign());
        }
        if (resp.isNotFound()) {
            log.warn("front config not found for app [{}] and path [{}]", app.getKey(), path);
        } else if (!resp.isNotModified()) {
            config = ConfigParser.parseFrontConfig(app, resp.asString());
            config.setLocation(path);
            config.setSign(resp.getSign());
            config.setLoadedAt(DateTime.now().toDate());
            atomConfigCache.put(path, config);
//            log.info("load app [{}] front config success, config: {}", app, frontConfig);
        }
        //noinspection unchecked
        return (T) config;
    }

    private String resolvePath(String originPath, String relativePath) {
        String originFolderPath = originPath.substring(0, originPath.lastIndexOf("/") + 1);
        return originFolderPath + relativePath;
    }

    public static BackConfig parseBackConfig(App app, String yamlStr) {
        BackConfig backConfig = yaml.loadAs(yamlStr, BackConfig.class);
        backConfig.setApp(app.getKey());
        // handle services
        if (backConfig.getServices() != null) {
            for (Service service : backConfig.getServices().values()) {
                if (Strings.isNullOrEmpty(service.getApp())) {
                    service.setApp(app.getKey());
                }
            }
        }
        return backConfig;
    }

    public static FrontConfig parseFrontConfig(App app, String yamlStr) {
        FrontConfig frontConfig = yaml.loadAs(yamlStr, FrontConfig.class);
        frontConfig.setApp(app.getKey());
        // handle components
        if (frontConfig.getComponents() != null) {
            for (String path : frontConfig.getComponents().keySet()) {
                Component component = frontConfig.getComponents().get(path);
                component.setPath(path);
                if (!component.getCategory().isDesignable()) {
                    continue;
                }
                List<Component> componentList = frontConfig.getComponentCategoryListMap().get(component.getCategory());
                if (componentList == null) {
                    componentList = Lists.newArrayList();
                    frontConfig.getComponentCategoryListMap().put(component.getCategory(), componentList);
                }
                componentList.add(component);
            }
        }
        // handle auths
        if (frontConfig.getAuths() != null && frontConfig.getAuths().getWhiteList() != null) {
            for (WhiteAuth whiteAuth : frontConfig.getAuths().getWhiteList()) {
                whiteAuth.setRegexPattern(Pattern.compile("^" + whiteAuth.getPattern() + "$"));
            }
        }
        if (frontConfig.getAuths() != null && frontConfig.getAuths().getProtectedList() != null) {
            for (ProtectedAuth protectedAuth : frontConfig.getAuths().getProtectedList()) {
                protectedAuth.setRegexPattern(Pattern.compile("^" + protectedAuth.getPattern() + "$"));
            }
        }
        return frontConfig;
    }
}
