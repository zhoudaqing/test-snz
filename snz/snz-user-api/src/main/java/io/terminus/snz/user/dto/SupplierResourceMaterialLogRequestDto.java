package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.PurchaserAuthority;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/26/14
 */
public class SupplierResourceMaterialLogRequestDto implements Serializable {
    private static final long serialVersionUID = 1196109110059659482L;

    @Getter
    @Setter
    private Set<Long> subjectIds;  // 条目id，初始提交时生成，之后永远不变

    @Getter
    @Setter
    private Set<String> roles;     // 相关权限，与subjectIds相同，初始生成后永不变

    @Getter
    @Setter
    private Set<Long> bcIds;       // 后台类目id列表，系统自动触发时会增加

    @Getter
    @Setter
    private Set<PurchaserAuthority> authMatrix;    // 所有权限清单，增加类目时或邀请时会增加

    @Getter
    @Setter
    private Set<PurchaserAuthority> checkedMatrix; // 已检查过的
}
