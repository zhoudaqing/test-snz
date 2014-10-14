/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
public class AppWithConfigInfo implements Serializable {
    private static final long serialVersionUID = 4841183019498081777L;
    @Getter
    @Setter
    private App app;
    @Getter
    @Setter
    private ConfigInfo frontConfig;
    @Getter
    @Setter
    private ConfigInfo backConfig;

    public static class ConfigInfo implements Serializable {
        private static final long serialVersionUID = -4371408950758643247L;
        @Getter
        @Setter
        private String sign;
        @Getter
        @Setter
        private Date loadedAt;

        public ConfigInfo(String sign, Date loadedAt) {
            this.sign = sign;
            this.loadedAt = loadedAt;
        }
    }
}
