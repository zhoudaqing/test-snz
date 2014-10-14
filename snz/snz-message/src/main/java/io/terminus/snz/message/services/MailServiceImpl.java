package io.terminus.snz.message.services;

import io.terminus.snz.message.components.MailCenter;
import io.terminus.snz.message.models.Mail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private MailCenter mailCenter;

    @Override
    public void send(Mail<?> mail) {
        mailCenter.publish(mail);
    }

    @Override
    public void batchSend(List<Mail<?>> mails) {
        for (Mail<?> mail : mails) {
            mailCenter.publish(mail);
        }
    }
}
