/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.collect.Maps;
import io.terminus.pampas.client.ParamUtil;
import org.junit.Test;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-27
 */
public class ParamConverterTest {
    @Test
    public void testConverterWholeContext2Map() {
        Map<String, Object> context = Maps.newHashMap();
        context.put("a", 1);
        context.put("b", 2);
        Object obj = ParamConverter.convertParam("not exists", Map.class, context);
        Object result = ParamUtil.convert(obj, Map.class, null, context);
        System.out.println(result);
    }
}
