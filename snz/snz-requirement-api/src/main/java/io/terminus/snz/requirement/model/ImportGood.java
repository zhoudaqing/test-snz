package io.terminus.snz.requirement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static io.terminus.common.utils.Arguments.isNull;

/**
 * 新品导入详情页面上对应一个商家的记录。
 * 表：snz_income_goods
 *
 * Date: 7/9/14
 * Time: 11:32
 * Author: 2014年 <a href="mailto:dong.worker@gmail.com">张成栋</a>
 */
@ToString
@EqualsAndHashCode
public class ImportGood implements Serializable {
    private static final long serialVersionUID = 1152575603144529286L;

    @Getter
    @Setter
    private Long id;

    // 供货商ID
    @Getter
    @Setter
    private Long supplierId;

    // 冗余的需求ID
    @Getter
    @Setter
    private Long requirementId;

    // 供货商公司ID
    @Getter
    @Setter
    private Long companyId;

    // 模块ID
    @Getter
    @Setter
    private Long moduleId;

    // 供货商公司名字
    @Getter
    @Setter
    private String companyName;

    // 合同签订预算详情记录的ID
    @Getter
    @Setter
    private Long estimateContract;

    // 图纸详细设计预算详情记录的ID
    @Getter
    @Setter
    private Long estimateDesign;

    // 模具开制预算详情记录的ID
    @Getter
    @Setter
    private Long estimateModuling;

    // 检验单预算详情记录的ID
    @Getter
    @Setter
    private Long estimateDetection;

    // 收样(开发)预算详情记录的ID
    @Getter
    @Setter
    private Long estimateSample;

    // 自检报告预算详情记录的ID
    @Getter
    @Setter
    private Long estimateSelfTest;

    // IQC检验预算详情记录的ID
    @Getter
    @Setter
    private Long estimateIcq;

    // 型式试验预算详情记录的ID
    @Getter
    @Setter
    private Long estimateFormTest;

    // 检测结论预算详情记录的ID
    @Getter
    @Setter
    private Long estimateTestConclusion;

    // 最终结论预算详情记录的ID
    @Getter
    @Setter
    private Long estimateFinalConclusion;

    // 责任位
    @Getter
    @Setter
    private String inCharge;

    // 当前进行的步骤，参见 ImportGood->STAGE:enum
    @Getter
    @Setter
    private Integer stage;

    @Getter
    @Setter
    @JsonIgnore
    private Date createdAt;

    @Getter
    @Setter
    @JsonIgnore
    private Date updatedAt;

    public STAGE getStageEnum() {
        return isNull(stage)?null:STAGE.fromNumber(stage);
    }

    public Long getCurrentRowID() {
        return getRowID(stage);
    }

    public Long getRowID(Integer stage) {
        if (Objects.equal(STAGE.UNDEFINED.toNumber(), stage) || isNull(stage)) {
            return null;
        }

        switch (STAGE.fromNumber(stage)) {
            case UNDEFINED:
                return null;
            case CONTRACT:
                return estimateContract;
            case DESIGN:
                return estimateDesign;
            case MODULE:
                return estimateModuling;
            case DETECTION:
                return estimateDetection;
            case SELF_TEST:
                return estimateSelfTest;
            case SAMPLE:
                return estimateSample;
            case ICQ:
                return estimateIcq;
            case FORM_TEST:
                return estimateFormTest;
            case TEST_CONCLUSION:
                return estimateTestConclusion;
            case FINAL_CONCLUSION:
                return estimateFinalConclusion;
            default:return null;
        }
    }

    public List<Long> getAvailableRowId() {
        if (Objects.equal(STAGE.UNDEFINED.toNumber(), stage) || isNull(stage)) {
            return Collections.emptyList();
        }

        List<Long> available = Lists.newArrayList();
        for (STAGE s : STAGE.values()) {
            if (s.before(stage) || Objects.equal(getStageEnum() , s)) {
                available.add(getRowID(s.toNumber()));
            }
        }

        return available;
    }

    public static enum STAGE {
        UNDEFINED(-1, "未开始"),
        CONTRACT(1, "合同签订"),
        DESIGN(2, "图纸详细设计"),
        MODULE(3, "模具开发"),
        DETECTION(4, "检验单"),
        SELF_TEST(5, "自检报告"),
        SAMPLE(6, "收样（开发）"),
        ICQ(7, "ICQ检验"),
        FORM_TEST(8, "型式检验"),
        TEST_CONCLUSION(9, "检验结论"),
        FINAL_CONCLUSION(10, "最终结论");

        private int value;
        private String describe;

        private STAGE(int value, String describe) {
            this.value = value;
            this.describe = describe;
        }

        public Boolean before(int value) {
            return value < this.value;
        }

        public Boolean after(int value) {
            return value > this.value;
        }

        public static STAGE fromNumber (Integer num) {
            for (STAGE s: STAGE.values()) {
                if (s.value == num) {
                    return s;
                }
            }

            return null;
        }

        public Integer toNumber() {
            return Integer.valueOf(value);
        }

        public String toName() {
            return describe;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
