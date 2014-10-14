package io.terminus.snz.user.event;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.model.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-27.
 */
@Component
@Slf4j
public class SupplierEventListener {

    private final SupplierEventBus eventBus;

    private final MessageService messageService;

    private final CompanyDao companyDao;

    @Autowired
    public SupplierEventListener(SupplierEventBus eventBus, MessageService messageService, CompanyDao companyDao) {
        this.eventBus = eventBus;
        this.messageService = messageService;
        this.companyDao = companyDao;
    }

    @PostConstruct
    public void init() {
        this.eventBus.register(this);
    }

    @Subscribe
    public void sendMsg(ApproveEvent approveEvent) {

        Message.Type msgType = null;
        switch (approveEvent.getApproveResult()) {
            case ENTER_FAIL:
                msgType = Message.Type.APPLIER_UNPASS;
                break;
            case ENTER_OK:
                msgType = Message.Type.APPLIER_PASS;
                break;
            case MODIFY_INFO_FAIL:
                msgType = Message.Type.APPLIER_INFO_UNPASS;
                break;
            case MODIFY_INFO_OK:
                msgType = Message.Type.APPLIER_INFO_PASS;
                break;
        }

        if (msgType == null) {
            log.error("unknown message type when approve supplier(user id={})", approveEvent.getReceiverId());
            return;
        }

        Company company = companyDao.findByUserId(approveEvent.getReceiverId());
        if (company == null) {
            log.error("company not found when send message after approve supplier(use id={})", approveEvent.getReceiverId());
            return;
        }

        Map<String, String> map = Maps.newHashMap();
        map.put("companyName", company.getCorporation());
        map.put("description", approveEvent.getDescription());
        map.put("approver", approveEvent.getApprover());
        map.put("mobile", approveEvent.getMobile());

        messageService.push(msgType, approveEvent.getSenderId(), approveEvent.getReceiverId(), map);
    }

}
