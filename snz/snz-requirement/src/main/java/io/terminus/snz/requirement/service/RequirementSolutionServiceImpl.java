package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.BackendJSON;
import io.terminus.snz.requirement.dto.CheckSolEndDto;
import io.terminus.snz.requirement.dto.SupplierSolutionDto;
import io.terminus.snz.requirement.manager.SolutionManager;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.statistic.service.SolutionCountService;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.model.SupplierModuleDetail;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import io.terminus.snz.user.model.SupplierTQRDCInfo;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Desc:整体需求方案处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-05.
 */
@Slf4j
@Service
public class RequirementSolutionServiceImpl implements RequirementSolutionService {
    private static final int TECHNOLOGY = 0;               //需求的技术默认数值

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private SolutionManager solutionManager;

    @Autowired
    private ModuleSolutionDao moduleSolutionDao;

    @Autowired
    private ModuleQuotationDao moduleQuotationDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RequirementCountService requirementCountService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SolutionCountService solutionCountService;

    @Autowired
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    @Autowired
    private SupplierModuleDetailService supplierModuleDetailService;

    //将整体需求方案进行cache一把
    private final LoadingCache<Long , RequirementSolution> solutionCache = CacheBuilder.newBuilder().expireAfterAccess(3 , TimeUnit.MINUTES).build(
            new CacheLoader<Long , RequirementSolution>() {
                @Override
                public RequirementSolution load(Long solutionId) throws Exception {
                    //查询需求详细信息
                    return requirementSolutionDao.findById(solutionId);
                }
            }
    );

