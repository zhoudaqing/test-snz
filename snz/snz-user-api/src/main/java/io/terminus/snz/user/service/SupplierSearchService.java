package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierFacetSearchResult;

/**
 * Created by yangzefeng on 14-6-24
 */
public interface SupplierSearchService {

    /**
     * 前台供应商列表
     * @param pageNo 页码
     * @param size   大小
     * @param cn      供应商公司名称
     * @param secondId  前台二级类目id
     * @param firstId 前台一级类目id
     * @param sort      排序规则
     */
    @Export(paramNames = {"pageNo", "size", "cn", "thirdId", "secondId", "firstId", "sort"})
    public Response<SupplierFacetSearchResult> facetSearchSupplier(Integer pageNo, Integer size,String cn,
                                                                   Long thirdId, Long secondId,Long firstId,
                                                                   String sort);
}
