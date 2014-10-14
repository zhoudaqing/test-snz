package io.terminus.snz.related.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
* Author:  wenhaoli
* Date: 2014-08-15
*/
public class ArchFileDto implements Serializable {
    private static final long serialVersionUID = -251870820204365350L;

    @Getter
    @Setter
    private  String companyMainBusiness;       //主营业务String CompanyMainBusiness

    @Getter
    @Setter
    private  Integer supplierTQRDCInfoTmp;    //绩效得分Integer  SupplierTQRDCInfoTmp

    @Getter
    @Setter
    private List<String> director;                 //获取生产总监String  FactoryProductionDirector

    @Getter
    @Setter
    private int sumOfMonth;                   //一个月内索赔次数

    @Getter
    @Setter
    private Long sumOfLoss;                    //一个月索赔总和
}
