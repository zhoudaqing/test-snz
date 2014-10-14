package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.CompanySupplyPark;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-4.
 */
public class CompanyDto implements Serializable {

    @Setter
    @Getter
    private Company company;                                  //企业基本信息

    @Setter
    @Getter
    private List<CompanyMainBusiness> companyMainBusinesses;  //企业主营业务

    @Setter
    @Getter
    private List<CompanySupplyPark> companySupplyParks;       //可供货园区信息

}
