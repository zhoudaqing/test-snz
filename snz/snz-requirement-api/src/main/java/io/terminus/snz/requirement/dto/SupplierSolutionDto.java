package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Desc:用于显示参与需求的供应商信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-13.
 */
@ToString
@EqualsAndHashCode
public class SupplierSolutionDto implements Serializable {
    private static final long serialVersionUID = -4941161176618949983L;

    @Setter
    @Getter
    private Long supplierId;            //供应商编号（公司）

    @Setter
    @Getter
    private String supplierName;        //供应商名称（公司）

    @Setter
    @Getter
    private List<String> supplierTags;  //供应商标签

    @Setter
    @Getter
    private Long userId;                //用户编号

    @Setter
    @Getter
    private String userName;            //用户名称

    @Setter
    @Getter
    private Date dealTime;              //处理时间

    @Setter
    @Getter
    private Integer interactiveStatus;  //底线承诺处理 1:待承诺, 2:全部承诺, 3:部分无法承诺

    @Setter
    @Getter
    private Integer creditStatus;       //信用等级-2 C, -1 B, 1 BBB, 2 A, 3 AA, 4 AAA, 10 OTHERS, 11 APPLYING,

    @Setter
    @Getter
    private Integer qualifyStatus;      //资质验证状态1:已提交，等待审核, 2:资质检查通过, -2:资质检查不通过

    @Setter
    @Getter
    private Integer paidStatus;         //保证金状态0：未提交，1：提交成功，-1：提交失败，2：交易成功，-2：交易失败

    @Setter
    @Getter
    private Boolean solutionStatus;     //是否可以进入方案终投false:还未满足，true:可进入配额
}
