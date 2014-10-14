package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@ToString
@EqualsAndHashCode
public class DeliverySale implements Serializable{

    private static final long serialVersionUID = 4800785558353114106L;

    @Getter
    @Setter
    private Integer salesId;        //采购单位编号

    @Getter
    @Setter
    private String salesName;       //采购单位名称
}
