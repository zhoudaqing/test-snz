package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.SupplierQualification;
import io.terminus.snz.user.dto.FinanceDto;
import io.terminus.snz.user.model.Company;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用于MDM供应商核审时，供应商列表、第一步、第二步核审步骤。
 *
 * Date: 7/22/14
 * Time: 11:39
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
@NoArgsConstructor
public class MDMBaseCompanyDto implements Serializable {
    private static final long serialVersionUID = 7463505202642674134L;

    @Getter
    @Setter
    private Company company;

    @Getter
    @Setter
    private SupplierQualification supplierQualification;

    @Getter
    @Setter
    private FinanceDto financeDto;

    @Getter
    @Setter
    private Long requirementId;

    @Getter
    @Setter
    private String requiremntName;

    @Getter
    @Setter
    private Long seriesId;

    @Getter
    @Setter
    private String seriesName;

    @Getter
    @Setter
    private String finGroup;                // 供应商财务组: 采购经理选择，MDM提供清单，一般为1100

    @Getter
    @Setter
    private String nick;                     //用户昵称

    @Getter
    @Setter
    private String contryCode;               // 国家代码

    @Getter
    @Setter
    private String officePhone;              //联系电话

    public MDMBaseCompanyDto(Company company) {
        if (company!=null) {
            this.company = company;
        }
    }

}
