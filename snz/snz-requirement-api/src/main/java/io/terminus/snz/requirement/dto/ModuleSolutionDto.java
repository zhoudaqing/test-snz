package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleSolution;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-06.
 */
public class ModuleSolutionDto extends ModuleSolution {
    private static final long serialVersionUID = 8340544905069107679L;

    /*
        作为一个标识用于表示供应商对于某个需求的具体模块的处理状态（1:已提交的模块方案，2:未提交的模块方案）
        为了向供应商显示哪些模块他已经提交了方案或者还没有提交方案
     */
    @Setter
    @Getter
    private Integer solutionStatus;

    @Setter
    @Getter
    private Integer oldQuality;    //旧的质量

    @Setter
    @Getter
    private Date oldReaction;      //旧的互动

    @Setter
    @Getter
    private Integer oldDelivery;   //旧的产能

    public ModuleSolutionDto(){}

    public ModuleSolutionDto(ModuleSolution moduleSolution , Integer solutionStatus){
        this.setId(moduleSolution.getId());
        this.setSolutionStatus(solutionStatus);
        this.setSolutionId(moduleSolution.getSolutionId());
        this.setModuleId(moduleSolution.getModuleId());
        this.setModuleName(moduleSolution.getModuleName());
        this.setTechnology(moduleSolution.getTechnology());
        this.setQuality(moduleSolution.getQuality());
        this.setReaction(moduleSolution.getReaction());
        this.setDelivery(moduleSolution.getDelivery());
        this.setCost(moduleSolution.getCost());
        this.setUnits(moduleSolution.getUnits());
        this.setCreatedAt(moduleSolution.getCreatedAt());
        this.setUpdatedAt(moduleSolution.getUpdatedAt());
    }

    //写入模块信息作为模块方案填写的标准数据信息
    public ModuleSolutionDto(Module module , Long solutionId, Integer solutionStatus){
        this.setSolutionStatus(solutionStatus);
        this.setSolutionId(solutionId);
        this.setModuleId(module.getId());
        this.setModuleName(module.getModuleName());
        this.setTechnology(module.getAttestations());
        this.setQuality(module.getQuality());
        this.setReaction(module.getSupplyAt());
        this.setDelivery(module.getDelivery());
        this.setCost(module.getCost());
        this.setUnits(module.getUnits());
    }
}
