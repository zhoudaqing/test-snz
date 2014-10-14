package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierCreditQualify;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 7/31/14
 * Time: 22:29
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierCreditQualifyDto implements Serializable {
    private static final long serialVersionUID = 7334332053451362824L;

    @Getter
    @Setter
    private SupplierCreditQualify supplierCreditQualify;

    @Getter
    @Setter
    private Company company;
}
