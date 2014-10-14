package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/13/14
 */
public class Deposit implements Serializable {

    private static final long serialVersionUID = -5231719461853382700L;

    @Getter
    @Setter
    private Long id;                // 自增主键

    @Getter
    @Setter
    private Long requirementId;     // 需求id

    @Getter
    @Setter
    private Long supplierId;        // 供应商id

    @Getter
    @Setter
    private String dealId;          // 快捷通交易号（付款）

    @Getter
    @Setter
    private Date dealTime;          // 快捷通付款订单交易时间

    @Getter
    @Setter
    private String dealUrl;         // 交易链接

    @Getter
    @Setter
    private Long amount;            // 保证金金额

    @Getter
    @Setter
    private String bankInfo;        // 供应商所填写的退款银行账户信息, @see KjtTransDto, 用json保存, TODO: 加密

    @Getter
    @Setter
    private Integer type;           // 保证金类型（1: 付款，2: 退款）

    @Getter
    @Setter
    private Integer status;         // 保证金状态（0：未提交，1：提交成功，-1：提交失败，2：交易成功，-2：交易失败）

    @Getter
    @Setter
    private Integer syncStatus;     // 与海尔SAP系统同步状态（0：未同步，1：已同步）

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    public enum Type {
        PAY(1, "付款"), REFUND(2, "退款");

        private final Integer value;
        private final String description;

        private Type(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public Integer value() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    public enum Status {
        INIT(0, "未提交"),
        SUBMIT_SUCCESS(1, "提交成功"), SUBMIT_FAILED(-1, "提交失败"),
        TRANS_SUCCESS(2, "交易成功"), TRANS_FAILED(-2, "交易失败");

        private final Integer value;
        private final String description;

        private Status(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Status from(Integer value) {
            for(Status status : Status.values()) {
                if(Objects.equal(value, status.value)) {
                    return status;
                }
            }
            return null;
        }

        public Integer value(){
            return value;
        }

        @Override
        public String toString(){
            return description;
        }
    }
}
