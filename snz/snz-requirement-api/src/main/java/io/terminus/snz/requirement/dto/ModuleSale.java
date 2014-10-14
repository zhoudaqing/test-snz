package io.terminus.snz.requirement.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Desc:模块的单位信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
public class ModuleSale implements Serializable {

    private static final long serialVersionUID = 4198738693399012197L;

    @Setter
    @Getter
    private CostSale cost;

    @Setter
    @Getter
    private DeliverySale delivery;
}
