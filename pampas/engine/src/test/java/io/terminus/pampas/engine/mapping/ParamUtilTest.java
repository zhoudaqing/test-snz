/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.client.ParamUtil;
import io.terminus.pampas.engine.model.App;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-27
 */
public class ParamUtilTest {
    @Export(paramNames = "p")
    public void testMethod(Map<String, List<App>> p) {

    }
    @Export(paramNames = "p")
    public void testMethod2(App p) {

    }

    @Test
    public void testParametricTypeConvert() {
        Method[] methods = ParamUtilTest.class.getDeclaredMethods();
        JavaType javaType = null;
        for (Method method : methods) {
            if (!Objects.equal(method.getName(), "testMethod")) {
                continue;
            }
            ParamUtil.MethodInfo methodInfo = ParamUtil.getMethodInfo(new ParamUtilTest(), method);
            javaType = methodInfo.getParams().get("p").getJavaType();
        }
        Map<String, List<App>> map = Maps.newHashMap();
        List<App> appList1 = Lists.newArrayList();
        App app1 = new App();
        app1.setKey("app1");
        app1.setDesc("wtf123");
        appList1.add(app1);
        List<App> appList2 = Lists.newArrayList();
        App app2 = new App();
        app2.setKey("app2");
        app2.setDesc("wtf234");
        appList2.add(app2);
        App app3 = new App();
        app3.setKey("app3");
        app3.setDesc("wtf345");
        appList2.add(app3);
        map.put("key1", appList1);
        map.put("key2", appList2);
        String jsonStr = JsonMapper.nonEmptyMapper().toJson(map);
        System.out.println(JsonMapper.nonEmptyMapper().fromJson(jsonStr, javaType));
    }

    @Test
    public void testNormalTypeConvert() {
        Method[] methods = ParamUtilTest.class.getDeclaredMethods();
        JavaType javaType = null;
        for (Method method : methods) {
            if (!Objects.equal(method.getName(), "testMethod2")) {
                continue;
            }
            ParamUtil.MethodInfo methodInfo = ParamUtil.getMethodInfo(new ParamUtilTest(), method);
            javaType = methodInfo.getParams().get("p").getJavaType();
        }
        App app1 = new App();
        app1.setKey("app1");
        app1.setDesc("wtf123");
        String jsonStr = JsonMapper.nonEmptyMapper().toJson(app1);
        System.out.println(JsonMapper.nonEmptyMapper().fromJson(jsonStr, javaType));
    }
}
