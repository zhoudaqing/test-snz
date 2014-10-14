package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/15/14
 */
public class FactoryProductionDirector implements Serializable {
    private static final long serialVersionUID = 6656589969446767771L;

    @Getter
    @Setter
    private Long id;                    // 自增主键

    @Getter
    @Setter
    private String factoryNum;          // 工厂编号

    @Getter
    @Setter
    private String description;         // 工厂描述

    @Getter
    @Setter
    private Long productLineId;         // 产品线id （产品线即为后台二级类目）

    @Getter
    @Setter
    private String productLineName;     // 产品线名称 （后台二级类目名称）

    @Getter
    @Setter
    private String directorId;          // 总监用户id（工号）

    @Getter
    @Setter
    private String directorName;        // 总监名

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
