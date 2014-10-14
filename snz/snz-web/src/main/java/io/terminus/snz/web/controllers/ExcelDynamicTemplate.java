package io.terminus.snz.web.controllers;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.MapBuilder;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.requirement.dto.ExcelErrorDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.model.IdentifyName;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.ModuleFactory;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.service.ModuleService;
import io.terminus.snz.requirement.service.RequirementExcelService;
import io.terminus.snz.requirement.service.RequirementService;
import io.terminus.snz.web.util.AnalyzeExcelWeb;
import io.terminus.snz.web.util.HttpHeaderUtil;
import io.terminus.snz.web.util.ImportModulesWithExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-8-27.
 */
@Slf4j
@Controller
@RequestMapping("/api/template")
public class ExcelDynamicTemplate {

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private BackendCategoryService backendCategoryService;

    @Autowired
    private MessageSources messageSources;

    @Autowired
    private AnalyzeExcelWeb analyzeExcelWeb;

    @Autowired
    private RequirementExcelService requirementExcelService;

    @Value("#{app.fileBaseUrl}")
    private String fileBaseUrl;

    private static HashBiMap<String,String> salesMap = HashBiMap.create();

    private static HashBiMap<String,String> quantityMap = HashBiMap.create();

    static{
        //初始化salesMap
        salesMap.put("1","EA");
        salesMap.put("2","JUA");
        salesMap.put("3","KG");
        salesMap.put("4","BAG");
        salesMap.put("5","BOT");
        salesMap.put("6","G");
        salesMap.put("7","L");
        salesMap.put("8","M");
        salesMap.put("9","M2");
        salesMap.put("10","M3");
        salesMap.put("11","ML");
        salesMap.put("12","MM");
        salesMap.put("13","PIA");
        salesMap.put("14","TAO");
        salesMap.put("15","TEN");
        salesMap.put("16","TIA");
        salesMap.put("17","TO");
        salesMap.put("18","ZHA");
        //初始化quantityMap
        quantityMap.put("1","1.0");
        quantityMap.put("2","10.0");
        quantityMap.put("3","100.0");
        quantityMap.put("4","1000.0");
    }

