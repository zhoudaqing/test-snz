package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-4.
 */
public class SupplierReportExportDto implements Serializable {

    @Setter
    @Getter
    private String creditQualifyResult;  //信用等级评价结果

    @Setter
    @Getter
    private String aptitudeQualifyResult; //资质验证结果

    @Setter
    @Getter
    private Integer selectedSolutionCount; //方案中选数量

    @Setter
    @Getter
    private String regCountryName;        //注册国家名称

    @Setter
    @Getter
    private String regRegion;             //隶属地区

    @Setter
    @Getter
    private User user;          //状态、类型

    @Setter
    @Getter
    private Company company;      //公司相关信息

    @Setter
    @Getter
    private ContactInfo contactInfo;  //联系人信息

    @Setter
    @Getter
    private Finance finance;     //销售额、净利润

    @Setter
    @Getter
    private List<CompanySupplyPark> companySupplyParks;  //可供货园区

    @Setter
    @Getter
    private CompanyExtraRD companyExtraRD;  //研发资产、实验室面积、专利数量

}
