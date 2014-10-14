package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.user.model.Company;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/16/14
 */
public class DepositPayment implements Serializable {

    private static final long serialVersionUID = 1906725191688568181L;

    @Getter
    @Setter
    private Requirement requirement;    // 对应需求

    @Getter
    @Setter
    private Company company;            // 对应公司信息

    @Getter
    @Setter
    @Deprecated
    private Integer status;             // 是否需要支付保证金（0: 不需要，1: 需要，2: 表示已经支付过）

    @Getter
    @Setter
    private Boolean isPaid;             // 是否已支付过

    @Getter
    @Setter
    private Boolean isOldSupplier;      // 是否是老供应商

    @Getter
    @Setter
    private Boolean isEnough;           // 是否足够支付（不需要保证金提交）

    @Getter
    @Setter
    private Long origMoney;             // 原付金额

    @Getter
    @Setter
    private Long money;                 // 实际应付（单位：分）

    @Getter
    @Setter
    private Long riskMortgageAmount;    // 风险抵押金

    @Getter
    @Setter
    private Long hgvsBalance;           // hgvs余额

    @Getter
    @Setter
    private Long inPaying;              // 累计已缴纳保证金
}