    private LoadingCache<String,List<BackendCategoryProperty>> cache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<String, List<BackendCategoryProperty>>() {
        @Override
        public List<BackendCategoryProperty> load(String key) throws Exception {
            try{
                Response<List<BackendCategoryProperty>> response = backendCategoryService.findPropertiesByBcId(Long.valueOf(key));
                if(response.isSuccess()){
                    return response.getResult();
                }
                else{
                    log.error("fail to find BackendCategoryProperty by bcid {},cause {}", key, response.getError());
                    return Lists.newArrayList();
                }
            }catch (Exception e){
                log.error("fail to find BackendCategoryProperty by bcid {},cause {}", key, Throwables.getStackTraceAsString(e));
                return Lists.newArrayList();
            }
        }
    });

    /**
     * 批量导入模块excel模板
     * @param file
     */
    @RequestMapping(value = "/importmodules/{requirementId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ExcelErrorDto> ImportModulesTemplate(MultipartFile file, @PathVariable("requirementId") final Long requirementId){

        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new JsonResponseException(401, messageSources.get("user.not.login"));
        }

        if(file == null || file.getSize() == 0){
            throw new JsonResponseException(500, messageSources.get("excel.import.empty"));
        }

        Response<ModulesDto> response = moduleService.findByParams(requirementId, null, null);
        if(!response.isSuccess()){
            throw new JsonResponseException(500, messageSources.get(response.getError()));
        }

        Response<List<IdentifyName>> identifyNameResponse =  moduleService.findAllIdentify();
        if(!identifyNameResponse.isSuccess()){
            throw new JsonResponseException(500, messageSources.get(identifyNameResponse.getError()));
        }

        Response<RequirementDto> reqRes = requirementService.findById(requirementId);
        if(!reqRes.isSuccess()){
            throw new JsonResponseException(500, messageSources.get(reqRes.getError()));
        }

        final Requirement requirement = reqRes.getResult().getRequirement();

        Response<List<ExcelErrorDto>> result;
        try {
            ModulesDto moduleDto = response.getResult();
            //动态列当中的资源量工厂
            final List<AddressFactory> addressFactories = moduleDto.getRequirementFactoryDto().getFactoryList();
            //模块属性
            String seriesIds = moduleDto.getRequirementFactoryDto().getRequirement().getSeriesIds();

            //模块属性的双向map
            final HashBiMap<String,String> seriesMap =  HashBiMap.create();
            final HashBiMap<String,Long> propertyMap =  HashBiMap.create();
            if(!Strings.isNullOrEmpty(seriesIds)){
                List<Map<String,String>> list = JsonMapper.nonDefaultMapper().fromJson(seriesIds, List.class);
                for(Map<String,String> map : list){
                    seriesMap.put(map.get("bcId"), map.get("name"));
                    List<BackendCategoryProperty> backendCategoryProperties = cache.get(map.get("bcId"));
                    for(BackendCategoryProperty backendCategoryProperty: backendCategoryProperties){
                        propertyMap.put(map.get("name")+"--"+backendCategoryProperty.getName(),backendCategoryProperty.getId());
                    }
                }
            }

            final HashBiMap<String,IdentifyName> identifyNameMap =  HashBiMap.create();
            for (IdentifyName identifyName :identifyNameResponse.getResult()){
                identifyNameMap.put(identifyName.getName(), identifyName);
            }

            List<ModuleInfoDto> infoDtos = analyzeExcelWeb.analyzeFile(file.getInputStream(), AnalyzeExcelWeb.EXCEL_TYPE.XLSX, 0, 2, new AnalyzeExcelWeb.AnalyzeAction<ModuleInfoDto>(){

                @Override
                public ModuleInfoDto transform(String[] analyzeInfo) throws NumberFormatException {
                    ModuleInfoDto moduleInfoDto = new ModuleInfoDto();
                    Module module = new Module();
                    module.setRequirementId(requirementId);

                    for(int i = 0; i < analyzeInfo.length; i++){
                        switch (i){
                            case 0: {
                                //模块专用号
                                module.setModuleNum(NumberUtils.isNumber(analyzeInfo[i]) ? Float.valueOf(analyzeInfo[i]).intValue()+"" : analyzeInfo[i]);
                                break;
                            }
                            case 1: {
                                //模块描述
                                module.setModuleName(analyzeInfo[i]);

                                //如果是空数据则直接返回null
                                if(Strings.isNullOrEmpty(module.getModuleName())){
                                    return null;
                                }
                                break;
                            }
                            case 2: {
                                //模块属性
                                String seriesName = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(seriesName)){
                                    String seriesId = seriesMap.inverse().get(seriesName);
                                    module.setSeriesId(Long.valueOf(seriesId));
                                    module.setSeriesName(seriesName);
                                }
                                break;
                            }
                            case 3: {
                                //产品分类
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string)){
                                    module.setPropertyId(propertyMap.get(string));
                                }
                                break;
                            }
                            case 4:{
                                //认证(多个以,区分)
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string)){
                                    String[] identifyNames = string.split(",");
                                    List<IdentifyName> list = Lists.newArrayList();
                                    for(String identifyName : identifyNames){
                                        if(identifyNameMap.get(identifyName)!=null){
                                            list.add(identifyNameMap.get(identifyName));
                                        }
                                    }
                                    module.setAttestations(JsonMapper.nonEmptyMapper().toJson(list));
                                }
                                break;
                            }
                            case 5:{
                                //质量目标(PPM)
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string) && NumberUtils.isNumber(string)){
                                    module.setQuality(Double.valueOf(string).intValue());
                                }
                                break;
                            }
                            case 6:{
                                //成本目标
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string) && NumberUtils.isNumber(string)){
                                    module.setCost((int) (Float.valueOf(string) * 100));
                                }
                                break;
                            }
                            case 7:break;
                            case 8:break;
                            case 9:{
                                //高峰月产能
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string) && NumberUtils.isNumber(string)){
                                    module.setDelivery(Float.valueOf(string).intValue());
                                }
                                break;
                            }
                            case 10:{
                                //成本目标数量单位 7 成本目标资源量单位 8 高峰月产能资源量单位 10
                                String quantityName = analyzeInfo[7];
                                String salesName =  analyzeInfo[8];
                                String salesNameDelivery = analyzeInfo[10];
                                module.setUnits(JsonMapper.nonEmptyMapper().toJson(
                                        MapBuilder.<String,Map<String,String>>of()
                                                .put("cost",MapBuilder.<String,String>of()
                                                        .put("salesId",salesMap.inverse().get(salesName))
                                                        .put("salesName",salesName)
                                                        .put("quantityId",quantityMap.inverse().get(quantityName))
                                                        .put("quantityName",Double.valueOf(quantityName).intValue()+"").map())
                                                .put("delivery",MapBuilder.<String,String>of()
                                                        .put("salesId",salesMap.inverse().get(salesNameDelivery))
                                                        .put("salesName",salesNameDelivery).map())
                                        .map()
                                ));
                                break;
                            }
                            case 11:{
                                //要求供货时间
                                String string = analyzeInfo[i];
                                if(!Strings.isNullOrEmpty(string) && NumberUtils.isNumber(string)){
                                    module.setSupplyAt(HSSFDateUtil.getJavaDate(Double.valueOf(string)));
                                }
                                break;
                            }
                        }

                    }

                    Integer total = 0;
                    List<ModuleFactory> factoryList = Lists.newArrayList();
                    for(int i =0; i < addressFactories.size(); i++){
                        AddressFactory factory = addressFactories.get(i);
                        ModuleFactory moduleFactory = new ModuleFactory();
                        moduleFactory.setFactoryName(factory.getFactoryName());
                        moduleFactory.setFactoryNum(factory.getFactoryNum());
                        moduleFactory.setPropertyId(module.getPropertyId());
                        String resource = analyzeInfo[12+2*i];
                        String saleName = analyzeInfo[12+2*i+1];
                        String saleId = salesMap.inverse().get(saleName);
                        moduleFactory.setSalesId(Float.valueOf(saleId).intValue());
                        moduleFactory.setSalesName(saleName);
                        moduleFactory.setResourceNum(Double.valueOf(resource).intValue());
                        factoryList.add(moduleFactory);

                        total += moduleFactory.getResourceNum();
                    }

                    module.setType(requirement.getModuleType());
                    module.setTotal(total);
                    moduleInfoDto.setFactoryList(factoryList);
                    moduleInfoDto.setModule(module);
                    return moduleInfoDto;
                }
            });

            result = requirementExcelService.createModule(requirementId, infoDtos);

        } catch (Exception e) {
            log.error("export module templates fail.cause {}", Throwables.getStackTraceAsString(e));
            throw new JsonResponseException(500,messageSources.get("import.moduletemplate.fail"));
        }

        if(!result.isSuccess()){
            throw new JsonResponseException(500,messageSources.get(result.getError()));
        }

        return result.getResult();
    }

    /**
     * 导出批量导入模块的模板
     * @param requirementId 需求id
     * @return
     */
    @RequestMapping(value = "/modulestemplate/{requirementId}", method = RequestMethod.GET)
    public void exportModulesTemplate(@PathVariable("requirementId") Long requirementId,HttpServletRequest request,HttpServletResponse httpResponse) {
        BaseUser user = UserUtil.getCurrentUser();
        if (user == null) {
            throw new ServiceException(messageSources.get("user.not.login"));
        }
        Response<ModulesDto> response = moduleService.findByParams(requirementId, null, null);
        if(!response.isSuccess()){
            throw new ServiceException(messageSources.get(response.getError()));
        }

        //询问这个地方是否需要判断是否为null
        ModulesDto dto = response.getResult();
        try{
            //动态资源量，动态列
            String[] dynamicTitle = new String[dto.getRequirementFactoryDto().getFactoryList().size()];
            for(int i = 0; i< dto.getRequirementFactoryDto().getFactoryList().size(); i++){
                AddressFactory factory = dto.getRequirementFactoryDto().getFactoryList().get(i);
                dynamicTitle[i] = factory.getFactoryNum();
            }
            String seriesIds = dto.getRequirementFactoryDto().getRequirement().getSeriesIds();
            String[] moduleNameValues = null;
            String[] moduleIds = null;
            Map<String,String> modulesMap = Maps.newHashMap();
            if(!Strings.isNullOrEmpty(seriesIds)){
                List list = JsonMapper.nonDefaultMapper().fromJson(seriesIds, List.class);
                moduleNameValues = new String[list.size()];
                moduleIds = new String[list.size()];
                for(int i = 0; i < list.size(); i++){
                    Map<String,String> map = (Map<String,String>)list.get(i);
                    moduleNameValues[i] = map.get("name");
                    moduleIds[i] = map.get("bcId");
                    modulesMap.put(map.get("bcId"),map.get("name"));
                }
            }

            List<String> categoryPropertyList = Lists.newArrayList();
            if(moduleIds != null && moduleIds.length > 0 ){
                for(String moduleId: moduleIds){
                    final String moduleName = modulesMap.get(moduleId);
                    List<BackendCategoryProperty> backendCategoryProperties = cache.get(moduleId);
                    categoryPropertyList.addAll(Lists.transform(backendCategoryProperties,new Function<BackendCategoryProperty, String>() {
                        @Nullable
                        @Override
                        public String apply(BackendCategoryProperty input) {
                            return  moduleName +"--"+input.getName();
                        }
                    }));
                }
                //cache.get()
            }

            HttpHeaderUtil.setDowloadHeader(request, httpResponse, "模块批量导入模板.xlsx");
            ImportModulesWithExcel.getInstance().exportModuleTemplate(dynamicTitle,moduleNameValues,categoryPropertyList.toArray(new String[0]), httpResponse.getOutputStream());

        }catch (Exception e){
            log.error("export module templates fail.cause {}", Throwables.getStackTraceAsString(e));
            throw new ServiceException(messageSources.get("export.moduletemplate.fail"));
        }

    }

    private StringBuilder formatErrorLog(List<ExcelErrorDto> errorDtos){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("Excel上传成功，但某些数据校验失败,具体失败原因如下:\n");

        for(ExcelErrorDto excelErrorDto : errorDtos){
            stringBuffer.append("警告信息：\n");
            stringBuffer.append("第").append(excelErrorDto.getLineNum()).append("行:");
            for(String warning : excelErrorDto.getWarningView().values()){
                stringBuffer.append(messageSources.get(warning)).append(" ");
            }
            stringBuffer.append("\n");

        }

        for(ExcelErrorDto excelErrorDto : errorDtos){
            stringBuffer.append("错误信息：\n");
            stringBuffer.append("第").append(excelErrorDto.getLineNum()).append("行:");

            for(String error : excelErrorDto.getErrorView().values()){
                stringBuffer.append(messageSources.get(error)).append(" ");
            }

            stringBuffer.append("\n");
        }
        return stringBuffer;
    }
}
