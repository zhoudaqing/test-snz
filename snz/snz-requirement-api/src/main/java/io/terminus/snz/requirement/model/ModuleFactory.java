package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:模块的资源对于每一个工厂的信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-05.
 */
public class ModuleFactory implements Serializable {
    private static final long serialVersionUID = 7087215878687451533L;

    @Getter
    @Setter
    private Long id;                //自增主键

    @Getter
    @Setter
    private Long moduleId;          //模块编号

    @Getter
    @Setter
    private String factoryNum;      //工厂编号（9889）

    @Getter
    @Setter
    private String factoryName;     //工厂名称

    @Getter
    @Setter
    private Long propertyId;        //针对于工厂的类目属性

    @Getter
    @Setter
    private Integer selectNum;      //对应工厂选取的中标供应商人数

    @Getter
    @Setter
    private Integer resourceNum;    //工厂的资源需求数量

    @Getter
    @Setter
    private Integer salesId;        //工厂的资源单位编号

    @Getter
    @Setter
    private String salesName;       //工厂的资源的单位名称

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间
}
