package io.terminus.snz.message.components;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import io.terminus.snz.message.models.Mail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
@Component @Slf4j
public class MailListener {
    @Autowired
    private JavaMailSender mailSender;

    @Subscribe
    public void listen(Mail<?> mail) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), Charsets.UTF_8.toString());
            helper.setSubject(mail.getType().getTitle());
            helper.setText(MailTemplates.build(mail.getType(), mail.getData()), true);
            helper.setTo(mail.getTo());
            helper.setFrom("noreply@ihaier.com");

            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            log.error("failed to handle mail, mail={}, cause:{}",
                    mail, Throwables.getStackTraceAsString(e));
        }
    }
}
