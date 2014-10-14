package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.SupplierModuleCount;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-28.
 */
public class SupplierModuleCountDto implements Serializable {

    @Setter
    @Getter
    private List<SupplierModuleCount> supplierModuleCounts;

    @Setter
    @Getter
    private int supplierTotal;      //模块商总数

    @Setter
    @Getter
    private int bestTotal;           //优选供应商总数

    @Setter
    @Getter
    private int standardTotal;       //合格供应商总数

    @Setter
    @Getter
    private int limitedTotal;        //受限制供应商总数

    @Setter
    @Getter
    private int badTotal;             //淘汰供应商总数

}
