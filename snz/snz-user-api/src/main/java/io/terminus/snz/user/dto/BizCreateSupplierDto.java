package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-8.
 */
public class BizCreateSupplierDto implements Serializable {

    private static final long serialVersionUID = 6775112951966698431L;

    //========================用户信息=====================
    @Getter
    @Setter
    private Integer accountType;               //用户类型

    @Getter
    @Setter
    private String nick;                       //昵称

    @Getter
    @Setter
    private String password;                    //密码

    @Getter
    @Setter
    private String name;                       //联系人姓名

    @Getter
    @Setter
    private String department;                 //联系人部门

    @Getter
    @Setter
    private String duty;                       //联系人职务

    @Getter
    @Setter
    private String phone;                      //固定电话

    @Getter
    @Setter
    private String email;                      //电子邮箱

    @Getter
    @Setter
    private String mobile;                     //手机

    //========================公司信息=====================
    @Getter
    @Setter
    private String corporation;                   //法人公司名称

    @Getter
    @Setter
    private String actingBrand;                   //代理的品牌

    @Getter
    @Setter
    private String initAgent;                     //法人代表

    @Getter
    @Setter
    private Integer regCountry;                   //注册国家代号

    @Getter
    @Setter
    private String desc;                          //公司简介

    @Getter
    @Setter
    private String customers;                    //客户群

    @Getter
    @Setter
    private String businessLicense;               //营业执照路径

    @Getter
    @Setter
    private String businessLicenseId;            //营业执照号

    @Getter
    @Setter
    private String orgCert;                       //组织机构证路径

    @Getter
    @Setter
    private String orgCertId;                     //组织机构证号

    @Getter
    @Setter
    private String taxNo;                         //税务登记证路径

    @Getter
    @Setter
    private String taxNoId;                      //税务登记证号

    @Setter
    @Getter
    private String productLineIds;              //所属产品线编号（多个id之间用逗号分隔）

    @Getter
    @Setter
    private String supplyParkIds;                //可供货园区id（多个id之间用逗号分隔）

    @Getter
    @Setter
    private String mainBusinessIds;              //主营业务编号id（多个id之间用逗号分隔）


}
