/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import io.terminus.common.utils.MapBuilder;
import io.terminus.pampas.engine.config.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-19
 */
@Slf4j
public class DubboExecutorTest {
    @Test
    public void testExecutor() {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/dubbo-executor-reference-test.xml");
        ac.start();
        DubboExecutor dubboExecutor = ac.getBean(DubboExecutor.class);
        Service service = new Service();
        service.setApp("test-provider1");
        service.setType(Service.ServiceType.DUBBO);
        service.setUri("io.terminus.pampas.engine.mapping.TestProvider:call");
        Map<String, Object> params = MapBuilder.<String, Object>of()
                .put("param1", "value1", "param2", "value2")
                .map();
        Stopwatch firstTime = Stopwatch.createStarted();
        Object result = dubboExecutor.exec(service, params);
        log.info("time elapsed {}ms, invoke result {}", firstTime.elapsed(TimeUnit.MILLISECONDS), result);
        assertThat(result, instanceOf(Map.class));
        assertThat((Map)result, hasEntry("key", "value1"));
        assertThat((Map)result, hasEntry("domain", "value2"));
        Stopwatch hundredTimes = Stopwatch.createStarted();
        for (int i = 0; i < 100; i++) {
            dubboExecutor.exec(service, params);
        }
        log.info("100 times elapsed {}ms", hundredTimes.elapsed(TimeUnit.MILLISECONDS));
        Service service2 = new Service();
        service2.setApp("test-provider2");
        service2.setType(Service.ServiceType.DUBBO);
        service2.setUri("io.terminus.pampas.engine.mapping.TestProvider2:call2");
        String arg2 = "{\n" +
                      "  \"s1\": \"I'm s1\",\n" +
                      "  \"s2\": \"2\"\n" +
                      "}";
        Map<String, Object> params2 = MapBuilder.<String, Object>of()
                .put("arg1", "value3", "arg2", arg2)
                .map();
        Object result2 = dubboExecutor.exec(service2, params2);
        assertThat(result2, instanceOf(Map.class));
        assertThat((Map)result2, hasEntry("key", "value3"));
        assertThat((Map)result2, hasEntry("domain", "I'm s1"));
        assertThat((Map)result2, hasEntry("desc", "2"));
        Map<String, Object> params3 = MapBuilder.<String, Object>of()
                .put("arg1", "value3", "s1", "I'm s1", "s2", "2")
                .map();
        Object result3 = dubboExecutor.exec(service2, params3);
        assertThat(result3, instanceOf(Map.class));
        assertThat((Map)result3, hasEntry("key", "value3"));
        assertThat((Map)result3, hasEntry("domain", "I'm s1"));
        assertThat((Map)result3, hasEntry("desc", "2"));
        ac.close();
    }

    @Test
    public void testCallWithNaiveParam() throws Exception {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/dubbo-executor-reference-test.xml");
        ac.start();
        DubboExecutor dubboExecutor = ac.getBean(DubboExecutor.class);
        Service service = new Service();
        service.setApp("test-provider1");
        service.setType(Service.ServiceType.DUBBO);
        service.setUri("io.terminus.pampas.engine.mapping.TestProvider:callNaiveParam");
        Map<String, Object> context = Maps.newHashMap();
        context.put("a", "1");
        context.put("b", "true");
        context.put("c", 'c');
        System.out.println(dubboExecutor.exec(service, context));
    }

    @Test
    public void deployProvider1() throws InterruptedException {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/dubbo-executor-provider1-test.xml");
        ac.start();
        log.info("service started successfully");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("Shutdown hook was invoked. Shutting down Service.");
                ac.close();
            }
        });
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void deployProvider2() throws InterruptedException {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/dubbo-executor-provider2-test.xml");
        ac.start();
        log.info("service started successfully");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("Shutdown hook was invoked. Shutting down Service.");
                ac.close();
            }
        });
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
