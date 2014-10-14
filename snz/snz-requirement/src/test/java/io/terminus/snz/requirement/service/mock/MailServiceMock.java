package io.terminus.snz.requirement.service.mock;

import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.services.MailService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-15.
 */
@Service
public class MailServiceMock implements MailService {
    @Override
    public void send(Mail<?> mail) {

    }

    @Override
    public void batchSend(List<Mail<?>> mails) {

    }
}
