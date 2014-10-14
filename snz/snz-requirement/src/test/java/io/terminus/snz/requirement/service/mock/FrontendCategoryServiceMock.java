package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.FrontEndCategoryNav;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class FrontendCategoryServiceMock implements FrontendCategoryService {
    @Override
    public Response<Long> create(FrontendCategory fc) {
        return null;
    }

    @Override
    public Response<Boolean> update(FrontendCategory fc) {
        return null;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        return null;
    }

    @Override
    public Response<FrontendCategory> findById(Long id) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findChildrenOf(Long id) {
        return null;
    }

    @Override
    public Response<List<CategoryPair>> findBindingsOf(Long id) {
        return null;
    }

    @Override
    public Response<LinkedHashSet<Long>> findFcIdsByBcId(Long bcId) {
        return null;
    }

    @Override
    public Response<LinkedHashSet<Long>> findFcIdsByBcIds(List<Long> bcIds) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findFrontendByIds(String bcIds) {
        return null;
    }

    @Override
    public Response<Boolean> bind(Long fcid, String bcs) {
        return null;
    }

    @Override
    public Response<Boolean> unbind(Long fcid) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> ancestorsOf(Long id) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findByLevels(Integer level) {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findByStringIds(String ids) {
        return null;
    }

    @Override
    public Response<List<FrontEndCategoryNav>> findAll() {
        return null;
    }

    @Override
    public Response<List<FrontendCategory>> findAllTotally() {
        return null;
    }

    @Override
    public Response<FrontendCategory> findFrontendCategoryByLevelAndName(Integer level, String name) {
        return null;
    }
}
