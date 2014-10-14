package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.SupplierResourceMaterialSubject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/25/14
 */
public class SupplierResourceMaterialLogRichRecordDto implements Serializable {
    private static final long serialVersionUID = -7226631725659434833L;

    @Getter
    @Setter
    private SupplierResourceMaterialSubject subject;

    @Getter
    @Setter
    private SupplierResourceMaterialLogRecordDto record;
}
