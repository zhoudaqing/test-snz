package io.terminus.snz.message.components;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.terminus.snz.message.models.Mail;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
@Slf4j
public class MailCenter {
    protected final EventBus eventBus;

    protected MailCenter(Integer threadCount) {
        this(threadCount, null);
    }

    protected MailCenter(List<Object> listeners) {
        this(Runtime.getRuntime().availableProcessors() + 1, listeners);
    }

    protected MailCenter(Integer threadCount, List<Object> listeners) {
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadCount));
        log.info("Mail Center has started!");
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
        log.info("register a MailListener({})", listener);
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
     * @param mail 消息传输对象
     */
    public void publish(Mail<?> mail) {
        eventBus.post(mail);
    }
}
