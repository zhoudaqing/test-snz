package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;

import static io.terminus.common.utils.Arguments.notNull;

/**
 * stub
 *
 * Date: 7/22/14
 * Time: 13:38
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@ToString
@EqualsAndHashCode
public class SupplierQualification implements Serializable {
    private static final long serialVersionUID = -6265406683995850852L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long supplierId;

    @Getter
    @Setter
    private String finGroup;

    @Getter
    @Setter
    private String vCode;           // 商家v码

    @Getter
    @Setter
    private String piPartner;       // PI 合作伙伴

    @Getter
    @Setter
    private String comCode;         // 公司编码

    @Getter
    @Setter
    private String slave;           // 统奴科目

    @Getter
    @Setter
    private String order;           // 排序码

    @Getter
    @Setter
    private String payTerm;         // 付款条款

    @Getter
    @Setter
    private String payMethod;       // 付款方式

    @Getter
    @Setter
    private String bankNum;         // 银行账号

    @Getter
    @Setter
    private String bankNation;      // 银行国家

    @Getter
    @Setter
    private String bankOwner;       // 户主，默认 1

    @Getter
    @Setter
    private String bankCode;        // 银行代码

    @Getter
    @Setter
    private String purchOrg;         // 采购组织

    @Getter
    @Setter
    private String currency;        // 货币

    @Getter
    @Setter
    private Integer stage;          // 当前核审步骤，1-采购经理，2-共享财务, 3-done!

    @Getter
    @Setter
    private String opCode;

    @Getter
    @Setter
    private String opMsg;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    public Boolean maskByStage(@Nullable Integer stage) {
        stage  = Objects.firstNonNull(stage, this.stage);
        if (stage==null) return false;

        switch (stage) {
            case 1: // 保留采购经理可以设置的
                currency=null;
                purchOrg=null;
                payMethod = null;
                payTerm = null;
                return true;
            case 2:
                finGroup=null;
                comCode=null;
                order  = null;
                slave = null;
                return true;
            default:
                return false;
        }
    }

    public Boolean validateByStage(@Nullable Integer stage) {
        stage  = Objects.firstNonNull(stage, this.stage);
        if (stage==null) return false;

        switch (stage) {
            case 1:
                return notNull(finGroup) && notNull(order)// && notNull(comCode)  && validateBank()
                        && notNull(slave);
            case 2:
                return notNull(currency) && notNull(purchOrg) && notNull(payMethod)// && validateBank()
                        && notNull(payTerm);
            default: return false;
        }
    }

    private Boolean validateBank() {
        return notNull(bankCode) && notNull(bankNation) && notNull(bankNum) &&
                notNull(bankOwner)&& notNull(piPartner);
    }
}
