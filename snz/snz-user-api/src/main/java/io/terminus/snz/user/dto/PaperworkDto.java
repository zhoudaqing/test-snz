package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-6-27.
 */
public class PaperworkDto implements Serializable {

    private static final long serialVersionUID = -746080905715162962L;

    @Getter
    @Setter
    private Integer regCountry;                   //注册国家代号

    @Getter
    @Setter
    private String businessLicense;               //营业执照路径

    @Getter
    @Setter
    private String businessLicenseId;            //营业执照号

    @Getter
    @Setter
    private Date blDate;                          //营业执照有效期

    @Getter
    @Setter
    private String orgCert;                       //组织机构证路径

    @Getter
    @Setter
    private String orgCertId;                     //组织机构证号

    @Getter
    @Setter
    private Date ocDate;                          //组织机构证号有效期

    @Getter
    @Setter
    private String taxNo;                         //税务登记证路径

    @Getter
    @Setter
    private String taxNoId;                      //税务登记证号

    @Getter
    @Setter
    private Date tnDate;                          //税务登记证号有效期

}
