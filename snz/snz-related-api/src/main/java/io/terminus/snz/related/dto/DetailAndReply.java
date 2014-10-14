package io.terminus.snz.related.dto;

import io.terminus.common.model.Paging;
import io.terminus.snz.related.models.CompensationDetail;
import io.terminus.snz.related.models.CompensationReply;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 明细页
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
public class DetailAndReply implements Serializable {

    private static final long serialVersionUID = -7334491890059785650L;

    @Getter
    @Setter
    private List<CompensationDetail> compensationDetail;

    @Getter
    @Setter
    private Paging<CompensationReply> replyPaging;
}
