package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
public class SupplierGroupRelation implements Serializable {
    private static final long serialVersionUID = -9116128834806302827L;

    @Getter
    @Setter
    private Long id;                // 自增主键

    @Getter
    @Setter
    private Long supplierId;        // 供应商id

    @Getter
    @Setter
    private Long groupId;           // 供应商所在组群的id

    @Getter
    @Setter
    private Date createdAt;         // 创建时间

    @Getter
    @Setter
    private Date updatedAt;         // 更新时间
}
