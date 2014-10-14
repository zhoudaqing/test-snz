package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public class SupplierResourceMaterialSubject implements Serializable {
    private static final long serialVersionUID = -5262236762124804341L;

    @Getter
    @Setter
    private Long id;            // 自增主键

    @Getter
    @Setter
    private String name;        // 资质交互审核条目名称

    @Getter
    @Setter
    private Integer type;       // 0. 非否决项, 1. 否决项

    @Getter
    @Setter
    private String role;        // 检查此条目的权限位, @see User.JobRole

    @Getter
    @Setter
    private Integer status;     // 0. 未生效, 1. 生效

    @Getter
    @Setter
    private Long version;       // 标注是第几次的修改版本，默认从1开始

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
