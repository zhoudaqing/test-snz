package io.terminus.snz.message.components;

import com.google.common.collect.Maps;
import io.terminus.snz.message.models.Mail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
public class MailListenerTest {
    @Spy
    private JavaMailSender mailSender = new JavaMailSenderImpl();

    @InjectMocks
    private MailListener mailListener;

    @Before
    public void init(){
        ((JavaMailSenderImpl) mailSender).setHost("10.135.1.205");
        ((JavaMailSenderImpl) mailSender).setPort(25);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListen() {
        Map<String, String> context = Maps.newHashMap();
        context.put("companyName", "屌爆公司");
        context.put("requirementName", "酷炫需求");
        Mail<Map<String, String>> mail = new Mail<Map<String, String>>();
        mail.setType(Mail.Type.REQUIREMENT_INVITATION);
        mail.setTo("v@terminus.io");
        mail.setData(context);
        mailListener.listen(mail);
    }
}
