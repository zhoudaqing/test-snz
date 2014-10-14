package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-30.
 */
public class SupplierCountByIndustry implements Serializable {

    private static final long serialVersionUID = 8466666197590857967L;

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private Date date;

    @Setter
    @Getter
    private Long industry;   //行业编号（一级类目编号）

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
