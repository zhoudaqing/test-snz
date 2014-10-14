package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
/**
 * Desc: 资源小微供应商对应关系中间表
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-9-1.
 */
public class SupplierResource implements Serializable {
    private static final long serialVersionUID = 8275770677190988618L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String vcode;

    @Getter
    @Setter
    private String supplierName;

    @Getter
    @Setter
    private Long supplierUid;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private String userName;


}
