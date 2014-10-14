package io.terminus.snz.related.services;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.daos.CompensationDao;
import io.terminus.snz.related.daos.CompensationDetailDao;
import io.terminus.snz.related.daos.CompensationReplyDao;
import io.terminus.snz.related.dto.ArchFileDto;
import io.terminus.snz.related.dto.DetailAndReply;
import io.terminus.snz.related.models.Compensation;
import io.terminus.snz.related.models.CompensationDetail;
import io.terminus.snz.related.models.CompensationReply;
import io.terminus.snz.related.models.FactoryProductionDirector;
import io.terminus.snz.user.dto.CompanyPerformance;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.CompanyMainBusiness;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.CompanyService;
import io.terminus.snz.user.service.SupplierTQRDCInfoTmpService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 索赔信息服务实现
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-10
 */
@Service @Slf4j
public class CompensationServiceImpl implements CompensationService {

    @Autowired
    private CompensationDao compensationDao;

    @Autowired
    private CompensationDetailDao compensationDetailDao;

    @Autowired
    private CompensationReplyDao compensationReplyDao;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FactoryProductionDirectorService factoryProductionDirectorService;

    @Autowired
    private SupplierTQRDCInfoTmpService supplierTQRDCInfoTmpService;

    @Override
    public Response<Paging<Compensation>> paging(BaseUser user, Integer pageNo, Integer pageSize, Compensation criteria, String deductedStartAt, String deductedEndAt) {
        Response<Paging<Compensation>> resp = new Response<Paging<Compensation>>();
        PageInfo page = new PageInfo(pageNo, pageSize);
        try{
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            Paging<Compensation> pagingResult = null;
            if (User.is(user, User.Type.SUPPLIER)) {
                //供应商只能查看到自己相关的索赔列表
                pagingResult = pagingForSupplier(user, page, criteria, deductedStartAt, deductedEndAt);
            } else if (User.is(user, User.Type.PURCHASER)){
                //采购商可以看见自己所管理的工厂下索赔信息
                pagingResult = pagingForPurchaser(user, page, criteria, deductedStartAt, deductedEndAt);
            }
            resp.setResult(pagingResult);
        } catch (Exception e){
            log.error("failed to paging compensation(pageNo={}, pageSize={}, criteria={}), cause: {}",
                    pageNo, pageSize, criteria, Throwables.getStackTraceAsString(e));
            resp.setError("compensation.paging.fail");
        }
        return resp;
    }

    /**
     * 供应商查询索赔列表信息
     */
    private Paging<Compensation> pagingForSupplier(BaseUser user, PageInfo page, Compensation criteria, String deductedStartAt, String deductedEndAt) {
        Company companyResp = companyService.findCompanyByUserId(user.getId()).getResult();
        criteria.setSupplierName(companyResp.getCorporation());
        return compensationDao.paging(page.getOffset(), page.getLimit(), criteria, deductedStartAt, deductedEndAt);
    }

    /**
     * 采购商查询索赔列表信息
     */
    private Paging<Compensation> pagingForPurchaser(BaseUser user, PageInfo page, Compensation criteria, String deductedStartAt, String deductedEndAt){
        Response<List<FactoryProductionDirector>> fpdResp = factoryProductionDirectorService.findByUserNick(user.getNickName());
        if (!fpdResp.isSuccess()){
            throw new ServiceException(fpdResp.getError());
        }
        List<FactoryProductionDirector> fpds = fpdResp.getResult();
        if (Iterables.isEmpty(fpds)){
            return new Paging<Compensation>(0L, Collections.<Compensation>emptyList());
        }
        // 该采购商所管理的工厂列表
        List<String> factories = Lists.transform(fpds, new Function<FactoryProductionDirector, String>() {
            @Override
            public String apply(FactoryProductionDirector fpd) {
                return fpd.getFactoryNum();
            }
        });
        return compensationDao.pagingForPurchaser(page.getOffset(), page.getLimit(), criteria, deductedStartAt, deductedEndAt, factories);
    }

