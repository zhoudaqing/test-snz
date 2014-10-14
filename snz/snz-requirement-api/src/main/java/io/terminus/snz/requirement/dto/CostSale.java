package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:采购单位信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@ToString
@EqualsAndHashCode
public class CostSale implements Serializable{

    private static final long serialVersionUID = 6080197565789913721L;

    @Getter
    @Setter
    private Integer salesId;        //采购单位编号

    @Getter
    @Setter
    private String salesName;       //采购单位名称

    @Getter
    @Setter
    private Integer quantityId;     //数量单位

    @Getter
    @Setter
    private String quantityName;    //数量单位名称
}
