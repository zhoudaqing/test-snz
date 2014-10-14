package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.service.CategoryBindingService;
import io.terminus.snz.requirement.dao.ModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.dao.RequirementQuestionnaireDao;
import io.terminus.snz.requirement.dao.RequirementTeamDao;
import io.terminus.snz.requirement.dao.RequirementTimeDao;
import io.terminus.snz.requirement.dto.RequirementDetailDto;
import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.dto.TopicUser;
import io.terminus.snz.requirement.event.RequirementEventBus;
import io.terminus.snz.requirement.manager.RequirementManager;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import io.terminus.snz.user.dto.PurchaserDto;
import io.terminus.snz.user.dto.TeamMemeberDto;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.PurchaserExtra;
import io.terminus.snz.user.model.SupplierAppointed;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.SupplierAppointedService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Desc:需求详细处理逻辑
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-04.
 */
@Slf4j
@Service
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private RequirementTeamDao requirementTeamDao;

    @Autowired
    private RequirementTimeDao requirementTimeDao;

    @Autowired
    private RequirementManager requirementManager;

    @Autowired
    private CategoryBindingService categoryBindingService;

    @Autowired
    private RequirementIndexService requirementIndexService;

    @Autowired
    private RequirementCountService requirementCountService;

    @Autowired
    private AccountService<User> accountService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private RequirementEventBus eventBus;

    @Autowired
    private RequirementQuotaService requirementQuotaService;

    @Autowired
    private SupplierAppointedService supplierAppointedService;  //创建甲指供应商逻辑
    
    @Autowired
    private RequirementQuestionnaireDao requirementQuestionnaireDao;

    //将需求进行cache一把
    private final LoadingCache<Long , Requirement> requirementCache = CacheBuilder.newBuilder().expireAfterAccess(3 , TimeUnit.MINUTES).build(
            new CacheLoader<Long , Requirement>() {
                @Override
                public Requirement load(Long requirementId) throws Exception {
                    //查询需求详细信息
                    return requirementDao.findById(requirementId);
                }
            }
    );

    @Override
    public Response<Long> create(RequirementDto requirementDto , BaseUser user) {
        //这一部分对于采购商的权限验证因为采购商入驻一期没有做先不做验证
        Response<Long> result = new Response<Long>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement user can't be null.");
            result.setError("requirement.creatorId.null");
            return result;
        }

        //验证用户的权限
        if(Objects.equal(User.Type.from(user.getType()) , User.Type.SUPPLIER)){
            log.error("user no power to create requirement.");
            result.setError("requirement.creator.power");
            return result;
        }

        //需求中的团队信息的设置
        Response<Boolean> teamRes = checkTeams(requirementDto.getTeamList());
        if(!teamRes.isSuccess()){
            log.error("check teams form failed, error code={}", teamRes.getError());
            result.setError(teamRes.getError());
            return result;
        }

        //需求中的阶段时间设置
        Response<Boolean> timeRes = checkTimes(requirementDto.getTimeList() , requirementDto.getRequirement().getModuleType());
        if(!timeRes.isSuccess()){
            log.error("check times form failed, error code={}", timeRes.getError());
            result.setError(timeRes.getError());
            return result;
        }

        Requirement requirement = requirementDto.getRequirement();
        requirement.setCreatorId(user.getId());
        requirement.setCreatorName(user.getName());
        requirement.setCreatorPhone(user.getMobile());
        //现在采购商默认是只有海尔
        requirement.setPurchaserId(0l);
        requirement.setPurchaserName("海尔");
        requirement.setCheckResult(Requirement.CheckResult.WAIT_SUBMIT.value());
        requirement.setModuleNum(0);
        requirement.setModuleTotal(0);

        //需求表单数据验证
        Response<Boolean> requirementRes = checkRequirement(requirement);
        if(!requirementRes.isSuccess()){
            log.error("check requirement form failed, error code={}", requirementRes.getError());
            result.setError(requirementRes.getError());
            return result;
        }

        try{
            //创建需求详细信息（含事务处理）
            Long requirementId = requirementManager.createRequirement(requirementDto);

            //创建甲指需求时，将甲指供应商信息写入甲指库
            if(requirementDto.getRequirement().getModuleType().intValue() == Module.Type.JIA_ZHI.value().intValue()){
                SupplierAppointed supplierAppointed = requirementDto.getSupplierAppointed();
                supplierAppointed.setRequirementId(requirementId);
                supplierAppointed.setSeriesIds(requirement.getSeriesIds());
                supplierAppointed.setCreatorId(requirement.getCreatorId());
                supplierAppointed.setRequirementName(requirement.getName());
                supplierAppointed.setStatus(SupplierAppointed.Status.SUBMITTED.value());
                supplierAppointedService.batchSupplierAppointed(supplierAppointed);
            }

            result.setResult(requirementId);
        }catch(Exception e){
            log.error("create requirement failed, error code.", Throwables.getStackTraceAsString(e));
            result.setError("requirement.create.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> existName(Long purchaserId, String name) {
        Response<Boolean> result = new Response<Boolean>();

        if(purchaserId == null){
            log.error("check requirement need purchaserId.");
            result.setError("requirement.purchaserId.null");

            return result;
        }

        if(Strings.isNullOrEmpty(name)){
            log.error("check requirement need requirementName");
            result.setError("requirement.name.null");

            return result;
        }

        try{
            result.setResult(requirementDao.findByName(purchaserId , name) != null);
        }catch(Exception e){
            log.error("check requirement name exist failed, purchaserId={}, name={}, error code={}"
                    , purchaserId, name, Throwables.getStackTraceAsString(e));
            result.setError("requirement.checkName.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> update(RequirementDto requirementDto) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementDto.getRequirement().getId() == null){
            log.error("update requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            if(requirementDto.getRequirement().getStatus() != null){
                if(requirementDto.getRequirement().getStatus() > 2){
                    //当方案已锁定以后将无法更改需求内容(或是被删除状态都不能更改状态)
                    log.error("requirement have be locked or delete, can't to be update. requirementId={}", requirementDto.getRequirement().getId());
                    result.setError("requirement.lock.existed");
                    return result;
                }

                if(requirementDto.getRequirement().getStatus() == -1){
                    log.error("requirement have be delete, can't to be update. requirementId={}", requirementDto.getRequirement().getId());
                    result.setError("requirement.update.delete");
                    return result;
                }
            }

            requirementManager.updateRequirement(requirementDto);

            //更新甲指需求时，更新甲指库中甲指供应商
            if(requirementDto.getRequirement().getModuleType().intValue() == Module.Type.JIA_ZHI.value().intValue()){
                SupplierAppointed supplierAppointed = requirementDto.getSupplierAppointed();
                Requirement requirement = requirementDto.getRequirement();
                supplierAppointed.setSeriesIds(requirement.getSeriesIds());
                supplierAppointed.setCreatorId(requirement.getCreatorId());
                supplierAppointed.setRequirementName(requirement.getName());
                supplierAppointed.setRequirementId(requirement.getId());
                supplierAppointed.setCreatorId(requirementDao.findById(requirement.getId()).getCreatorId());
                supplierAppointed.setStatus(SupplierAppointed.Status.SUBMITTED.value());
                supplierAppointedService.batchSupplierAppointed(supplierAppointed);
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.update.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateAccessories(Long requirementId, String accessories) {
        Response<Boolean> result = new Response<Boolean>();
        if(requirementId == null){
            log.error("update requirement accessories need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }
        try{
            Requirement requirement = requirementDao.findById(requirementId);
            if(requirement == null){
                log.error("update requirement accessories need requirement exit.requirementId {}",requirementId);
                result.setError("requirement.find.failed");
                return result;
            }
            requirement.setAccessories(accessories);
            requirementDao.update(requirement);
            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.update.failed");
        }
        return result;
    }

    @Override
    public Response<List<RequirementTeam>> findRequirementTeam(Long requirementId) {
        Response<List<RequirementTeam>> result = new Response<List<RequirementTeam>>();

        if(requirementId == null){
            log.error("find requirement team need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            result.setResult(requirementTeamDao.findByRequirementId(requirementId));
        }catch(Exception e){
            log.error("find requirement team failed, requirementId={}, error code={}");
            result.setError("team.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> deleteTeam(Long teamId) {
        Response<Boolean> result = new Response<Boolean>();

        if(teamId == null){
            log.error("delete team user need teamId.");
            result.setError("team.id.null");
            return result;
        }

        try{
            result.setResult(requirementTeamDao.delete(teamId));
        }catch(Exception e){
            log.error("delete requirement failed, teamId={} error code={}", teamId, Throwables.getStackTraceAsString(e));
            result.setError("team.delete.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> delete(Long requirementId) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("find requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Requirement requirement = requirementCache.get(requirementId);
            requirement.setStatus(-1);  //设置需求删除标识

            result.setResult(requirementDao.update(requirement));
            //刷新缓冲数据
            requirementCache.refresh(requirementId);

            //准实时dump
            Response<Boolean> indexR = requirementIndexService.realTimeIndex(requirementId, Requirement.SearchStatus.DELETE);
            if(!indexR.isSuccess()) {
                log.error("fail to realTime index requirement id={}, error code:{}",
                        requirementId, indexR.getError());
            }
        }catch(Exception e){
            log.error("delete requirement failed, requirementId={} error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("requirement.delete.failed");
        }

        return result;
    }

    @Override
    public Response<RequirementDto> findById(Long requirementId) {
        Response<RequirementDto> result = new Response<RequirementDto>();

        if(requirementId == null){
            //为了解决需求创建页面调用这个接口没有requirementId问题
            result.setResult(new RequirementDto());
            return result;
        }

        try{
            RequirementDto requirementDto = requirementManager.findByRequirementId(requirementId);
            Requirement requirement = requirementDto.getRequirement();
            //如果为甲指需求查询甲指供应商信息
            if(requirement.getModuleType().intValue() == Module.Type.JIA_ZHI.value().intValue()){
                Response<SupplierAppointed> supplierAppointedResponse = supplierAppointedService.findbyId(requirement.getId());
                if(supplierAppointedResponse.isSuccess()){
                    SupplierAppointed supplierAppointed = supplierAppointedResponse.getResult();
                    requirementDto.setSupplierAppointed(supplierAppointed);
                }
            }
            result.setResult(requirementDto);
        }catch(Exception e){
            log.error("find requirement failed, requirementId={}, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<RequirementDetailDto> findDetailById(Long requirementId , BaseUser user) {
        Response<RequirementDetailDto> result = new Response<RequirementDetailDto>();

        if(requirementId == null){
            log.error("find detail requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        if(user == null){
            log.error("find detail requirement user must login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //是否是采购商
            if(Objects.equal(user.getType() , User.Type.SUPPLIER.value())){
                Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
                if(!companyRes.isSuccess()){
                    log.error("find company info by user id failed, error code={}", companyRes.getError());
                    result.setError(companyRes.getError());
                    return result;
                }

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

                RequirementDetailDto requirementDetailDto = requirementManager.findDetailRequirement(requirementId , companyRes.getResult().getId());

                RequirementQuestionnaire requirementQuestionnaire = requirementQuestionnaireDao.findByUserIdAndRequirementId(user.getId(),requirementId);
                requirementDetailDto.setIsAcceptQuestionnaire(requirementQuestionnaire != null);

                result.setResult(requirementDetailDto);
            }else{
                result.setResult(requirementManager.findDetailRequirement(requirementId , null));
            }
        }catch (NumberFormatException numException){
            log.error("analyse requirement park and factory info failed, requirementId={}, error code={}", Throwables.getStackTraceAsString(numException));
            result.setError("requirement.find.failed");
        }catch(Exception e){
            log.error("find detail requirement failed, requirementId={}, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<Requirement>> findByPurchaser(BaseUser user, Integer queryType, Integer status, String reqName,
                                                         String startAt, String endAt, Integer pageNo , Integer size) {
        Response<Paging<Requirement>> result = new Response<Paging<Requirement>>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement user can't be null.");
            result.setError("requirement.creatorId.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            params.put("startAt" , startAt);
            params.put("endAt" , endAt);
            params.put("createTime" , "desc");
            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            //需求的状态(>=-1显示需求状态，<-1显示审核的状态信息)前台：-5:未提交审核,-4:待审核,-3:审核通过,-2:审核不通过,
            //0:未提交审核,1:待审核,2:审核通过,-1:审核不通过
            if(status == null || status >= -1){
                params.put("status" , status);
            }else{
                //切换到审核状态查询
                params.put("checkResult" , status == -2 ? status+1 : status+5);
            }

            //默认查询自己创建的需求
            Paging<Requirement> paging;

            //admin查询全部的数据信息
            if(Objects.equal(User.Type.from(user.getType()) , User.Type.ADMIN)){
                params.put("name", reqName);

                paging = requirementDao.findByParams(0l, params);
                setReqCount(paging);
                result.setResult(paging);
            }else {
                switch (RequirementService.QueryType.from(Objects.firstNonNull(queryType, QueryType.MY_CREATE.value()))) {
                    case MY_CREATE:
                        //获取用户创建的需求
                        params.put("creatorId", user.getId());
                        params.put("name", reqName);

                        //todo 现在先不设置采购商的逻辑判断默认都是海尔（海尔编号：0）
                        paging = requirementDao.findByParams(0l, params);
                        //写入统计数据
                        setReqCount(paging);
                        result.setResult(paging);
                        break;

                    case MY_JOIN:
                        //获取参与的团队的需求信息
                        params.put("userId", user.getId());
                        params.put("name", reqName);

                        paging = requirementDao.findReqByTeam(0l, params);
                        setReqCount(paging);
                        result.setResult(paging);
                        break;

                    case MY_AUDIT:
                        //获取作为审核人员的需求信息
                        params.put("checkId", user.getId());
                        params.put("name", reqName);

                        paging = requirementDao.findByParams(0l, params);
                        setReqCount(paging);
                        result.setResult(paging);
                        break;
                }
            }
        }catch(Exception e){
            log.error("find requirements failed, userId={}, error code={}", user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<Requirement>> findByParams(Long frontId, Integer status, Integer pageNo, Integer size) {
        Response<Paging<Requirement>> result = new Response<Paging<Requirement>>();

        try{
            //获取后台的类目，用于查询
            Map<String , Object> params = Maps.newHashMap();
            if(frontId != null){
                Response<List<CategoryPair>> pairsR = categoryBindingService.findByFcid(frontId);
                if(!pairsR.isSuccess()) {
                    log.error("fail to find categoryBinding by fcid={}, error code:{}",
                            frontId, pairsR.getError());
                    result.setError(pairsR.getError());
                    return result;
                }
                List<CategoryPair> pairs = pairsR.getResult();
                if(pairs != null && !pairs.isEmpty()){
                    params.put("pairs" , pairs);
                }
            }

            params.put("status" , status);

            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());

            result.setResult(requirementDao.findByParams(null, params));
        }catch(Exception e){
            log.error("find requirements failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.find.failed");
        }

        return result;
    }

    @Override
    public Response<String> askAudit(Long requirementId , BaseUser user) {
        Response<String> result = new Response<String>();

        if(requirementId == null){
            log.error("find requirement need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        //验证用户是否已登入
        if(user == null){
            log.error("ask audit requirement user can't be null.");
            result.setError("requirement.creatorId.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);

            if(!Objects.equal(requirement.getCreatorId() , user.getId())){
                log.error("the operator is not the requirement creator.");
                result.setError("requirement.creatorId.noPower");
                return result;
            }

            //实现当审核失败后还可以再提交审核
            if(Objects.equal(Requirement.CheckResult.WAIT_SUBMIT , Requirement.CheckResult.from(requirement.getCheckResult()))){
                Requirement newReq = new Requirement();
                newReq.setId(requirementId);
                newReq.setName(requirement.getName());
                newReq.setCheckResult(Requirement.CheckResult.WAIT.value());

                //获取领导信息
                Response<User> queryRes = queryLeader(user.getId());
                if(!queryRes.isSuccess()){
                    log.error("find user leader failed, error code={}", queryRes.getError());
                    result.setError(queryRes.getError());
                    return result;
                }

                newReq.setCheckId(queryRes.getResult().getId());
                newReq.setCheckName(queryRes.getResult().getName());
                requirementDao.update(newReq);
                result.setResult(queryRes.getResult().getName());

                //向上级领导发送消息
                eventBus.post(new AuditMessage(newReq, user.getId(), Lists.newArrayList(user.getId())));
            }else if(Objects.equal(Requirement.CheckResult.FAILED , Requirement.CheckResult.from(requirement.getCheckResult()))){
                //再次提交审核（当审核失败后）
                Requirement newReq = new Requirement();
                newReq.setId(requirementId);
                newReq.setName(requirement.getName());
                newReq.setCheckResult(Requirement.CheckResult.WAIT.value());
                requirementDao.update(newReq);
                result.setResult(requirement.getName());

                //向上级领导发送消息
                eventBus.post(new AuditMessage(newReq, user.getId(), Lists.newArrayList(user.getId())));
            }else{
                log.error("requirement status can't to submit request audit.");
                result.setError("requirement.status.error");
                return result;
            }
        }catch(Exception e){
            log.error("ask audit failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
        }

        return result;
    }

    @Override
    public Response<Boolean> auditRequirement(Long requirementId, Integer auditRes, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("audit requirement need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        if(user == null){
            log.error("audit requirement need auditorId.");
            result.setError("requirement.auditor.null");
            return  result;
        }

        if(Requirement.CheckResult.from(auditRes) == null){
            log.error("audit requirement need audit result.");
            result.setError("requirement.auditRes.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);

            if(!Objects.equal(requirement.getCheckId() , user.getId())){
                //判断用户是否是需求的审核人员
                log.error("requirement audit need creator's leader. userId={}", user.getId());
                result.setError("requirement.auditor.noPower");
                return result;
            }

            if(!Objects.equal(Requirement.CheckResult.from(requirement.getCheckResult()), Requirement.CheckResult.WAIT)){
                //已经被审核过无法再被审核
                log.error("requirement audit existed, can't to be audit again. requirementId={}", requirementId);
                result.setError("requirement.audit.existed");
                return result;
            }

            Requirement newReq = new Requirement();
            newReq.setId(requirementId);
            newReq.setName(requirement.getName());
            newReq.setCheckResult(auditRes);
            newReq.setCheckTime(DateTime.now().toDate());
            //审核通过就将状态转换到－》等待需求发布阶段
            newReq.setStatus(Objects.equal(Requirement.CheckResult.from(auditRes) , Requirement.CheckResult.SUCCESS) ? 0 : null);
            requirementDao.update(newReq);
            result.setResult(true);

            //向需求创建者发送需求审核信息
            eventBus.post(new AuditMessage(newReq , user.getId(), Lists.newArrayList(requirement.getCreatorId())));

            //审核通过 向团队成员推送请求消息
            if(Objects.equal(Requirement.CheckResult.from(auditRes) , Requirement.CheckResult.SUCCESS)){
                //获取团队成员信息
                List<RequirementTeam> teamList = requirementTeamDao.findByRequirementId(requirementId);
                //用于区分不同的阶段
                requirement.setCheckResult(Requirement.CheckResult.WAIT_SUBMIT.value());
                eventBus.post(new AuditMessage(requirement , user.getId(), Lists.transform(teamList , new Function<RequirementTeam, Long>() {
                    @Override
                    public Long apply(RequirementTeam input) {
                        return input.getUserId();
                    }
                })));
            }
        }catch(Exception e){
            log.error("audit requirement failed, requirementId={} auditorId={} error code={}",
                    requirementId, user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("requirement.audit.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> transitionStatus(Long requirementId, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        if(requirementId == null){
            log.error("transition requirement status need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        if(user == null){
            log.error("transition requirement status need userId.");
            result.setError("team.userId.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);

            Response<Boolean> checkRes = transitionStatusCheck(requirement , user);

            if(!checkRes.isSuccess()){
                log.error("check requirement transition status failed, error code={}", checkRes.getError());
                result.setError(checkRes.getError());
                return result;
            }

            //切换时间状态信息
            requirementManager.transitionTime(requirement, user.getId(), user.getName(), false);

            //准实时dump,只有在状态从0-1才需要dump
            if (Objects.equal(requirement.getStatus(), RequirementStatus.WAIT_SEND.value())) {
                Response<Boolean> indexR = requirementIndexService.realTimeIndex(requirementId, Requirement.SearchStatus.INDEX);
                if (!indexR.isSuccess()) {
                    log.error("fail to realTime index requirement id={}, error code:{}",
                            requirementId, indexR.getError());
                }
            }
            result.setResult(true);
        }catch(Exception e){
            log.error("transition requirement status failed, requirementId={}, userId={}, error code={}",
                    requirementId, user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("requirement.transition.failed");
        }

        return result;
    }

    @Override
    public Response<List<TopicUser>> findTopicPeople(Long requirementId) {
        Response<List<TopicUser>> result = new Response<List<TopicUser>>();

        if(requirementId == null){
            log.error("find requirement teams need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            //获取可以参与交互的用户数据信息
            result.setResult(requirementManager.findTopicUsers(requirementId));
        }catch(Exception e){
            log.error("find requirement teams failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("team.find.failed");
        }

        return result;
    }

    @Override
    public Response<Long> findRequirementCount() {
        Response<Long> result = new Response<Long>();

        try{
            result.setResult(requirementDao.findRequirementCount());
        }catch(Exception e){
            log.error("find all requirement count info failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("requirement.count.failed");
        }

        return result;
    }

    @Override
    public void transitionExpire() {
        try{
            //批量切换需求状态
            requirementManager.transitionJob();
        }catch(Exception e){
            log.error("transition batch requirement time status failed, error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void warningExpire(int warningDay) {
        try{
            //批量的对所有卓越运营的需求进行预警测试
            requirementManager.warningJob(warningDay);
        }catch(Exception e){
            log.error("query requirement warning info failed, error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void dumpCountModuleToRedis() {
        try{
            //同步统计数据到redis(针对还未进入需求锁定的所有需求)
            requirementManager.dumpCountModule();
        }catch(Exception e){
            log.error("count requirement modules failed , error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public Response<Long> findModuleTypeByRequirementId(Long requirementId) {
        Response<Long> result = new Response<Long>();

        if(requirementId == null){
            log.error("find moduleType need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);
            if(requirement == null){
                log.error("find requirement moduleType need requirement exit.requirementId {}",requirementId);
                result.setError("requirement.find.failed");
                return result;
            }
            //获取可以参与交互的用户数据信息
            result.setResult(requirement.getMaterielType());
        }catch(Exception e){
            log.error("find requirement moduleType failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("moduleType.find.failed");
        }
        return result;
    }

    /**
     * 需求表单检验
     * @param requirement   需求表单信息
     * @return  返回需求信息检验是否正常
     */
    private Response<Boolean> checkRequirement(Requirement requirement){
        Response<Boolean> result = new Response<Boolean>();

        //名称判断
        Response<Boolean> existRes = existName(requirement.getPurchaserId() , requirement.getName());
        if(!existRes.isSuccess()){
            log.error("check requirement name if exist failed.");
            result.setError(existRes.getError());
            return result;
        }
        if(existRes.getResult()){
            //名称已存在
            log.error("requirement name have existed.");
            result.setError("requirement.checkName.existed");
            return result;
        }

        //整体需求级别的模块类型
        if(Requirement.ModuleType.from(requirement.getModuleType()) == null){
            log.error("create requirement need module type");
            result.setError("requirement.moduleType.null");
            return result;
        }

        //需求的物料 类别
        if((requirement.getMaterielType()) == null){
            log.error("create requirement need module materiel type");
            result.setError("requirement.materielType.null");
            return result;
        }

        //需求的物料类别名称
        if((requirement.getMaterielName()) == null){
            log.error("create requirement need module materiel name");
            result.setError("requirement.materielType.null");
            return result;
        }

        if(requirement.getDescription() != null && requirement.getDescription().length() > 256){
            log.error("create requirement description value is to long.");
            result.setError("requirement.description.toLong");
        }

        //系列编号长度校验
        if(requirement.getSeriesIds() != null && requirement.getSeriesIds().length() > 1024){
            log.error("create requirement seriesIds value is to long.");
            result.setError("requirement.seriesId.tooLong");
            return result;
        }

        //配送园区&厂商（使用json保存->1,2,3）
        if(Strings.isNullOrEmpty(requirement.getDeliveryAddress())){
            log.error("create requirement need delivery address.");
            result.setError("requirement.deliveryAddress.null");
            return result;
        }

        //需求描述信息
        if(Strings.isNullOrEmpty(requirement.getDescription())){
            log.error("create requirement need description.");
            result.setError("requirement.description.null");
            return result;
        }

        //模块策略编号
        if(requirement.getTacticsId() == null){
            log.error("create requirement need tactics id.");
            result.setError("requirement.tacticsId.null");
            return result;
        }

        //引领点
        if(requirement.getHeadDrop() == null){
            log.error("create requirement need head drop.");
            result.setError("requirement.headDrop.null");
            return result;
        }

        //币种
        if(CoinType.from(requirement.getCoinType()) == null){
            log.error("create requirement need coin type");
            result.setError("requirement.coinType.null");
            return result;
        }

        //设置创建人员信息
        if(requirement.getCreatorId() == null){
            log.error("create requirement need creator id.");
            result.setError("requirement.creatorId.null");
            return result;
        }

        //推送供应商长度校验
        if(requirement.getCompanyScope() != null && requirement.getCompanyScope().length() > 1024){
            log.error("create requirement companyScope value is to long.");
            result.setError("requirement.companyScope.tooLong");
            return result;
        }

        if(Strings.isNullOrEmpty(requirement.getCreatorName())){
            log.error("create requirement need creator name.");
            result.setError("requirement.creatorName.null");
            return result;
        }

        if(Strings.isNullOrEmpty(requirement.getCreatorPhone())){
            log.error("create requirement need creator phone.");
            result.setError("requirement.creatorPhone.null");
            return result;
        }

        result.setResult(true);
        return result;
    }

    /**
     * 验证需要创建需求时团队的确认处理
     * @param teamList  团队列表
     * @return Boolean
     * 返回创建是否成功
     */
    private Response<Boolean> checkTeams(List<RequirementTeam> teamList){
        Response<Boolean> result = new Response<Boolean>();

        for(RequirementTeam team : teamList){
            if(RequirementTeam.Type.from(team.getType()) == null){
                log.error("create team need type.");
                result.setError("team.type.null");
                return result;
            }

            if(team.getUserId() == null){
                log.error("create team need user id.");
                result.setError("team.userId.null");
                return result;
            }

            if(Strings.isNullOrEmpty(team.getUserName())){
                log.error("create team need user name.");
                result.setError("team.userName.null");
                return result;
            }

            if(team.getUserNumber() == null){
                log.error("create team need user number.");
                result.setError("team.userNumber.null");
                return result;
            }
        }

        result.setResult(true);
        return result;
    }

    /**
     * 验证需求创建时的需求阶段时间的设置处理
     * @param timeList  需求每个阶段的时间设置
     * @return Boolean
     * 返回创建时间是否成功
     * (这个需要验证每一个阶段都必须要有时间阶段的设定)
     */
    private Response<Boolean> checkTimes(List<RequirementTime> timeList , Integer moduleType){
        Response<Boolean> result = new Response<Boolean>();

        Integer[] statusNum = { 0, 0, 0, 0, 0};
        for(RequirementTime time : timeList){
            //是否存在这种时间阶段
            if(RequirementStatus.from(time.getType()) == null){
                log.error("create time need type.");
                result.setError("time.type.null");
                return result;
            }

            if(time.getPredictStart() == null){
                log.error("create time need predict start time.");
                result.setError("time.predictStart.null");
                return result;
            }

            if(time.getPredictEnd() == null){
                log.error("create time need predict end time.");
                result.setError("time.predictEnd.null");
                return result;
            }

            //用于判断每个阶段是否都已写入时间
            switch(RequirementStatus.from(time.getType())){
                case RES_INTERACTIVE:
                    statusNum[0] = 1;
                    break;

                case RES_LOCK:
                    statusNum[1] = 1;
                    break;

                case SOL_INTERACTIVE:
                    statusNum[2] = 1;
                    break;

                case SOL_END:
                    statusNum[3] = 1;
                    break;

                case SUP_SOL:
                    statusNum[4] = 1;
                    break;

                default:
                    break;
            }
        }

        Integer timeNum = 0;
        for(Integer num : statusNum){
            timeNum += num;
        }

        //如果moduleType为衍生号和甲指需求发布的时候，
        //时间计划只需校验status为4和5的时间计划必填，此时timeNum＝2
        if(moduleType == Module.Type.DERIVE_TYPE.value() || moduleType == Module.Type.JIA_ZHI.value()){
            if(timeNum != 2){
                log.error("need create all stage time");
                result.setError("time.type.num.error");
                return result;
            }
        }else{
            //其他类型的需求发布时，依旧校验所有status时间计划必填
            if(timeNum != 5){
                log.error("need create all stage time");
                result.setError("time.type.num.error");
                return result;
            }
        }

        result.setResult(true);
        return result;
    }

    /**
     * 根据当前用户编号获取用户的上级领导信息
     * @param userId  当前用户信息编号
     * @return  User
     * 返回领导信息
     */
    private Response<User> queryLeader(Long userId){
        Response<User> result = new Response<User>();

        //获取上级用户的系统编号
        Response<PurchaserDto> userRes = accountService.findPurchaserById(userId);
        if(!userRes.isSuccess()){
            //获取用户详细数据失败
            log.error("find user Detail info failed, error code={}", userRes.getError());
            result.setError(userRes.getError());
            return result;
        }

        //验证用户的上级是否存在
        PurchaserExtra purchaserExtra = userRes.getResult().getPurchaserExtra();
        if(purchaserExtra == null || purchaserExtra.getId() == null){
            log.error("find leader need leaderId");
            result.setError("user.not.found");
            return result;
        }

        //获取上级领导编号获取领导的详细信息
        Response<TeamMemeberDto> leaderRes = accountService.findStaffByWorkNo(purchaserExtra.getLeader());
        if(!leaderRes.isSuccess()){
            log.error("find leader info failed, error code={}", leaderRes.getError());
            result.setError(leaderRes.getError());
            return result;
        }

        if(leaderRes.getResult() == null){
            log.error("don't find leader info.");
            result.setError("leader.find.failed");
            return result;
        }

        result.setResult(leaderRes.getResult());
        return result;
    }

    /**
     * 需求切换时验证数据正确性
     * @param requirement   需求信息
     * @param user          用户信息
     * @return  Boolean
     * 返回状态切换验证结果
     */
    private Response<Boolean> transitionStatusCheck(Requirement requirement , BaseUser user){
        Response<Boolean> result = new Response<Boolean>();

        //只有审核通过才能切换状态
        if(Objects.equal(Requirement.CheckResult.from(requirement.getCheckResult()) , Requirement.CheckResult.SUCCESS)){
            //判断用户是否有权限更改阶段状态(现在状态更改只有需求创建者可以)
            if (Objects.equal(requirement.getCreatorId(), user.getId())) {
                //不同阶段的数据验证
                switch(RequirementStatus.from(requirement.getStatus())){
                    case RES_LOCK:
                        //需求锁定阶段不需要等到时间到达预期时间（特殊时间阶段）同时验证所有的模块数据是否填写完整
                        List<Module> moduleList = moduleDao.findModules(requirement.getId());
                        //判断需求锁定阶段是否有模块数据
                        if(moduleList.isEmpty()){
                            log.error("requirement module info can't be null.");
                            result.setError("module.list.null");
                            return result;
                        }

                        for(Module module : moduleList){
                            if(module.getRequirementId() == null || module.getType() == null || module.getModuleName() == null || module.getSeriesId() == null
                                    || module.getPropertyId() == null || module.getTotal() == null || module.getQuality() == null || module.getCost() == null
                                    || module.getDelivery() == null || module.getAttestations() == null || module.getSupplyAt() == null || module.getUnits() == null){
                                log.error("check module is not detail.");
                                result.setError("module.not.detail");
                                return result;
                            }
                        }

                        result.setResult(true);
                        break;

                    default:
                        //当前时间必须大于阶段时间
                        RequirementTime requirementTime = requirementTimeDao.findByStatus(requirement.getId(), requirement.getStatus());
                        if (requirementTime == null || Days.daysBetween(DateTime.now(), new DateTime(requirementTime.getPredictEnd())).getDays() < 0) {
                            result.setResult(true);
                        } else {
                            log.error("transition requirement status need the current time is greater than the expected time");
                            result.setError("requirement.transition.noTime");
                        }
                }
            } else {
                log.error("user don't have power to transition requirement.");
                result.setError("requirement.transition.noPower");
            }
        }else{
            log.error("requirement status don't allow change requirement status");
            result.setError("requirement.audit.allow");
        }

        return result;
    }

    /**
     * 写入需求的统计信息
     * @param paging    分页统计数据
     */
    private void setReqCount(Paging<Requirement> paging){
        for(Requirement requirement : paging.getData()){
            //写入统计数据(当阶段还未进入选定供应商与方案，统计数据都是存放在redis中)
            if(requirement.getStatus() == null || requirement.getStatus() < RequirementStatus.SUP_SOL.value()){
                Map<RequirementCountType , Integer> reqMaps = requirementCountService.findReqCount(requirement.getId() , RequirementCountType.values());
                requirement.setSendSu(reqMaps.get(RequirementCountType.SEND_SU));
                requirement.setAnswerSu(reqMaps.get(RequirementCountType.ANSWER_SU));
                requirement.setSendSo(reqMaps.get(RequirementCountType.SEND_SO));
                requirement.setTopicNum(reqMaps.get(RequirementCountType.TOPIC_NUM));
            }
        }
    }

}
