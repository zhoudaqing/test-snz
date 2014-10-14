package io.terminus.snz.statistic.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Desc:采购商需求的统计数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-10.
 */
@ToString
@EqualsAndHashCode
public class PurchaserRequirementCount implements Serializable {
    private static final long serialVersionUID = 307955313272517105L;

    @Setter
    @Getter
    private Long userId;                            //采购商用户编号

    @Setter
    @Getter
    private String userName;                        //采购商用户名称

    @Setter
    @Getter
    private Map<Integer , Integer> statusCounts;    //采购商的不同阶段的需求统计数据
}
