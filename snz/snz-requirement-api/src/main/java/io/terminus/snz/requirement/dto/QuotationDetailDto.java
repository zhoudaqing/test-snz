package io.terminus.snz.requirement.dto;

import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementSend;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:配额的详细数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-16.
 */
@ToString
@EqualsAndHashCode
public class QuotationDetailDto implements Serializable {
    private static final long serialVersionUID = -3061844034239371977L;

    @Setter
    @Getter
    private Requirement requirement;

    @Setter
    @Getter
    private RequirementSend requirementSend;

    @Setter
    @Getter
    private Paging<TransactInfoDto> transactPaging;
}
