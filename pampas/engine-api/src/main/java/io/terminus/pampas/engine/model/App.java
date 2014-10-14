/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.model;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-21
 */
@ToString
public class App implements Serializable {
    public static final String FRONT_CONFIG_FILE = "front_config.yaml";
    public static final String BACK_CONFIG_FILE = "back_config.yaml";

    private static final long serialVersionUID = 6605245210822077562L;

    @Getter
    @Setter
    private String key;
    @Getter
    @Setter
    private String domain;
    @Setter
    private String proxyRoot;
    @Getter
    @Setter
    private String assetsHome;
    @Setter
    private String configJsFile;
    @Getter
    @Setter
    private String configPath;
    @Getter
    @Setter
    private String desc;

    public String getProxyRoot() {
        return Strings.isNullOrEmpty(proxyRoot) ? "http://" + domain + "/" : proxyRoot;
    }

    public String getConfigJsFile() {
        return Strings.isNullOrEmpty(configJsFile) ? "assets/scripts/config.js" : configJsFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof App)) return false;

        App app = (App) o;

        if (!key.equals(app.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
