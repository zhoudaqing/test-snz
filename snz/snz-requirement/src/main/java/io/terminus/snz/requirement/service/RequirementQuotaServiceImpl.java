package io.terminus.snz.requirement.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.haier.manager.PLMModuleManager;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.BackendJSON;
import io.terminus.snz.requirement.dto.DetailQuotaDto;
import io.terminus.snz.requirement.dto.SolutionQuotaDto;
import io.terminus.snz.requirement.dto.SolutionRankDto;
import io.terminus.snz.requirement.manager.QuotaManager;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.SupplierCreditQualifyService;
import io.terminus.snz.user.service.SupplierResourceMaterialService;
import io.terminus.snz.user.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:模块配额处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-07.
 */
@Slf4j
@Service
public class RequirementQuotaServiceImpl implements RequirementQuotaService {

    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private QuotaManager quotaManager;

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private RequirementTeamDao requirementTeamDao;

    @Autowired
    private ModuleFactoryDao moduleFactoryDao;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private RequirementRankDao requirementRankDao;

    @Autowired
    private ModuleQuotaDao moduleQuotaDao;

    @Autowired
    private ModuleQuotationDao moduleQuotationDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PLMModuleManager plmModuleManager;

    @Autowired
    private DepositService depositService;

    @Autowired
    private RequirementSendDao requirementSendDao;

    @Autowired
    private SupplierCreditQualifyService supplierCreditQualifyService;

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;

    @Autowired
    private TagService tagService;

