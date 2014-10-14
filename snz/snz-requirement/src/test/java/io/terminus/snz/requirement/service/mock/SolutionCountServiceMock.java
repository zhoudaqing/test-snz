package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.model.SupplierSolutionCount;
import io.terminus.snz.statistic.service.SolutionCountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:mock对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class SolutionCountServiceMock implements SolutionCountService {
    @Override
    public Boolean setSupCount(SupplierSolutionCount solutionCount) {
        return null;
    }

    @Override
    public Boolean updateBatchCounts(List<Long> supplierIds, Integer oldStatus, Integer newStatus) {
        return null;
    }

    @Override
    public Response<SupplierSolutionCount> findSupplierCount(BaseUser user) {
        return null;
    }

    @Override
    public Boolean setSolCountInfo(Long userId, SolutionCountType countType, Integer addCount) {
        return null;
    }

    @Override
    public Map<SolutionCountType, Integer> findSupSolCount(Long userId, SolutionCountType... countTypes) {
        return null;
    }

}
