package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/26/14
 */
public class SupplierResourceMaterialLogRecordDto implements Serializable {
    private static final long serialVersionUID = -7474172933043547633L;

    @Getter
    @Setter
    private Long supplierId;        // 供应商id

    @Getter
    @Setter
    private Integer status;         // 0. 否决 1. 通过

    @Getter
    @Setter
    private Long subjectId;         // 条目id

    @Getter
    @Setter
    private String attachUrl;       // 附件

    @Getter
    @Setter
    private String view;            // 意见
}
