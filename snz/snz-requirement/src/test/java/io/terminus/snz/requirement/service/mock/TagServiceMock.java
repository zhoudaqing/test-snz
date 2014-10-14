package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.Company;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.TagService;
import org.springframework.stereotype.Service;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class TagServiceMock implements TagService {
    @Override
    public Response<Boolean> addTag(Long userId, User.SupplierTag tag) {
        return null;
    }

    @Override
    public Response<Boolean> delTag(Long userId, User.SupplierTag tag) {
        return null;
    }

    @Override
    public Response<Boolean> addTQRDCTag(Long userId, Integer score) {
        return null;
    }

    @Override
    public Response<Boolean> updateTagsByCompany(Long userId, Company company) {
        return null;
    }

    @Override
    public Response<Boolean> addSupplierStatusTag(Long userId, User.SupplierTag tag) {
        return null;
    }
}
