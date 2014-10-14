/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import io.terminus.pampas.engine.model.App;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-19
 */
public class TestProviderImpl implements TestProvider, TestProvider2 {
    @Override
    public App call(String param1, String param2) {
        App app = new App();
        app.setKey(param1);
        app.setDomain(param2);
        return app;
    }

    @Override
    public App callComplexParam(Map<String, List<App>> p) {
        return p.values().iterator().next().get(0);
    }

    @Override
    public App callNaiveParam(int a, boolean b, char c) {
        App app = new App();
        app.setKey(String.valueOf(a));
        app.setDomain(String.valueOf(b));
        app.setDesc(String.valueOf(c));
        return app;
    }

    @Override
    public App call2(String param1, Param2 param2) {
        App app = new App();
        app.setKey(param1);
        app.setDomain(param2.getS1());
        app.setDesc(param2.getS2());
        return app;
    }
}
