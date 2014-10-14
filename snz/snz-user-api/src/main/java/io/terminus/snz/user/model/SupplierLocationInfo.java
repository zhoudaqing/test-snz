package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 供应商整体区域信息
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-19
 */
public class SupplierLocationInfo implements Serializable{

    private static final long serialVersionUID = -8359964397493904779L;

    @Getter @Setter
    private String month;           //月份

    @Getter @Setter
    private String location;        //区域

    @Getter @Setter
    private Integer count;          //计数

}
