package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;


@XStreamAlias("CoreSMS")
public class SmsRequest {

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
    private String action = "Submit";
    @Getter
    @Setter
    @XStreamAlias("Category")
    //数据包类型
    private String category = "0";
    @Getter
    @Setter
    @XStreamAlias("Body")
    //请求内容
    private SmsRequestBody body;


    public SmsRequest(String operID, String operPass, SmsRequestBody body) {
        this.operID = operID;
        this.operPass = operPass;
        this.body = body;
    }


}
