package io.terminus.snz.requirement.dto;

import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.Module;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:用于添加模块页面的数据显示
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-23.
 */
public class ModulesDto implements Serializable {
    private static final long serialVersionUID = -6729998449124207570L;

    @Setter
    @Getter
    private RequirementFactoryDto requirementFactoryDto;        //需求详细信息

    @Setter
    @Getter
    private Paging<ModuleInfoDto> modulePaging;                 //模块的分页数据信息

    // 模块列表，在新品导入中使用，由于 modulePaging 提供的模块数据不是连续的，
    // 当需要采购商的需求下面的模块以逻辑上连续的形式返回，使用 moduleList 替换
    // Paging
    @Getter
    @Setter
    private List<Module> moduleList;

}
