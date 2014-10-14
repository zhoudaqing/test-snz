package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.OldModule;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:衍生号展示数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-09.
 */
@ToString
@EqualsAndHashCode
public class DerivativeDto implements Serializable{
    private static final long serialVersionUID = -6171051345964464504L;

    @Setter
    @Getter
    private Integer listSize;                   //页面一条记录的供应商条数

    @Setter
    @Getter
    private Module module;                      //衍生号模块信息

    @Setter
    @Getter
    private OldModule oldModule;                //衍生品的模块专用号关联的老品信息（有名基准号）

    @Setter
    @Getter
    private List<ModuleQuota> moduleQuotaList;  //获取老品对应的供应商配额信息
}
