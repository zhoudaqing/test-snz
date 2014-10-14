package io.terminus.snz.statistic.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.dao.PurchaserRequirementCountDao;
import io.terminus.snz.statistic.dao.RequirementCountDao;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementCountType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Desc:需求统计信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-24.
 */
@Slf4j
@Service
public class RequirementCountServiceImpl implements RequirementCountService {
    @Autowired
    private RequirementCountDao requirementCountDao;

    @Autowired
    private PurchaserRequirementCountDao purchaserRequirementCountDao;

    @Override
    public Boolean setReqCountInfo(Long requirementId , RequirementCountType countType, Integer addCount) {
        Boolean setRes = false;

        if(requirementId == null){
            log.error("set requirement count info need requirementId");
            return false;
        }

        try{
            requirementCountDao.setReqCount(requirementId , countType, addCount);
            setRes = true;
        }catch(Exception e){
            log.error("set requirement count info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Boolean incTopicSuppliers(Long requirementId, Long supplierId) {
        Boolean setRes = false;

        if(requirementId == null){
            log.error("set requirement topic count info need requirementId");
            return false;
        }

        if(supplierId == null){
            log.error("set requirement topic count info need supplierId");
            return false;
        }

        try{
            requirementCountDao.incTopicSuppliers(requirementId , supplierId);
            setRes = true;
        }catch(Exception e){
            log.error("set requirement topic count info failed, requirementId={}, supplierId, error code={}", requirementId, supplierId, Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Map<RequirementCountType , Integer> findReqCount(Long requirementId, RequirementCountType... countTypes) {
        Map<RequirementCountType , Integer> countValues = null;

        if(requirementId == null){
            log.error("find requirement count info need requirementId");
            return null;
        }

        if(countTypes == null || countTypes.length < 1){
            log.error("find requirement count info need countType");
            return null;
        }

        try{
            String[] typeArray = new String[countTypes.length];
            for(int i=0; i<countTypes.length; i++){
                typeArray[i] = countTypes[i].value();
            }

            countValues = requirementCountDao.findReqCount(requirementId, typeArray);
        }catch(Exception e){
            log.error("find requirement count info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
        }

        return countValues;
    }

    @Override
    public Boolean deleteReqCount(Long requirementId) {
        Boolean setRes = false;

        if(requirementId == null){
            log.error("delete requirement count info need requirementId");
            return false;
        }

        try{
            //删除需求统计数据
            requirementCountDao.deleteReqCount(requirementId);

            //删除需求的话题统计数据
            requirementCountDao.deleteTopicCount(requirementId);
            setRes = true;
        }catch(Exception e){
            log.error("delete requirement count info failed, requirementId={}, error code={}", requirementId, Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Boolean setPurchaserReqCount(PurchaserRequirementCount requirementCount) {
        Boolean setRes = false;

        if(requirementCount.getUserId() == null){
            log.error("create purchaser requirement count info need userId.");
            return false;
        }

        try{
            //写入采购的需求统计数据
            purchaserRequirementCountDao.setPurCount(requirementCount);
            setRes = true;
        }catch(Exception e){
            log.error("create purchaser requirement count info failed, userId={}, error code={}", requirementCount.getUserId(), Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Boolean updatePurchaserReqCount(Long userId, Integer oldStatus, Integer newStatus) {
        Boolean setRes = false;

        if(userId == null){
            log.error("update purchaser requirement count info need userId.");
            return false;
        }

        try{
            //更新采购的需求统计数据
            purchaserRequirementCountDao.updateReqCount(userId , oldStatus, newStatus);
            setRes = true;
        }catch(Exception e){
            log.error("update purchaser requirement count info failed, userId={}, error code={}", userId, Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Response<PurchaserRequirementCount> findPurchaserReqCount(BaseUser user) {
        Response<PurchaserRequirementCount> result = new Response<PurchaserRequirementCount>();

        //用户是否登入
        if(user == null){
            log.error("find purchaser requirement count info need userId.");
            result.setError("user.not.login");
            return result;
        }

        try{
            result.setResult(purchaserRequirementCountDao.findReqCount(user.getId()));
        }catch(Exception e){
            log.error("find purchaser requirement count info failed, userId={}, error code={}", user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("reqCount.find.failed");
        }

        return result;
    }
}
