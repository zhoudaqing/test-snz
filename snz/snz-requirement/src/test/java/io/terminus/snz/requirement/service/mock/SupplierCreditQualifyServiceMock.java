package io.terminus.snz.requirement.service.mock;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierCreditQualifyDto;
import io.terminus.snz.user.model.SupplierCreditQualify;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.SupplierCreditQualifyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-04.
 */
@Service
public class SupplierCreditQualifyServiceMock implements SupplierCreditQualifyService {
    @Override
    public Response<SupplierCreditQualify> findCreditQualifyByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<List<SupplierCreditQualify>> findCreditQualifyByUserIds(List<Long> userIds) {
        return null;
    }

    @Override
    public Response<Long> applyCreditQualify(BaseUser currentUser) {
        return null;
    }

    @Override
    public Response<Boolean> systemApplyCreditQualify(Long uid, String msg, Integer status) {
        return null;
    }

    @Override
    public Response<Long> appealCreditQualify(BaseUser currentUser, String message) {
        return null;
    }

    @Override
    public Response<Boolean> updateCreditQualify(BaseUser user, Long id, String msg, Integer status) {
        return null;
    }

    @Override
    public Response<Paging<SupplierCreditQualifyDto>> pagingCreditQualify(BaseUser currentUser, Integer type, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Boolean> rejectQualify(User user, Long id) {
        return null;
    }

    @Override
    public Response<Boolean> notifyUpcomingIn(Integer days) {
        return null;
    }

    @Override
    public Response<Boolean> notifyDelayed(Integer days) {
        return null;
    }
}
