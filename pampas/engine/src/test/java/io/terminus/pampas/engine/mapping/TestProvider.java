/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.engine.model.App;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-19
 */
public interface TestProvider {
    @Export(paramNames = {"param1", "param2"})
    App call(String param1, String param2);
    @Export(paramNames = "p")
    App callComplexParam(Map<String, List<App>> p);
    @Export(paramNames = {"a", "b", "c"})
    App callNaiveParam(int a, boolean b, char c);
}
