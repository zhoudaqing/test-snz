package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.category.model.BackendCategoryProperty;
import io.terminus.snz.category.service.BackendCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class BackendCategoryServiceMock implements BackendCategoryService {
    @Override
    public Response<Long> create(BackendCategory bc) {
        return null;
    }

    @Override
    public Response<Boolean> update(BackendCategory bc) {
        return null;
    }

    @Override
    public Response<Boolean> delete(Long id) {
        return null;
    }

    @Override
    public Response<BackendCategory> findById(Long id) {
        return null;
    }

    @Override
    public Response<List<BackendCategory>> findByIds(List<Long> ids) {
        return null;
    }

    @Override
    public Response<List<BackendCategory>> findChildrenOf(Long id) {
        return null;
    }

    @Override
    public Response<List<BackendCategory>> findByLevels(Integer level) {
        Response<List<BackendCategory>> result = new Response<List<BackendCategory>>();
        result.setResult(new ArrayList<BackendCategory>());
        return result;
    }

    @Override
    public Response<List<BackendCategory>> findByParentIdAndLevel(Integer parentId, Integer level) {
        return null;
    }

    @Override
    public Response<Long> createProperty(BackendCategoryProperty property) {
        return null;
    }

    @Override
    public Response<Boolean> updateProperty(Long id, BackendCategoryProperty property) {
        return null;
    }

    @Override
    public Response<Boolean> deleteProperty(Long id) {
        return null;
    }

    @Override
    public Response<BackendCategoryProperty> findPropertyById(Long id) {
        return null;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcId(Long bcId) {
        return null;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcIds(List<Long> bcIds) {
        return null;
    }

    @Override
    public Response<List<BackendCategoryProperty>> findPropertiesByBcIdWithFactoryNums(Long bcId, String factoryNums) {
        return null;
    }

}
