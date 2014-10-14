package io.terminus.snz.statistic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

/**
 * Desc:dubbo启动程序
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-10.
 */
@Slf4j
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/statistic-dubbo-provider.xml", "spring/statistic-dubbo-consumer.xml");
        ac.start();
        log.info("statistic service started successfully");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("Shutdown hook was invoked. Shutting down statistic Service.");
                ac.close();
            }
        });
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