    @Override
    public Response<List<RequirementSolution>> findEndSolutions(Long requirementId, Integer sortType, BaseUser user) {
        Response<List<RequirementSolution>> result = new Response<List<RequirementSolution>>();

        //验证用户是否已登入
        if(user == null){
            log.error("find requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find requirement solution need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        //默认为成本指标排序
        Integer newSortType = sortType == null ? 5 : sortType;

        try{
            //非需求创建者则返回空的分页数据
            Requirement requirement = requirementDao.findById(requirementId);
            if(!Objects.equal(requirement.getCreatorId() , user.getId())){
                log.debug("requirement solution end info just requirement creator can see it.");
                result.setResult(new ArrayList<RequirementSolution>());
                return result;
            }

            //获取已提交保证金的用户列表
            Response<List<Long>> paidIdRes = depositService.findPaidListByRequirement(requirementId);
            if(!paidIdRes.isSuccess()){
                log.error("find paid deposit supplier ids failed, error code={}", paidIdRes.getError());
                result.setError(paidIdRes.getError());
                return result;
            }

            if(paidIdRes.getResult() == null || paidIdRes.getResult().isEmpty()){
                result.setResult(new ArrayList<RequirementSolution>());
                return result;
            }else{
                List<RequirementSolution> solutionList = requirementSolutionDao.findSolutionEnds(requirementId, paidIdRes.getResult(), 5, RequirementSolution.Status.SEND_END.value());

                Response<List<RequirementSolution>> filterRes = filterSolution(requirementId , solutionList);
                if(!filterRes.isSuccess()){
                    log.error("filter solution failed, error code={}", filterRes.getError());
                    result.setError(filterRes.getError());
                    return result;
                }

                result.setResult(filterRes.getResult());
            }
        }catch(Exception e){
            log.error("find solution failed, requirementId={}, sortType={}, error code={}." , requirementId, newSortType, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Integer> findEndSolutionNum(Long requirementId, Integer queryType) {
        Response<Integer> result = new Response<Integer>();

        List<RequirementSolution> solutionList = Lists.newArrayList();
        switch(QueryType.from(queryType)){
            case QUALIFY:
                //供应商资质符合(不需要验证保证金)
                solutionList = requirementSolutionDao.findSolutionEnds(requirementId, Lists.transform(requirementSolutionDao.findAllSolution(requirementId) , new Function<RequirementSolution, Long>() {
                    @Nullable
                    @Override
                    public Long apply(RequirementSolution solution) {
                        return solution.getSupplierId();
                    }
                }), 5, null);

                break;

            case ALL:
                //全部条件符合
                //获取已提交保证金的用户列表
                Response<List<Long>> paidIdRes = depositService.findPaidListByRequirement(requirementId);
                if(!paidIdRes.isSuccess()){
                    log.error("find paid deposit supplier ids failed, error code={}", paidIdRes.getError());
                    result.setError(paidIdRes.getError());
                    return result;
                }

                if(paidIdRes.getResult() == null || paidIdRes.getResult().isEmpty()){
                    result.setResult(0);
                    return result;
                }

                solutionList = requirementSolutionDao.findSolutionEnds(requirementId, paidIdRes.getResult(), 5, RequirementSolution.Status.SEND_END.value());

                break;

            default:
                break;
        }

        Response<List<RequirementSolution>> filterRes = filterSolution(requirementId , solutionList);
        if(!filterRes.isSuccess()){
            log.error("filter solution failed, error code={}", filterRes.getError());
            result.setError(filterRes.getError());
            return result;
        }
        result.setResult(filterRes.getResult().size());

        return result;
    }

    @Override
    public Response<List<RequirementSolution>> findTagSolutions(Long requirementId) {
        Response<List<RequirementSolution>> result = new Response<List<RequirementSolution>>();

        try {
            //获取已提交保证金的用户列表
            Response<List<Long>> paidIdRes = depositService.findPaidListByRequirement(requirementId);
            if (!paidIdRes.isSuccess()) {
                log.error("find paid deposit supplier ids failed, error code={}", paidIdRes.getError());
                result.setError(paidIdRes.getError());
                return result;
            }

            if (paidIdRes.getResult() == null || paidIdRes.getResult().isEmpty()) {
                result.setResult(new ArrayList<RequirementSolution>());
                return result;
            }

            List<RequirementSolution> solutionList = requirementSolutionDao.findSolutionEnds(requirementId, paidIdRes.getResult(), 5, RequirementSolution.Status.SEND_END.value());

            Response<List<RequirementSolution>> filterRes = filterSolution(requirementId, solutionList);
            if (!filterRes.isSuccess()) {
                log.error("filter solution failed, error code={}", filterRes.getError());
                result.setError(filterRes.getError());
                return result;
            }

            result.setResult(filterRes.getResult());
        }catch(Exception e){
            log.error("find tag solution failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> createRanks(Long requirementId , Integer sortType, BaseUser user) {
        //todo 后期做权限的验证
        Response<Boolean> result = new Response<Boolean>();

        //根据排序获取排名信息
        Response<List<RequirementSolution>> solutions = findEndSolutions(requirementId, sortType , user);
        if(!solutions.isSuccess()){
            log.error("find requirement solution failed.");
            result.setError(solutions.getError());
            return result;
        }

        //需求方案排序数据不能未空
        if(solutions.getResult() == null || solutions.getResult().isEmpty()){
            log.error("create requirement ranks, requirement solution can't be empty.");
            result.setError("rank.info.null");
            return result;
        }

        try{
            //判断需求阶段是否符合
            Requirement requirement = requirementDao.findById(requirementId);
            if(!Objects.equal(RequirementStatus.from(requirement.getStatus()) , RequirementStatus.SUP_SOL)){
                log.error("create requirement ranks, the requirement status must be SUP_SOL");
                result.setError("rank.requirement.statusError");
                return result;
            }

            //判断是否已经创建了排名信息
            if(requirement.getSolutionId() != null){
                log.error("requirement ranks have created, can't create again.");
                result.setError("rank.have.created");
                return result;
            }

            //根据需求中的合作供应商数量&备选供应商数量确定名次规则
            int rankNum = 0;
            int allRankNum = requirement.getSelectNum() + requirement.getReplaceNum();
            List<RequirementRank> selectRanks = Lists.newArrayList();

            RequirementRank rank;
            for(RequirementSolution solution : solutions.getResult()){
                //判断全部的名次信息内容
                if(++rankNum <= allRankNum){
                    rank = new RequirementRank();
                    rank.setRank(rankNum);
                    rank.setSupplierId(solution.getSupplierId());
                    rank.setSupplierName(solution.getSupplierName());
                    rank.setRequirementId(solution.getRequirementId());

                    if(rankNum <= requirement.getSelectNum()){
                        //正选供应商名单
                        rank.setType(RequirementRank.Type.OFFICIAL.value());
                        selectRanks.add(rank);
                    }else{
                        //备选供应商名单
                        rank.setType(RequirementRank.Type.REPLACE.value());
                        selectRanks.add(rank);
                    }
                }else{
                    break;
                }
            }

            int selectNum = rankNum > requirement.getSelectNum() ? requirement.getSelectNum() : rankNum;
            switch(sortType){
                case 1:
                    quotaManager.createQuotaWithT(requirement , selectNum, selectRanks);
                    break;

                case 5:
                    quotaManager.createQuotaWithC(requirement , selectNum, selectRanks);
                    break;

                default:

            }

            result.setResult(true);
        }catch(Exception e){
            log.error("create requirement ranks failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("rank.create.failed");
        }

        return result;
    }

    @Override
    public Response<List<SolutionRankDto>> findTSolutionRank(Long requirementId, BaseUser user) {
        Response<List<SolutionRankDto>> result = new Response<List<SolutionRankDto>>();

        //验证用户是否已登入
        if(user == null){
            log.error("find requirement solution, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find requirement solution need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Response<List<RequirementSolution>> solutionRes = findEndSolutions(requirementId , null, user);
            if(!solutionRes.isSuccess()){
                log.error("find requirement solutions with end status failed, error code={}" , solutionRes.getError());
                result.setError(solutionRes.getError());
                return result;
            }

            List<SolutionRankDto> solutionRankList = new ArrayList<SolutionRankDto>();
            Requirement requirement = requirementDao.findById(requirementId);
            List<RequirementRank> rankList = new ArrayList<RequirementRank>();
            //已经创建排名则写入排名数据信息
            if(requirement.getSolutionId() != null){
                rankList = requirementRankDao.findAllRanks(requirementId , 3);
            }

            SolutionRankDto solutionRank;
            for(RequirementSolution solution : solutionRes.getResult()){
                solutionRank = new SolutionRankDto();
                //写入需求方案信息
                solutionRank.setRequirementSolution(solution);

                for(RequirementRank requirementRank : rankList){
                    //写入供应商对应的排名信息
                    if(Objects.equal(requirementRank.getSupplierId() , solution.getSupplierId())){
                        solutionRank.setRequirementRank(requirementRank);
                    }
                }
                solutionRankList.add(solutionRank);
            }

            result.setResult(solutionRankList);
        }catch(Exception e){
            log.error("find requirement solutions & ranks failed, requirementId={} error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("requirement.solutionRank.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> createRankByT(String requirementRanks, BaseUser user) {
        List<RequirementRank> requirementRankList = JSON_MAPPER.fromJson(requirementRanks , JSON_MAPPER.createCollectionType(List.class , RequirementRank.class));

        Response<Boolean> result = new Response<Boolean>();

        //根据排序获取排名信息
        if(requirementRankList == null || requirementRankList.isEmpty()){
            log.error("create requirement ranks, requirement rank can't be empty.");
            result.setError("rank.info.null");
            return result;
        }

        if(user == null){
            log.error("create rank user must be login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //判断需求阶段是否符合
            Requirement requirement = requirementDao.findById(requirementRankList.get(0).getRequirementId());

            if(!Objects.equal(RequirementStatus.from(requirement.getStatus()) , RequirementStatus.SUP_SOL)){
                log.error("create requirement ranks, the requirement status must be SUP_SOL");
                result.setError("rank.requirement.statusError");
                return result;
            }

            //判断是否已经创建了排名信息
            if(requirement.getSolutionId() != null){
                log.error("requirement ranks have created, can't create again.");
                result.setError("rank.have.created");
                return result;
            }

            //只有卓越运营无法选择T排名
            if(Objects.equal(Tactics.from(requirement.getTacticsId()) , Tactics.EXCELLENCE)){
                log.error("create requirement ranks excellence can't use tactics with T");
                result.setError("rank.t.failed");
                return result;
            }

            //根据需求中的合作供应商数量&备选供应商数量确定名次规则
            int rankNum = 0;
            int allRankNum = requirement.getSelectNum() + requirement.getReplaceNum();
            List<RequirementRank> selectRanks = Lists.newArrayList();

            //重至排名顺序
            RequirementRank[] rankArray = new RequirementRank[requirementRankList.size()];
            int i = requirementRankList.size() - 1;
            for(RequirementRank requirementRank : requirementRankList){
                if(requirementRank.getRank() == null){
                    rankArray[i] = requirementRank;
                }else{
                    rankArray[requirementRank.getRank()-1] = requirementRank;
                }
            }

            for(RequirementRank requirementRank : rankArray){
                //判断全部的名次信息内容
                if(++rankNum <= allRankNum){
                    if(rankNum <= requirement.getSelectNum()){
                        //正选供应商名单
                        requirementRank.setType(RequirementRank.Type.OFFICIAL.value());
                        selectRanks.add(requirementRank);
                    }else{
                        //备选供应商名单
                        requirementRank.setType(RequirementRank.Type.REPLACE.value());
                        selectRanks.add(requirementRank);
                    }
                }else{
                    break;
                }
            }

            int selectNum = rankNum > requirement.getSelectNum() ? requirement.getSelectNum() : rankNum;
            //按照T统计配额数据
            quotaManager.createQuotaWithT(requirement, selectNum, selectRanks);

            result.setResult(true);
        }catch(Exception e){
            log.error("create requirement ranks failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("rank.create.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> selectEndStatus(Long requirementId, Integer endStatus, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        //模块配额编号
        if(requirementId == null){
            log.error("find requirement solution need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        if(endStatus == null || EndStatus.from(endStatus) == null){
            log.error("update requirement end status can't be null.");
            result.setError("requirement.endStatus.null");
            return result;
        }

        if(user == null){
            log.error("create quota user must be login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //todo 判断用户是否可以允许更改阶段状态（only：C & creator）
            Requirement requirement = requirementDao.findById(requirementId);
            if(!Objects.equal(user.getId() , requirement.getCreatorId())){
                if(requirementTeamDao.findByIdType(user.getId() , RequirementTeam.Type.C.value()) == null){
                    log.error("user not have power to do, userId={}", user.getId());
                    result.setError("requirement.transition.noPower");
                    return result;
                }
            }

            //是否是选定供应商阶段
            if(!Objects.equal(RequirementStatus.from(requirement.getStatus()) , RequirementStatus.SUP_SOL)){
                log.error("the requirement status can't allow user change end status, requirementId={} , status={} ", requirement.getId(), requirement.getStatus());
                result.setError("requirement.status.error");
                return result;
            }

            //阶段数据状态
            RequirementSend oldSend = requirementSendDao.findByRequirementId(requirementId);

            RequirementSend newSend = new RequirementSend();
            newSend.setId(oldSend.getId());

            switch(EndStatus.from(endStatus)){
                case BUS_NEG:       //商务谈判状态
                    newSend.setBusinessNegotiate(RequirementSend.Type.COMMIT.value());
                    break;

                case SUP_SIGN:      //迁移到供应商跟标
                    //重新写入配额的报价数据信息
                    Response<Boolean> quotaRes = updateNewQuota(requirement);
                    if(!quotaRes.isSuccess()){
                        log.error("update quota failed, error code={}", quotaRes.getError());
                        result.setError(quotaRes.getError());
                        return result;
                    }

                    newSend.setSupplierSign(RequirementSend.Type.COMMIT.value());
                    break;

                case RES_PUB:       //配额结果公示

                    if(Objects.equal(RequirementSend.Type.from(oldSend.getResultPublicity()) , RequirementSend.Type.COMMIT)){
                        log.error("requirement quota result have publicity, can't again. requirementId={}", requirementId);
                        result.setError("requirement.endView.showed");
                        return result;
                    }

                    //设置默认的配额数据
                    Response<Boolean> updateRes = updateEndQuota(requirement);
                    if(!updateRes.isSuccess()){
                        log.error("update new quota failed, requirementId={}, error code={}", requirement.getId(), updateRes.getError());
                        result.setError(updateRes.getError());
                        return result;
                    }

                    //为供应商打标签
                    for(RequirementRank requirementRank : requirementRankDao.findAllRanks(requirementId , RequirementRank.Type.OFFICIAL.value())){
                        Response<Company> companyRes = companyService.findCompanyById(requirementRank.getSupplierId());
                        if(companyRes.isSuccess()){
                            tagService.addSupplierStatusTag(companyRes.getResult().getUserId(), User.SupplierTag.IN_SUPPLIER);
                        }
                    }

                    //状态流转
                    newSend.setResultPublicity(RequirementSend.Type.COMMIT.value());

                    break;

                default:
            }

            if(requirementSendDao.findByRequirementId(requirementId) == null){
                requirementSendDao.create(newSend);
            }else{
                requirementSendDao.update(newSend);
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("update requirement end status can't be null");
            result.setError("requirement.endStatus.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> signSolution(Long quotaId, Integer status, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();
        //模块配额编号
        if(quotaId == null){
            log.error("update module quota quotaId can't be null.");
            result.setError("quota.id.null");
            return result;
        }

        if(ModuleQuota.Status.from(status) == null){
            log.error("update module quota status can't be null.");
            result.setError("quota.status.null");
            return result;
        }

        if(user == null){
            log.error("create quota user must be login.");
            result.setError("user.not.login");
            return result;
        }

        try{
            //todo 权限的判断后期在规定
            if(Objects.equal(ModuleQuota.Status.from(status) , ModuleQuota.Status.ACCEPT)){
                //模块报价接受
                ModuleQuota moduleQuota = new ModuleQuota();
                moduleQuota.setId(quotaId);
                moduleQuota.setStatus(status);

                moduleQuotaDao.update(moduleQuota);
            }else{
                //模块报价不接受
                ModuleQuota quota = moduleQuotaDao.findById(quotaId);

                List<ModuleQuota> quotaList = moduleQuotaDao.findOtherQuota(quota.getRequirementId(), quota.getModuleId(), quotaId);
                if(quotaList.isEmpty()){
                    log.error("now quota is empty can't to unAccept.");
                    result.setError("quota.empty.set");
                    return result;
                }

                //更新其余报价的数据信息
                quotaManager.updateQuotas(quotaList);

                //写入模块跟标信息
                quota.setStatus(status);
                moduleQuotaDao.update(quota);
            }

            //写入需求当前状态
            setReqQuotaStatus(quotaId);
            result.setResult(true);
        }catch(Exception e){
            log.error("sign module quotation failed, quotaId={}, userId={}, error code={}", quotaId, user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("quotation.sign.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateQuota(String moduleQuotas, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();
        List<ModuleQuota> quotaList = JSON_MAPPER.fromJson(moduleQuotas , JSON_MAPPER.createCollectionType(List.class , ModuleQuota.class));

        if(user == null){
            log.error("update quota user must be login.");
            result.setError("user.not.login");
            return result;
        }

        if(quotaList.isEmpty()){
            log.error("update quota info need quota list");
            result.setError("quota.list.empty");
            return result;
        }

        try{
            //判断更改之后的配额数据是否正确
            ModuleFactory moduleFactory = moduleFactoryDao.findById(quotaList.get(0).getModuleFactoryId());

            int scale = 0 , quantity = 0;
            for(ModuleQuota moduleQuota : quotaList){
                quantity += moduleQuota.getQuantity();
                scale += moduleQuota.getScale();
            }

            //新的配额数据是否正确
            if(moduleFactory.getResourceNum() != quantity || scale != 100){
                log.error("update quota info is error");
                result.setError("quota.newInfo.failed");
                return result;
            }

            //更新新的配额数据
            for(ModuleQuota moduleQuota : quotaList){
                moduleQuotaDao.update(moduleQuota);
            }

            result.setResult(true);
        }catch(Exception e){
            log.error("update module quota info failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quota.update.failed");
        }

        return result;
    }

    @Override
    public Response<List<SolutionQuotaDto>> findDetailSolution(Long requirementId) {
        Response<List<SolutionQuotaDto>> result = new Response<List<SolutionQuotaDto>>();

        //需求编号
        if(requirementId == null){
            log.error("find supplier's requirement solution need requirementId.");
            result.setError("rank.requirementId.null");
            return result;
        }

        try{
            //获取可进行排名的数据信息
            result.setResult(quotaManager.findDetailSolution(requirementId));
        }catch(Exception e){
            log.error("find requirement solution failed , requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("solution.find.failed");
        }

        return result;
    }

    @Override
    public Response<List<RequirementRank>> findRequirementRanks(Long requirementId, Integer type) {
        Response<List<RequirementRank>> result = new Response<List<RequirementRank>>();

        //需求编号
        if(requirementId == null){
            log.error("find supplier's requirement solution need requirementId.");
            result.setError("rank.requirementId.null");
            return result;
        }

        //默认显示全部数据
        Integer newType = RequirementRank.Type.from(type) == null ? 3 : type;

        try{
            result.setResult(requirementRankDao.findAllRanks(requirementId, newType));
        }catch(Exception e){
            log.error("find requirement ranks failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("rank.find.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleQuota>> findByRequirementId(Long requirementId, Integer pageNo, Integer size) {
        Response<Paging<ModuleQuota>> result = new Response<Paging<ModuleQuota>>();

        if(requirementId == null){
            log.error("find module quotas need requirementId.");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? 10 : size);
            params.putAll(pageInfo.toMap());

            result.setResult(moduleQuotaDao.findByParams(requirementId, params));
        }catch(Exception e){
            log.error("find module quotas failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quota.fina.failed");
        }

        return result;
    }

    @Override
    public Response<Paging<ModuleQuota>> findQuotasBySupplier(BaseUser user, Long requirementId, Integer pageNo, Integer size) {
        Response<Paging<ModuleQuota>> result = new Response<Paging<ModuleQuota>>();

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

        Response<Company> companyRes = companyService.findCompanyByUserId(user.getId());
        if(!companyRes.isSuccess()){
            log.error("query company failed, error code={}", companyRes.getError());
            result.setError(companyRes.getError());
            return result;
        }

        try{
            RequirementSend requirementSend = requirementSendDao.findByRequirementId(requirementId);
            Paging<ModuleQuota> quotaPaging;

            //是否已到供应商跟标阶段
            if(Objects.equal(RequirementSend.Type.from(requirementSend.getSupplierSign()) , RequirementSend.Type.COMMIT)){

                Map<String , Object> params = Maps.newHashMap();
                PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
                params.putAll(pageInfo.toMap());
                params.put("supplierId" , companyRes.getResult().getId());

                quotaPaging = moduleQuotaDao.findByParams(requirementId, params);
            }else{
                quotaPaging = new Paging<ModuleQuota>(0l , new ArrayList<ModuleQuota>());
            }

            result.setResult(quotaPaging);
        }catch(Exception e){
            log.error("find module quotas failed, supplierId={}, requirementId={}, error code={}", user.getId(), requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quota.fina.failed");
        }

        return result;
    }

    @Override
    public Response<DetailQuotaDto> findRequirementQuota(Long requirementId, Integer pageNo, Integer size) {
        Response<DetailQuotaDto> result = new Response<DetailQuotaDto>();

        if(requirementId == null){
            log.error("find module quotas need requirementId.");
            result.setError("solution.requirementId.null");
            return result;
        }

        try{
            RequirementSend requirementSend = requirementSendDao.findByRequirementId(requirementId);
            Paging<ModuleQuota> quotaPaging;
            DetailQuotaDto detailQuotaDto = new DetailQuotaDto();

            Map<String , Object> params = Maps.newHashMap();
            PageInfo pageInfo = new PageInfo(pageNo , Objects.firstNonNull(size , 10));
            params.putAll(pageInfo.toMap());
            params.put("status" , ModuleQuota.Status.ACCEPT.value());

            //当选定供应商可跟标||供应商已全部确定配额
            if(Objects.equal(RequirementSend.Type.from(requirementSend.getSupplierSign()) , RequirementSend.Type.COMMIT) ||
               Objects.equal(RequirementSend.Type.from(requirementSend.getReplyModuleNum()) , RequirementSend.Type.COMMIT)){
                quotaPaging = moduleQuotaDao.findByParams(requirementId , params);

                detailQuotaDto.setQuotaStatus(1);
                detailQuotaDto.setQuotaPaging(quotaPaging);
            }else{
                quotaPaging = new Paging<ModuleQuota>(0l , new ArrayList<ModuleQuota>());

                detailQuotaDto.setQuotaStatus(0);
                detailQuotaDto.setQuotaPaging(quotaPaging);
            }

            result.setResult(detailQuotaDto);
        }catch(Exception e){
            log.error("find requirement detail quota failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quota.fina.failed");
        }

        return result;
    }

    @Override
    public Response<List<ModuleQuota>> findModuleQuotas(Long moduleId, Long moduleFactoryId) {
        Response<List<ModuleQuota>> result = new Response<List<ModuleQuota>>();

        if(moduleId == null){
            log.error("find all factory module quota need moduleId.");
            result.setError("quota.moduleId.failed");
            return result;
        }

        if(moduleFactoryId == null){
            log.error("find all factory module quota need factoryId.");
            result.setError("quota.factoryId.failed");
            return result;
        }

        try{
            result.setResult(moduleQuotaDao.findQuotaByFactoryId(moduleId , moduleFactoryId));
        }catch(Exception e){
            log.error("find all factory module quotas failed, moduleId={}, moduleFactoryId={}, error code={}", moduleId, moduleFactoryId, Throwables.getStackTraceAsString(e));
            result.setError("quota.fina.failed");
        }

        return result;
    }

    @Override
    public void plmCompanyVExpire() {
        try{
            //获取选定供应商与方案阶段的所有需求列表
            List<Requirement> requirementList = requirementDao.findReqByStatus(RequirementStatus.SUP_SOL.value());

            for(Requirement requirement : requirementList){
                //获取全部未确认配额的信息
                List<ModuleQuota> emptyQuotas = moduleQuotaDao.findByRequirementId(requirement.getId() , null);
                if(emptyQuotas.isEmpty()){
                    //所有的配额都已跟标向中间表写入供应商V码
                    List<ModuleQuota> quotaList = moduleQuotaDao.findByRequirementId(requirement.getId() , ModuleQuota.Status.ACCEPT.value());

                    //更新供应商v码到plm中间表
                    Integer updateLen = plmModuleManager.updateSupplierV(quotaList);

                    RequirementSend requirementSend = new RequirementSend();
                    requirementSend.setRequirementId(requirement.getId());
                    requirementSend.setSendVCode(Objects.equal(updateLen , quotaList.size()) ? RequirementSend.Type.COMMIT.value() : RequirementSend.Type.UN_COMMIT.value());

                    //创建详细的配额信息(拆分工厂数据到每一条详细的配额数据信息)
                    quotaManager.createDetailQuota(requirement , quotaList);
                    requirementSend.setWriteDetailQuota(RequirementSend.Type.COMMIT.value());

                    //写入成功后记录一个新的状态为需求终结阶段
                    Requirement newReq = new Requirement();
                    newReq.setId(requirement.getId());
                    newReq.setStatus(RequirementStatus.TENDER_END.value());
                    requirementDao.update(newReq);

                    requirementSendDao.update(requirementSend);
                }
            }
        }catch(Exception e){
            log.error("set company v code to plm table failed, error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 重新写入配额的报价数据信息
     * @param requirement   需求信息
     * @return Boolean
     * 返回更新报价是否成功
     */
    private Response<Boolean> updateNewQuota(Requirement requirement){
        Response<Boolean> result = new Response<Boolean>();
        Long requirementId = requirement.getId();

        //当上一个阶段是从谈判过来的则需要更新已有的配额的用户报价信息(只有技术领先需要走这个流程)
        RequirementSend requirementSend = requirementSendDao.findByRequirementId(requirementId);

        try{
            if(Objects.equal(requirementSend.getBusinessNegotiate() , RequirementSend.Type.COMMIT.value())
                    && Objects.equal(Tactics.from(requirement.getTacticsId()) , Tactics.TECHNOLOGY_NEW)){

                //必须上传文件才能走下一个流程
                if(Strings.isNullOrEmpty(requirementSend.getNegotiateFile())){
                    log.error("supplier sign requirement quota need send negotiate file.");
                    result.setError("requirement.negotiateFile.null");
                    return result;
                }

                //获取最优的报价数据信息
                RequirementSolution bestSolution = requirementSolutionDao.findById(requirement.getSolutionId());
                List<ModuleQuotation> bestQuotations = moduleQuotationDao.findAllQuotations(bestSolution.getId());
                Map<Long , ModuleQuotation> quotationMap = Maps.newHashMap();

                for(ModuleQuotation moduleQuotation : bestQuotations){
                    quotationMap.put(moduleQuotation.getModuleId() , moduleQuotation);
                }

                List<RequirementRank> rankList = requirementRankDao.findAllRanks(requirementId , RequirementRank.Type.OFFICIAL.value());
                for(RequirementRank requirementRank : rankList){
                    RequirementSolution requirementSolution = requirementSolutionDao.findByRequirementId(requirementId , requirementRank.getSupplierId());

                    List<ModuleQuotation> moduleQuotations = moduleQuotationDao.findAllQuotations(requirementSolution.getId());

                    //获取所有的模块的报价信息
                    List<ModuleQuota> moduleQuotas = moduleQuotaDao.findByRequirementId(requirementId , null);

                    //写入新的模块报价数据信息
                    ModuleQuota newQuota;
                    ModuleQuotation bestQuotation;
                    for(ModuleQuotation moduleQuotation : moduleQuotations){
                        for(ModuleQuota moduleQuota : moduleQuotas){
                            if(Objects.equal(moduleQuotation.getModuleId() , moduleQuota.getModuleId())){
                                newQuota = new ModuleQuota();
                                newQuota.setId(moduleQuota.getId());
                                newQuota.setOriginalCost(moduleQuotation.getTransactPrice() == null ? moduleQuotation.getPrice() : moduleQuotation.getTransactPrice());
                                //写入第一名的报价,没有谈判价格就依据原始价格(作为跟标价格)
                                bestQuotation = quotationMap.get(moduleQuota.getModuleId());
                                newQuota.setActualCost(bestQuotation.getTransactPrice() == null ? bestQuotation.getPrice() : bestQuotation.getTransactPrice());

                                moduleQuotaDao.update(newQuota);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("update new quota failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("quota.update.failed");
        }

        result.setResult(true);

        return result;
    }

    /**
     * 当采购商点击结果公示后将重新计算去除那些没有跟标的供应商的配额份额
     * @param requirement   需求信息
     * @return  Boolean
     * 返回自动更新方案终投的结果处理
     */
    public Response<Boolean> updateEndQuota(Requirement requirement){
        Response<Boolean> result = new Response<Boolean>();

        try {
            //查询已经跟标的以及还未跟标的重新设置配额数量信息
            List<ModuleQuota> moduleQuotas = moduleQuotaDao.findAgreeQuotas(requirement.getId());
            Map<Long, List<ModuleQuota>> quotaCache = Maps.newHashMap();

            //依据模块编号拆分模块报价数据
            for (ModuleQuota moduleQuota : moduleQuotas) {
                List<ModuleQuota> quotaList = quotaCache.get(moduleQuota.getModuleFactoryId());
                if (quotaList != null) {
                    quotaList.add(moduleQuota);
                } else {
                    quotaList = Lists.newArrayList(moduleQuota);
                    quotaCache.put(moduleQuota.getModuleFactoryId(), quotaList);
                }
            }

            //对所有的报价进行自动配额处理
            quotaManager.autoUpdateQuota(quotaCache);

            result.setResult(true);
        }catch(Exception e){
            log.error("update module quota failed, requirementId={}, error code={}", requirement.getId(), Throwables.getStackTraceAsString(e));
            result.setError("quota.update.failed");
        }

        return result;
    }

   /*
    * 获取所有符合要求规则的供应商方案
    * @param requirementId 需求编号
    * @param solutionList 需要过滤的方案数据信息
    * @return  List
    * 返回需求信息列表
    */
    private Response<List<RequirementSolution>> filterSolution(Long requirementId , List<RequirementSolution> solutionList) {
        Response<List<RequirementSolution>> result = new Response<List<RequirementSolution>>();

        Requirement requirement = requirementDao.findById(requirementId);

        //符合规则的需求方案
        List<RequirementSolution> filterList = Lists.newArrayList();

        if (!solutionList.isEmpty()) {
            //获取供应商的信用信息
            Response<List<SupplierCreditQualify>> creditRes = supplierCreditQualifyService.findCreditQualifyByUserIds(Lists.transform(solutionList, new Function<RequirementSolution, Long>() {
                @Nullable
                @Override
                public Long apply(RequirementSolution solution) {
                    return solution.getUserId();
                }
            }));

            if (!creditRes.isSuccess()) {
                log.error("find supplier credit qualify failed, error code={}", creditRes.getError());
                result.setError(creditRes.getError());
                return result;
            }

            //过滤信用等级不合格的
            List<Long> filterSupplierIds = Lists.newArrayList();
            for (SupplierCreditQualify creditQualify : creditRes.getResult()) {
                if (creditQualify.isCreditQualified()) {
                    filterSupplierIds.add(creditQualify.getSupplierId());
                }
            }

            //获取后台三级类目信息
            List<BackendJSON> categoryList = JSON_MAPPER.fromJson(requirement.getSeriesIds(), JSON_MAPPER.createCollectionType(List.class, BackendJSON.class));

            //过滤资质验证不通过的
            Response<Map<Long, List<Long>>> materialRes = supplierResourceMaterialService.bulkGetApprovedBcIds(filterSupplierIds);
            if (!materialRes.isSuccess()) {
                log.error("find supplier material qualify failed, error code={}", materialRes.getError());
                result.setError(materialRes.getError());
                return result;
            }

            //过滤资质验证的用户的方案
            for (RequirementSolution solution : solutionList) {
                for (Map.Entry<Long, List<Long>> entry : materialRes.getResult().entrySet()) {
                    //验证供应商是否资质符合标准
                    if (Objects.equal(solution.getSupplierId(), entry.getKey()) && checkSupplierQualify(categoryList, entry.getValue())) {
                        filterList.add(solution);
                        break;
                    }
                }
            }
        }

        result.setResult(filterList);

        return result;
    }

    /**
     * 根据供应商的资质信息和需求的三级类目进行比较
     * @return Boolean
     * 返回符合三级类目
     */
    private Boolean checkSupplierQualify(List<BackendJSON> categoryList , List<Long> categoryIds){
        Boolean result = false;
        //匹配需求对应的类目信息判断供应商对应该需求的资质校验是否通过
        for(BackendJSON backendCategory : categoryList){
            for(Long categoryId : categoryIds){
                if(Objects.equal(backendCategory.getBcId() , categoryId)){
                    result = true;
                    break;
                }else{
                    result = false;
                }
            }
            //校验全部资质验证是否通过
            if(!result){
                break;
            }
        }

        return result;
    }

    /**
     * 根据配额编号获取对应的需求是否已全部完成跟标操作
     * @param quotaId 需求编号
     */
    private void setReqQuotaStatus(Long quotaId){
        //获取配额信息
        ModuleQuota moduleQuota = moduleQuotaDao.findById(quotaId);

        //获取全部未确认配额的信息
        List<ModuleQuota> emptyQuotas = moduleQuotaDao.findByRequirementId(moduleQuota.getRequirementId() , null);
        if(emptyQuotas.isEmpty()){
            //写入需求为全部已确认配额
            RequirementSend requirementSend = new RequirementSend();
            requirementSend.setRequirementId(moduleQuota.getRequirementId());
            requirementSend.setConfirmQuota(RequirementSend.Type.COMMIT.value());

            requirementSendDao.update(requirementSend);
        }
    }

    /**
     * 对于需求方案的排名表单验证
     * @param rankList  排名信息
     * @return Boolean
     * 返回验证表单是否成功
     */
    private Response<Boolean> checkForm(List<RequirementRank> rankList){
        Response<Boolean> result = new Response<Boolean>();

        //用于记录名次信息（防止名次相同现象）
        Map<Integer , Integer> rankNum = Maps.newHashMap();

        for(RequirementRank rankInfo : rankList){
            //需求编号
            if(rankInfo.getRequirementId() == null){
                log.error("create requirement rank need requirementId.");
                result.setError("rank.requirementId.null");
                return result;
            }

            //排名类型
            if(RequirementRank.Type.from(rankInfo.getType()) == null){
                log.error("create requirement rank need type");
                result.setError("rank.type.null");
                return result;
            }

            //名次信息
            if(rankInfo.getRank() == null){
                log.error("create requirement rank need rank");
                result.setError("rank.rank.null");
                return result;
            }

            //供应商编号
            if(rankInfo.getSupplierId() == null){
                log.error("create requirement rank need ");
                result.setError("rank.supplierId.null");
                return result;
            }

            Integer nowNum = rankNum.get(rankInfo.getRank());
            rankNum.put(rankInfo.getRank() , nowNum == null ? 0 : nowNum+1);
        }

        for(RequirementRank rankInfo : rankList){
            if(rankNum.get(rankInfo.getRank()) > 1){
                log.error("create requirement rank, rank num must only");
                result.setError("rank.rank.only");
                return result;
            }
        }

        result.setResult(true);
        return result;
    }
}
