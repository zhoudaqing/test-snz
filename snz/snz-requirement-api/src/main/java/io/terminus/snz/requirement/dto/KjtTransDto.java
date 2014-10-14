package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/15/14
 */
public class KjtTransDto implements Serializable {

    private static final long serialVersionUID = 3596166431730572426L;

    @Getter
    @Setter
    private String cardNum;             // 银行卡号

    @Getter
    @Setter
    private Integer accountType;        // 账户类型（0: 个人，1: 企业）

    @Getter
    @Setter
    private String peerName;            // 收款方姓名

    @Getter
    @Setter
    private String bankName;            // 开户银行

    @Getter
    @Setter
    private String peerBankName;        // 开户支行

    @Getter
    @Setter
    private String province;            // 省份

    @Getter
    @Setter
    private String city;                // 城市

    @Getter
    @Setter
    private String remark;              // 备注
}
