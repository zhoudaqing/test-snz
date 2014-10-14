package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
public class SupplierCountBySupplyPark implements Serializable {

    private static final long serialVersionUID = -994991884571095397L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private Date date;

    @Setter
    @Getter
    private Long supplyParkId;

    @Setter
    @Getter
    private Long supplierCount;

    @Setter
    @Getter
    private Date createdAt;

    @Setter
    @Getter
    private Date updatedAt;

}
