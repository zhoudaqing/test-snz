package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.RequirementFacetSearchResult;

/**
 * Created by yangzefeng on 14-6-20
 */
public interface RequirementSearchService {

    /**
     * 前台需求列表
     * @param pageNo 页码
     * @param size   大小
     * @param q      需求名称
     * @param thirdId   前台三级类目id
     * @param secondId  前台二级类目id
     * @param firstId 前台一级类目id
     * @param sort      排序规则
     */
    @Export(paramNames = {"pageNo","size","q", "thirdId", "secondId", "firstId", "sort"})
    public Response<RequirementFacetSearchResult> facetSearchRequirement(Integer pageNo, Integer size,
                                       String q, Long thirdId, Long secondId, Long firstId, String sort);
}
