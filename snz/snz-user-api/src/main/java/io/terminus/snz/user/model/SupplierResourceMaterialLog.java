package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public class SupplierResourceMaterialLog implements Serializable {
    private static final long serialVersionUID = -6332623263738097569L;

    @Getter
    @Setter
    private Long id;            // 自增主键

    @Getter
    @Setter
    private Long supplierId;    // 供应商id

    @Getter
    @Setter
    private Long checkerId;     // 审核员id（0为系统行为，-1表示没有参与）

    @Getter
    @Setter
    private Long times;         // 审核次数，唯一确定一次完整的审核 @see SupplierResourceMaterialInfo.times

    @Getter
    @Setter
    private Integer type;       // 1. 一级审核, 2. 二级审核, 5. 供应商提交申请, 6. 系统自动提交申请, 7. 系统自动设为最终通过

    public enum Type {
        LEVEL1(1, "一级审核"),
        LEVEL2(2, "二级审核"), // 暂未用到，保留字段
        L1_CHG_REQ(3, "一级审核,增加已审核过的role"),
        SUBMIT_BY_SUPPLIER(5, "供应商提交申请"),
        SUBMIT_BY_SYSTEM(6, "系统自动提交申请"),
        APPROVED_BY_SYSTEM(7, "系统自动设为通过"), // 所提交的模块都已通过时，不需要再审核
        APPROVED_LEVEL1(11, "通过一级审核"),
        FAILED_LEVEL1(-11, "一级审核不通过"),
        ASK_FOR_REJECT(13, "请求上级驳回"),
        REJECTED(-13, "被上级驳回"),
        INVITE_ANOTHER(20, "邀请其他审核员来审核"),
        FAIL_BY_SYSTEM(21, "过长时间无人处理，系统自动设为审核不通过"),
        FORCE_APPROVE(22, "强制通过类目（无需审核）");

        private final Integer value;
        private final String description;

        private Type(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Type from(Integer value) {
            for (Type type : Type.values()) {
                if (Objects.equal(value, type.value)) {
                    return type;
                }
            }
            return null;
        }

        public Integer value() {
            return value;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    @Getter
    @Setter
    private Integer status;     // 0. 否决, 1. 通过 (供应商提交申请默认为通过, 系统自动提交申请亦同)

    @Getter
    @Setter
    private String content;     // 视type而定，若type为1. 一级审核，暂定存放 @see SupplierQualifyRecordDto

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
