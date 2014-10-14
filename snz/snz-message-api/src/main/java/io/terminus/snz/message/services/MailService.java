package io.terminus.snz.message.services;

import io.terminus.snz.message.models.Mail;

import java.util.List;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
public interface MailService {
    void send(Mail<?> mail);

    void batchSend(List<Mail<?>> mails);
}
