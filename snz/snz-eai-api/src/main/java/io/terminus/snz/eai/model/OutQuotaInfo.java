package io.terminus.snz.eai.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by luyuzhou on 14-9-5.
 */
public class OutQuotaInfo implements Serializable{

    private static final long serialVersionUID = 1972560031775826118L;
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
    private String factorynum;

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
    private Long quantity;

    @Getter
    @Setter
    private Date createdat;

    @Getter
    @Setter
    private Date updatedat;
}
