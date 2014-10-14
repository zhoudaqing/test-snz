package io.terminus.snz.sns;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * dubbo start
 */
@Slf4j
public class Bootstrap {
    public static void main(String[] args) throws Exception {
        System.setProperty("spring.profiles.active", "test");
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/sns-dubbo-provider.xml", "spring/sns-dubbo-consumer.xml");
        ac.start();
        log.info("sns service started successfully");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("Shutdown hook was invoked. Shutting down SNS Service.");
                ac.close();
            }
        });
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
