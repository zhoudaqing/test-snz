/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@ToString
public class BackConfig extends BaseConfig {
    private static final long serialVersionUID = 5571843585899076268L;
    @Getter
    @Setter
    private Map<String, Service> services;
}
