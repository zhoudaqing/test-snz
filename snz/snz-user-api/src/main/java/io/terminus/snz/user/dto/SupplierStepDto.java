package io.terminus.snz.user.dto;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-31.
 */
public class SupplierStepDto implements Serializable {

    private static final long serialVersionUID = 7784119462937635542L;
    @Setter
    @Getter
    private Integer step;                         //所处阶段

    @Setter
    @Getter
    private Boolean aptitudeQualified;            //资质验证是否通过

    @Setter
    @Getter
    private Boolean creditQualified;              //等级验证是否通过

    @Setter
    @Getter
    private Integer approveStatus;                //审核状态(1:没有被驳回，-1：入驻被驳回，-2;修改信息被驳回)

    public static enum ApproveStatus {
        ENTER_FAIL(-1, "入驻被驳回"),
        MODIFY_INFO_FAIL(-2, "修改信息被驳回"),
        NORMAL(1, "没有被驳回");

        private final int value;

        private final String display;

        private ApproveStatus(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ApproveStatus from(Integer value) {
            for (ApproveStatus status : ApproveStatus.values()) {
                if (Objects.equal(status.value, value)) {
                    return status;
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

    @Setter
    @Getter
    private String approveDescription;            //审核意见

    @Setter
    @Getter
    private String message;                       //消息

    @Setter
    @Getter
    private Long infoCompletePercent;             //信息完善度

}
