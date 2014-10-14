package io.terminus.snz.eai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Bootstrap {

    public static void main(String[] args) throws Exception {
        final ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("spring/eai-dubbo-provider.xml","spring/eai-dubbo-consumer.xml");
        ac.start();
        log.info("snz-eai service started successfully");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                log.debug("Shutdown hook was invoked. Shutting down EAI Service.");
                ac.close();
            }
        });
        //prevent main thread from exit
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
