/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.service;

import io.terminus.pampas.engine.config.model.BackConfig;
import io.terminus.pampas.engine.config.model.FrontConfig;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.model.AppWithConfigInfo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
public interface ConfigService {
    List<AppWithConfigInfo> listAllAppWithConfigInfo();

    App getApp(String appKey);

    FrontConfig getFrontConfig(String appKey);

    BackConfig getBackConfig(String appKey);
}
