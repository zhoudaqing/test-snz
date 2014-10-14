package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-4.
 */
public class RichSupplierDto implements Serializable {

    private static final long serialVersionUID = 4575942220106855725L;
    @Setter
    @Getter
    private User user;                                           //用户信息

    @Setter
    @Getter
    private Company company;                                     //企业基础信息

    @Setter
    @Getter
    private ContactInfo contactInfo;                             //联系人信息

    @Setter
    @Getter
    private List<CompanyMainBusiness> companyMainBusinesses;     //主营业务

    @Setter
    @Getter
    private List<CompanySupplyPark> companySupplyParks;          //可供货园区


}
