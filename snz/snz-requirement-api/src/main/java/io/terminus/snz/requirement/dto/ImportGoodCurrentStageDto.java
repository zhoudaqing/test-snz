package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.ImportGood;
import io.terminus.snz.requirement.model.ImportGoodRow;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 7/14/14
 * Time: 11:06
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodCurrentStageDto implements Serializable {
    private static final long serialVersionUID = -7600886880163974446L;

    @Getter
    @Setter
    ImportGood importGood;

    @Getter
    @Setter
    ImportGoodRow importGoodRow;
}
