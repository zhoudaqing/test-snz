package io.terminus.snz.requirement.dto;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.MarketBadRecord;
import io.terminus.snz.requirement.model.SceneBadRecord;
import io.terminus.snz.requirement.model.TmoneBadRecord;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 一个供应商某天的某个方式的罚款明细，包括那天的罚款笔数和总数
 * 例如昨天入厂索赔的明细
 */
public class SupplierReparationDailyDto implements Serializable {
    private static final long serialVersionUID = -7727035331659101735L;

    @Getter
    @Setter
    private List<SupplierReparationDetailDto> reparations = Lists.newArrayList();

    @Setter
    @Getter
    private Integer quantity = 0;

    @Setter
    @Getter
    private Double money = 0.0;

    @Getter
    @Setter
    private Date currentDate;


    public void setAllMarket(List<MarketBadRecord> markets) {
        Map<String, SupplierReparationDetailDto> dtos = Maps.newHashMap();
        SupplierReparationDetailDto dto;

        int money = 0, quantity = 0;
        for (MarketBadRecord m:markets) {
            if ((dto=dtos.get(m.getModuleNum()))==null) {
                dto = new SupplierReparationDetailDto();
                dto.setModuleNum(m.getModuleNum());
                dtos.put(m.getModuleNum(), dto);
            }

            dto.setMarket(m);
            money += m.getFee() + m.getPrice();
            quantity += 1;
        }

        this.money += ((double)money)/100;
        this.quantity += quantity;
        reparations.addAll(dtos.values());
    }

    public void setAllScene(List<SceneBadRecord> allScene) {
        Map<String, SupplierReparationDetailDto> dtos = Maps.newHashMap();
        SupplierReparationDetailDto dto;

        int money = 0, quantity = 0;
        for (SceneBadRecord s:allScene) {
            if ((dto=dtos.get(s.getModuleNum()))==null) {
                dto = new SupplierReparationDetailDto();
                dto.setModuleNum(s.getModuleNum());
                dtos.put(s.getModuleNum(), dto);
            }

            dto.setOnScene(s);
            money += s.getMoney();
            quantity += s.getWCount();
        }

        this.money += ((double)money)/100;
        this.quantity += quantity;
        reparations.addAll(dtos.values());
    }

    public void setAllTmone(List<TmoneBadRecord> allTmone ){
        Map<String, SupplierReparationDetailDto> dtos = Maps.newHashMap();
        SupplierReparationDetailDto dto;

        int quantity = 0;
        for(TmoneBadRecord t : allTmone){
            dto = new SupplierReparationDetailDto();
            dto.setModuleNum(t.getMaterielNo());
            dtos.put(t.getId().toString(), dto);

            dto.setTmone(t);
            quantity += t.getNumOfDelivery();
        }

        this.quantity += quantity;
        reparations.addAll(dtos.values());
    }

}
