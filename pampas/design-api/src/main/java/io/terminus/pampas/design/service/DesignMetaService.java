/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import io.terminus.pampas.design.model.DesignMetaInfo;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-17
 */
public interface DesignMetaService {
    DesignMetaInfo getMetaInfo();

    String renderTemplate(String appKey, Long siteInstanceId, String path, Map<String, Object> context);

    String renderComponent(String appKey, String template, Map<String, Object> context);
}
