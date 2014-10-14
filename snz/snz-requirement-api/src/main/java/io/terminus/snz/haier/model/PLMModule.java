package io.terminus.snz.haier.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:PLM的模块信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-07.
 */
@ToString
@EqualsAndHashCode
public class PLMModule implements Serializable {

    private static final long serialVersionUID = 138105946886487783L;

    @Getter
    @Setter
    private String supplierVId;         //供应商V码(海尔提供的数据,供应商在海尔系统中的唯一标识)

    @Getter
    @Setter
    private String supplierName;        //供应商名称

    @Getter
    @Setter
    private String releaseDate;         //图纸设计完成时间

    @Getter
    @Setter
    private String partNumber;          //物料号

    @Getter
    @Setter
    private String buNo;                //产品部代码（对应我们的产品线一级类目）

    @Getter
    @Setter
    private String demandId;            //需求号

    @Getter
    @Setter
    private String demandName;          //需求名称

    @Getter
    @Setter
    private String demandRelPerson;     //需求发布人

    @Getter
    @Setter
    private String demandRelTime;       //需求发布时间

    @Getter
    @Setter
    private String moduleExampleId;     //模块号

    @Getter
    @Setter
    private String moduleExampleName;   //模号名称
}
