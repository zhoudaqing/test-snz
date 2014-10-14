package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:需求具体模块的方案信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
public class ModuleSolution implements Serializable {
    private static final long serialVersionUID = 8846735946283563367L;

    @Setter
    @Getter
    private Long id;            //自增主键

    @Setter
    @Getter
    private Long solutionId;    //整体方案编号

    @Setter
    @Getter
    private Long moduleId;      //模块编号（这个是具体模块需求的编号）

    @Setter
    @Getter
    private String moduleName;  //模块名称

    @Setter
    @Getter
    private String technology;  //技术

    @Setter
    @Getter
    private Integer quality;    //质量

    @Setter
    @Getter
    private Date reaction;      //互动

    @Setter
    @Getter
    private Integer delivery;   //产能

    @Setter
    @Getter
    private Integer cost;       //成本

    @Setter
    @Getter
    private String units;       //单位名称

    @Setter
    @Getter
    private Date createdAt;     //创建时间

    @Setter
    @Getter
    private Date updatedAt;     //修改时间
}
