package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@XStreamAlias("CoreSMS")
@XmlRootElement
public class SmsResponse {

    @Getter
    @Setter
    @XStreamAlias("OperID")
    //用户标识
    private String operID;
    @Getter
    @Setter
    @XStreamAlias("OperPass")
    //操作密码
    private String operPass;
    @Getter
    @Setter
    @XStreamAlias("Action")
    //操作命令
    private String action;
    @Getter
    @Setter
    @XStreamAlias("Category")
    //数据包类型
    private String category;

    @Getter
    @Setter
    @XStreamAlias("Body")
    //消息体
    private SmsResponseBody body;

    public SmsResponse(String operID, String operPass, String action, String category, SmsResponseBody body) {
        this.operID = operID;
        this.operPass = operPass;
        this.action = action;
        this.category = category;
        this.body = body;
    }
}
