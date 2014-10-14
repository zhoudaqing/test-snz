package io.terminus.snz.requirement.service;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dao.OldModuleDao;
import io.terminus.snz.requirement.dao.RequirementDao;
import io.terminus.snz.requirement.model.OldModule;
import io.terminus.snz.requirement.model.Requirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by jiaoyuan on 14-7-10.
 */
@Slf4j
@Service
public class OldModuleServiceImpl implements OldModuleService{

    @Autowired
    private OldModuleDao oldModuleDao;
    @Autowired
    private RequirementDao requirementDao;

    @Override
    public Response<Paging<OldModule>> findByFilters(String moduleNum, Integer timeTotalStart, Integer timeTotalEnd, String seriesName) {
        Response<Paging<OldModule>> paging = new Response<Paging<OldModule>>();
        Map<String,Object> params = Maps.newHashMap();
        params.put("moduleNum",moduleNum);
        params.put("timeTotalStart",timeTotalStart);
        params.put("timeTotalEnd",timeTotalEnd);
        params.put("seriesName",seriesName);
        try {
            paging.setResult(oldModuleDao.findByFilters(params));
        }catch(Exception e){
            log.error("find oldModule failed,moduleNum={},timeTotalStart={},timeTotalEnd={},seriesName={}, error code={}",moduleNum,timeTotalStart,timeTotalEnd,seriesName,
                    Throwables.getStackTraceAsString(e));
            paging.setError("oldModule.find.failed");
        }
        return paging;
    }

    @Override
    public Response<OldModule> findByModuleNum(String moduleNum) {
        Response<OldModule> result = new Response<OldModule>();
        if(moduleNum==null){
            log.error("find OldModule  need moduleNum");
            result.setError("oldModule.moduleNum.null");
            return result;
        }
        try {
            result.setResult(oldModuleDao.findByModuleNum(moduleNum));
        }catch (Exception e){
            log.error("find OldModule failed,moduleNum={},error code={}",moduleNum,Throwables.getStackTraceAsString(e));
            result.setError("oldModule.find.failed");
        }

        return result;
    }

    @Override
    public Response<Boolean> delete(Long purchaserId) {
        Response<Boolean> result = new Response<Boolean>();
        if(purchaserId==null){
            log.error("delete OldModule need purchaserId");
            result.setError("oldModule.purchaserId.null");
        }
        try{
            result.setResult(oldModuleDao.delete(purchaserId));
        }catch (Exception e){
            log.error("delete OldModule failed ,purchaserId={},error code={}",purchaserId,Throwables.getStackTraceAsString(e));
            result.setError("oldModule.delete.failed");
        }

        return result;
    }

    @Override
    public Response<OldModule> findById(Long purchaserId) {
        Response<OldModule> result = new Response<OldModule>();
        if(purchaserId==null){
            log.error("find OldModule need purchaserId");
            result.setError("oldModule.purchaserId.null");
        }
        try {
            result.setResult(oldModuleDao.findById(purchaserId));
        }catch (Exception e){
            log.error("find OldModule failed,purchaserId={},error code={}",purchaserId,Throwables.getStackTraceAsString(e));
            result.setError("oldModule.find.failed");
        }
        return result;
    }

    @Override
    public Response<Boolean> update(OldModule oldModule) {
        Response<Boolean> result = new Response<Boolean>();
        if(oldModule==null){
            log.error("update OldModule need purchaserId ");
            result.setError("oldModule.purchaserId.null");
            return result;
        }
        try {
            result.setResult(oldModuleDao.update(oldModule));
        }catch (Exception e){
            log.error("update OldModule failed,oldModule={},error code={}",oldModule,Throwables.getStackTraceAsString(e));
            result.setError("oldModule.update.failed");
        }
        return result;
    }

    @Override
    public Response<Boolean> create(OldModule oldModule) {
        Response<Boolean> result = new Response<Boolean>();
        //招标编码/方案编码
        if(oldModule.getRequirementId() == null){
            log.error("create oldModule need requirementId");
            result.setError("oldModule.requirementId.null");
            return result;
        }
        //物料号
        if(Strings.isNullOrEmpty(oldModule.getModuleNum())){
            log.error("create oldModule need moduleNum");
            result.setError("oldModule.moduleNum.null");
            return result;
        }
        //PLM状态
        if(oldModule.getPrioritySelect() == null){
            log.error("create oldModule need prioritySelect");
            result.setError("oldModule.prioritySelect.null");
            return result;
        }
        //物料描述
        if(Strings.isNullOrEmpty(oldModule.getModuleName())){
            log.error("create oldModule need moduleName");
            result.setError("oldModule.moduleName.null");
            return result;
        }
        //价格
        if(oldModule.getCost() == null){
            log.error("create oldModule need cost");
            result.setError("oldModule.cost.null");
            return result;
        }
        //工厂
        if(Strings.isNullOrEmpty(oldModule.getResourceNum())){
            log.error("create oldModule need resourceNum");
            result.setError("oldModule.resourceNum.null");
            return result;
        }
        //全部资源量
        if(oldModule.getTotal() == null){
            log.error("create oldModule need total");
            result.setError("oldModule.total.null");
            return result;
        }
        //系列编号
        if(oldModule.getSeriesId() == null){
            log.error("create oldModule need seriesId");
            result.setError("oldModule.seriesId.null");
            return result;
        }
        //系列名称
        if(Strings.isNullOrEmpty(oldModule.getSeriesName())){
            log.error("create oldModule need seriesName");
            result.setError("oldModule.seriesName.null");
            return result;
        }
        //批量供货时间
        if(oldModule.getSupplyAt() == null){
            log.error("create oldModule need supplyAt");
            result.setError("oldModule.supplyAt.null");
            return result;
        }
        try {
            Requirement requirement = requirementDao.findById(oldModule.getRequirementId());
            if(requirement == null){
                log.error("requirement not exist. requirementId={}",oldModule.getRequirementId());
                result.setError("requirement.not.exist");
                return result;
            }
            if(requirement.getStatus() != null && requirement.getStatus() > 2){
                //当方案已锁定以后将无法更改需求内容(或是被删除状态都不能更改状态)
                log.error("requirement have be locked or delete, can't to create module info. requirementId={}",
                        oldModule.getRequirementId());
                result.setError("requirement.lock.existed");
                return result;
            }
            oldModule.setHeadDrop(requirement.getHeadDrop());
            oldModule.setPurchaserId(requirement.getPurchaserId());
            oldModule.setPurchaserName(requirement.getPurchaserName());
            oldModule.setRequirementName(requirement.getName());
            oldModule.setTacticsId(requirement.getTacticsId());
            result.setResult(oldModuleDao.create(oldModule) != null);
        }catch (Exception e){
            log.error("create oldModule failed,moduleNum={},moduleName={},prioritySelect={},cost={},requirementId={}",oldModule.getModuleNum(),oldModule.getModuleName(),
                    oldModule.getPrioritySelect(),oldModule.getCost(),oldModule.getRequirementId());
            result.setError("oldModule.create.failed");
        }
        return result;
    }
}
