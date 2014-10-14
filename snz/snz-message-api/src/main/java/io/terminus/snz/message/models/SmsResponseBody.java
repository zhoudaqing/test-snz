package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SmsResponseBody {

    @Getter
    @Setter
    @XStreamAlias("Code")
    //响应码
    private String code;

    @Getter
    @Setter
    @XStreamImplicit
    //消息列表
    private List<SmsResponseMessage> messageList;


    public SmsResponseBody(String code, List<SmsResponseMessage> messageList) {
        this.code = code;
        this.messageList = messageList;
    }


}
