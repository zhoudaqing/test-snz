/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.engine.model.App;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-19
 */
public interface TestProvider2 {
    @Export(paramNames = {"arg1", "arg2"})
    App call2(String param1, Param2 param2);

    class Param2 {
        @Getter
        @Setter
        private String s1;
        @Getter
        @Setter
        private String s2;
    }
}
