package io.terminus.snz.eai.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Date: 8/28/14
 * Time: 0:14
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
@ToString
@NoArgsConstructor
@XStreamAlias("row")
public class MDMModuleInfoRow implements Serializable {
    private static final long serialVersionUID = 9168777453096592560L;

    @Getter
    @Setter
    private String MATERIAL_CODE; // 物料号

    @Getter
    @Setter
    private String MATERIAL_DESCRITION; // 物料描述

    @Getter
    @Setter
    private String PRIMARY_UOM; // 使用单位

    @Setter
    @Getter
    private String MTL_GROUP_CODE; // 类目编号

    @Getter
    @Setter
    private String HRMODULARNAME; // 三级类目名
}
