package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-16.
 */
public class SupplierApproveDto implements Serializable {

    private static final long serialVersionUID = -3981288296768554681L;

    @Setter
    @Getter
    private Long userId;             //待审核供应商的用户编号

    @Setter
    @Getter
    private Integer operation;       //操作类型：1：供应商入驻or修改企业信息审核通过，2：供应商入驻or修改企业信息审核不通过,3:驳回7天审核通过的供应商

    @Setter
    @Getter
    private Integer resourceType;    //资源类型(0：普通资源，1：标杆企业，2：500强)

    @Setter
    @Getter
    private String competitors;      //竞争对手(对采购商而言)（多个用逗号分隔)

    @Setter
    @Getter
    private String description;      //审核意见

    @Setter
    @Getter
    private Integer approveStatus;   //供应商当前审核状态

    @Setter
    @Getter
    private Long approverId;         //审核人的用户编号

    @Setter
    @Getter
    private String approverName;     //审核人名称

}
