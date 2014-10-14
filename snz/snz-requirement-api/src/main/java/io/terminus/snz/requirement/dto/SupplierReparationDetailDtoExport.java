package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.MarketBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-26.
 */
@NoArgsConstructor
public class SupplierReparationDetailDtoExport implements Serializable {
    private static final long serialVersionUID = -4883228461387042727L;

    public SupplierReparationDetailDtoExport(Object s) {
        if (s instanceof MarketBadRecord) {
            this.market = (MarketBadRecord) s;
            date = market.getReportAt();
        } else if (s instanceof SceneBadRecord) {
            this.onScene = (SceneBadRecord) s;
            date = onScene.getSendAt();
        }
        // t minus one
    }

    @Setter
    @Getter
    private Date date;

    @Setter
    @Getter
    private Integer type;

    @Setter
    @Getter
    private SceneBadRecord onScene;

    @Setter
    @Getter
    private MarketBadRecord market;
}
