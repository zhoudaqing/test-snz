package io.terminus.snz.requirement.service;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dto.ExcelErrorDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.manager.RequirementExcelManager;
import io.terminus.snz.requirement.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:需求信息的excel解析数据处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-28.
 */
@Slf4j
@Service
public class RequirementExcelServiceImpl implements RequirementExcelService {

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private RequirementExcelManager requirementExcelManager;

    @Autowired
    private BackendCategoryService backendCategoryService;

    @Autowired
    private MWOldModuleInfoService mwOldModuleInfoService;

    @Override
    public Response<List<ExcelErrorDto>> createModule(Long requirementId , List<ModuleInfoDto> moduleInfoDtoList) {
        Response<List<ExcelErrorDto>> result = new Response<List<ExcelErrorDto>>();

        if (moduleInfoDtoList.isEmpty()) {
            result.setError("module.import.empty");
            return result;
        }

        //需求编号
        if(requirementId == null){
            log.error("create module need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);
            if(requirement.getStatus() != null && requirement.getStatus() > RequirementStatus.RES_LOCK.value()){
                //当需求已经操作了需求锁定阶段则不允许在导入模块数据信息
                log.error("requirement status is lock can't create modules");
                result.setError("requirement.status.createModule");
                return result;
            }

            List<ExcelErrorDto> errorDtoList = Lists.newArrayList();
            ExcelErrorDto errorDto;

            List<ModuleInfoDto> moduleInfoList = Lists.newArrayList();
            for(int i=0; i<moduleInfoDtoList.size(); i++){
                errorDto = checkModule(moduleInfoDtoList.get(i));
                if(errorDto.getError()){
                    //校验错误信息
                    errorDto.setLineNum(i + 1);   //记录第几行数据存在问题
                    errorDtoList.add(errorDto);
                }else if(errorDto.getWarning()){
                    //校验警告信息
                    errorDto.setLineNum(i + 1);
                    errorDtoList.add(errorDto);
                    moduleInfoList.add(moduleInfoDtoList.get(i));
                }else{
                    moduleInfoList.add(moduleInfoDtoList.get(i));
                }
            }

            //批量创建模块信息
            requirementExcelManager.createModules(moduleInfoList);

            result.setResult(errorDtoList);
        }catch(Exception e){
            log.error("check module info failed, requirementId={}, error code={}" , Throwables.getStackTraceAsString(e));
            result.setError("module.check.failed");
        }

        return result;
    }

    /**
     * 校验每个Module的数据信息
     * @param moduleInfoDto 每个模块的数据测试
     * @return ExcelErrorDto
     * 返回校验的错误信息
     */
    private ExcelErrorDto checkModule(ModuleInfoDto moduleInfoDto){
        ExcelErrorDto excelErrorDto = new ExcelErrorDto();

        Module module = moduleInfoDto.getModule();
        Map<String , String> errorView = Maps.newHashMap();
        Map<String , String> warningView = Maps.newHashMap();
        if(module != null){
            Response<List<MWOldModuleInfo>> oldModuleRes = mwOldModuleInfoService.findByModuleNumOrModuleName(module.getModuleNum(), module.getModuleName());
            if(!oldModuleRes.isSuccess() || oldModuleRes.getResult().isEmpty()){
                module.setModuleNum(null);
                warningView.put("moduleNum" , "module.Num.null");
            }

            //需求编号
            if(module.getRequirementId() == null){
                log.error("create module need requirementId.");
                errorView.put("requirementId", "requirement.id.null");
            }

            //模块名称
            if(module.getModuleName() == null){
                log.error("create module need module name.");
                errorView.put("moduleName", "module.moduleName.null");
            }

            //模块资源总量
            if(module.getTotal() == null){
                log.error("create module need module total.");
                errorView.put("moduleTotal", "module.total.null");
            }

            //验证是否满足模块类目的最小资源量信息
            Response<BackendCategoryProperty> propertyRes = backendCategoryService.findPropertyById(module.getPropertyId());
            if(!propertyRes.isSuccess()){
                log.error("find backend category property failed, error code={}", propertyRes.getError());
                errorView.put("seriesId", propertyRes.getError());
            }

            //验证模块数量是否大于最小资源量信息
            if(Integer.parseInt(propertyRes.getResult().getValue1()) > module.getTotal()){
                log.error("module total must greater than backend category property.");
                errorView.put("total", "module.total.small");
            }
        }

        //工厂数据
        List<ModuleFactory> factoryList = moduleInfoDto.getFactoryList();
        for(ModuleFactory moduleFactory : factoryList){
            if(Strings.isNullOrEmpty(moduleFactory.getFactoryNum())){
                errorView.put("factoryNum", "module.factory.num.null");
            }

            if(moduleFactory.getResourceNum() == null){
                errorView.put("resourceNum", "module.resource.num.null");
            }
        }

        if(!errorView.isEmpty()){
            //存在数据校验问题
            excelErrorDto.setError(true);
            excelErrorDto.setErrorView(errorView);
            excelErrorDto.setWarningView(warningView);
        }else if(!warningView.isEmpty()){
            //存在数据校验问题
            excelErrorDto.setError(false);
            excelErrorDto.setWarning(true);
            excelErrorDto.setErrorView(errorView);
            excelErrorDto.setWarningView(warningView);
        }

        return excelErrorDto;
    }
}
