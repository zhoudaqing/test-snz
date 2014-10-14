/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-29
 */
public interface Agent {
    LinkedHashMap<String, String> getParamsInfo(String key);

    WrapResp call(String key, Map<String, Object> params, Map<String, Object> context);
}
