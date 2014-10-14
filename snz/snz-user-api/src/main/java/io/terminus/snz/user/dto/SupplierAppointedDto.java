package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierAppointed;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Grancy Guo
 * Created on 14-9-15.
 */
public class SupplierAppointedDto implements Serializable{
    private static final long serialVersionUID = -3014790645897808802L;

    @Getter
    @Setter
    private SupplierAppointed supplierAppointed;

    @Getter
    @Setter
    private Company company;

}