    @Override
    public Response<Boolean> batchAdd(List<Compensation> compensations) {
        Response<Boolean> resp = new Response<Boolean>();
        try {
            if (Iterables.isEmpty(compensations)){
                log.error("Compensations can't be empty when add batch.");
                resp.setError("compensation.is.empty");
                return resp;
            }
            Integer added = compensationDao.creates(compensations);
            if (!Objects.equal(added, compensations.size())){
                log.error("some compensations hasn't been add.");
                resp.setError("compensation.batchadd.miss");
                return resp;
            }
            resp.setSuccess(Boolean.TRUE);
        } catch(Exception e){
            log.error("failed to batch add compensations, cause:{}", Throwables.getStackTraceAsString(e));
            resp.setError("compensation.batchadd.fail");
        }
        return resp;
    }

    @Override
    public Response<Boolean> updateStatus(BaseUser user, Long cid, Integer status) {
        Response<Boolean> resp = new Response<Boolean>();
        try{
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            if (cid == null || cid <= 0L){
                log.error("The compensation id is invalid");
                resp.setError("compensation.id.invalid");
                return resp;
            }
            Compensation updated = new Compensation();
            updated.setId(cid);
            updated.setStatus(status);
            compensationDao.update(updated);
            resp.setResult(Boolean.TRUE);
        } catch(Exception e){
            log.error("failed to update compensation(id={}) status(new status={}), cause:{}",
                    cid, status, Throwables.getStackTraceAsString(e));
            resp.setError("compensation.updatestatus.fail");
        }
        return resp;
    }

    @Override
    public Response<DetailAndReply> findDetail(BaseUser user, Long listId, Integer pageNo, Integer pageSize) {
        Response<DetailAndReply> resp = new Response<DetailAndReply>();

        try {
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            if (listId == null || listId <= 0L){
                log.error("The compensation id is invalid");
                resp.setError("compensation.id.invalid");
                return resp;
            }
            DetailAndReply result = new DetailAndReply();
            result.setCompensationDetail(compensationDetailDao.findDetail(listId));
            result.setReplyPaging(compensationReplyDao.allPagingForDid(listId,pageNo,pageSize));
            resp.setResult(result);
        } catch (Exception e){
            log.error("failed to find compensationDetail(id={}), cause:{}",
                    listId, Throwables.getStackTraceAsString(e));
            resp.setError("compensation.findDetail.fail");
        }
        return resp;
    }

    @Override
    public Response<List<CompensationDetail>> findDetailList(BaseUser user, Long listId){
        Response<List<CompensationDetail>> resp = new Response<List<CompensationDetail>>();
        try{
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            if(listId == null || listId <= 0L){
                log.error("The compensation id is invalid");
                resp.setError("compensation.id.invalid");
                return resp;
            }
            // EAI暂没提供id
            //resp.setResult(compensationDetailDao.findDetail(listId));
            // 暂通过: 供应商,物料号,工厂,扣款时间查询
            Compensation compensation = compensationDao.load(listId);
            if (compensation == null){
                log.error("compensation(id={}) isn't existed.", listId);
                resp.setError("compensation.not.exist");
                return resp;
            }
            List<CompensationDetail> details = compensationDetailDao.findByCompensation(compensation);
            resp.setResult(details);
        } catch (Exception e){
            log.error("failed to find compensationDetail(compensationId={}), cause:{}",
                    listId, Throwables.getStackTraceAsString(e));
            resp.setError("compensation.findDetail.fail");
        }
        return resp;
    }

