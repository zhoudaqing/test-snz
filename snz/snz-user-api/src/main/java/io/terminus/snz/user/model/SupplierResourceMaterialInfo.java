package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc: 供应商资质交互(状态)信息
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public class SupplierResourceMaterialInfo implements Serializable {
    private static final long serialVersionUID = 1870873864132822156L;

    @Getter
    @Setter
    private Long id;                        // 自增主键

    @Getter
    @Setter
    private Long supplierId;                // 供应商id

    @Getter
    @Setter
    private String supplierName;            // 供应商名称 (冗余, 分页查找用)

    @Getter
    @Setter
    private String approvedModuleIds;       // 已通过审核的类目id列表, "1,2,3,..."

    @Getter
    @Setter
    private String notApprovedModuleIds;    // 未通过审核的类目id列表, "7,4,5,..."

    @Getter
    @Setter
    private Long times;                     // 审核的次数，标识当前是第几次审核，第一次为1

    @Getter
    @Setter
    private Date lastSubmitTime;            // 最近提交时间（用于周期管理）

    @Getter
    @Setter
    private Integer status;                 // 状态

    public enum Status {
        SUBMITTED(1, "已提交，等待审核"),
        NO_SUBMISSION(-1, "没有提交（或提交失败）"),
        QUALIFIED(2, "资质检查通过"),
        QUALIFY_FAILED(-2, "资质检查不通过"),
//        FINAL_QUALIFIED(3, "最终通过"),
        REJECTED(-3, "已通过，但被二级审核员否决"),
        QUERY_FOR_REJECTED(-4, "申请修改，请求二级审核员驳回"),
        FORCE_QUALIFIED(10, "强制通过审核，不可驳回");

        private final Integer value;
        private final String description;

        private Status(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public static Status from(Integer value) {
            for (Status status : Status.values()) {
                if (Objects.equal(value, status.value)) {
                    return status;
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
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
