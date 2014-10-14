/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import com.google.common.base.MoreObjects;
import io.terminus.pampas.engine.model.App;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.PostConstruct;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-22
 */
public class Setting {
    private static ThreadLocal<App> currentApp = new ThreadLocal<App>();
    private static ThreadLocal<String> currentDomain = new ThreadLocal<String>();

    @Getter
    @Setter
    private Mode mode;

    @Getter
    @Setter
    private String rootPath;

    @Getter
    @Setter
    private App implantApp;

    @Getter
    @Setter
    private String registryId;

    @Getter
    @Setter
    private boolean devMode = false;

    @PostConstruct
    private void init() {
        switch (mode) {
            case IMPLANT:
                checkNotNull(implantApp, "implantApp in [Setting] should not be null when IMPLANT mode");
                checkNotNull(implantApp.getAssetsHome(), "assetsHome in implantApp should not be null");
                checkNotNull(implantApp.getConfigPath(), "configPath in implantApp should not be null");
                checkNotNull(implantApp.getDomain(), "domain in implantApp should not be null");
                checkNotNull(implantApp.getKey(), "key in implantApp should not be null");
                implantApp.setAssetsHome(normalize(implantApp.getAssetsHome()));
                break;
            case CENTER:
                checkNotNull(rootPath, "rootPath in [Setting] should not be null when CENTER mode");
                rootPath = normalize(rootPath);
                break;
        }
    }

    public static enum Mode {
        CENTER, IMPLANT
    }

    public static void setCurrentApp(App app) {
        currentApp.set(app);
    }

    public static App getCurrentApp() {
        return currentApp.get();
    }

    public static void clearCurrentApp() {
        currentApp.remove();
    }

    public static String getCurrentAppKey() {
        return getCurrentApp().getKey();
    }

    public static void setCurrentDomain(String domain) {
        currentDomain.set(domain);
    }

    public static String getCurrentDomain() {
        return MoreObjects.firstNonNull(currentDomain.get(), getCurrentApp().getDomain());
    }

    public static void clearCurrentDomain() {
        currentDomain.remove();
    }

    private String normalize(String rootPath) {
        if (rootPath.endsWith("/")) {
            return rootPath;
        }
        return rootPath + "/";
    }
}