    @Override
    public Response<Paging<CompensationReply>> findReplyPage(BaseUser user, Long listId, Integer pageNo, Integer pageSize){
        Response<Paging<CompensationReply>> resp = new Response<Paging<CompensationReply>>();
        try {
            if (user == null){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            if (listId == null || listId <= 0L){
                log.error("The compensation id is invalid");
                resp.setError("compensation.id.invalid");
                return resp;
            }
            resp.setResult(compensationReplyDao.allPagingForDid(listId, pageNo, pageSize));
        } catch (Exception e){
            log.error("failed to find compensationreply (id={}),cause:{}",
                    listId, Throwables.getStackTraceAsString(e));
            resp.setError("compensation.findreply.fail");
        }
        return resp;
    }

    @Override
    public Response<ArchFileDto> findFileInfo(BaseUser user, Long listId){
        Response<ArchFileDto> resp = new Response<ArchFileDto>();
        ArchFileDto result = new ArchFileDto();
        List<String> directors = new ArrayList<String>();
        Compensation compensation = compensationDao.load(listId);
        try {
            if (user == null ){
                log.error("user isn't login");
                resp.setError("user.not.login");
                return resp;
            }
            //得到生产总监name
            Response<List<FactoryProductionDirector>> Directors = factoryProductionDirectorService.findByFactoryNumAndProductLineName(compensation.getFactory(),compensation.getProductLine());
            List<FactoryProductionDirector> sumDirectors = Directors.getResult();
            for(FactoryProductionDirector midDirector : sumDirectors){
                directors.add(midDirector.getDirectorName());
            }
            result.setDirector(directors);
            //得到主营业务
            if(user.getType()==2){
                CompanyMainBusiness companyMainBusinesses = companyService.findMainBussinessByUserId(user).getResult().get(0);
                if(companyMainBusinesses!=null){
                    result.setCompanyMainBusiness(companyMainBusinesses.getName());
                }
                else {
                    result.setCompanyMainBusiness("null");
                }

            }
            if(user.getType()==3){
                Compensation mid = compensationDao.load(listId);
                Long companyId = Long.parseLong(mid.getFactory());
                Response<List<CompanyMainBusiness>> rm= companyService.findMainBusinessByCompanyId(companyId);
                if(rm!=null&&rm.getResult()!=null&&rm.getResult().size()!=0){
                    CompanyMainBusiness companyMainBusiness = rm.getResult().get(0);
                    if(companyMainBusiness!=null){
                        result.setCompanyMainBusiness(companyMainBusiness.getName());
                    }
                    else{
                        result.setCompanyMainBusiness(null);
                    }
                }
                else {
                    result.setCompanyMainBusiness(null);
                }

            }
            //得到绩效得分
            if(user.getType()==2){
                SupplierTQRDCInfoTmp supplierTQRDCInfoTmp = supplierTQRDCInfoTmpService.findByUserId(user).getResult();
                if(supplierTQRDCInfoTmp!=null){
                    result.setSupplierTQRDCInfoTmp(supplierTQRDCInfoTmp.getCompositeScore());
                }else {
                    result.setSupplierTQRDCInfoTmp(0);
                }
            }
            if(user.getType()==3){
                Compensation midC = compensationDao.load(listId);
                Response<CompanyPerformance> rp = companyService.findDetailSupplierTQRDCInfo(midC.getSupplierAccount());
                if (rp != null) {
                    CompanyPerformance companyPerformance = rp.getResult();
                    if (companyPerformance != null && companyPerformance.getSupplierTQRDCInfo() != null) {
                        result.setSupplierTQRDCInfoTmp(companyPerformance.getSupplierTQRDCInfo().getCompositeScore());
                    } else {
                        result.setSupplierTQRDCInfoTmp(0);
                    }
                } else {
                    result.setSupplierTQRDCInfoTmp(0);
                }
            }

            //获得一个月索赔次数
            Date dateLine = DateTime.now().minusDays(30).toDate();
            List<Compensation> sumOfMonth = compensationDao.getSumOfMonth(dateLine);
            result.setSumOfMonth(sumOfMonth.size());
            //获得一个月索赔金额总和
            Long count;
            long midCount = 0;
            for(Compensation midcompensation : sumOfMonth){
                if(midcompensation.getStatus() == 3){
                    midCount = midCount + Long.parseLong(midcompensation.getMoney());
                }
            }
            count = midCount;
            result.setSumOfLoss(count);
            resp.setResult(result);
        } catch (Exception e){
            log.error("failed to find file info", e);
            resp.setError("fileinfo.find.fail");
        }
        return resp;
    }

    //自动更新列表状态
    public Boolean autoUpdateStatus (DateTime time) {
        try{
            if (time == null){
                log.error("current time is empty");
                return Boolean.FALSE;
            }
            compensationDao.autoUpdateStatus(time);
        }catch (Exception e){
            log.error("failed to autoupdate compensation status");
        }
        return Boolean.TRUE;
    }
}
