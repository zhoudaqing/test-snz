package io.terminus.snz.user.service;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.services.MessageService;
import io.terminus.snz.user.dao.CompanyDao;
import io.terminus.snz.user.dao.SupplierAppointedDao;
import io.terminus.snz.user.dto.SupplierAppointedDto;
import io.terminus.snz.user.manager.SupplierAppointedManager;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.SupplierAppointed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Author: Grancy Guo
 * Created on 14-9-15.
 */
@Slf4j
@Service
public class SupplierAppointedServiceImpl implements SupplierAppointedService{

    @Autowired
    private SupplierAppointedDao supplierAppointedDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private MessageService messageService;                  //消息推送处理

    @Autowired
    private SupplierResourceMaterialService supplierResourceMaterialService;    //供应商资质处理

    @Autowired
    private SupplierAppointedManager supplierAppointedManager;    //处理甲指供应商数据

    /**
     * 根据条件查询甲指库信息
     *
     * @param status       甲指供应商审核状态
     * @param supplierName 甲指供应商模糊搜索名称
     * @param pageNo       当前分页
     * @param size         分页大小
     * @return Paging<SupplierAppointedDto> 符合条件的甲指列表
     */
    @Override
    public Response<Paging<SupplierAppointedDto>> findbyParams(Integer status, String supplierName, Integer pageNo, Integer size) {
        Response<Paging<SupplierAppointedDto>> resp = new Response<Paging<SupplierAppointedDto>>();
        try {
            HashMap<String, Object> params = Maps.newHashMap();
            Paging<SupplierAppointedDto> paging = new Paging<SupplierAppointedDto>();
            PageInfo pageInfo = new PageInfo(pageNo, Objects.firstNonNull(size, 10));
            params.put("offset", pageInfo.getOffset());
            params.put("limit", pageInfo.getLimit());
            params.put("status", status);
            params.put("supplierName", supplierName);

            //查询记录总数
            Long total = supplierAppointedDao.countSupplierAppointed(params);
            if(total == 0){
                paging = new Paging<SupplierAppointedDto>(0L, Collections.<SupplierAppointedDto>emptyList());
                resp.setResult(paging);
                return resp;
            }

            //定义页面显示数据
            List<SupplierAppointedDto> supplierAppointedDtoList = Lists.newArrayList();

            //组装页面显示数据
            List<SupplierAppointed> supplierAppointedList = supplierAppointedDao.findSupplierAppiontedByParams(params);
            for(SupplierAppointed supplierAppointed : supplierAppointedList){
                Long company_id = supplierAppointed.getCompanyId();
                Company company = new Company();
                //如果公司id为空则说明供应商没有注册
                if(company_id != null){
                    company = companyDao.findById(company_id);
                }
                SupplierAppointedDto supplierAppointedDto = new SupplierAppointedDto();
                supplierAppointedDto.setSupplierAppointed(supplierAppointed);
                supplierAppointedDto.setCompany(company);
                supplierAppointedDtoList.add(supplierAppointedDto);
            }

            //回填至分页组件
            paging.setTotal(total);
            paging.setData(supplierAppointedDtoList);
            resp.setResult(paging);
        }catch (Exception e){
            log.error("error to find SupplierAppointed with status={}, supplierName={}", status, supplierName, Throwables.getStackTraceAsString(e));
            resp.setError("supplier.appointed.find.failed");
        }

        return resp;
    }

    /**
     * 终审通过之前，需要检验该甲指供应商是否在系统已经入围
     * @param supplierId 甲指供应商id
     * @return true or false 通过校验，已经入围，为true
     */
    private Boolean checkSuppier(Long supplierId){
        Boolean result = true;
        //在注册之前，被甲方指定进入甲指库的供应商，注册进入系统后需要将公司id写入甲指库companyId
        //通过校验该字段是否为空做判断
        Long companyId = (supplierAppointedDao.findCompanyById(supplierId)).getCompanyId();
        if(companyId == null){
            result = false;
        }
        return result;
    }

