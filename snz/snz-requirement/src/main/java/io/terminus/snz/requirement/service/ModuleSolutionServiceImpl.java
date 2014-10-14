package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.ModuleQuotationDto;
import io.terminus.snz.requirement.dto.ModuleSolutionDto;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:整体需求的具体模块的方案信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */
@Slf4j
@Service
public class ModuleSolutionServiceImpl implements ModuleSolutionService {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    private static final int PAGE_SIZE = 10;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private ModuleSolutionDao moduleSolutionDao;

    @Autowired
    private ModuleQuotationDao moduleQuotationDao;

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private CompanyService companyService;

    @Override
    public Response<Boolean> createSolution(ModuleSolution moduleSolution) {
        Response<Boolean> result = new Response<Boolean>();

        Response<Boolean> existRes = existSolution(moduleSolution.getSolutionId(), moduleSolution.getModuleId());
        if(!existRes.isSuccess()){
            log.error("check module solution exist failed, error code={}", existRes.getResult());
            result.setError(existRes.getError());
            return result;
        }
        if(existRes.getResult()){
            //已填写了关于某个模块的方案信息，无法再创建一个新的数据
            log.error("module solution have existed, can't be create.");
            result.setError("solution.moduleId.checkError");
            return result;
        }

        //校验模块表单数据信息
        Response<Boolean> checkRes = checkSolutionForm(moduleSolution);
        if(!checkRes.isSuccess()){
            log.error("check module solution form failed, error code={}", checkRes.getResult());
            result.setError(checkRes.getError());
            return result;
        }

        try{
            result.setResult(moduleSolutionDao.create(moduleSolution) != null);
        }catch(Exception e){
            log.error("create module solution ({})failed, error code={}",moduleSolution,
                    Throwables.getStackTraceAsString(e));
            result.setError("solution.create.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> batchSolution(String solutionList) {
        Response<Boolean> result = new Response<Boolean>();
        List<ModuleSolution> solutions = JSON_MAPPER.fromJson(solutionList , JSON_MAPPER.createCollectionType(List.class , ModuleSolution.class));

        try{
            //更新模块方案数据信息
            if(solutions != null) {
                List<ModuleSolution> newSolutions = Lists.newArrayList();
                for (ModuleSolution solution : solutions) {
                    if (solution.getId() == null) {
                        newSolutions.add(solution);
                    } else {
                        moduleSolutionDao.update(solution);
                    }
                }

                if (!newSolutions.isEmpty()) {
                    //过滤已经创建过模块方案的数据
                    Iterables.removeIf(newSolutions , new Predicate<ModuleSolution>(){
                        @Override
                        public boolean apply(ModuleSolution moduleSolution) {
                            return existSolution(moduleSolution.getSolutionId() , moduleSolution.getModuleId()).getResult();
                        }
                    });
                    //批量创建模块方案
                    moduleSolutionDao.createBatch(newSolutions);
                }
            }
            result.setResult(true);
        }catch(Exception e){
            log.error("batch create solution failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("solution.batch.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> existSolution(Long solutionId, Long moduleId) {
        Response<Boolean> result = new Response<Boolean>();

        if(solutionId == null){
            log.error("check module solution exist need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        if(moduleId == null){
            log.error("check module solution exist need moduleId");
            result.setError("solution.moduleId.null");
            return result;
        }

        try{
            result.setResult(moduleSolutionDao.findExist(solutionId , moduleId) != null);
        }catch(Exception e){
            log.error("check module solution exist failed, solutionId={}, moduleId={}, error code={}",
                    solutionId, moduleId, Throwables.getStackTraceAsString(e));
            result.setError("solution.moduleId.checkError");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateSolution(ModuleSolution solution) {
        Response<Boolean> result = new Response<Boolean>();

        if(solution.getId() == null){
            log.error("update module solution need moduleSolutionId");
            result.setError("solution.module.Id.null");
            return result;
        }

        try{
            result.setResult(moduleSolutionDao.update(solution));
        }catch(Exception e){
            log.error("update module solution failed, id={}, error code={}", solution.getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> batchUpdateSolution(String moduleSolutions) {
        Response<Boolean> result = new Response<Boolean>();
        List<ModuleSolution> solutionList = JSON_MAPPER.fromJson(moduleSolutions , JSON_MAPPER.createCollectionType(List.class , ModuleSolution.class));

        try{
            //批量更新模块方案
            if(!solutionList.isEmpty()){
                for(ModuleSolution solution : solutionList){
                    moduleSolutionDao.update(solution);
                }
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("batch update module solution failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<ModuleSolution> findById(Long id) {
        Response<ModuleSolution> result = new Response<ModuleSolution>();

        if(id == null){
            log.error("find module solution need id");
            result.setError("solution.id.null");
            return result;
        }

        try{
            result.setResult(moduleSolutionDao.findById(id));
        }catch(Exception e){
            log.error("find module solution failed, id={}, error code={}", id, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleSolutionDto>> findModules(BaseUser user , Long requirementId, Integer status, Integer pageNo, Integer size) {
        Response<Paging<ModuleSolutionDto>> result = new Response<Paging<ModuleSolutionDto>>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        //需求编号
        if(requirementId == null){
            log.error("find modules need requirementId");
            result.setError("requirement.id.null");
            return result;
        }

        //查询状态（默认为显示全部的模块方案）
        Integer newStatus = status == null ? 1 : status;
        try{
            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());
            if(solution == null){
                log.error("requirement solution haven't created.");
                result.setError("solution.no.created");
                return result;
            }

            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? PAGE_SIZE : size);
            params.putAll(pageInfo.toMap());

            result.setResult(querySolutionPaging(requirementId , solution.getId(), newStatus, params));
        }catch(Exception e){
            log.error("find modules failed , requirementId={}, status={}, error code={}", requirementId, newStatus, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleSolution>> findSolutionByPurchaser(Long solutionId, Integer pageNo, Integer size) {
        Response<Paging<ModuleSolution>> result = new Response<Paging<ModuleSolution>>();

        if(solutionId == null){
            log.error("find module solution need solutionId.");
            result.setError("solution.id.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? PAGE_SIZE : size);
            params.putAll(pageInfo.toMap());
            Paging<ModuleSolution> paging = moduleSolutionDao.findSubmitted(solutionId, params);

            result.setResult(paging);
        }catch(Exception e){
            log.error("find module solution failed, solutionId={}, error code={}", solutionId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> createQuotation(ModuleQuotation moduleQuotation) {
        Response<Boolean> result = new Response<Boolean>();

        Response<Boolean> exist = existQuotation(moduleQuotation.getSolutionId(), moduleQuotation.getModuleId());
        if(!exist.isSuccess()){
            log.error("check module quotation exist failed, error code={}", exist.getResult());
            result.setError(exist.getError());
            return result;
        }
        if(exist.getResult()){
            //已填写了关于某个模块的报价方案信息，无法再创建一个新的数据
            log.error("module quotation have existed, can't be create.");
            result.setError("solution.moduleId.checkError");
            return result;
        }

        //校验模块报价表单数据信息
        Response<Boolean> checkRes = checkQuotationForm(moduleQuotation);
        if(!checkRes.isSuccess()){
            log.error("check module quotation form failed, error code={}", checkRes.getResult());
            result.setError(checkRes.getError());
            return result;
        }

        try{
            result.setResult(moduleQuotationDao.create(moduleQuotation) != null);
        }catch(Exception e){
            log.error("create module quotation ({})failed, error code={}", moduleQuotation, Throwables.getStackTraceAsString(e));
            result.setError("solution.create.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> batchQuotation(String quotationList , BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        List<ModuleQuotation> quotations = JSON_MAPPER.fromJson(quotationList , JSON_MAPPER.createCollectionType(List.class , ModuleQuotation.class));

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //获取用户对应的采购商信息
            Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
            if(!companyRes.isSuccess()){
                log.error("query company failed, error code={}", companyRes.getError());
                result.setError(companyRes.getError());
                return result;
            }

            if(quotations != null){
                List<ModuleQuotation> newQuotations = Lists.newArrayList();
                for(ModuleQuotation quotation : quotations){
                    if(quotation.getId() == null){
                        if(!existQuotation(quotation.getSolutionId(), quotation.getModuleId()).getResult()){
                            quotation.setSupplierId(companyRes.getResult().getId());
                            quotation.setSupplierName(companyRes.getResult().getCorporation());
                            newQuotations.add(quotation);
                        }
                    }else{
                        moduleQuotationDao.update(quotation);
                    }
                }

                if(!newQuotations.isEmpty()){
                    //批量创建模块报价方案
                    moduleQuotationDao.createBatch(newQuotations);
                }

            }

            result.setResult(true);
        }catch(Exception e){
            log.error("batch create module quotation failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quotation.batch.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> existQuotation(Long solutionId, Long moduleId) {
        Response<Boolean> result = new Response<Boolean>();

        if(solutionId == null){
            log.error("check module quotation exist need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        if(moduleId == null){
            log.error("check module quotation exist need moduleId");
            result.setError("solution.moduleId.null");
            return result;
        }

        try{
            result.setResult(moduleQuotationDao.findExist(solutionId , moduleId) != null);
        }catch(Exception e){
            log.error("check module quotation exist failed, solutionId={}, moduleId={}, error code={}",
                    solutionId, moduleId, Throwables.getStackTraceAsString(e));
            result.setError("quotation.moduleId.checkError");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateQuotation(ModuleQuotation moduleQuotation) {
        Response<Boolean> result = new Response<Boolean>();

        if(moduleQuotation.getId() == null){
            log.error("update module quotation need module quotationId");
            result.setError("quotation.id.null");
            return result;
        }

        try{
            result.setResult(moduleQuotationDao.update(moduleQuotation));
        }catch(Exception e){
            log.error("update module quotation failed, id={}, error code={}", moduleQuotation.getId(), Throwables.getStackTraceAsString(e));
            result.setError("quotation.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> batchUpdateQuotation(String moduleQuotations) {
        Response<Boolean> result = new Response<Boolean>();
        List<ModuleQuotation> moduleQuotationList = JSON_MAPPER.fromJson(moduleQuotations , JSON_MAPPER.createCollectionType(List.class , ModuleQuotation.class));

        try{
            //批量更新模块报价方案
            if(!moduleQuotationList.isEmpty()){
                for(ModuleQuotation quotation : moduleQuotationList){
                    moduleQuotationDao.update(quotation);
                }
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("batch update module quotation failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quotation.update.failed");
        }

        return result;
    }

    @Override
    public Response<ModuleQuotation> findByQuotationId(Long quotationId) {
        Response<ModuleQuotation> result = new Response<ModuleQuotation>();

        if(quotationId == null){
            log.error("find module quotation need id");
            result.setError("quotation.id.null");
            return result;
        }

        try{
            result.setResult(moduleQuotationDao.findById(quotationId));
        }catch(Exception e){
            log.error("find module quotation failed, id={}, error code={}", quotationId, Throwables.getStackTraceAsString(e));
            result.setError("quotation.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleQuotationDto>> findQuotations(BaseUser user , Long requirementId, Integer status, Integer pageNo, Integer size) {
        Response<Paging<ModuleQuotationDto>> result = new Response<Paging<ModuleQuotationDto>>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find modules need requirementId");
            result.setError("requirement.id.null");
            return result;
        }

        //查询状态（默认为显示全部的模块报价）
        Integer newStatus = status == null ? 1 : status;

        try{
            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());
            if(solution == null){
                log.error("requirement solution haven't created.");
                result.setError("solution.no.created");
                return result;
            }

            Requirement requirement = requirementDao.findById(requirementId);

            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? PAGE_SIZE : size);
            params.putAll(pageInfo.toMap());

            result.setResult(queryQuotationPaging(requirement , solution.getId() , newStatus, params));
        }catch(Exception e){
            log.error("find modules for module quotation failed , requirementId={}, status={}, error code={}", requirementId, newStatus,
                    Throwables.getStackTraceAsString(e));
            result.setError("quotation.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleQuotation>> findQuotationByPurchaser(Long solutionId, Integer pageNo, Integer size) {
        Response<Paging<ModuleQuotation>> result = new Response<Paging<ModuleQuotation>>();

        if(solutionId == null){
            log.error("find module solution need solutionId.");
            result.setError("solution.id.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? PAGE_SIZE : size);
            params.putAll(pageInfo.toMap());
            Paging<ModuleQuotation> paging = moduleQuotationDao.findSubmitted(solutionId, params);

            result.setResult(paging);
        }catch(Exception e){
            log.error("find module solution failed, solutionId={}, error code={}", solutionId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateQuotationFile(BaseUser user, Long requirementId, String quotationFile) {
        Response<Boolean> result = new Response<Boolean>();

        if(user == null){
            log.error("create quota user must be login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find module quotas need requirementId.");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            RequirementSolution requirementSolution = requirementSolutionDao.findByUserId(requirementId , user.getId());

            RequirementSolution newReqSolution = new RequirementSolution();
            newReqSolution.setId(requirementSolution.getId());
            //写入报价单详细文档
            newReqSolution.setQuotationFile(quotationFile);

            result.setResult(requirementSolutionDao.update(newReqSolution));
        }catch(Exception e){
            log.error("update quotation file failed, requirementId={}, quotationFile={}, error code={}", requirementId, quotationFile, Throwables.getStackTraceAsString(e));
            result.setError("solution.quotationFile.null");
        }

        return result;
    }

    /**
     * 根据需求编号以及模块方案信息对整体的数据进行分页处理
     * @param requirementId 需求编号
     * @param solutionId    方案编号
     * @param newStatus     查询状态
     * @param params        分页类型
     * @return Paging
     * 返回分页数据
     */
    private Paging<ModuleSolutionDto> querySolutionPaging(Long requirementId , Long solutionId , Integer newStatus, Map<String , Object> params){
        Paging<ModuleSolution> pagInfo;
        Long total;

        List<ModuleSolutionDto> solutionDtoList = Lists.newArrayList();

        //默认为显示全部的模块方案
        if(newStatus == 1){
            //查询已提交的模块方案信息
            pagInfo = moduleSolutionDao.findSubmitted(solutionId, params);

            //记录状态信息
            solutionDtoList.addAll(getCommitSolution(pagInfo.getData()));

            //未获取到完整的模块方案信息
            if(pagInfo.getData().size() == 0){
                //获取已提交的模块方案编号
                List<Long> moduleIds = Lists.transform(moduleSolutionDao.findAllSolutions(solutionId) , new Function<ModuleSolution, Long>() {
                    @Override
                    public Long apply(ModuleSolution input) {
                        return input.getModuleId();
                    }
                });

                //实际偏移量
                int offset = (Integer)params.get("offset");
                //获取相对与已填写数据的偏移量
                int moduleOffset = offset - moduleIds.size();

                //获取相对分页数据的未填写的信息
                params.put("offset" , moduleOffset);

                //查询采购商的标准模块数据
                solutionDtoList.addAll(getNoCommitSolution(requirementId , solutionId, moduleIds, params));
            }else if(pagInfo.getData().size() < PAGE_SIZE){
                //查询已提交的模块方案信息
                List<Long> moduleIds = Lists.transform(moduleSolutionDao.findAllSolutions(solutionId) , new Function<ModuleSolution, Long>() {
                    @Override
                    public Long apply(ModuleSolution input) {
                        return input.getModuleId();
                    }
                });

                //获取相对分页数据的未填写的信息
                params.put("offset" , 0);
                params.put("limit" , PAGE_SIZE - pagInfo.getData().size());

                //查询采购商的标准模块数据
                solutionDtoList.addAll(getNoCommitSolution(requirementId , solutionId, moduleIds, params));
            }

            total = (long)moduleDao.countById(requirementId);
        }else if (newStatus == 2) {
            //查询已提交的模块方案信息
            List<Long> moduleIds = Lists.transform(moduleSolutionDao.findAllSolutions(solutionId) , new Function<ModuleSolution, Long>() {
                @Override
                public Long apply(ModuleSolution input) {
                    return input.getModuleId();
                }
            });

            //查询采购商的标准数据（过滤已提交的信息）
            List<ModuleSolutionDto> solutionDtoLists = getNoCommitSolution(requirementId , solutionId, moduleIds, params);
            solutionDtoList.addAll(solutionDtoLists);
            total = (long)solutionDtoLists.size();
        } else {
            //查询已提交的模块方案信息
            pagInfo = moduleSolutionDao.findSubmitted(solutionId, params);

            solutionDtoList.addAll(getCommitSolution(pagInfo.getData()));
            total = pagInfo.getTotal();
        }

        Paging<ModuleSolutionDto> paging = new Paging<ModuleSolutionDto>();
        paging.setData(solutionDtoList);
        paging.setTotal(total);

        return paging;
    }

    /**
     * 根据需求编号以及模块方案信息对整体的模块报价数据进行分页处理
     * @param requirement   需求信息
     * @param solutionId    方案编号
     * @param newStatus     查询状态
     * @param params        分页类型
     * @return Paging
     * 返回分页数据
     */
    private Paging<ModuleQuotationDto> queryQuotationPaging(Requirement requirement , Long solutionId , Integer newStatus, Map<String , Object> params){
        Paging<ModuleQuotation> pagInfo;
        Long total;

        List<ModuleQuotationDto> quotations = Lists.newArrayList();

        //默认为显示全部的模块报价
        if(newStatus == 1){
            //查询已提交的模块报价信息
            pagInfo = moduleQuotationDao.findSubmitted(solutionId, params);

            //记录状态信息
            for(ModuleQuotation quotation : pagInfo.getData()){
                quotation.setSolutionId(solutionId);
                quotations.add(new ModuleQuotationDto(quotation , CoinType.from(requirement.getCoinType()).toString(), true));
            }

            //未获取到完整的模块报价信息
            if(pagInfo.getData().size() == 0){
                //查询已提交的全部模块报价信息
                List<Long> moduleIds = Lists.transform(moduleQuotationDao.findAllQuotations(solutionId) , new Function<ModuleQuotation, Long>() {
                    @Override
                    public Long apply(ModuleQuotation input) {
                        return input.getModuleId();
                    }
                });

                //实际偏移量
                int offset = (Integer)params.get("offset");
                //获取相对与已填写数据的偏移量
                int moduleOffset = offset - moduleIds.size();

                //获取相对分页数据的未填写的信息
                params.put("offset" , moduleOffset);

                //查询采购商的标准模块数据
                Paging<Module> modules = moduleDao.findFilterModule(requirement.getId() , moduleIds, params);
                for (Module module : modules.getData()) {
                    quotations.add(new ModuleQuotationDto(module , solutionId, CoinType.from(requirement.getCoinType()).toString(), false));
                }

            }else if(pagInfo.getData().size() < PAGE_SIZE){
                //查询已提交的模块方案信息
                List<Long> moduleIds = Lists.transform(moduleQuotationDao.findAllQuotations(solutionId) , new Function<ModuleQuotation, Long>() {
                    @Override
                    public Long apply(ModuleQuotation input) {
                        return input.getModuleId();
                    }
                });

                //获取相对分页数据的未填写的信息
                params.put("offset" , 0);
                params.put("limit" , PAGE_SIZE - pagInfo.getData().size());

                //查询采购商的标准模块数据
                Paging<Module> modules = moduleDao.findFilterModule(requirement.getId() , moduleIds, params);
                for (Module module : modules.getData()) {
                    quotations.add(new ModuleQuotationDto(module , solutionId, CoinType.from(requirement.getCoinType()).toString(), false));
                }
            }

            total = (long)moduleDao.countById(requirement.getId());
        }else if (newStatus == 2) {
            //查询已提交的模块方案信息
            List<Long> moduleIds = Lists.transform(moduleQuotationDao.findAllQuotations(solutionId) , new Function<ModuleQuotation, Long>() {
                @Override
                public Long apply(ModuleQuotation input) {
                    return input.getModuleId();
                }
            });

            //查询采购商的标准数据（过滤已提交的信息）
            Paging<Module> modules = moduleDao.findFilterModule(requirement.getId() , moduleIds, params);
            for (Module module : modules.getData()) {
                quotations.add(new ModuleQuotationDto(module , solutionId, CoinType.from(requirement.getCoinType()).toString(), false));
            }
            total = modules.getTotal();
        } else {
            //查询已提交的模块报价信息
            pagInfo = moduleQuotationDao.findSubmitted(solutionId, params);

            for(ModuleQuotation quotation : pagInfo.getData()){
                quotation.setSolutionId(solutionId);
                quotations.add(new ModuleQuotationDto(quotation , CoinType.from(requirement.getCoinType()).toString(), true));
            }
            total = pagInfo.getTotal();
        }

        Paging<ModuleQuotationDto> paging = new Paging<ModuleQuotationDto>();
        paging.setData(quotations);
        paging.setTotal(total);

        return paging;
    }

    /**
     * 封装已提交的模块的tqrd原始的数据
     * @param moduleSolutions 已提交的tqrd数据
     * @return  List
     * 返回一提交的模块TQRD
     */
    private List<ModuleSolutionDto> getCommitSolution(List<ModuleSolution> moduleSolutions){
        List<ModuleSolutionDto> solutionDtoList = Lists.newArrayList();

        ModuleSolutionDto solutionDto;
        Module oldModule;
        for (ModuleSolution moduleSolution : moduleSolutions) {
            solutionDto = new ModuleSolutionDto(moduleSolution, 1);//以提交的模块方案
            oldModule = moduleDao.findById(moduleSolution.getModuleId());

            //转载老数据
            solutionDto.setOldQuality(oldModule.getQuality());
            solutionDto.setOldReaction(oldModule.getSupplyAt());
            solutionDto.setOldDelivery(oldModule.getDelivery());
            solutionDtoList.add(solutionDto);
        }

        return solutionDtoList;
    }

    /**
     * 封装还未提交的模块的tqrd原始的数据
     * @param requirementId 需求编号
     * @param solutionId    方案编号
     * @param moduleIds     过滤模块编号
     * @param params        查询参数
     * @return  List
     * 返回一提交的模块TQRD
     */
    private List<ModuleSolutionDto> getNoCommitSolution(Long requirementId , Long solutionId, List<Long> moduleIds, Map<String , Object> params){
        Paging<Module> modules = moduleDao.findFilterModule(requirementId , moduleIds, params);

        List<ModuleSolutionDto> solutionDtoList = Lists.newArrayList();
        ModuleSolutionDto moduleSolutionDto;
        for (Module module : modules.getData()) {
            moduleSolutionDto = new ModuleSolutionDto(module , solutionId, 2);
            moduleSolutionDto.setOldQuality(module.getQuality());
            moduleSolutionDto.setOldReaction(module.getSupplyAt());
            moduleSolutionDto.setOldDelivery(module.getDelivery());
            solutionDtoList.add(moduleSolutionDto);
        }

        return solutionDtoList;
    }

    /**
     * 校验模块需求方案的字段信息
     * @param solution  模块方案信息
     * @return  Boolean
     * 返回校验结果
     */
    private Response<Boolean> checkSolutionForm(ModuleSolution solution){
        Response<Boolean> result = new Response<Boolean>();

        //技术指标
        if(solution.getTechnology() == null){
            log.error("create module solution need technology");
            result.setError("solution.technology.null");
            return result;
        }

        //质量指标
        if(solution.getQuality() == null){
            log.error("create module solution need quality");
            result.setError("solution.quality.null");
            return result;
        }

        //互动指标
        if(solution.getReaction() == null){
            log.error("create module solution need reaction");
            result.setError("solution.reaction.null");
            return result;
        }

        //产能指标
        if(solution.getDelivery() == null){
            log.error("create module solution need delivery");
            result.setError("solution.delivery.null");
            return result;
        }

        //成本指标
        if(solution.getCost() == null){
            log.error("create module solution need cost");
            result.setError("solution.cost.null");
            return result;
        }

        result.setResult(true);
        return result;
    }

    /**
     * 校验模块报价方案的字段信息
     * @param quotation  模块报价方案信息
     * @return  Boolean
     * 返回校验结果
     */
    private Response<Boolean> checkQuotationForm(ModuleQuotation quotation){
        Response<Boolean> result = new Response<Boolean>();

        //整体方案编号
        if(quotation.getSolutionId() == null){
            log.error("create module quotation need solutionId");
            result.setError("quotation.solutionId.null");
            return result;
        }

        //模块编号
        if(quotation.getModuleId() == null){
            log.error("create module quotation need moduleId");
            result.setError("quotation.moduleId.null");
            return result;
        }

        //计算单价
        if(quotation.getPrice() == null){
            log.error("create module quotation need price");
            result.setError("quotation.price.null");
            return result;
        }

        //货币类型
        if(Strings.isNullOrEmpty(quotation.getCoinType())){
            log.error("create module quotation need coin type");
            result.setError("quotation.coinType.null");
            return result;
        }

        //汇率
        if(quotation.getExchangeRate() == null){
            log.error("create module quotation need exchange rate");
            result.setError("quotation.exchangeRate.null");
            return result;
        }

        result.setResult(true);
        return result;
    }
}
