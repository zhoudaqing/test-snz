package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:模块的报价单信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-10.
 */
@ToString
@EqualsAndHashCode
public class ModuleQuotation implements Serializable {
    private static final long serialVersionUID = 4001597026426507693L;

    @Setter
    @Getter
    private Long id;                //自增主键

    @Setter
    @Getter
    private Long solutionId;        //整体方案编号(最优的方案编号)

    @Setter
    @Getter
    private Long moduleId;          //模块编号（这个是具体模块需求的编号）

    @Setter
    @Getter
    private String moduleName;      //模块名称

    @Setter
    @Getter
    private Long supplierId;        //供应商编号

    @Setter
    @Getter
    private String supplierName;    //供应商名称

    @Setter
    @Getter
    private Integer total;          //整体模块资源量

    @Setter
    @Getter
    private Integer price;          //计算单价

    @Setter
    @Getter
    private Integer transactPrice;  //谈判单价

    @Getter
    @Setter
    private String costUnit;        //价格单位的json字段{salesId:1, name:EA, quantityId:1, quantityName:1}

    @Setter
    @Getter
    private String coinType;        //货币

    @Setter
    @Getter
    private Integer exchangeRate;   //汇率

    @Setter
    @Getter
    private Date createdAt;         //创建时间

    @Setter
    @Getter
    private Date updatedAt;         //修改时间
}
