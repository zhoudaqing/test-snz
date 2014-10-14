package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc: 模块详细的信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-05.
 */
@ToString
@EqualsAndHashCode
public class ModuleInfoDto implements Serializable {
    private static final long serialVersionUID = -8227197740049688157L;

    @Getter
    @Setter
    private Module module;                  //模块信息

    @Getter
    @Setter
    private List<ModuleFactory> factoryList;//模块的工厂资源配置
}
