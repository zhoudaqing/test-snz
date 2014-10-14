package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.NewProductStep;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanggen on 14-8-6.
 */
@Data
public class NewProductStepsDto implements Serializable {
    private static final long serialVersionUID = -3990436652477109865L;

    private String moduleNum;       //模块号

    private String supplierCode;    //供应商代码

    private String supplierName;    //供应商名称

    private Integer currStep;       //当前进行到的步骤

    private NewProductStep step1;
    private NewProductStep step2;
    private NewProductStep step3;
    private NewProductStep step4;
    private NewProductStep step5;
    private NewProductStep step6;
    private NewProductStep step7;
    private NewProductStep step8;


    private NewProductStepsDto() {}
    public static NewProductStepsDto create(String mouldNumber, String supplierCode, String supplierName){
        NewProductStepsDto stepsDto = new NewProductStepsDto();
        stepsDto.setModuleNum(mouldNumber);
        stepsDto.setSupplierCode(supplierCode);
        stepsDto.setSupplierName(supplierName);
        return stepsDto;
    }

    public NewProductStepsDto supplierCode(String supplierCode){
        this.supplierCode = supplierCode; return this;
    }

    public NewProductStepsDto supplierName(String supplierName){
        this.supplierName = supplierName; return this;
    }

}
