package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-26.
 */
public class SupplierImportDto implements Serializable {

    private static final long serialVersionUID = -8386474655955635971L;
    @Setter
    @Getter
    private User user;

    @Setter
    @Getter
    private Company company;

    @Setter
    @Getter
    private CompanyRank companyRank;

    @Setter
    @Getter
    private ContactInfo contactInfo;

    @Setter
    @Getter
    private List<CompanyMainBusiness> companyMainBusinesses;

}
