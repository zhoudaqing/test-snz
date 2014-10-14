package io.terminus.snz.requirement.service;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.*;
import io.terminus.snz.requirement.dto.QuotationDetailDto;
import io.terminus.snz.requirement.dto.TransactInfoDto;
import io.terminus.snz.requirement.model.*;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:需求的谈判流程处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-05.
 */
@Slf4j
@Service
public class RequirementTransactServiceImpl implements RequirementTransactService {
    private static final JsonMapper JSON_MAPPER = JsonMapper.JSON_NON_DEFAULT_MAPPER;

    @Autowired
    private RequirementSolutionDao requirementSolutionDao;

    @Autowired
    private ModuleQuotationDao moduleQuotationDao;

    @Autowired
    private ModuleQuotaDao moduleQuotaDao;

    @Autowired
    private RequirementDao requirementDao;

    @Autowired
    private RequirementSendDao requirementSendDao;

    @Autowired
    private RequirementRankDao requirementRankDao;

    @Override
    public Response<QuotationDetailDto> findSupplierQuota(Long requirementId , Integer queryType, Integer pageNo, Integer size) {
        Response<QuotationDetailDto> result = new Response<QuotationDetailDto>();

        try{
            Map<String , Object> params = Maps.newHashMap();
            params.put("queryType" , queryType == null ? 3 : queryType);
            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? 10 : size);
            params.putAll(pageInfo.toMap());

            //获取正选的排名
            List<RequirementRank> rankList = requirementRankDao.findAllRanks(requirementId , RequirementRank.Type.OFFICIAL.value());

            List<Long> solutionIds = Lists.newArrayList();
            RequirementSolution solution;
            for(RequirementRank requirementRank : rankList){
                //获取用户方案
                solution = requirementSolutionDao.findByRequirementId(requirementId , requirementRank.getSupplierId());
                solutionIds.add(solution.getId());
            }

            //获取分页的报价数据
            if(solutionIds.isEmpty()){
                result.setResult(new QuotationDetailDto());
            }else{
                Requirement requirement = requirementDao.findById(requirementId);

                Paging<ModuleQuotation> quotationPaging = moduleQuotationDao.findByTransact(solutionIds , params);
                QuotationDetailDto quotationDetailDto = new QuotationDetailDto();
                quotationDetailDto.setRequirement(requirement);
                quotationDetailDto.setRequirementSend(requirementSendDao.findByRequirementId(requirementId));

                List<TransactInfoDto> transactInfoDtoList = Lists.newArrayList();
                TransactInfoDto transactInfoDto;
                for(ModuleQuotation moduleQuotation : quotationPaging.getData()){
                    transactInfoDto = new TransactInfoDto();

                    //写入报价
                    transactInfoDto.setModuleQuotation(moduleQuotation);
                    //写入每个模块的工厂配额
                    transactInfoDto.setModuleQuotaList(moduleQuotaDao.findQuotaBySolutionId(moduleQuotation.getModuleId(), moduleQuotation.getSupplierId()));
                    transactInfoDtoList.add(transactInfoDto);
                }
                quotationDetailDto.setTransactPaging(new Paging<TransactInfoDto>(quotationPaging.getTotal() , transactInfoDtoList));

                result.setResult(quotationDetailDto);
            }
        }catch(Exception e){
            log.error("find module quotation info failed, when transact, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quotation.find.failed");
        }

