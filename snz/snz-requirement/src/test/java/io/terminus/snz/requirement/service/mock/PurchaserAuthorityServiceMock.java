package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.user.service.PurchaserAuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-15.
 */
@Service
public class PurchaserAuthorityServiceMock implements PurchaserAuthorityService {
    @Override
    public Response<Boolean> grantAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        return null;
    }

    @Override
    public Response<Boolean> revokeAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        return null;
    }

    @Override
    public Response<List<Long>> getAuthorizedBcIds(Long userId, Integer position, String role) {
        return null;
    }

    @Override
    public Response<List<Long>> getUserIdsHavingAuthInBcId(Long bcId, Integer position, String role) {
        return null;
    }

    @Override
    public Response<List<Long>> getUserIdsHavingAuthInBcIds(List<Long> bcIds, Integer position, String role) {
        return null;
    }

    @Override
    public Response<Boolean> checkAuthInBcId(Long userId, Long bcId, Integer position, String role) {
        return null;
    }

    @Override
    public Response<Boolean> checkAuthInAnyBcIds(Long userId, List<Long> bcIds, Integer position, String role) {
        return null;
    }

    @Override
    public Response<Boolean> checkAuthInAllBcIds(Long userId, List<Long> bcIds, Integer position, String role) {
        return null;
    }
}
