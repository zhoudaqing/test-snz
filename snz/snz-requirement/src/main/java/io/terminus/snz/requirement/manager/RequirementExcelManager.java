package io.terminus.snz.requirement.manager;

import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Desc:需求的Excel解析处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-29.
 */
@Slf4j
@Component
public class RequirementExcelManager {

    @Autowired
    private ModuleService moduleService;

    /**
     * 批量创建从excel导入的模块数据(添加事务)
     * @param moduleInfoDtoList excel导入的模块数据
     */
    @Transactional
    public void createModules(List<ModuleInfoDto> moduleInfoDtoList){
        //批量创建模块数据
        for(ModuleInfoDto moduleInfoDto : moduleInfoDtoList){
            moduleService.createModule(moduleInfoDto);
        }
    }
}
