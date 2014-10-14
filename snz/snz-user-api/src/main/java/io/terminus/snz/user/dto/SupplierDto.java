package io.terminus.snz.user.dto;

import com.google.common.base.Objects;
import io.terminus.snz.user.model.Company;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-5.
 */
public class SupplierDto implements Serializable {

    private static final long serialVersionUID = -5916888425345227169L;

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

    @Getter
    @Setter
    private Long id;                         //企业编号

    @Getter
    @Setter
    private String corporation;              //法人公司名称

    @Getter
    @Setter
    private Long userId;                     //用户编号

    @Getter
    @Setter
    private String nick;                     //用户昵称

    @Getter
    @Setter
    private String initAgent;                //法人代表

    @Getter
    @Setter
    private Long regCapital;                 //注册资金

    @Getter
    @Setter
    private String personScale;              //公司规模

    @Getter
    @Setter
    private Integer participateCount;        //交互参与数

    @Setter
    @Getter
    private String includeKeywords;          //公司名称包含的黑名单关键字

    @Getter
    @Setter
    private List<String> mainBusinessNames;  //所有主营业务名称

    @Setter
    @Getter
    private List<String> approverNames;      //审核人(admin可以查看)

    @Getter
    @Setter
    private String officePhone;              //联系人电话

    @Setter
    @Getter
    private String mobile;                   //联系人手机

    @Getter
    @Setter
    private Date createdAt;                   //入驻时间

    @Getter
    @Setter
    private String vCode;

    public SupplierDto() {

    }

    public SupplierDto(Company company) {
        if (company != null) {
            this.id = company.getId();
            this.corporation = company.getCorporation();
            this.userId = company.getUserId();
            this.initAgent = company.getInitAgent();
            this.regCapital = company.getRegCapital();
            this.personScale = company.getPersonScale();
            this.participateCount = company.getParticipateCount();
            this.includeKeywords = company.getIncludeKeywords();
            this.createdAt = company.getCreatedAt();
            this.vCode = company.getSupplierCode();
        }
    }

}
