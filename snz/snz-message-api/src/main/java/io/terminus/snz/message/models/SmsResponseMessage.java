package io.terminus.snz.message.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.Setter;


@XStreamAlias("Message")
public class SmsResponseMessage {

    @Getter
    @Setter
    @XStreamAlias("DesMobile")
    private String desMobile;
    @Getter
    @Setter
    @XStreamAlias("SMSID")
    private String smsId;


    public SmsResponseMessage(String desMobile, String smsId) {
        this.desMobile = desMobile;
        this.smsId = smsId;
    }
}
