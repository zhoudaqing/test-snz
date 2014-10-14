package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import io.terminus.snz.haier.manager.PLMModuleManager;
import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.related.services.DeliveryService;
import io.terminus.snz.requirement.dao.IdentifyNameDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.ModuleFactoryDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.ModuleQuotaDao;
import io.terminus.snz.requirement.dto.ModuleDetailDto;
import io.terminus.snz.requirement.dto.ModuleInfoDto;
import io.terminus.snz.requirement.dto.ModulesDto;
import io.terminus.snz.requirement.dto.RequirementFactoryDto;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.manager.CountManager;
import io.terminus.snz.requirement.manager.ModuleManager;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.requirement.tool.FlowNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Throwables.getStackTraceAsString;
import static io.terminus.common.utils.Arguments.isNullOrEmpty;
import static io.terminus.common.utils.Arguments.positive;

/**
 * Desc:模块信息处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */
@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private RequirementDao requirementDao;                      //需求信息

    @Autowired
    private ModuleDao moduleDao;                                //模块管理

    @Autowired
    private OldModuleDao oldModuleDao;

    @Autowired
    private ModuleQuotaDao moduleQuotaDao;

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;                  //模块工厂管理

    @Autowired
    private IdentifyNameDao identifyNameDao;                    //认证信息管理

    @Autowired
    private CountManager countManager;                          //统计管理

    @Autowired
    private ModuleManager moduleManager;                        //模块管理

    @Autowired
    private PLMModuleManager plmModuleManager;                  //plm系统管理

    @Autowired
    private BackendCategoryService backendCategoryService;      //类目信息判断

    @Autowired
    private DeliveryService deliveryService;                    //园区信息

    @Override
    public Response<Boolean> createModule(ModuleInfoDto moduleInfoDto) {
        Response<Boolean> result = new Response<Boolean>();

        //模块表单验证
        Response<Boolean> checkFormRes = checkForm(moduleInfoDto);
        if(!checkFormRes.isSuccess()){
            log.error("check module form failed, error code={}", checkFormRes.getError());
            result.setError(checkFormRes.getError());
            return result;
        }

        try{
            Module module = moduleInfoDto.getModule();

            Requirement requirement = requirementDao.findById(module.getRequirementId());
            if(requirement.getStatus() != null && requirement.getStatus() > 2){
                //当方案已锁定以后将无法更改需求内容(或是被删除状态都不能更改状态)
                log.error("requirement have be locked or delete, can't to create module info. requirementId={}", module.getRequirementId());
                result.setError("requirement.lock.existed");
                return result;
            }

            //模块的数据校验
            Response<Boolean> checkRes = null;
            switch(Module.Type.from(module.getType())){
                case OLD_TYPE:      //老品
                    break;

                case DERIVE_TYPE:   //衍生号
                    checkRes = checkDerive(module);
                    break;

                case JIA_ZHI:       //甲指
                    break;

                case PATENT:        //专利
                    break;
            }

            //数据检验失败
            if(checkRes != null && !checkRes.isSuccess()){
                log.error("check module info failed, error code={}", checkRes.getError());
                result.setError(checkRes.getError());
                return result;
            }

            //校验模块的工厂信息
            for(ModuleFactory moduleFactory : moduleInfoDto.getFactoryList()){
                if(moduleFactory.getPropertyId() == null){
                    //未找到工厂对应的类目信息
                    log.error("create module factory need factory propertyId.");
                    result.setError("module.factory.propertyId");
                    return result;
                }
            }

            //写入模块的唯一流水码
            module.setModuleFlowNum(FlowNumber.createModuleFlowNum(requirement.getProductName() , requirement.getMaterielName()));

            //写入模块详细的信息
            Long moduleId = moduleDao.create(module);

            //写入模块的工厂信息
            for(ModuleFactory moduleFactory : moduleInfoDto.getFactoryList()){
                moduleFactory.setModuleId(moduleId);
            }
            moduleFactoryDao.createBatch(moduleInfoDto.getFactoryList());

            //将数据写入redis做统计数据信息
            countManager.createMEvent(module.getRequirementId(), module);

            result.setResult(true);
        }catch(Exception e){
            log.error("create moduleInfoDto ({}) failed, cause:{}", moduleInfoDto, getStackTraceAsString(e));
            result.setError("module.create.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> update(ModuleInfoDto moduleInfoDto) {
        Response<Boolean> result = new Response<Boolean>();

        if(moduleInfoDto.getModule().getId() == null){
            log.error("update module need id.");
            result.setError("module.id.null");
            return result;
        }

        try{
            Module module = moduleInfoDto.getModule();

            Requirement requirement = requirementDao.findById(module.getRequirementId());
            if(requirement.getStatus() != null && requirement.getStatus() > 2){
                //当方案已锁定以后将无法更改需求内容(或是被删除状态都不能更改状态)
                log.error("requirement have be locked or delete, can't to update module info. requirementId={}",
                        module.getRequirementId());
                result.setError("requirement.lock.existed");
                return result;
            }

            //需求模块信息
            moduleDao.update(module);

            //更新工厂数据信息
            if(moduleInfoDto.getFactoryList() != null){
                List<ModuleFactory> moduleFactoryList = Lists.newArrayList();
                for(ModuleFactory moduleFactory : moduleInfoDto.getFactoryList()){
                    if(moduleFactory.getModuleId() == null){
                        moduleFactory.setModuleId(module.getId());
                        moduleFactoryList.add(moduleFactory);
                    }else{
                        //更新数据
                        moduleFactory.setModuleId(module.getId());
                        moduleFactoryDao.update(moduleFactory);
                    }
                }
                //添加新数据
                if(!moduleFactoryList.isEmpty()){
                    moduleFactoryDao.createBatch(moduleFactoryList);
                }
            }

            //获取旧的数据
            Module oldModule = moduleDao.findById(module.getId());

            //将数据写入redis做统计数据信息
            countManager.updateMEvent(module.getRequirementId(), module, oldModule);

            result.setResult(moduleDao.update(module));
        }catch(Exception e){
            log.error("update module failed, cause:{}", getStackTraceAsString(e));
            result.setError("module.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> delete(Long moduleId) {
        Response<Boolean> result = new Response<Boolean>();

        //需求模块编号
        if(moduleId == null){
            log.error("delete module need id.");
            result.setError("module.id.null");
            return result;
        }

        try{
            //获取旧的数据
            Module module = moduleDao.findById(moduleId);
            Requirement requirement = requirementDao.findById(module.getRequirementId());
            if(requirement.getStatus() != null && requirement.getStatus() > 2){
                //当方案已锁定以后将无法更改需求内容(或是被删除状态都不能更改状态)
                log.error("requirement have be locked or delete, can't to delete module info. requirementId={}",
                        module.getRequirementId());
                result.setError("requirement.lock.existed");
                return result;
            }

            //将数据写入redis做统计数据信息
            countManager.deleteMEvent(module.getRequirementId(), module);

            result.setResult(moduleDao.delete(moduleId));
        }catch(Exception e){
            log.error("delete module failed, moduleId={} error code={}",
                    moduleId, getStackTraceAsString(e));
            result.setError("module.delete.failed");
        }

        return result;
    }

    @Override
    public Response<List<IdentifyName>> findAllIdentify() {
        Response<List<IdentifyName>> result = new Response<List<IdentifyName>>();

        try{
            result.setResult(identifyNameDao.findAllName());
        }catch (Exception e){
            log.error("find all identify name failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("find.identify.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> countSelectNum(Long requirementId) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("find requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            moduleManager.countSelectNum(requirementId);
            result.setResult(true);
        }catch(Exception e){
            log.error("count requirement module select number failed, requirementId={}, error code={}", requirementId , Throwables.getStackTraceAsString(e));
            result.setError("module.countNum.failed");
        }

        return result;
    }

    @Override
    public Response<ModulesDto> findByParams(Long requirementId, Integer pageNo, Integer size) {
        Response<ModulesDto> result = new Response<ModulesDto>();

        if(requirementId == null){
            log.error("find requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            ModulesDto modulesDto = new ModulesDto();

            RequirementFactoryDto requirementFactoryDto = new RequirementFactoryDto();

            //获取需求信息
            Requirement requirement = requirementDao.findById(requirementId);
            requirementFactoryDto.setRequirement(requirement);

            //获取全部的需求工厂信息
            // guava不支持序列化
            List<Long> ids = Lists.transform(Splitters.COMMA.splitToList(requirement.getDeliveryAddress()) , new Function<String, Long>() {
                @Nullable
                @Override
                public Long apply(String input) {
                    return Long.parseLong(input);
                }
            });

            Response<List<AddressFactory>> factoryResponse = deliveryService.findFactoryByIds(ids);

            if(!factoryResponse.isSuccess()){
                log.error("find requirement factory info failed, error code={}", factoryResponse.getError());
                result.setError(factoryResponse.getError());
                return result;
            }
            requirementFactoryDto.setFactoryList(factoryResponse.getResult());

            //获取模块信息
            Paging<Module> modulePaging = moduleDao.findByParams(requirementId , params);

            //写入工厂配额信息
            List<ModuleInfoDto> moduleInfoList = Lists.newArrayList();
            ModuleInfoDto moduleInfoDto;
            for(Module module : modulePaging.getData()){
                moduleInfoDto = new ModuleInfoDto();
                moduleInfoDto.setModule(module);
                moduleInfoDto.setFactoryList(moduleFactoryDao.findByModuleId(module.getId()));

                moduleInfoList.add(moduleInfoDto);
            }

            //获取模块分页信息
            modulesDto.setRequirementFactoryDto(requirementFactoryDto);
            modulesDto.setModulePaging(new Paging<ModuleInfoDto>(modulePaging.getTotal() , moduleInfoList));
            result.setResult(modulesDto);
        }catch(Exception e){
            log.error("find module failed, requirementId={}, error code={}",
                    requirementId, getStackTraceAsString(e));
            result.setError("module.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleDetailDto>> findDetailByParams(Long requirementId, Integer moduleType, Long moduleId, Integer pageNo, Integer size) {
        Response<Paging<ModuleDetailDto>> result = new Response<Paging<ModuleDetailDto>>();

        try{
            Map<String , Object> params = Maps.newHashMap();
            Map<Long , Requirement> reqCache = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());
            if(moduleId!=null) {
                params.put("id", moduleId);
            }
            if(moduleType!=null) {
                params.put("type", moduleType);
            }

            //获取模块分页信息
            Paging<Module> paging = moduleDao.findByParams(requirementId , params);

            Requirement requirement;
            Paging<ModuleDetailDto> detailPaging;
            if(paging.getTotal() == 0){
                detailPaging = new Paging<ModuleDetailDto>(0l , new ArrayList<ModuleDetailDto>());
            }else{
                List<ModuleDetailDto> detailDtoList = Lists.newArrayList();
                ModuleDetailDto moduleDetailDto;
                for(Module module : paging.getData()){
                    moduleDetailDto = new ModuleDetailDto();

                    //写入需求数据信息
                    requirement = getReqCache(reqCache , module.getRequirementId());
                    if(requirement != null){
                        moduleDetailDto.setRequirementId(requirement.getId());
                        moduleDetailDto.setRequirementName(requirement.getName());
                        moduleDetailDto.setProductId(requirement.getProductId());
                        moduleDetailDto.setProductName(requirement.getProductName());
                    }

                    moduleDetailDto.setModuleId(module.getId());
                    moduleDetailDto.setModuleName(module.getModuleName());
                    moduleDetailDto.setModuleNum(module.getModuleNum());
                    moduleDetailDto.setSeriesId(module.getSeriesId());
                    moduleDetailDto.setSeriesName(module.getSeriesName());
                    detailDtoList.add(moduleDetailDto);
                }

                detailPaging = new Paging<ModuleDetailDto>(paging.getTotal() , detailDtoList);
            }

            result.setResult(detailPaging);
        }catch(Exception e){
            log.error("find module detail info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("module.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<Module>> findByRequirementId(Long requirementId, Long creatorId,
                                                        Integer pageNo, Integer size) {
        Response<Paging<Module>> result = new Response<Paging<Module>>();

        if(requirementId == null){
            log.error("find requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        if(creatorId == null){
            log.error("find requirement need creatorId");
            result.setError("requirement.creatorId.null");
            return result;
        }

        try{
            //是否是需求创建者
            if(!Objects.equal(requirementDao.findById(requirementId).getCreatorId() , creatorId)){
                log.error("the user is not requirement creator.");
                result.setError("requirement.creatorId.noPower");
                return result;
            }
            Map<String , Object> params = Maps.newHashMap();
            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());
            params.put("type", Requirement.ModuleType.NEW_TYPE.value());

            //获取模块分页信息
            result.setResult(moduleDao.findByParams(requirementId, params));
        }catch(Exception e){
            log.error("find module failed, requirementId={}, error code={}",
                    requirementId, getStackTraceAsString(e));
            result.setError("module.find.failed");
        }

        return result;
    }

    @Override
    public Boolean setModuleNum(){
        Boolean result = false;

        try{
            //调用plm程序写入模块专用号信息
            plmModuleManager.setModuleNum();
            result = true;
        }catch(Exception e){
            log.error("set module number failed, error code={}", getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    public Response<Paging<ModulesDto>> pagingByPurchaserId(Long purchaserId, Integer pageNo, Integer size) {
        PageInfo page = new PageInfo(pageNo, size);
        Response<Paging<ModulesDto>> result = new Response<Paging<ModulesDto>>();

        try {
            checkArgument(Objects.equal(purchaserId, 0L), positive(purchaserId));

            Map<String, Object> findReqParam = Maps.newHashMap();
            findReqParam.putAll(page.toMap(null, "size"));
            findReqParam.put("status", RequirementStatus.SUP_SOL.value());
            Paging<Requirement> requirements = requirementDao.findByParams(
                    purchaserId, findReqParam);
            if (Objects.equal(requirements.getTotal(), 0L) || isNullOrEmpty(requirements.getData())) {
                result.setResult(new Paging<ModulesDto>(0l, Lists.<ModulesDto>newArrayList()));
                return result;
            }

            // 实际上的偏移量
            Integer realOffSet, sectionLimit, limit=page.getLimit();
            List<ModulesDto> modules = Lists.newArrayList();
            Integer logicalOffset = page.getOffset();
            RequirementFactoryDto requirementFactoryDto;
            for (Requirement r : requirements.getData()) {
                // 找到实际上的 offset，和每个 section 的 limit
                Integer count = moduleDao.countById(r.getId());
                if (count <= logicalOffset) {
                    logicalOffset -= count;
                    continue;
                } else {
                    // 偏移量命中
                    realOffSet = logicalOffset;

                    sectionLimit = count - logicalOffset;
                    if (limit >= sectionLimit) {
                        limit -= sectionLimit;
                    } else {
                        sectionLimit = limit;
                        limit = 0;
                    }

                    logicalOffset = 0;
                }

                // now use r.getId, realOffset, sectionLimit paging module
                Map<String, Object> pagingModuleParam = Maps.newHashMap();
                pagingModuleParam.put("offset", realOffSet);
                pagingModuleParam.put("size", sectionLimit);
                Paging<Module> modulePage = moduleDao.findByParams(r.getId(), pagingModuleParam);
                if (Objects.equal(0L, modulePage.getTotal()) || isNullOrEmpty(modulePage.getData())) {
                    continue;
                }
                ModulesDto dto = new ModulesDto();
                List<Module> data = modulePage.getData();

                requirementFactoryDto = new RequirementFactoryDto();
                requirementFactoryDto.setRequirement(r);
                dto.setRequirementFactoryDto(requirementFactoryDto);
                dto.setModuleList(data);
                modules.add(dto);

                // List<ModuleDto> 里面的 module 记录数量已经达到上限
                if (limit == 0) {
                    break;
                }
            }

            // 返回的 Paging，total 为改采购商下面所有需求的模块
            // Data
            Map<String, Object> countBy = Maps.newHashMap();
            countBy.put("status", RequirementStatus.SUP_SOL.value());
            long total = moduleDao.countByPurchaser(purchaserId, countBy);
            result.setResult(new Paging<ModulesDto>(total, modules));
        } catch (IllegalArgumentException e) {
            log.error("`pagingByPurchaserId` invoke with illegal arguments: purchaserId:{}, e:{}",
                    purchaserId, getStackTraceAsString(e));
        }catch (Exception e) {
            log.error("`pagingByPurchaserId` invoke fail. Params: purchaserId:{}, pageNo:{}, size:{}, e:{}",
                    purchaserId, pageNo, size, getStackTraceAsString(e));
            result.setError("module.paging.with.purchaser.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Boolean> writeOldModule() {
        Response<Boolean> result = new Response<Boolean>();
        try {
            moduleManager.writeOldModule();
            result.setResult(true);
        } catch (Exception e) {
            log.error("`writeOldModule` invoke fail. e:{}", e);
            result.setError("module.info.dump.fail");
            return result;
        }

        return result;
    }

    @Override
    public Response<Module> findModuleByModuleNum(String moduleNum) {
        Response<Module> resp = new Response<Module>();
        try{
            Module param = new Module();
            param.setModuleNum(moduleNum);
            Module module = moduleDao.findOneBy(param);
            resp.setResult(module);
            return resp;
        }catch (Exception e){
            resp.setError("module.find.failed");
            log.error("Faild to select from `snz_modules` with param:{}", moduleNum, e);
            return resp;
        }
    }

    /**
     * 校验衍生号的数据信息
     * @param module    模块信息
     * @return  Boolean
     * 返回校验是否成功
     */
    private Response<Boolean> checkDerive(Module module){
        Response<Boolean> result = new Response<Boolean>();

        //老品编号
        if(module.getOldModuleId() == null){
            log.error("create derive module oldModuleId can't be null");
            result.setError("module.oldModuleId.null");
            return result;
        }

        //模块专用号
        if(Strings.isNullOrEmpty(module.getModuleNum())){
            log.error("create derive module moduleNum can't be null.");
            result.setError("module.moduleNum.null");
            return result;
        }

        //是否存在衍生号
        if(!moduleDao.findDerive(module.getModuleNum() , module.getType()).isEmpty()){
            log.error("derive module have existed, can't create again.");
            result.setError("module.derive.exist");
            return result;
        }

        result.setResult(true);
        return result;
    }

    /**
     * 验证模块的基本数据信息是否正确
     * @param moduleInfoDto    模块详细数据信息
     * @return  Boolean
     * 返回模块表单数据信息
     */
    private Response<Boolean> checkForm(ModuleInfoDto moduleInfoDto){
        Response<Boolean> result = new Response<Boolean>();

        Module module = moduleInfoDto.getModule();

        //需求编号
        if(module.getRequirementId() == null){
            log.error("create module need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        //模块名称
        if(Strings.isNullOrEmpty(module.getModuleName())){
            log.error("create module need module name.");
            result.setError("module.moduleName.null");
            return result;
        }

        //模块资源总量
        if(module.getTotal() == null){
            log.error("create module need module total.");
            result.setError("module.total.null");
            return result;
        }

        //验证是否满足模块类目的最小资源量信息
        Response<List<BackendCategoryProperty>> propertyRes = backendCategoryService.findPropertiesByBcId(module.getSeriesId());
        if(!propertyRes.isSuccess()){
            log.error("find backend category property failed, error code={}", propertyRes.getError());
            result.setError(propertyRes.getError());
            return result;
        }

        if(propertyRes.getResult() == null || propertyRes.getResult().isEmpty()){
            log.error("backend category property is empty.");
            result.setError("module.property.null");
            return result;
        }

        //验证模块数量是否大于最小资源量信息
        if(Integer.parseInt(propertyRes.getResult().get(0).getValue1()) > module.getTotal()){
            log.error("module total must greater than backend category property.");
            result.setError("module.total.small");
            return result;
        }

        result.setResult(true);
        return result;
    }

    /**
     * 根据物料号查询老品基本信息
     * @param moduleNum 老品物料号
     * @return 老品基本信息
     */
    @Override
    public Response<DerivativeDto> queryOldModule(String moduleNum) {
        Response<DerivativeDto> resp = new Response<DerivativeDto>();
        DerivativeDto DerivativeDto = new DerivativeDto();
        try{
            //传入参数不可为空
            if(Strings.isNullOrEmpty(moduleNum)){
                log.error("moduleId cannot be null to select from snz_moduels");
                resp.setError("oldmodule.find.failed");
                return  resp;
            }
            //查询老品基本信息详情
            OldModule oldModule = oldModuleDao.findByModuleNum(moduleNum) ;
            //查询老品供应商配额信息
            String quotas[] = oldModule.getQuotaIds().split(",");
            List<ModuleQuota> moduleQuotaList = moduleQuotaDao.findByIds(quotas);
            //返回值
            DerivativeDto.setOldModule(oldModule);
            DerivativeDto.setModuleQuotaList(moduleQuotaList);
            resp.setResult(DerivativeDto);
            return resp;
        }catch (Exception e){
            resp.setError("oldmodule.find.failed");
            log.error("Faild to select from `snz_moduels` with param:{}", moduleNum, e);
            return resp;
        }
    }

    /**
     * 获取需求的信息
     * @param reqMap        需求数据缓冲信息
     * @param requirementId 需求编号
     * @return
     */
    private Requirement getReqCache(Map<Long , Requirement> reqMap, Long requirementId){
        if(requirementId == null){
            return null;
        }

        if(reqMap.get(requirementId) == null){
            reqMap.put(requirementId , requirementDao.findById(requirementId));
        }

        return reqMap.get(requirementId);
    }

    /**
     * 验证模块的基本数据信息是否正确
     * @param module    模块数据信息
     * @return  Boolean
     * 返回模块表单数据信息
     */
    private Response<Boolean> checkDeriveForm(Module module){
        Response<Boolean> result = new Response<Boolean>();

        //需求编号
        if(module.getRequirementId() == null){
            log.error("create module need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        //模块名称
        if(Strings.isNullOrEmpty(module.getModuleName())){
            log.error("create module need module name.");
            result.setError("module.moduleName.null");
            return result;
        }

        //模块资源总量
        if(module.getTotal() == null){
            log.error("create module need module total.");
            result.setError("module.total.null");
            return result;
        }

        //验证是否满足模块类目的最小资源量信息
        Response<List<BackendCategoryProperty>> propertyRes = backendCategoryService.findPropertiesByBcId(module.getSeriesId());
        if(!propertyRes.isSuccess()){
            log.error("find backend category property failed, error code={}", propertyRes.getError());
            result.setError(propertyRes.getError());
            return result;
        }

        if(propertyRes.getResult() == null || propertyRes.getResult().isEmpty()){
            log.error("backend category property is empty.");
            result.setError("module.property.null");
            return result;
        }

        //验证模块数量是否大于最小资源量信息
        if(Integer.parseInt(propertyRes.getResult().get(0).getValue1()) > module.getTotal()){
            log.error("module total must greater than backend category property.");
            result.setError("module.total.small");
            return result;
        }

        result.setResult(true);
        return result;
    }

    /**
     * 根据基准号(老品)信息生成衍生号信息
     * @param derivativeDto 新品老品数据
     * @return true or false
     */
    @Override
    public Response<Boolean> createDeriveModule(DerivativeDto derivativeDto) {
        Response<Boolean> result = new Response<Boolean>();
        try{
            //获取衍生号信息
            Module derive = derivativeDto.getModule();
            //获取衍生号的基准号信息
            OldModule oldModule = derivativeDto.getOldModule();
            //写入衍生号和基准号的关联系
            derive.setOldModuleId(oldModule.getId());
            derive.setTotal(oldModule.getTotal());
            derive.setType(Module.Type.DERIVE_TYPE.value());
            //模块表单验证
            Response<Boolean> checkFormRes = checkDeriveForm(derive);
            if(!checkFormRes.isSuccess()){
                log.error("check derive_module form failed, error code={}", checkFormRes.getError());
                result.setError(checkFormRes.getError());
                return result;
            }
            //模块的数据校验(必填校验，唯一校验)
            Response<Boolean> checkRes = checkDerive(derive);
            if(checkRes != null && !checkRes.isSuccess()){
                log.error("check derive_module info failed, error code={}", checkRes.getError());
                result.setError(checkRes.getError());
                return result;
            }
            /**
             * 方式一，暂时不用
             * 保存衍生号，初始化差异表
             * moduleManager.createDeriveAndDiff(derive);
            **/
            //初始化衍生号信息
            moduleDao.create(derive);
        }catch (Exception e){
            log.error("create deriveModule ({}) failed, cause:{}", derivativeDto, getStackTraceAsString(e));
            result.setError("module.create.failed");
        }
        result.setResult(true);
        return result;
    }

}
