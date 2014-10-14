package io.terminus.snz.eai.dto;

import com.haier.OuterSysVendorInfoToMDM.RSPVENDORBANKTABLE;
import com.haier.OuterSysVendorInfoToMDM.RSPVENDORCOMPANYTABLE;
import com.haier.OuterSysVendorInfoToMDM.RSPVENDORPURTABLE;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 7/28/14
 * Time: 17:33
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
@ToString
@NoArgsConstructor
public class MDMVendorCodeApply implements Serializable {
    private static final long serialVersionUID = -2092586252990367701L;

    @Getter
    @Setter
    private Long supplierId;

    @Getter
    @Setter
    private String finGroup;

    @Getter
    @Setter
    private String vCode;           // 商家v码

    @Getter
    @Setter
    private String accountGroup;    // 账户组

    @Getter
    @Setter
    private String taxCode;         // 税号

    @Getter
    @Setter
    private String streetRoom;      // 街道

    @Getter
    @Setter
    private String postCode;        // 邮编

    @Getter
    @Setter
    private String country;         // 国家

    @Getter
    @Setter
    private String region;          // 地区

    @Getter
    @Setter
    private String phone;           // 电话

    @Getter
    @Setter
    private String supplierName;    // 供应商名字

    @Getter
    @Setter
    private RSPVENDORBANKTABLE tBank = new RSPVENDORBANKTABLE();

    @Getter
    @Setter
    private RSPVENDORCOMPANYTABLE tCompany = new RSPVENDORCOMPANYTABLE();

    @Getter
    @Setter
    private RSPVENDORPURTABLE tPru = new RSPVENDORPURTABLE();
}
