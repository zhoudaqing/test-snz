package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.DerivativeDiffDao;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.ModuleQuotaDao;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.ModuleFactoryDao;
import io.terminus.snz.requirement.dto.DerivativeDto;
import io.terminus.snz.requirement.manager.DerivativeDiffManager;
import io.terminus.snz.requirement.model.Module;
import io.terminus.snz.requirement.model.OldModule;
import io.terminus.snz.requirement.model.ModuleQuota;
import io.terminus.snz.requirement.model.ModuleFactory;
import io.terminus.snz.requirement.model.DerivativeDiff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:提供衍生品信息的相关操作
 * Created by jiaoyuan on 14-7-7.
 */
@Slf4j
@Service
public class DerivativeServiceImpl implements DerivativeService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;
    @Autowired
    private DerivativeDiffDao derivativeDiffDao;

    @Autowired
    private OldModuleDao oldModuleDao;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private ModuleQuotaDao moduleQuotaDao;

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;

    @Autowired
    private DerivativeDiffManager derivativeDiffManager;

    @Override
    public Response<Paging<DerivativeDto>> findByRequirementId(Long requirementId, Integer pageNo, Integer size) {
        Response<Paging<DerivativeDto>> result = new Response<Paging<DerivativeDto>>();

        if (requirementId == null) {
            log.error("find derivative info need requirement id");
            result.setError("derivative.requirementId.null");
            return result;
        }

        try {
            Paging<DerivativeDto> derivativeInfo = new Paging<DerivativeDto>();
            Map<String, Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            //获取衍生号数据
            params.put("type",Module.Type.DERIVE_TYPE.value());
            Paging<Module> moduleList = moduleDao.findByParams(requirementId, params);
            if (moduleList.getTotal() == 0) {
                derivativeInfo.setTotal(0l);
                derivativeInfo.setData(new ArrayList<DerivativeDto>());
                result.setResult(derivativeInfo);
                return result;
            }

            //封装老品数据信息
            List<Long> moduleIds = Lists.transform(moduleList.getData(), new Function<Module, Long>() {
                @Nullable
                @Override
                public Long apply(Module module) {
                    return module.getOldModuleId();
                }
            });

            List<OldModule> oldModuleList = oldModuleDao.findByIds(moduleIds);
            //封装老品对应的供应商数据信息
            Map<Long , OldModule> oldModuleMap = Maps.newHashMap();
            Map<Long, List<ModuleQuota>> quotaMap = Maps.newHashMap();
            for (OldModule oldModule : oldModuleList) {
                //获取供应商配额字段，按逗号分割存入数组
                String quotas[] = oldModule.getQuotaIds().split(",");
                //查询供应商信息
                quotaMap.put(oldModule.getId(), moduleQuotaDao.findByIds(quotas));
                //查询老品对应工厂信息
                List<ModuleFactory> factoryList= moduleFactoryDao.findByModuleId(oldModule.getModuleId());
                //组装工厂信息，以逗号分割
                String factoryName = null;
                for(ModuleFactory factory : factoryList){
                    factoryName = Strings.isNullOrEmpty(factoryName)?factory.getFactoryName():factoryName.concat(",").concat(factory.getFactoryName());
                }
                oldModule.setResourceNum(factoryName);
                oldModuleMap.put(oldModule.getId(), oldModule);
            }

            //数据组合，用于前台显示
            List<DerivativeDto> derivativeDtoList = Lists.newArrayList();
            DerivativeDto derivativeDto;
            for (Module module : moduleList.getData()) {
                derivativeDto = new DerivativeDto();
                derivativeDto.setModule(module);

                //封装老品数据，以及老品对应的供应商信息
                OldModule oldModule = oldModuleMap.get(module.getOldModuleId());
                if(oldModule != null){
                    derivativeDto.setOldModule(oldModule);
                    derivativeDto.setModuleQuotaList(quotaMap.get(oldModule.getId()));
                    //为了便于页面供应商分栏新增的栏数字段
                    derivativeDto.setListSize(derivativeDto.getModuleQuotaList().size()+1);
                }

                derivativeDtoList.add(derivativeDto);
            }

            derivativeInfo.setTotal(moduleList.getTotal());
            derivativeInfo.setData(derivativeDtoList);
            result.setResult(derivativeInfo);
        } catch (Exception e) {
            log.error("find derivative info by requirementId failed.");
            result.setError("derivative.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> create(DerivativeDiff derivativeDiff) {
        Response<Boolean> result = new Response<Boolean>();
        //需求编号
        if (derivativeDiff.getRequirementId() == null) {
            log.error("create derivativeDiff need requirement id");
            result.setError("derivativeDiff.requirementId.null");
            return result;
        }
        //模块编号
        if (derivativeDiff.getModuleId() == null) {
            log.error("create derivativeDiff need module id");
            result.setError("derivativeDiff.moduleId.null");
            return result;
        }
        try {

            result.setResult(derivativeDiffDao.create(derivativeDiff) != null);
        } catch (Exception e) {
            log.error("create derivativeDiff ({}) failed, cause:{}", derivativeDiff, Throwables.getStackTraceAsString(e));
            result.setError("derivativeDiff.create.failed");
        }
        return result;
    }

    @Override
    public Response<Boolean> update(DerivativeDiff derivativeDiff) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            result.setResult(derivativeDiffDao.update(derivativeDiff));
        } catch (Exception e) {
            log.error("update DerivativeDiff={} failed, error code={}", derivativeDiff, Throwables.getStackTraceAsString(e));
            result.setError("derivativeDiff.update.failed");
        }
        return result;

    }

    @Override
    public Response<Paging<DerivativeDiff>> findDiffByRequirementId(final Long requirementId, Integer pageNo, Integer size) {

        Response<Paging<DerivativeDiff>> result = new Response<Paging<DerivativeDiff>>();

        if (requirementId == null) {
            log.error("find DerivativeDiff need requirementId.");
            result.setError("derivativeDiff.requirementId.null");
            return result;
        }

        try {
            Paging<DerivativeDiff> diffPaging = new Paging<DerivativeDiff>();
            Map<String, Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size);
            params.putAll(pageInfo.toMap());
            //Paging<OldModule> oldModules = oldModuleDao.findByRequirementId(requirementId, params);
            Paging<Module> derive = moduleDao.findByParams(requirementId,params);
            if(derive.getTotal() == 0){
                diffPaging.setData(Lists.<DerivativeDiff>newArrayList());
                diffPaging.setTotal(0L);
                result.setResult(diffPaging);
                return result;
            }
            //如果oldModules当中含有但是derivativeDiff中没有说明是新建的对象，那么new一个空对象扔到页面
            List<DerivativeDiff> derivativeDiffs = derivativeDiffDao.findByRequirementId(requirementId);
            final Map<Long,DerivativeDiff> derivativeDiffMap = Maps.newHashMap();
            for(DerivativeDiff diff : derivativeDiffs){
                derivativeDiffMap.put(diff.getModuleId(),diff);
            }

            List<DerivativeDiff> results = Lists.transform(derive.getData(),new Function<Module, DerivativeDiff>() {
                @Nullable
                @Override
                public DerivativeDiff apply(Module derive) {
                    DerivativeDiff derivativeDiff = derivativeDiffMap.get(derive.getId());
                    if(derivativeDiff == null){
                        derivativeDiff = new DerivativeDiff();
                        derivativeDiff.setModuleId(derive.getId());
                        derivativeDiff.setModuleName(derive.getModuleName());
                        derivativeDiff.setRequirementId(derive.getRequirementId());
                    }
                    return derivativeDiff;
                }
            });
            diffPaging.setTotal(Long.valueOf(results.size()));
            diffPaging.setData(results);
            result.setResult(diffPaging);
        } catch (Exception e) {
            log.error("find derivativeDiff failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("derivativeDiff.find.failed");
        }

        return result;
    }

    /**
     * 通过moduleNum查询老品信息
     *
     * @param moduleNum 老品编号
     * @return 老品信息
     */
    @Override
    public Response<OldModule> findByModuleNum(String moduleNum) {
        Response<OldModule> result = new Response<OldModule>();
        if (moduleNum == null) {
            log.error("find OldModule need moduleNum");
            result.setError("oldModule.moduleNum.null");
            return result;
        }
        try {
            result.setResult(oldModuleDao.findByModuleNum(moduleNum));

        } catch (Exception e) {
            log.error("find oldModule failed,moduleNum={},error code ={}", moduleNum, Throwables.getStackTraceAsString(e));
            result.setError("oldModule.find.failed ");
        }

        return result;
    }

    /**
     * 批量处理衍生号场景信息
     * @param derivativeList 衍生号信息Json字符串
     * @return
     * 是否处理成功
     */
    @Override
    public Response<Boolean> batchDerivative(String derivativeList) {
        Response<Boolean> result = new Response<Boolean>();
        if(Strings.isNullOrEmpty(derivativeList)){
            log.error("batch Derivativediffs need derivativeList");
            result.setError("derivativeDiff.bash.null");
            return result;
        }
        try {
            List<DerivativeDiff> derivativeDiffs = JSON_MAPPER.fromJson(derivativeList, JSON_MAPPER.createCollectionType(List.class, DerivativeDiff.class));
            if(derivativeDiffs == null || derivativeDiffs.size() == 0){
                log.error("batch Derivativediffs need derivativeDiffsList size need gt 0");
                result.setError("derivativeDiff.bash.empty");
                return result;
            }
            result.setResult(derivativeDiffManager.batchDerivativeDiffs(derivativeDiffs));
        }catch (Exception e){
            log.error("batch insert failed,derivativeList={},error code={}",derivativeList,Throwables.getStackTraceAsString(e));
            result.setError("derivativeDiff.bash.failed");
        }
        return result;
    }

    /**
     * 根据需求id和衍生号id删除衍生号，同时清除差异分析表的数据
     * @param requirementId 需求id， moduleId 需求模块id
     * @return true or false
     */
    @Override
    public Response<Boolean> deleteDeriveModule(Long requirementId, Long moduleId) {
        Response<Boolean> result = new Response<Boolean>();
        //需求编号不能为空
        if(requirementId == null){
            log.error("delete dervieModule need requirementId");
            result.setError("requirement.id.null");
            return result;
        }
        //衍生号id不能为空
        if(moduleId == null){
            log.error("update module need id.");
            result.setError("module.id.null");
            return result;
        }
        try {
            //先删除衍生号差异分析表相关数据，再删除该衍生号数据
            result.setResult(derivativeDiffManager.deleteDeriveModule(requirementId, moduleId));
        }catch (Exception e){
            log.error("deleteDeriveModule failed,error code={}",Throwables.getStackTraceAsString(e));
            result.setError("module.delete.failed");
        }

        return result;
    }

}
