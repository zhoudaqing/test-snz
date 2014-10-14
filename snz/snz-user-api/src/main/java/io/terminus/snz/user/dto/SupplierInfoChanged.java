package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
public class SupplierInfoChanged implements Serializable {

    private static final long serialVersionUID = -6529525869269794136L;

    @Setter
    @Getter
    private boolean companyChanged;

    @Setter
    @Getter
    private boolean paperworkChanged;

    @Setter
    @Getter
    private boolean contactInfoChanged;

    @Setter
    @Getter
    private boolean financeChanged;

    @Setter
    @Getter
    private boolean qualityChanged;

}
