package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleQuotation;
import lombok.Getter;
import lombok.Setter;

/**
 * Desc:详细的模块报价信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-10.
 */
public class ModuleQuotationDto extends ModuleQuotation {
    private static final long serialVersionUID = -5544108706256386451L;

    /*
        作为一个标识用于表示供应商对于某个需求的具体模块的报价处理状态，true 已提交报价，false 未提交报价
     */
    @Setter
    @Getter
    private Boolean quoted;

    public ModuleQuotationDto(){}

    public ModuleQuotationDto(ModuleQuotation quotation, String coinType, Boolean isQuoted){
        this.setId(quotation.getId());
        this.setQuoted(isQuoted);
        this.setSolutionId(quotation.getSolutionId());
        this.setTotal(quotation.getTotal());
        this.setModuleId(quotation.getModuleId());
        this.setModuleName(quotation.getModuleName());
        this.setPrice(quotation.getPrice());
        this.setCoinType(coinType);
        this.setCostUnit(quotation.getCostUnit());
        this.setExchangeRate(quotation.getExchangeRate());
        this.setCreatedAt(quotation.getCreatedAt());
        this.setUpdatedAt(quotation.getUpdatedAt());
    }

    public ModuleQuotationDto(Module module, Long solutionId, String coinType, Boolean isQuoted){
        this.setQuoted(isQuoted);
        this.setSolutionId(solutionId);
        this.setTotal(module.getTotal());
        this.setModuleId(module.getId());
        this.setModuleName(module.getModuleName());
        this.setPrice(module.getCost());
        this.setCoinType(coinType);
        this.setCostUnit(module.getUnits());
    }
}
