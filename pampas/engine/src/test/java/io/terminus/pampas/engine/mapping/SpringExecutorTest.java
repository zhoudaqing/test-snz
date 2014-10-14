/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.engine.config.model.Service;
import io.terminus.pampas.engine.model.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/spring/spring-executor-test.xml"
})
public class SpringExecutorTest {
    @Autowired
    private SpringExecutor springExecutor;

    @Test
    public void testRunMethodWithComplexParametricTypeParam() {
        Service service = new Service();
        service.setApp("test");
        service.setType(Service.ServiceType.SPRING);
        service.setUri("io.terminus.pampas.engine.mapping.TestProvider:callComplexParam");
        Map<String, Object> context = Maps.newHashMap();
        Map<String, List<App>> p = Maps.newHashMap();
        List<App> list = Lists.newArrayList();
        App app = new App();
        app.setAssetsHome("123");
        app.setKey("appKey");
        app.setDesc("wtf");
        list.add(app);
        p.put("param", list);
        context.put("p", p);
        System.out.println(springExecutor.exec(service, context));
    }

    @Test
    public void testRunMethodWithNaiveParam() throws Exception {
        Service service = new Service();
        service.setApp("test");
        service.setType(Service.ServiceType.SPRING);
        service.setUri("io.terminus.pampas.engine.mapping.TestProvider:callNaiveParam");
        Map<String, Object> context = Maps.newHashMap();
        context.put("a", "1");
        context.put("b", "true");
        context.put("c", 'c');
        System.out.println(springExecutor.exec(service, context));
    }
}
