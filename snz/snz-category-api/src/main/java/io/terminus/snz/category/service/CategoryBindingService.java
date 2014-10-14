package io.terminus.snz.category.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.dto.CategoryPairWithBcName;
import io.terminus.snz.category.model.CategoryBinding;

import java.util.List;

/**
 * Created by yangzefeng on 14-6-26
 */
public interface CategoryBindingService {

    Response<Long> create(CategoryBinding categoryBinding);

    Response<Boolean> update(CategoryBinding categoryBinding);

    Response<Boolean> delete(Long id);

    /**
     * 根据前台类目id查询后台类目
     * @param fcid    前台类目id
     * @return 所要查询的绑定
     */
    @Export(paramNames = {"fcid"})
    Response<List<CategoryPair>> findByFcid(Long fcid);

    /**
     * 根据前台类目id查询后台类目名称（包装了CategoryPair）
     * @param fcid    前台类目id
     * @return 后台类目名称列表
     */
    @Export(paramNames = {"fcid"})
    Response<List<CategoryPairWithBcName>> findBcNamesByFcid(Long fcid);

    Response<CategoryBinding> findBy(Long fcid);

    Response<CategoryBinding> findById(Long id);
}