        return result;
    }

    @Override
    public Response<QuotationDetailDto> findTransactQuota(BaseUser user, Long requirementId, Integer status, Integer pageNo, Integer size) {
        Response<QuotationDetailDto> result = new Response<QuotationDetailDto>();

        //验证用户是否已登入
        if(user == null){
            log.error("create requirement user can't be null.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("find supplier quota need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        try{
            Map<String , Object> params = Maps.newHashMap();
            params.put("queryType" , status == null ? 3 : status);
            //默认显示10条数据
            PageInfo pageInfo = new PageInfo(pageNo , size == null ? 10 : size);
            params.putAll(pageInfo.toMap());

            RequirementSolution solution = requirementSolutionDao.findByUserId(requirementId , user.getId());
            //获取分页的报价数据
            if(solution == null){
                result.setResult(new QuotationDetailDto());
            }else{

                Requirement requirement = requirementDao.findById(requirementId);

                Paging<ModuleQuotation> quotationPaging = moduleQuotationDao.findByTransact(Lists.newArrayList(solution.getId()) , params);
                QuotationDetailDto quotationDetailDto = new QuotationDetailDto();
                quotationDetailDto.setRequirement(requirement);
                quotationDetailDto.setRequirementSend(requirementSendDao.findByRequirementId(requirementId));

                List<TransactInfoDto> transactInfoDtoList = Lists.newArrayList();
                TransactInfoDto transactInfoDto;
                for(ModuleQuotation moduleQuotation : quotationPaging.getData()){
                    transactInfoDto = new TransactInfoDto();

                    //写入报价&谈判报价
                    transactInfoDto.setModuleQuotation(moduleQuotation);
                    //写入每个模块的工厂配额
                    transactInfoDto.setModuleQuotaList(moduleQuotaDao.findQuotaBySolutionId(moduleQuotation.getModuleId(), moduleQuotation.getSupplierId()));
                    transactInfoDtoList.add(transactInfoDto);
                }
                quotationDetailDto.setTransactPaging(new Paging<TransactInfoDto>(quotationPaging.getTotal() , transactInfoDtoList));

                result.setResult(quotationDetailDto);
            }
        }catch(Exception e){
            log.error("find module quotation info failed, when transact, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("quotation.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> updateTransactPrice(ModuleQuotation moduleQuotation) {
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
    public Response<Boolean> updateBatchTransactPrice(String moduleQuotations) {
        Response<Boolean> result = new Response<Boolean>();

        try{
            List<ModuleQuotation> moduleQuotationList = JSON_MAPPER.fromJson(moduleQuotations , JSON_MAPPER.createCollectionType(List.class , ModuleQuotation.class));

            //批量更新模块谈判报价方案
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
    public Response<Boolean> uploadNegotiateFile(Long requirementId, String negotiateFile, BaseUser user) {
        Response<Boolean> result = new Response<Boolean>();

        //验证用户是否已登入
        if(user == null){
            log.error("upload negotiate file, user must login.");
            result.setError("user.not.login");
            return result;
        }

        if(requirementId == null){
            log.error("upload negotiate file, need requirement id.");
            result.setError("requirement.id.null");
            return result;
        }

        if(Strings.isNullOrEmpty(negotiateFile)) {
            log.error("upload negotiate file, need negotiate file.");
            result.setError("requirement.negotiateFile.null");
            return result;
        }

        try{
            Requirement requirement = requirementDao.findById(requirementId);
            if(!Objects.equal(requirement.getCreatorId() , user.getId())){
                log.error("upload negotiate file, user must creator");
                result.setError("requirement.creatorId.noPower");
                return result;
            }

            RequirementSend requirementSend = requirementSendDao.findByRequirementId(requirementId);
            //判断是否时谈判阶段
            if(!Objects.equal(RequirementSend.Type.from(requirementSend.getBusinessNegotiate()) , RequirementSend.Type.COMMIT)){
                log.error("requirement status can't allow user to upload negotiate file");
                result.setError("requirement.status.error");
                return result;
            }

            RequirementSend newSend = new RequirementSend();
            newSend.setId(requirementSend.getId());
            newSend.setNegotiateFile(negotiateFile);

            result.setResult(requirementSendDao.update(newSend));
        }catch(Exception e){
            log.error("upload negotiate file filed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
            result.setError("requirement.negotiateFile.upFiled");
        }

        return result;
    }

    @Override
    public Response<Boolean> uploadSolEndFile (Long requirementId, String transactFile, BaseUser user){
        Response<Boolean> result = new Response<Boolean>();

        if (user == null) {
            log.error("update requirement transact file need user login.");
            result.setError("user.not.login");
            return result;
        }

        if (Strings.isNullOrEmpty(transactFile)) {
            log.error("update requirement transition file can't be null.");
            result.setError("requirement.transactFile.null");
            return result;
        }

        if (requirementId == null) {
            log.error("transition requirement status need requirementId.");
            result.setError("requirement.id.null");
            return result;
        }

        try {
            Requirement requirement = requirementDao.findById(requirementId);

            if (Objects.equal(Requirement.TransactType.from(requirement.getTransactType()), Requirement.TransactType.TRANSACT)) {
                Requirement newReq = new Requirement();
                newReq.setId(requirementId);
                newReq.setTransactFile(transactFile);

                requirementDao.update(newReq);
            } else {
                log.error("the requirement status is not transact status.");
                result.setError("transactFile.status.failed");
                return result;
            }

            result.setResult(true);
        } catch (Exception e) {
            log.error("update requirement transact file failed, error code={}", Throwables.getStackTraceAsString(e));
            result.setError("transactFile.update.failed");
        }

        return result;
    }
}
