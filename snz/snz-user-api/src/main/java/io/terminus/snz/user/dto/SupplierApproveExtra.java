package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-17.
 */
public class SupplierApproveExtra implements Serializable {

    private static final long serialVersionUID = 1121793797615470333L;

    @Setter
    @Getter
    private Date enterApprovedAt;       //入驻审核时间

    @Setter
    @Getter
    private String enterApproverName;   //入驻审核人

    @Setter
    @Getter
    private Date lastRegInfoUpdatedAt;  //最近的注册信息更改时间

    @Setter
    @Getter
    private String lastRegInfoUpdatedApproverName; //最近的注册信息更改审核人

}
