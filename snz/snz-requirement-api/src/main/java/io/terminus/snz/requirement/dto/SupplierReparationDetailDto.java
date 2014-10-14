package io.terminus.snz.requirement.dto;

import com.google.common.base.Objects;
import io.terminus.snz.requirement.model.MarketBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import io.terminus.snz.requirement.model.TmoneBadRecord;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Date: 8/11/14
 * Time: 16:48
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public class SupplierReparationDetailDto implements Serializable {
    private static final long serialVersionUID = -765481425378584954L;

    @Getter
    @Setter
    private String moduleNum;

    @Getter
    private SceneBadRecord onScene;

    @Getter
    private MarketBadRecord market;

    @Getter
    private TmoneBadRecord tmone;

    @Getter
    @Setter
    private Integer quantity = 0;

    @Getter
    @Setter
    private Integer money = 0;

    public void setMarket(MarketBadRecord market) {
        this.market = market;
        this.quantity += 1;
        this.money += market.getFee() + market.getPrice();
    }

    public void setOnScene(SceneBadRecord onScene) {
        this.onScene = onScene;
        money += Objects.firstNonNull(onScene.getMoney(), 0);
        quantity += Objects.firstNonNull(onScene.getWCount(), 0);
    }

    public void setTmone(TmoneBadRecord tmone) {
        this.tmone = tmone;
        this.quantity = Objects.firstNonNull(tmone.getNumOfDelivery(), 0 );
    }
}
