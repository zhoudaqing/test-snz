/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.service;

import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.model.App;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-22
 */
public interface AppService {
    List<App> listAll();

    Map<String, Object> listAllWithSetting();

    App findByDomain(String domain);

    App findByKey(String key);

    Response<String> create(App app);

    Response<Void> update(App app);

    Response<Void> updateExtraDomains(String key, Set<String> extraDomains);

    Set<String> getExtraDomains(String key);

    void delete(String key);
}
