package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementCountType;
import io.terminus.snz.statistic.service.RequirementCountService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Desc:mock对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class RequirementCountServiceMock implements RequirementCountService {
    @Override
    public Boolean setReqCountInfo(Long requirementId, RequirementCountType countType, Integer addCount) {
        return null;
    }

    @Override
    public Boolean incTopicSuppliers(Long requirementId, Long supplierId) {
        return null;
    }

    @Override
    public Map<RequirementCountType, Integer> findReqCount(Long requirementId, RequirementCountType... countTypes) {
        return null;
    }

    @Override
    public Boolean deleteReqCount(Long requirementId) {
        return null;
    }

    @Override
    public Boolean setPurchaserReqCount(PurchaserRequirementCount requirementCount) {
        return null;
    }

    @Override
    public Boolean updatePurchaserReqCount(Long userId, Integer oldStatus, Integer newStatus) {
        return null;
    }

    @Override
    public Response<PurchaserRequirementCount> findPurchaserReqCount(BaseUser user) {
        return null;
    }
}