    @Override
    public Response<Boolean> createSolution(RequirementSolution solution , BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        //获取用户对应的供应商信息
        Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
        if(!companyRes.isSuccess()){
            log.error("query company failed, error code={}", companyRes.getError());
            result.setError(companyRes.getError());
            return result;
        }

        //判断供应商是否已经对于需求进行了承诺
        Response<RequirementSolution> existRes = existSolution(solution.getRequirementId() , companyRes.getResult().getId());
        if(!existRes.isSuccess()){
            log.error("check solution existed failed, error code={}", existRes.getError());
            result.setError(existRes.getError());
            return result;
        }
        if(existRes.getResult() != null){
            log.error("supplier have send solution,can't send again.");
            result.setError("solution.existed.error");
            return result;
        }

        //需要保证该方案处于3:方案交互 or 4:方案综投（采购商才能够创建方案）
        RequirementStatus[] statuses = {RequirementStatus.SOL_INTERACTIVE , RequirementStatus.SOL_END};
        Response<Boolean> statusRes = checkRequirementStatus(solution.getRequirementId() , statuses);
        if(!statusRes.isSuccess()){
            log.error("check requirement status failed, error code={}", statusRes.getError());
            result.setError(statusRes.getError());
            return result;
        }
        //无法创建方案
        if(!statusRes.getResult()){
            log.error("requirement status can't allow suppler send solution.");
            result.setError("solution.status.stop");
            return result;
        }

        try{
            //获取需求方案信息
            Requirement requirement = requirementDao.findById(solution.getRequirementId());

            solution.setRequirementName(requirement.getName());
            solution.setSupplierId(companyRes.getResult().getId());
            solution.setSupplierName(companyRes.getResult().getCorporation());
            solution.setUserId(user.getId());
            //默认的T评分
            solution.setTechnology(TECHNOLOGY);

            //写入方案承诺后需求方案的状态
            if(solution.getNotAccept() == null){
                //全部承诺
                solution.setStatus(RequirementSolution.Status.ALL_ACCEPT.value());
            }else{
                solution.setStatus(RequirementSolution.Status.LITTLE_ACCEPT.value());
            }

            result.setResult(requirementSolutionDao.create(solution) != null);
        }catch(Exception e){
            log.error("create requirement solution failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("solution.create.failed");
        }

        return result;
    }

    @Override
    public Response<RequirementSolution> existSolution(Long requirementId, Long supplierId) {
        Response<RequirementSolution> result = new Response<RequirementSolution>();

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        if(supplierId == null){
            log.error("find requirement solution need supplierId");
            result.setError("solution.supplierId.null");
            return  result;
        }

        try{
            result.setResult(requirementSolutionDao.findByRequirementId(requirementId, supplierId));
        }catch(Exception e){
            log.error("find requirement solution failed , requirementId={}, supplierId={}, error code={}",
                    requirementId, supplierId, Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateSolution(RequirementSolution solution) {
        Response<Boolean> result = new Response<Boolean>();

        if(solution.getId() == null){
            log.error("update requirement solution need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        //需要保证该方案处于3:方案交互 or 4:方案综投.需求才能被更改
        RequirementStatus[] statuses = {RequirementStatus.SOL_INTERACTIVE , RequirementStatus.SOL_END};
        Response<Boolean> statusRes = checkRequirementStatus(solution.getRequirementId(), statuses);
        if(!statusRes.isSuccess()){
            log.error("check requirement status failed, error code={}", statusRes.getError());
            result.setError(statusRes.getError());
            return result;
        }
        if(!statusRes.getResult()){
            log.error("requirement status can't allow suppler update solution.");
            result.setError("solution.status.notUpdate");
            return result;
        }

        try{
            result.setResult(requirementSolutionDao.update(solution));
        }catch(Exception e){
            log.error("update requirement solution failed, solutionId={}, error code={}", solution.getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> signSecrecy(Long requirementId, Integer signType, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //是否是供应商
            if(Objects.equal(user.getType() , User.Type.SUPPLIER.value())){
                //验证供应商信息是否完整（不完整无法显示需求详情）
                Response<Boolean> checkRes = companyService.isComplete(user.getId());
                if(!checkRes.isSuccess()){
                    log.error("check user complete info failed, error code={}", checkRes.getError());
                    result.setError(checkRes.getError());
                    return result;
                }

                if(!checkRes.getResult()){
                    log.error("company info is not complete.");
                    result.setError("requirement.company.noComplete");
                    return result;
                }
            }else{
                //采购商直接跳转
                result.setResult(true);
                return result;
            }

            //查询供应商保密协议是否已签订
            if(signType == null){
                //查询登录的供应商是否已经淘汰
                Response<User> supplier = accountService.findUserById(user.getId());
                if(Objects.equal(supplier.getResult().getStep() , User.Step.DIE_OUT.value())){
                    log.error("supplier is die_out.");
                    result.setError("supplier.is.dieout");
                    return result;
                }
                //查询登录供应商是否绩效的质量得分在60下
                Response<SupplierTQRDCInfo> suTQRDinfo = companyService.findSupplierLastTQRDCInfoByUserId(user.getId());
                if(suTQRDinfo.getResult().getQualityScore()!=null) {
                    if (suTQRDinfo.getResult().getQualityScore() < 60) {
                        log.error("supplier quality score less.");
                        result.setError("supplier.quality.less");
                        return result;
                    }
                }
                //在淘汰供应商物料明细表中的也不让查看
                Response<Company> comtemp = companyService.findCompanyByUserId(user.getId());
                if(!comtemp.isSuccess()){
                    log.error("query company failed, error code={}", comtemp.getError());
                    result.setError(comtemp.getError());
                    return result;
                }
                if(comtemp.getResult().getSupplierCode()!=null||comtemp.getResult().getSupplierCode()!=""){
                    List<SupplierModuleDetail> templ = supplierModuleDetailService.findBySupplierCode(comtemp.getResult().getSupplierCode()).getResult();
                    if(templ.size()>0){
                        log.error("supplier module Detail have.");
                        result.setError("supplier.module.detail.have.some");
                        return result;
                    }
                }

                RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());
                result.setResult(solution != null);
            }else{
                RequirementSolution solution = new RequirementSolution();

                //获取用户对应的供应商信息
                Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
                if(!companyRes.isSuccess()){
                    log.error("query company failed, error code={}", companyRes.getError());
                    result.setError(companyRes.getError());
                    return result;
                }

                //判断供应商是否已经签订过协议
                Response<RequirementSolution> existRes = existSolution(requirementId , companyRes.getResult().getId());
                if(!existRes.isSuccess()){
                    log.error("check solution existed failed, error code={}", existRes.getError());
                    result.setError(existRes.getError());
                    return result;
                }
                if(existRes.getResult() != null){
                    log.error("supplier have send solution,can't send again.");
                    result.setError("solution.sign.failed");
                    return result;
                }

                //获取需求信息
                Requirement requirement = requirementDao.findById(requirementId);
                solution.setRequirementId(requirementId);
                solution.setRequirementName(requirement.getName());
                solution.setSupplierId(companyRes.getResult().getId());
                solution.setSupplierName(companyRes.getResult().getCorporation());
                solution.setUserId(user.getId());
                //默认的T评分
                solution.setTechnology(TECHNOLOGY);

                //签订保证协议
                solution.setStatus(RequirementSolution.Status.SIGN_CONF.value());

                if(requirementSolutionDao.create(solution) != null){
                    //增加供应商的方案统计数据
                    SupplierSolutionCount supplierSolutionCount = new SupplierSolutionCount();
                    supplierSolutionCount.setUserId(user.getId());
                    supplierSolutionCount.setUserName(user.getName());
                    supplierSolutionCount.setStatusCounts(ImmutableMap.of(requirement.getStatus(), 1));

                    solutionCountService.setSupCount(supplierSolutionCount);

                    //供应商交互方案统计
                    solutionCountService.setSolCountInfo(user.getId() , SolutionCountType.MUTUAL_SOL, 1);

                    result.setResult(true);
                }else{
                    result.setError("solution.sign.failed");
                }
            }
        }catch(Exception e){
            log.error("find user sign requirement solution failed, requirementId={}, userId={}, error code={}",
                    requirementId, user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> topicWithSupplier(Long solutionId, Long topicId, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(solutionId == null){
            log.error("update requirement solution need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            RequirementSolution requirementSolution = requirementSolutionDao.findById(solutionId);

            //需要保证该方案处于3:方案交互 or 4:方案综投.需求才能被更改
            RequirementStatus[] statuses = {RequirementStatus.SOL_INTERACTIVE , RequirementStatus.SOL_END};
            Response<Boolean> statusRes = checkRequirementStatus(requirementSolution.getRequirementId(), statuses);
            if(!statusRes.isSuccess()){
                log.error("check requirement status failed, error code={}", statusRes.getError());
                result.setError(statusRes.getError());
                return result;
            }
            if(!statusRes.getResult()){
                log.error("requirement status can't allow suppler update solution.");
                result.setError("solution.status.notUpdate");
                return result;
            }

            RequirementSolution solution = new RequirementSolution();
            solution.setId(solutionId);
            solution.setTopicId(topicId);
            result.setResult(requirementSolutionDao.update(solution));
        }catch(Exception e){
            log.error("update requirement solution failed, solutionId={}, error code={}", solutionId, Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<SupplierSolutionDto>> findSignByParam(final Long requirementId, Integer pageNo, Integer size) {
        Response<Paging<SupplierSolutionDto>> result = new Response<Paging<SupplierSolutionDto>>();

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            params.put("requirementId" , requirementId);

            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            Requirement requirement = requirementDao.findById(requirementId);

            //获取需求下的供应商提交的方案
            Paging<RequirementSolution> solutionPaging = requirementSolutionDao.findSolutionsByParams(params);

            //获取后台三级类目信息
            List<BackendJSON> categoryList = JSON_MAPPER.fromJson(requirement.getSeriesIds() , JSON_MAPPER.createCollectionType(List.class , BackendJSON.class));
            List<Long> categoryIds = Lists.newArrayList();
            for(BackendJSON backendJSON : categoryList){
                categoryIds.add(backendJSON.getBcId());
            }

            List<SupplierSolutionDto> supplierSolutionDtoList = Lists.newArrayList();
            for(RequirementSolution solution : solutionPaging.getData()){
                supplierSolutionDtoList.add(querySupplierDto(requirementId , solution, categoryIds));
            }

            result.setResult(new Paging<SupplierSolutionDto>(solutionPaging.getTotal() , supplierSolutionDtoList));
        }catch(Exception e){
            log.error("find requirement solution have sign secrecy failed , requirementId={} error code={}", requirementId , Throwables.getStackTraceAsString(e));
            result.setError("solution.supplier.findFailed");
        }

        return result;
    }

    @Override
    public Response<SupplierSolutionDto> findSolutionSupplier(Long requirementId, BaseUser user) {
        Response<SupplierSolutionDto> result = new Response<SupplierSolutionDto>();

        if(requirementId == null){
            log.error("find supplier info need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("find supplier info, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            if(Objects.equal(User.Type.from(user.getType()), User.Type.SUPPLIER)){
                RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());

                Requirement requirement = requirementDao.findById(requirementId);

                //获取后台三级类目信息
                List<BackendJSON> categoryList = JSON_MAPPER.fromJson(requirement.getSeriesIds() , JSON_MAPPER.createCollectionType(List.class , BackendJSON.class));
                List<Long> categoryIds = Lists.newArrayList();
                for(BackendJSON backendJSON : categoryList){
                    categoryIds.add(backendJSON.getBcId());
                }

                result.setResult(querySupplierDto(requirementId , solution, categoryIds));
            }else {
                result.setResult(null);
            }
        }catch (Exception e){
            log.error("find supplier detail info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("query.supplier.fail");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateSolutionAccept(Long requirementId, String acceptInfo, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("update requirement solution accept need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        //需要保证该方案处于3:方案交互 or 4:方案综投.需求才能被更改
        RequirementStatus[] statuses = {RequirementStatus.SOL_INTERACTIVE , RequirementStatus.SOL_END};
        Response<Boolean> statusRes = checkRequirementStatus(requirementId , statuses);
        if(!statusRes.isSuccess()){
            log.error("check requirement status failed, error code={}", statusRes.getError());
            result.setError(statusRes.getError());
            return result;
        }
        if(!statusRes.getResult()){
            log.error("requirement status can't allow suppler update solution.");
            result.setError("solution.status.notUpdate");
            return result;
        }

        try{
            //获取供应商提交的需求信息
            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());
            RequirementSolution newSolution = new RequirementSolution();
            newSolution.setId(solution.getId());
            newSolution.setNotAccept(acceptInfo);
            //写入更改方案承诺后需求方案的状态
            if(acceptInfo == null){
                //全部承诺
                newSolution.setStatus(RequirementSolution.Status.ALL_ACCEPT.value());

                //记录提交方案的供应商数量
                requirementCountService.setReqCountInfo(requirementId , RequirementCountType.SEND_SO, 1);

                //记录供应商承诺目标的数量
                solutionCountService.setSolCountInfo(user.getId(), SolutionCountType.ACCEPT_SOL, 1);
            }else{
                if(acceptInfo.length() > 2048){
                    log.error("accept info is to long");
                    result.setError("solution.accept.toLong");
                    return result;
                }
                newSolution.setStatus(RequirementSolution.Status.LITTLE_ACCEPT.value());
            }

            //更新需求的承诺信息
            result.setResult(requirementSolutionDao.update(newSolution));
        }catch(Exception e){
            log.error("update requirement solution accept failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> deleteSolution(Long solutionId) {
        Response<Boolean> result = new Response<Boolean>();

        if(solutionId == null){
            log.error("delete requirement solution need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        try{
            RequirementSolution solution = solutionCache.get(solutionId);

            //除5:选定供应商与方案，6:招标结束状态位其余都能删除
            RequirementStatus[] statuses = {RequirementStatus.SUP_SOL , RequirementStatus.TENDER_END};
            Response<Boolean> statusRes = checkRequirementStatus(solution.getRequirementId() , statuses);
            if(!statusRes.isSuccess()){
                log.error("check requirement status failed, error code={}", statusRes.getError());
                result.setError(statusRes.getError());
                return result;
            }
            if(statusRes.getResult()){
                log.error("requirement status can't allow suppler delete solution.");
                result.setError("solution.status.notUpdate");
                return result;
            }

            solutionManager.deleteSolution(solutionId);
            result.setResult(true);
        }catch(Exception e){
            log.error("delete requirement solution failed , solutionId={}, error code={}", solutionId, Throwables.getStackTraceAsString(e));
            result.setError("solution.delete.failed");
        }

        return result;
    }

    @Override
    public Response<RequirementSolution> findById(Long solutionId) {
        Response<RequirementSolution> result = new Response<RequirementSolution>();

        if(solutionId == null){
            log.error("update requirement solution need solutionId");
            result.setError("solution.id.null");
            return result;
        }

        try{
            result.setResult(solutionCache.get(solutionId));
        }catch(Exception e){
            log.error("requirement solution find failed, solutionId={}, error code={}", solutionId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<RequirementSolution> findSolutionBySupplier(Long requirementId, BaseUser user) {
        Response<RequirementSolution> result = new Response<RequirementSolution>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            //获取供应商提交的需求方案
            result.setResult(requirementSolutionDao.findByUserId(requirementId, user.getId()));
        }catch(Exception e){
            log.error("requirement solution find failed, requirementId={}, userId={}, error code={}", requirementId, user.getId(), Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    public Response<String> checkAcceptInfo(Long requirementId , BaseUser user) {
        Response<String> result = new Response<String>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            //获取供应商提交的需求方案
            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());

            if(solution == null){
                log.debug("requirement solution don't exist.");
                //还未创建需求方案
                result.setResult("0");
                return result;
            }

            result.setResult(solution.getNotAccept());
        }catch(Exception e){
            log.error("requirement solution find failed, requirementId={}, userId={}, error code={}", requirementId, user.getId(), Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    public Response<Boolean> checkSupplierInfo(Long requirementId, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        Requirement requirement = requirementDao.findById(requirementId);

        //写入供应商信用等级信息
        Response<SupplierCreditQualify> creditRes = supplierCreditQualifyService.findCreditQualifyByUserId(user.getId());

        if(!creditRes.isSuccess()){
            log.error("find supplier credit qualify info failed, error code={}", creditRes.getError());
            result.setError(creditRes.getError());
            return result;
        }

        if(!creditRes.getResult().isCreditQualified()){
            log.error("supplier credit is not allow to do.");
            result.setError("supplier.credit.failed");
            return result;
        }

        //获取公司信息
        Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());

        if(!companyRes.isSuccess()){
            result.setError(companyRes.getError());
            return result;
        }

        //获取后台三级类目信息
        List<BackendJSON> categoryList = JSON_MAPPER.fromJson(requirement.getSeriesIds() , JSON_MAPPER.createCollectionType(List.class , BackendJSON.class));
        List<Long> categoryIds = Lists.newArrayList();
        for(BackendJSON backendJSON : categoryList){
            categoryIds.add(backendJSON.getBcId());
        }

        //获取供应商资质验证信息
        Response<Integer> qualifyRes = supplierResourceMaterialService.getInfoInBcIds(companyRes.getResult().getId() , categoryIds);
        if(!qualifyRes.isSuccess()){
            log.error("find user qualify failed, userId={}, error code={}", user.getId(), qualifyRes.getError());
            result.setError(qualifyRes.getError());
            return result;
        }

        //资质是否全部验证通过
        if(!Objects.equal(SupplierResourceMaterialInfo.Status.from(qualifyRes.getResult()) , SupplierResourceMaterialInfo.Status.QUALIFIED)){
            log.error("supplier resource is not allow to do.");
            result.setError("supplier.resource.failed");
            return result;
        }

        result.setResult(true);

        return result;
    }

    @Override
    public Response<CheckSolEndDto> solutionEnd(Long requirementId , BaseUser user) {
        Response<CheckSolEndDto> result = new Response<CheckSolEndDto>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("update requirement solution need requirementId");
            result.setError("requirementId.id.null");
            return result;
        }

        try{
            //获取用户的需求方案信息
            RequirementSolution requirementSolution = requirementSolutionDao.findByUserId(requirementId, user.getId());

            //获取需求信息
            Requirement requirement = requirementDao.findById(requirementSolution.getRequirementId());

            Response<Boolean> checkSupplier = checkSupplierInfo(requirementId , user);
            if(!checkSupplier.isSuccess()){
                log.error("check supplier info for requirement failed, error code={}", checkSupplier.getError());
                result.setError(checkSupplier.getError());
                return result;
            }

            Response<Boolean> checkRes = checkReqStatusWithSol(requirement , requirementSolution);
            if(!checkRes.isSuccess()){
                log.error("check requirement & solution info failed, error code={}", checkRes.getError());
                result.setError(checkRes.getError());
                return result;
            }else {
                CheckSolEndDto checkSolEndDto = new CheckSolEndDto();

                //获取全部报价信息
                List<ModuleQuotation> quotations = moduleQuotationDao.findAllQuotations(requirementSolution.getId());

                //获取全部的TQRDC信息
                List<ModuleSolution> solutions = moduleSolutionDao.findAllSolutions(requirementSolution.getId());
                Map<Long , ModuleSolution> solutionMap = Maps.newHashMap();

                for(ModuleSolution solution : solutions){
                    solutionMap.put(solution.getModuleId() , solution);
                }

                //计算整个需求的Q&R数据用于显示
                Integer qualityValue = 0;
                Date maxReaction = null;
                Date cpmDate;

                List<Module> moduleList = moduleDao.findModules(requirementId);

                //计算总的模块报价总权值
                int error = 0;
                Integer totalValue = 0;
                for (ModuleQuotation quotation : quotations) {
                    for(Module module : moduleList){
                        //校验报价不能低于模块的价格(统计数量)
                        if(Objects.equal(quotation.getModuleId() , quotation.getModuleId()) && quotation.getPrice() > module.getCost()){
                            log.error("module quotation cost price can't more than module default price");
                            error++;
                        }
                    }

                    //计算总报价
                    totalValue += quotation.getTotal() * quotation.getPrice();

                    //计算总Q
                    qualityValue += quotation.getTotal() * solutionMap.get(quotation.getModuleId()).getQuality();

                    //计算最大R
                    cpmDate = solutionMap.get(quotation.getModuleId()).getReaction();
                    maxReaction = maxReaction == null ? cpmDate : (maxReaction.after(cpmDate) ? maxReaction : cpmDate);
                }

                if(error == 0){
                    //方案终投阶段只允许提交一次
                    RequirementSolution solution = new RequirementSolution();
                    solution.setId(requirementSolution.getId());
                    solution.setQuality(qualityValue);  //整体的Q
                    solution.setReaction(maxReaction);  //整体的R
                    solution.setCost(totalValue);       //整体的C
                    solution.setStatus(RequirementSolution.Status.SEND_END.value());
                    requirementSolutionDao.update(solution);
                }

                //判断采购商是否已经提交了保证金
                Response<Integer> paidRes = depositService.checkPaid(requirement.getId() , requirementSolution.getSupplierId());
                if(!paidRes.isSuccess()){
                    log.error("check supplier send deposit fee result failed, error code={}", result.getError());
                    result.setError(paidRes.getError());
                    return result;
                }

                checkSolEndDto.setPaidResult(paidRes.getResult());
                checkSolEndDto.setCostResult(error == 0);

                result.setResult(checkSolEndDto);
            }
        }catch(Exception e){
            log.error("change requirement solution status failed, requirementId={}, userId={}, error code={}", requirementId, user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.update.failed");
        }

        return result;
    }

    @Override
    public Response<List<RequirementSolution>> findAllSolution(Long requirementId , Integer status) {
        Response<List<RequirementSolution>> result = new Response<List<RequirementSolution>>();

        if(requirementId == null){
            log.error("find requirement solution need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        if(RequirementStatus.from(status) == null){
            log.error("can't find the status type.");
            result.setError("requirement.status.null");
            return result;
        }

        //当查询阶段为空时默认方案交互阶段
        Objects.firstNonNull(status , 3);

        try{
            List<RequirementSolution> solutionList;
            if(Objects.equal(RequirementStatus.SOL_END , RequirementStatus.from(status))){
                //当处于方案终投阶段（只能查询到最终确认最终方案的供应商的方案）
                solutionList = requirementSolutionDao.findSolutionEnds(requirementId , null, 5, RequirementSolution.Status.SEND_END.value());
            }else{
                solutionList = requirementSolutionDao.findAllSolution(requirementId);
            }
            result.setResult(solutionList);
        }catch(Exception e){
            log.error("find requirement solution failed, requirementId={}, error code={}" , requirementId, Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateBatchTechnology(String solutions) {
        Response<Boolean> result = new Response<Boolean>();
        List<RequirementSolution> solutionList = JSON_MAPPER.fromJson(solutions , JSON_MAPPER.createCollectionType(List.class , RequirementSolution.class));

        if(solutionList == null || solutionList.isEmpty()){
            log.error("requirement solutions is empty.");
            result.setError("requirement.solution.empty");
            return result;
        }

        try{
            RequirementSolution newSolution;
            for(RequirementSolution solution : solutionList){
                if(solution.getTechnology() == null){
                    log.error("create module solution need technology");
                    result.setError("solution.technology.null");
                    return result;
                }

                if(solution.getTechnology() < 0 || solution.getTechnology() > 10000){
                    log.error("requirement solution technology score is in 0~100.");
                    result.setError("solution.technology.scope");
                    return result;
                }

                newSolution = new RequirementSolution();
                newSolution.setId(solution.getId());
                newSolution.setTechnology(solution.getTechnology());
                //采购商对方案的技术进行评分
                requirementSolutionDao.update(newSolution);
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement solution technology is failed, error code={}.", Throwables.getStackTraceAsString(e));
            result.setError("solution.technology.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateSolutionFile(Long requirementId , String solutionFile, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("update requirement solution need requirementId");
            result.setError("requirement.id.null");
            return result;
        }

        if(Strings.isNullOrEmpty(solutionFile)){
            log.error("update requirement solution file can't be null.");
            result.setError("solution.file.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());


            //处理历史文档
            requirementSolutionDao.updateSolutionFile(solution.getId(), solutionFile);
            //记录提交方案的供应商数量
            requirementCountService.setReqCountInfo(solution.getRequirementId() , RequirementCountType.SEND_SO, 1);
            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement solution failed, requirementId={}, solutionFile={}, error code={}.", requirementId, solutionFile, Throwables.getStackTraceAsString(e));

        }

        return result;
    }

    @Override
    public Response<Boolean> existSolutions(BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        //验证用户是否已登入
        if(user == null){
            log.error("find requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //获取供应商参与需求的所有方案的统计
            Map<SolutionCountType , Integer> countMap = solutionCountService.findSupSolCount(user.getId() , new SolutionCountType[]{SolutionCountType.MUTUAL_SOL});

            result.setResult(countMap.get(SolutionCountType.MUTUAL_SOL) > 0);
        }catch(Exception e){
            log.error("find supplier requirement solution failed, userId={}, error code={}", user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.check.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<Requirement>> findByParams(BaseUser user, Integer qualifyStatus, Integer status, String reqName,
                                                      String startAt, String endAt, Integer pageNo , Integer size) {
        Response<Paging<Requirement>> result = new Response<Paging<Requirement>>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        //获取用户对应的供应商信息
        Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
        if(!companyRes.isSuccess()){
            log.error("query company failed, error code={}", companyRes.getError());
            result.setError(companyRes.getError());
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            params.put("name", Strings.isNullOrEmpty(reqName) ? null : reqName);
            params.put("qualifyStatus", qualifyStatus);
            params.put("status", status);
            params.put("startAt", startAt);
            params.put("endAt", endAt);

            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            result.setResult(requirementDao.findBySupplier(companyRes.getResult().getId(), params));
        }catch(Exception e){
            log.error("find requirement solutions failed, supplierId={}, error code={}", companyRes.getResult().getId(), Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<RequirementSolution>> findByRequirementId(Long requirementId, String statusArray, Integer pageNo, Integer size) {
        Response<Paging<RequirementSolution>> result = new Response<Paging<RequirementSolution>>();

        if(requirementId == null){
            log.error("find requirement solutions need requirementId");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());
            params.put("statusArray", statusArray == null ? null : Splitters.COMMA.splitToList(statusArray));
            result.setResult(requirementSolutionDao.findByParams(requirementId, params));
        }catch(Exception e){
            log.error("find requirement solutions failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    /**
     * 获取供应商的详细信息（保证金等信息）
     * @param requirementId 需求编号
     * @param solution      需求方案信息
     * @param categoryIds   需求类目信息
     * @return SupplierSolutionDto
     * 供应商详细信息
     */
    private SupplierSolutionDto querySupplierDto(Long requirementId , RequirementSolution solution , List<Long> categoryIds){
        SupplierSolutionDto supplierSolutionDto = new SupplierSolutionDto();

        supplierSolutionDto.setSupplierId(solution.getSupplierId());
        supplierSolutionDto.setSupplierName(solution.getSupplierName());
        supplierSolutionDto.setUserId(solution.getUserId());
        //是否已提交方案
        supplierSolutionDto.setDealTime(solution.getSolutionFile() == null ? null : solution.getUpdatedAt());

        //写入供应商信用等级信息
        Response<SupplierCreditQualify> creditRes = supplierCreditQualifyService.findCreditQualifyByUserId(solution.getUserId());
        supplierSolutionDto.setCreditStatus(creditRes.isSuccess() ? creditRes.getResult().getStatus() : null);

        //写入供应商资质验证信息
        Response<Integer> qualifyRes = supplierResourceMaterialService.getInfoInBcIds(solution.getSupplierId() , categoryIds);
        supplierSolutionDto.setQualifyStatus(qualifyRes.isSuccess() ? qualifyRes.getResult() : null);

        //写入供应商针对需求是否提交保证金
        Response<Integer> depositRes = depositService.checkPaid(requirementId , solution.getSupplierId());
        supplierSolutionDto.setPaidStatus(depositRes.isSuccess() ? depositRes.getResult() : Deposit.Status.INIT.value());

        //标注供应商是否可以进入配额流程
        supplierSolutionDto.setSolutionStatus(Objects.equal(RequirementSolution.Status.from(solution.getStatus()) , RequirementSolution.Status.SEND_END));

        Response<User> userRes = accountService.findUserById(solution.getUserId());
        supplierSolutionDto.setSupplierTags(userRes.isSuccess() ? userRes.getResult().buildTags() : null);

        //当阶段到达，已上传文件阶段则默认为全部承诺
        Integer interStatus = solution.getStatus() == 0 ? 1 : (solution.getStatus() >= 4 ? 2 : solution.getStatus());
        supplierSolutionDto.setInteractiveStatus(interStatus);

        return supplierSolutionDto;
    }

    /**
     * 根据需求信息查询需求对应的阶段下的方案是否符合最终的提交要求
     * @param requirement           需求信息
     * @param requirementSolution   用户的方案信息
     * @return  Boolean
     * 返回检验信息
     */
    private Response<Boolean> checkReqStatusWithSol(Requirement requirement , RequirementSolution requirementSolution){
        Response<Boolean> result = new Response<Boolean>();

        //是否是方案终投阶段
        if(Objects.equal(RequirementStatus.SOL_END , RequirementStatus.from(requirement.getStatus()))){
            //技术领先、差异化必须要提交方案（solutionFile:上传的详细的方案文档）其它场景不需要
            if(Objects.equal(Tactics.from(requirement.getTacticsId()) , Tactics.TECHNOLOGY_NEW) || Objects.equal(Tactics.from(requirement.getTacticsId()) , Tactics.DIFFERENTIATION)){
                if(requirementSolution.getSolutionFile() == null || requirementSolution.getSolutionFile().isEmpty()){
                    log.error("jump to requirement solution end need send solution file.");
                    result.setError("solution.file.null");
                    return result;
                }
            }

            //todo 添加判断用户是否已经提交报价单文档

            //模块方案信息
            List<ModuleSolution> solutions = moduleSolutionDao.findAllSolutions(requirementSolution.getId());

            //模块报价信息
            List<ModuleQuotation> quotations = moduleQuotationDao.findAllQuotations(requirementSolution.getId());

            //获取需求详细的模块数量
            Integer actualModuleNum = requirement.getModuleNum();

            //判断模块的TQRD信息是否填写完整
            if (!Objects.equal(actualModuleNum, solutions.size())) {
                log.error("send solution to end status, the module solution info must be enter.");
                result.setError("moduleSol.info.null");
                return result;
            }

            //判断是否填写完整的模块报价信息
            if (!Objects.equal(actualModuleNum, quotations.size())) {
                log.error("send solution to end status, the module quotation info must be enter.");
                result.setError("moduleQuo.info.null");
                return result;
            }

            //验证TQRD数据是否已全部填写
            for(ModuleSolution solution : solutions){
                if(solution.getTechnology() == null || solution.getQuality() == null || solution.getReaction() == null ||
                        solution.getDelivery() == null){
                    log.error("send solution to end status, the module solution info must be enter.");
                    result.setError("moduleSol.info.null");
                    return result;
                }
            }

            //验证报价数据是否填写完整
            for(ModuleQuotation quotation : quotations){
                if(quotation.getSolutionId() == null || quotation.getModuleId() == null || quotation.getPrice() == null ||
                        Strings.isNullOrEmpty(quotation.getCoinType()) || quotation.getExchangeRate() == null){
                    log.error("send solution to end status, the module quotation info must be enter.");
                    result.setError("moduleQuo.info.null");
                    return result;
                }
            }

            result.setResult(true);
        }else{
            log.error("requirement status don't allow requirement solution end.");
            result.setError("solution.status.not.end");
            return result;
        }

        return result;
    }

    /**
     * 通过需求编号&需求状态数组判断当前的需求的状态是否符合
     * @param requirementId 需求编号
     * @param statuses      需求状态编号
     * @return Boolean
     * 返回需求状态是否符合条件
     */
    private Response<Boolean> checkRequirementStatus(Long requirementId , RequirementStatus[] statuses){
        Response<Boolean> result = new Response<Boolean>();

        try{
            Requirement requirement = requirementDao.findById(requirementId);

            //验证状态是否符合
            for(RequirementStatus status : statuses){
                if(Objects.equal(status , RequirementStatus.from(requirement.getStatus()))){
                    result.setResult(true);
                    return result;
                }
            }
        }catch(Exception e){
            log.error("check requirement status failed, requirementId={}, statuses={}, error code={}",
                    requirementId, statuses, Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        result.setResult(false);
        return result;
    }
}
