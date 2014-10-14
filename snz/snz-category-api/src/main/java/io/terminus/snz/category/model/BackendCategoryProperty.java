package io.terminus.snz.category.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
public class BackendCategoryProperty implements Serializable {
    private static final long serialVersionUID = -4870110759707882106L;

    @Getter
    @Setter
    private Long id;            // 自增主键

    @Getter
    @Setter
    private Long bcId;          // 后台类目id

    @Getter
    @Setter
    private String name;        // 属性名

    @Getter
    @Setter
    private String type;        // 属性类型（默认，1为资源量）TODO: 未使用, 且默认是null, 需要改为Integer?

    @Getter
    @Setter
    private String factoryNum;  // 资源量对应的工厂，其编号 @see AddressFactory.factoryNum

    @Getter
    @Setter
    private String value1;      // 属性值1（最小量）

    @Getter
    @Setter
    private String value2;      // 属性值2

    @Getter
    @Setter
    private String value3;      // 属性值3

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
