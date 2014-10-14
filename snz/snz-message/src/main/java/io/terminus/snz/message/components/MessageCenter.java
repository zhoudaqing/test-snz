package io.terminus.snz.message.components;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.terminus.snz.message.dtos.MessageDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * 消息中心:
 * 实现异步消息发送
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-9
 */
@Slf4j
public class MessageCenter {

    protected final EventBus eventBus;

    protected MessageCenter(Integer threadCount) {
        this(threadCount, null);
    }

    protected MessageCenter(List<Object> listeners) {
        this(Runtime.getRuntime().availableProcessors() + 1, listeners);
    }

    protected MessageCenter(Integer threadCount, List<Object> listeners) {
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadCount));
        log.info("Message Center has started!");
        if (listeners != null) {
            for (Object listener : listeners) {
                subcribe(listener);
            }
        }
    }

    /**
     * 订阅监听器
     *
     * @param listener 监听器对象
     */
    public void subcribe(Object listener) {
        eventBus.register(listener);
        log.info("register a MessageListener({})", listener);
    }

    /**
     * 取消订阅
     *
     * @param listener 监听器对象
     */
    public void unsubcribe(Object listener) {
        eventBus.unregister(listener);
    }

    /**
     * 发布消息对象
     *
     * @param messageDto 消息传输对象
     */
    public void publish(MessageDto messageDto) {
        eventBus.post(messageDto);
    }
}
