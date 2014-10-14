package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.RequirementSolution;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:整体需求的方案信息（包含供应商的信用数据->TQRDC的数据信息，这个是由海尔PLM系统中获取的）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-07.
 */
public class SolutionQuotaDto extends RequirementSolution {
    private static final long serialVersionUID = 246216962436277785L;

    //现在数据类型还未确定，
    @Setter
    @Getter
    private String actualTechnology;      //实际的技术

    @Setter
    @Getter
    private String actualQuality;         //实际的质量

    @Setter
    @Getter
    private String actualReaction;        //实际的互动

    @Setter
    @Getter
    private String actualDelivery;        //实际的产能

    @Setter
    @Getter
    private Integer actualCost;           //实际的成本

    public SolutionQuotaDto(){}

    public SolutionQuotaDto(RequirementSolution solution){
        this.setId(solution.getId());
        this.setRequirementId(solution.getRequirementId());
        this.setRequirementName(solution.getRequirementName());
        this.setSupplierId(solution.getSupplierId());
        this.setSupplierName(solution.getSupplierName());
        this.setTechnology(solution.getTechnology());
        this.setQuality(solution.getQuality());
        this.setReaction(solution.getReaction());
        this.setDelivery(solution.getDelivery());
        this.setCost(solution.getCost());
        this.setCreatedAt(solution.getCreatedAt());
        this.setUpdatedAt(solution.getUpdatedAt());
    }
}
