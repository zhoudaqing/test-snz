package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.CategoryPairWithBcName;
import io.terminus.snz.category.model.CategoryBinding;
import io.terminus.snz.category.service.CategoryBindingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class CategoryBindingServiceMock implements CategoryBindingService {
    @Override
    public Response<Long> create(CategoryBinding categoryBinding) {
        return null;
    }

    @Override
    public Response<Boolean> update(CategoryBinding categoryBinding) {
        return null;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        return null;
    }

    @Override
    public Response<List<CategoryPair>> findByFcid(Long fcid) {
        return null;
    }

    @Override
    public Response<List<CategoryPairWithBcName>> findBcNamesByFcid(Long fcid) {
        return null;
    }

    @Override
    public Response<CategoryBinding> findBy(Long fcid) {
        return null;
    }

    @Override
    public Response<CategoryBinding> findById(Long id) {
        return null;
    }
}
