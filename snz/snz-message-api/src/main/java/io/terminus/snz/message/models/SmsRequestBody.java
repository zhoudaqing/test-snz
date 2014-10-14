package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.List;


public class SmsRequestBody {
    //默认的日期格式
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    @Getter
    @Setter
    @XStreamAlias("SendTime")
    private String sendTime;

    @Getter
    @Setter
    @XStreamAlias("AppendID")
    private String appendId;

    @Getter
    @Setter
    @XStreamImplicit
    private List<SmsRequestMessage> messageList;


    public SmsRequestBody(List<SmsRequestMessage> messageList) {
        this.appendId = "";
        this.sendTime = getDateTime();
        this.messageList = messageList;
    }


    public SmsRequestBody(String appendId, List<SmsRequestMessage> messageList) {
        this.sendTime = getDateTime();
        this.appendId = appendId;
        this.messageList = messageList;
    }

    public SmsRequestBody(String sendTime, String appendId, List<SmsRequestMessage> messageList) {
        this.sendTime = sendTime;
        this.appendId = appendId;
        this.messageList = messageList;
    }

    private String getDateTime() {
        DateTime date = new DateTime();
        return date.toString(DATE_PATTERN);
    }


}
