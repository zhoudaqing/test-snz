package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-5.
 */
public class SupplierCountByDimension implements Serializable {

    private static final long serialVersionUID = -7394394320916056281L;

    @Setter
    @Getter
    private Long registeredCount = 0L;            //注册（广选）供应商数量

    @Setter
    @Getter
    private Long completedCount = 0L;             //完善信息供应商数量

    @Setter
    @Getter
    private Long standardCount = 0L;              //入围供应商数量

    @Setter
    @Getter
    private Long alternativeCount = 0L;           //备选供应商数量

    @Setter
    @Getter
    private Long inCount = 0L;                    //合作供应商数量

    @Setter
    @Getter
    private Long dieOutCount = 0L;                //淘汰供应商数量

    public void summary() {
        this.registeredCount += (completedCount + standardCount + alternativeCount + inCount + dieOutCount);
        this.completedCount += (standardCount + alternativeCount + inCount + dieOutCount);
        this.standardCount += (alternativeCount + inCount + dieOutCount);
        this.alternativeCount += (inCount);
    }

}
