package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.ImportGood;
import io.terminus.snz.requirement.model.ImportGoodRow;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 7/14/14
 * Time: 16:38
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class ImportGoodDto implements Serializable {
    private static final long serialVersionUID = 8426913194220218666L;

    @Getter
    @Setter
    private ImportGood importGood;

    @Getter
    @Setter
    private List<ImportGoodRow> importGoodRowList;
}
