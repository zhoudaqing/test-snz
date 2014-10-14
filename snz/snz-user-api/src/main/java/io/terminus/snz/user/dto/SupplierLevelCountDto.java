package io.terminus.snz.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 某产品线在各个分值区间的供应商数量统计
 *
 * @author wanggen on 14-7-25.
 */
@Data
public class SupplierLevelCountDto implements Serializable {
    private static final long serialVersionUID = -4881565388057702302L;

    private SupplierLevelCountDto(Integer productLineId) {
        this.productLineId = productLineId;
    }

    public static SupplierLevelCountDto create(Integer productLineId) {
        return new SupplierLevelCountDto(productLineId);
    }

    private int productLineId;      //产品线ID(0:表示所有产品线，即不分产品线维度)

    private String productLine;     //产品线名称 -预留

    private int bestCount;          //优选供应商数量

    private int standardCount;      //合格供应商数量

    private int limitedCount;       //受限制供应商数量

    private int badCount;           //淘汰供应商数量


    /**
     * 根据分值将相应的等级数量增一
     *
     * @param score 分值
     * @return this
     */
    public SupplierLevelCountDto incrCountByScore(Integer score) {
        if (score == null || score < 0) return this;
        if (score >= 90 && score <= 100) bestCount++;
        else if (score >= 80 && score <= 89) standardCount++;
        else if (score >= 70 && score <= 79) limitedCount++;
        else if (score < 70) badCount++;
        return this;
    }

}
