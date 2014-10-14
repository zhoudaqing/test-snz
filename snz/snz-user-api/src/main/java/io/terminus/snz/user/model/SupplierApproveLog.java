package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-22.
 */
public class SupplierApproveLog implements Serializable {

    private static final long serialVersionUID = -3815750516174909688L;

    @Getter
    @Setter
    private Long id;                     //自增主键

    @Getter
    @Setter
    private Long userId;                 //用户编号

    @Setter
    @Getter
    private Integer approveType;         //审核类型(1:入驻审核，2：修改信息审核)

    public static enum ApproveType {
        ENTER(1, "入驻审核"),
        MODIFY_INFO(2, "修改信息审核");

        private final int value;

        private final String display;

        private ApproveType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ApproveType from(Integer value) {
            for (ApproveType type : ApproveType.values()) {
                if (Objects.equal(type.value, value)) {
                    return type;
                }
            }
            return null;
        }

        public int value() {
            return value;
        }

        public String toString() {
            return display;
        }
    }

    @Getter
    @Setter
    private Date approvedAt;             //审核时间

    @Getter
    @Setter
    private Long approverId;             //审核人编号

    @Getter
    @Setter
    private String approverName;         //审核人名称

    @Setter
    @Getter
    private Integer approveResult;       //审核结果(-1:审核不通过，1：审核通过)

    public static enum ApproveResult {
        FAIL(-1, "审核不通过"),
        OK(1, "审核通过");

        private final int value;

        private final String display;

        private ApproveResult(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ApproveResult from(Integer value) {
            for (ApproveResult type : ApproveResult.values()) {
                if (Objects.equal(type.value, value)) {
                    return type;
                }
            }
            return null;
        }

        public int value() {
            return value;
        }

        public String toString() {
            return display;
        }
    }

    @Getter
    @Setter
    private String description;          //审核意见

    @Getter
    @Setter
    private Date createdAt;              //创建时间

    @Getter
    @Setter
    private Date updatedAt;              //修改时间

}
