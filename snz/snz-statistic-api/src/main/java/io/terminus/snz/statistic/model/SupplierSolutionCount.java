package io.terminus.snz.statistic.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * Desc:供应商方案信息的统计数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-10.
 */
@ToString
@EqualsAndHashCode
public class SupplierSolutionCount implements Serializable {
    private static final long serialVersionUID = 3704715305941499197L;

    @Setter
    @Getter
    private Long userId;                            //供应商用户编号

    @Setter
    @Getter
    private String userName;                        //供应商用户名称

    @Setter
    @Getter
    private Map<Integer , Integer> statusCounts;    //供应商的不同阶段的统计数据
}
