package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.ModuleQuotation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:谈判模块数据（包含报价信息）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-16.
 */
@ToString
@EqualsAndHashCode
public class TransactInfoDto implements Serializable {
    private static final long serialVersionUID = -1083530238687930712L;

    @Setter
    @Getter
    private ModuleQuotation moduleQuotation;

    @Setter
    @Getter
    private List<ModuleQuota> moduleQuotaList;
}
