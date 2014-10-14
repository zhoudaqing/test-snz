package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;


@XStreamAlias("Message")
public class SmsRequestMessage {

    @Getter
    @Setter
    @XStreamAlias("DesMobile")
    //接收手机号
    private String desMobile;
    @Getter
    @Setter
    @XStreamAlias("Content")
    //发送内容
    private String content;
    @Getter
    @Setter
    @XStreamAlias("SendType")
    //消息类型,默认普通消息
    private String sentType = "15";


    public SmsRequestMessage(String desMobile, String content, String sentType) {
        this.desMobile = desMobile;
        this.content = content;
        this.sentType = sentType;
    }


}
