package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.ModuleFactory;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:向百卓抛送的数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-20.
 */
public class ModuleDetailBIZDto implements Serializable {
    private static final long serialVersionUID = -5137184924798199517L;

    @Getter
    @Setter
    private ModuleBIZDto module;                  //模块信息

    @Getter
    @Setter
    private List<ModuleFactory> factoryList;//模块的工厂资源配置
}
