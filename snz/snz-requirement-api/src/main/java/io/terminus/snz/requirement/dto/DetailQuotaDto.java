package io.terminus.snz.requirement.dto;

import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.ModuleQuota;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:配额信息包含当前需求是否已全部确认
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-31.
 */
public class DetailQuotaDto implements Serializable {
    private static final long serialVersionUID = 1478451646701966470L;

    @Setter
    @Getter
    private Integer quotaStatus;                //是否选定的供应商已全部确认配额信息（在确认配额生成后0:还未全部确认，1:已全部确认）

    @Setter
    @Getter
    private Paging<ModuleQuota> quotaPaging;    //配额信息的分页处理
}
