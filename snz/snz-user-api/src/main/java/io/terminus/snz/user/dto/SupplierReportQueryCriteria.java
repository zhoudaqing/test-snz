package io.terminus.snz.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:Guo Chaopeng
 * Created on 14-9-3.
 */
@NoArgsConstructor
@AllArgsConstructor
public class SupplierReportQueryCriteria implements Serializable {

    private static final long serialVersionUID = -8735299542414165189L;

    @Setter
    @Getter
    private Date registeredAtStart;           //注册时间开始范围

    @Setter
    @Getter
    private Date registeredAtEnd;             //结束时间结束范围

    @Setter
    @Getter
    private Long moduleId;                    //模块（前台一级类目）编号

    @Setter
    @Getter
    private Long mainBusinessId;              //主营业务（前台三级类目）编号

    @Setter
    @Getter
    private Integer step;                     //供应商所处阶段（状态）

    @Setter
    @Getter
    private Long supplyParkId;                //园区编号

    @Setter
    @Getter
    private Integer strategy;                 //策略

}
