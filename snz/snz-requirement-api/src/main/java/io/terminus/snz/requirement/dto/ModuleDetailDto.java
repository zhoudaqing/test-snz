package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:详细的模块数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-05.
 */
@ToString
@EqualsAndHashCode
public class ModuleDetailDto implements Serializable {
    private static final long serialVersionUID = 2354897348498636207L;

    @Getter
    @Setter
    private Long requirementId;     //需求编号

    @Getter
    @Setter
    private String requirementName; //需求名称

    @Getter
    @Setter
    private Long productId;         //产品类型（后台一级类目）

    @Getter
    @Setter
    private String productName;     //产品类型名称（后台一级类目）

    @Getter
    @Setter
    private Long moduleId;          //模块ID

    @Getter
    @Setter
    private String moduleNum;        //模块编号(海尔内部使用物料号)

    @Getter
    @Setter
    private String moduleName;      //模块名称

    @Getter
    @Setter
    private Long seriesId;          //系列编号（后台二级类目）

    @Getter
    @Setter
    private String seriesName;      //系列名称（后台二级类目）
}
