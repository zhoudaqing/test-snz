package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.SupplierReparationSumaries;
import io.terminus.snz.user.model.SupplierResource;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-9-1.
 */
public class SupplierReparationSummariesDto implements Serializable {
    private static final long serialVersionUID = 664681159568877795L;

    @Getter
    @Setter
    private SupplierResource supplierResource;

    @Getter
    @Setter
    private SupplierReparationSumaries supplierReparationSumaries;
}