    /**
     * 初审或终审确认通过甲指库供应商
     * 终审通过之前，需要校验该供应商是否入驻
     * 如果供应商已经入驻，审核通过后，赋予该供应商全部需求权
     *
     * @param jiaZhiId 甲指供应商id
     * @param flag     初审为0 终审为1
     * @return true or false
     */
    @Override
    public Response<Boolean> confirmJiaZhi(BaseUser baseUser, Long jiaZhiId, Integer flag) {
        Response<Boolean> resp = new Response<Boolean>();
        //参数不能为空
        if(jiaZhiId == null ){
            log.error("confirmJiaZhi need jiaZhiId, jiaZhiId is empty.");
            resp.setError("supplier.id.empty");
            return resp;
        }
        if(flag == null ){
            log.error("confirmJiaZhi need flag, flag is empty.");
            resp.setError("supplier.id.empty");
            return resp;
        }

        try {
            Map<String, Object> params = Maps.newHashMap();
            params.put("id", jiaZhiId);
            params.put("advice", "");
            Integer status = 0;
            if(flag == 0){
                //初审通过的操作
                status = SupplierAppointed.Status.FIRSTPASS.value();
                params.put("status", status);
                supplierAppointedDao.updateStatus(params);
            }else if(flag == 1){
                //终审通过的操作
                //终审通过之前，需要检验该甲指供应商是否在系统已经入围
                if(! checkSuppier(jiaZhiId)){
                    resp.setError("supplier.check.failed");
                    return resp;
                }
                //终审操作
                status = SupplierAppointed.Status.LASTPASS.value();
                params.put("status", status);
                supplierAppointedDao.updateStatus(params);
                //终审通过后，该指定甲指供应商 的 指定类目资质强行通过
                SupplierAppointed supplierAppointed = supplierAppointedDao.findCompanyById(jiaZhiId);
                //获取供应商id
                Long companyId = supplierAppointed.getCompanyId();
                //获取三级类目
                String seriesIds = supplierAppointed.getSeriesIds();
                //如果三级类目不为空则 强制通过相应资质
                if(!Strings.isNullOrEmpty(seriesIds)){
                    List<BackendCategoryProperty> jsonList = JsonMapper.nonDefaultMapper().fromJson(seriesIds, JsonMapper.nonDefaultMapper().createCollectionType(List.class, BackendCategoryProperty.class));
                    //转换为idList
                    List<Long> seriesIdsList =  Lists.transform(jsonList, new Function<BackendCategoryProperty, Long>() {
                        @Override
                        public Long apply(BackendCategoryProperty catgy) {
                            return catgy.getBcId();
                        }
                    });
                    //调用接口通过相应的资质
                    supplierResourceMaterialService.forceApprove(baseUser.getId(),companyId, seriesIdsList);
                }
            }
        }catch (Exception e){
            log.error("rejectJiaZhi failed with jiazhiId={}",jiaZhiId, Throwables.getStackTraceAsString(e));
            resp.setError("approve.supplier.fail");
            return resp;
        }
        resp.setResult(true);
        return resp;
    }

