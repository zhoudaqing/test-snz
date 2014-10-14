package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/21/14
 */
public class RiskMortgagePayment implements Serializable {

    private static final long serialVersionUID = -1524234464173316612L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String supplierCode;

    @Getter
    @Setter
    private String supplierDetail;

    @Getter
    @Setter
    private String purchaserCode;

    @Getter
    @Setter
    private Long amount;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
