package io.terminus.snz.requirement.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 需求调查问卷
 * Author:Guo Chaopeng
 * Created on 14-9-22.
 */
@ToString
public class RequirementQuestionnaire implements Serializable {

    private static final long serialVersionUID = -5683343489815391730L;

    @Setter
    @Getter
    private Long id;                                  //自增主键

    @Setter
    @Getter
    private Long userId;                                //用户编号

    @Setter
    @Getter
    private Long companyId;                             //公司编号

    @Setter
    @Getter
    private String corporation;                         //供应商名称

    @Setter
    @Getter
    private Long requirementId;                          //需求编号

    @Setter
    @Getter
    private String requirementName;                     //需求名称

    @Setter
    @Getter
    private String noInterfaceOrPackage;                 //无模块接口/封包标准

    @Setter
    @Getter
    private String noClearInterfaceOrPackage;            //接口/封包标准不清晰

    @Setter
    @Getter
    private String noStandardInterfaceOrPackage;          //接口/封包非行业标准

    @Setter
    @Getter
    private String lessOrderCount;                        //订单量较小

    @Setter
    @Getter
    private String parksNotMatch;                         //园区无法配套

    @Setter
    @Getter
    private String shortPeriod;                           //项目周期太短

    @Setter
    @Getter
    private String otherReason;                           //其它原因

    @Setter
    @Getter
    private Date createdAt;

    @Setter
    @Getter
    private Date updatedAt;

}
