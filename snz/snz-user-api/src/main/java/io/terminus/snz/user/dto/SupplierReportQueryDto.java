package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-3.
 */
public class SupplierReportQueryDto implements Serializable {

    private static final long serialVersionUID = -7766175919563114446L;

    @Setter
    @Getter
    private String step;               //所处阶段（状态）

    @Setter
    @Getter
    private String supplierCode;       //供应商代码

    @Setter
    @Getter
    private String supplierName;       //供应商代码

    @Setter
    @Getter
    private String accountType;               //类型

    @Setter
    @Getter
    private String creditQualifyResult;  //信用等级评价结果

    @Setter
    @Getter
    private Integer participateCount;      //响应交互数量

    @Setter
    @Getter
    private Integer selectedSolutionCount; //方案中选数量

    @Setter
    @Getter
    private String isListed;            //是否上市

    @Setter
    @Getter
    private String soldOfYear;          //年销售额

    @Setter
    @Getter
    private String personScale;         //人数规模

    @Setter
    @Getter
    private String customer;            //主营客户

}
