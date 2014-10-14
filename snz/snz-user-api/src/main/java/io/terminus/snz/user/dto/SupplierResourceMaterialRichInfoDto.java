package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/27/14
 */
public class SupplierResourceMaterialRichInfoDto implements Serializable {
    private static final long serialVersionUID = -7923505103807857509L;

    @Getter
    @Setter
    private SupplierResourceMaterialInfo info; // 供应商基本资质交互信息

    @Getter
    @Setter
    private Long userId;   // 为匹配供应商履历跳转接口参数

    @Getter
    @Setter
    private String approvingBcs;        // 等待审核中的类目列表（json）

    @Getter
    @Setter
    private Boolean checkable;      // 是否能够操作，1.待审核中时能够审核, 2.已通过时能够申请驳回, 3.等待驳回时能够驳回

    @Getter
    @Setter
    private Boolean inviteable;     // 是否能够邀请，只有待审核中时，才有可能需要邀请
}
