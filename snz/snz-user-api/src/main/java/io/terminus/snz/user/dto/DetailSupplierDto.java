package io.terminus.snz.user.dto;

import com.google.common.base.Objects;
import io.terminus.snz.user.model.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-10.
 */
public class DetailSupplierDto implements Serializable {

    private static final long serialVersionUID = 7462157298184175384L;

    @Setter
    @Getter
    private Integer approvedType;           //审核类型(1:入驻审核，2:修改审核)

    public static enum ApprovedType {
        ENTER(1, "入驻审核"),
        MODIFY(2, "修改审核");

        private final int value;

        private final String display;

        private ApprovedType(int value, String display) {
            this.value = value;
            this.display = display;
        }

        public static ApprovedType from(Integer value) {
            for (ApprovedType type : ApprovedType.values()) {
                if (Objects.equal(type.value, value)) {
                    return type;
                }
            }
            return null;
        }

        public int value() {
            return value;
        }

        public String toString() {
            return display;
        }
    }

    @Setter
    @Getter
    private User user;                                           //用户信息

    @Setter
    @Getter
    private ContactInfo contactInfo;                             //联系人信息

    @Setter
    @Getter
    private Company company;                                     //企业基本信息

    @Setter
    @Getter
    private List<CompanySupplyPark> companySupplyParks;          //可供货园区

    @Setter
    @Getter
    private List<CompanyMainBusiness> companyMainBusinesses;     //主营业务

}
