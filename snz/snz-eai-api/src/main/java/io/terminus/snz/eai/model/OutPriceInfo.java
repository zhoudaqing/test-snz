package io.terminus.snz.eai.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luyuzhou on 14-9-3.
 */
public class OutPriceInfo implements Serializable{

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String modulenum;

    @Getter
    @Setter
    private String modulename;

    @Getter
    @Setter
    private String matkl;

    @Getter
    @Setter
    private String supplierid;

    @Getter
    @Setter
    private String suppliername;

    @Getter
    @Setter
    private String purchaseorg;

    @Getter
    @Setter
    private String purchasetype;

    @Getter
    @Setter
    private Long scale;

    @Getter
    @Setter
    private Long feeunit;

    @Getter
    @Setter
    private String purchaseunit;

    @Getter
    @Setter
    private String cointype;

    @Getter
    @Setter
    private Date createdat;

    @Getter
    @Setter
    private Date updatedat;
}
