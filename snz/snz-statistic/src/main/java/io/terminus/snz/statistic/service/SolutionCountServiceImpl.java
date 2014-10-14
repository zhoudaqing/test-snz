package io.terminus.snz.statistic.service;

import com.google.common.base.Throwables;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.dao.SolutionCountDao;
import io.terminus.snz.statistic.dao.SupplierSolutionCountDao;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:供应商方案信息统计
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-24.
 */
@Slf4j
@Service
public class SolutionCountServiceImpl implements SolutionCountService {

    @Autowired
    private SupplierSolutionCountDao supplierSolutionCountDao;

    @Autowired
    private SolutionCountDao solutionCountDao;

    @Override
    public Boolean setSupCount(SupplierSolutionCount solutionCount) {
        Boolean setRes = false;

        if(solutionCount.getUserId() == null){
            log.error("set supplier count info need supplierId");
            return false;
        }

        try{
            supplierSolutionCountDao.setSupCount(solutionCount);
            setRes = true;
        }catch(Exception e){
            log.error("set supplier count info failed, supplierId={}, error code={}", solutionCount.getUserId(), Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Boolean updateBatchCounts(List<Long> userIds, Integer oldStatus, Integer newStatus) {
        Boolean setRes = false;

        if(userIds == null){
            log.error("update supplier count info need supplierId");
            return false;
        }

        try{
            supplierSolutionCountDao.updateBatchCounts(userIds, oldStatus, newStatus);
            setRes = true;
        }catch(Exception e){
            log.error("set supplier count info failed, error code={}", Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Response<SupplierSolutionCount> findSupplierCount(BaseUser user) {
        Response<SupplierSolutionCount> result = new Response<SupplierSolutionCount>();

        //用户是否登入
        if(user == null){
            log.error("find supplier requirement count info need userId.");
            result.setError("user.not.login");
            return result;
        }

        try{
            result.setResult(supplierSolutionCountDao.findSupplierCount(user.getId()));
        }catch(Exception e){
            log.error("find purchaser requirement count info failed, userId={}, error code={}", user.getId(), Throwables.getStackTraceAsString(e));
            result.setError("reqCount.find.failed");
        }

        return result;
    }

    @Override
    public Boolean setSolCountInfo(Long userId, SolutionCountType countType, Integer addCount) {
        Boolean setRes = false;

        if(userId == null){
            log.error("set supplier solution count info need userId");
            return false;
        }

        try{
            solutionCountDao.setSolCount(userId , countType, addCount);
            setRes = true;
        }catch(Exception e){
            log.error("set supplier solution count info failed, userId={}, error code={}", userId, Throwables.getStackTraceAsString(e));
        }

        return setRes;
    }

    @Override
    public Map<SolutionCountType, Integer> findSupSolCount(Long userId, SolutionCountType... countTypes) {
        Map<SolutionCountType, Integer> countValues = null;

        if(userId == null){
            log.error("find solution count info need userId");
            return null;
        }

        if(countTypes == null || countTypes.length < 1){
            log.error("find supplier solution count info need countType");
            return null;
        }

        try{
            String[] typeArray = new String[countTypes.length];
            for(int i=0; i<countTypes.length; i++){
                typeArray[i] = countTypes[i].value();
            }

            countValues = solutionCountDao.findSolCount(userId, typeArray);
        }catch(Exception e){
            log.error("find supplier solution count info failed, userId={}, error code={}", userId, Throwables.getStackTraceAsString(e));
        }

        return countValues;
    }
}