    /**
     * 初审不通过或终审不通过甲指库供应商，并推送消息至相关需求发布者
     *
     * @param jiaZhiId 甲指供应商id
     * @param flag     初审不通过为0 终审不通过为1
     * @param advice   不通过原因
     * @return true or false
     */
    @Override
    public Response<Boolean> rejectJiaZhi(BaseUser baseUser, Long jiaZhiId, Integer flag, String advice) {
        Response<Boolean> resp = new Response<Boolean>();
        //参数不能为空
        if(jiaZhiId == null ){
            log.error("rejectJiaZhi need jiaZhiId, jiaZhiId is empty.");
            resp.setError("supplier.id.empty");
            return resp;
        }
        if(flag == null ){
            log.error("rejectJiaZhi need flag, flag is empty.");
            resp.setError("supplier.id.empty");
            return resp;
        }

        try{
            Map<String, Object> params = Maps.newHashMap();
            params.put("id", jiaZhiId);
            params.put("advice", advice);
            Integer status = 0;
            //flag为0则不通过初审
            if(flag == 0){
                status = SupplierAppointed.Status.FIRSTNOTPASS.value();
                params.put("status", status);
                supplierAppointedDao.updateStatus(params);
            }else if(flag == 1){
                //flag为1则不通过终审
                status = SupplierAppointed.Status.LASTNOTPASS.value();
                params.put("status",status);
                supplierAppointedDao.updateStatus(params);
            }
            //推送消息至相关利共体研发告知发布的甲指供应商审核不通过
            //查询利共体id
            Long publish_id = (supplierAppointedDao.findCompanyById(jiaZhiId)).getCreatorId();
            messageService.push(Message.Type.SUPPLIER_APPOINTED_UNPASS, baseUser.getId(), publish_id,
                                ImmutableMap.of("approver",baseUser.getName(), "advice", advice, "mobile", baseUser.getMobile()));

        }catch (Exception e){
            log.error("rejectJiaZhi failed with jiazhiId={}",jiaZhiId, Throwables.getStackTraceAsString(e));
            resp.setError("approve.supplier.fail");
            return resp;
        }
        resp.setResult(true);
        return resp;
    }

    /**
     * 根据传入的Model SupplierAppointed，根据corporation检查甲指库，
     * 如果已存在则不操作，如果未存在则插入进甲指库
     *
     * @param supplierAppointed 甲指供应商model
     * @return true or false
     */
    @Override
    public Response<Boolean> batchSupplierAppointed(SupplierAppointed supplierAppointed) {
        Response<Boolean> result = new Response<Boolean>();
        try{
            if(supplierAppointed == null){
                log.error("supplierAppointed cannot be null");
                result.setError("batchSupplierAppointed.failed");
                return result;
            }
            result.setResult(supplierAppointedManager.batchSupplierAppointed(supplierAppointed));
        }catch (Exception e){
            log.error("fail to batchSupplierAppointed", Throwables.getStackTraceAsString(e));
            result.setError("fail to batchSupplierAppointed");
            return result;
        }
        return result;
    }

    /**
     * 甲指库供应商可能存在这种情况，甲方强行指定的供应商，该供应商并未在我们系统注册
     * 这个供应商在甲指需求发布的时候已经被指定进入系统甲指库
     * 以后这个供应商入驻我们系统，注册结束后，需要调用本方法将公司id写入系统甲指库
     *
     * @param companyId   供应商公司id
     * @param corporation 供应商公司全名
     * @return true or false
     */
    @Override
    public Response<Boolean> insertSupplierAppointedId(Long companyId, String corporation) {
        Response<Boolean> result = new Response<Boolean>();
        try{
            SupplierAppointed check = supplierAppointedDao.findCompanyByCorp(corporation, null);
            //判断甲指库是否存在这个供应商
            if(check == null){
                //如果甲指库不存在这个供应商，则甲指库还不需要这个供应商id插入
                result.setResult(true);
                return result;
            }else {
                //如果存在，那么插入companId
                supplierAppointedDao.updateCompanyIdByCorp(companyId, corporation);
            }
        }catch (Exception e){
            log.error("fail to insertSupplierAppointedId", Throwables.getStackTraceAsString(e));
            result.setError("SupplierAppointedId.insert.fail");
        }
        result.setResult(true);
        return result;
    }

    /**
     * 根据id查询甲指供应商信息
     *
     * @param requirementId 甲指供应商id
     * @return
     */
    @Override
    public Response<SupplierAppointed> findbyId(Long requirementId) {
        Response<SupplierAppointed> result = new Response<SupplierAppointed>();
        try {
            SupplierAppointed supplierAppointed = supplierAppointedDao.findCompany(requirementId);
            result.setResult(supplierAppointed);
        }catch (Exception e){
            log.error("fail to find SupplierAppointed byId with requirementId={}", requirementId, Throwables.getStackTraceAsString(e));
            e.printStackTrace();
            result.setError("supplier.findbyId.failed");
            return result;
        }
        return result;
    }
}
