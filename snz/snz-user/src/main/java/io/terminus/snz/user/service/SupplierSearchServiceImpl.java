package io.terminus.snz.user.service;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.PageInfo;
import io.terminus.pampas.common.Response;
import io.terminus.search.ESClient;
import io.terminus.search.Pair;
import io.terminus.search.RawSearchResult;
import io.terminus.search.SearchFacet;
import io.terminus.snz.category.model.FrontendCategory;
import io.terminus.snz.category.service.FrontendCategoryService;
import io.terminus.snz.user.dto.RichSupplier;
import io.terminus.snz.user.dto.SupplierFacetSearchResult;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.search.SupplierSearchHelper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.facet.FacetBuilder;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.terms.TermsFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 14-6-24
 */
@Service
@Slf4j
public class SupplierSearchServiceImpl implements SupplierSearchService {

    private static final String SUPPLIER_INDEX_NAME = "suppliers";

    private static final String SUPPLIER_INDEX_TYPE = "supplier";

    private static final String THIRD_FACETS = "third_facets";

    private static final String SECOND_FACETS = "second_facets";

    private static final String FIRST_FACETS = "first_facets";

    public static final ImmutableList<Pair> ROOT_CATEGORY = ImmutableList.of(new Pair("所有分类", 0L));

    public static final Joiner JOINER  = Joiner.on("_").skipNulls();

    @Autowired
    private ESClient esClient;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Override
    public Response<SupplierFacetSearchResult> facetSearchSupplier(Integer pageNo, Integer size, String cn,
                                                                   Long thirdId, Long secondId, Long firstId, String sort) {
        Response<SupplierFacetSearchResult> result = new Response<SupplierFacetSearchResult>();

        try {

            PageInfo pageInfo = new PageInfo(pageNo, size);
            Map<String, String> params = Maps.newHashMap();

            //只搜索类型为供应商，审核状态为审核通过的用户
            params.put("type", String.valueOf(User.Type.SUPPLIER.value()));
            params.put("approveStatus", JOINER.join(Lists.newArrayList(
                    User.ApproveStatus.MODIFY_INFO_FAIL.value(),User.ApproveStatus.OK.value(),User.ApproveStatus.MODIFY_INFO_WAITING_FOR_APPROVE.value())));
            //只搜索章台为OK的用户
            params.put("status", String.valueOf(User.Status.OK.value()));

            if (!Strings.isNullOrEmpty(cn)) {
                params.put("cn", cn);
            }
            boolean isThirdPresent = thirdId != null;
            if (thirdId != null) {
                params.put("thirdId", String.valueOf(thirdId));
            }
            boolean isSecondPresent = secondId != null;
            if (secondId != null) {
                params.put("secondId", String.valueOf(secondId));
            }

            boolean isFirstPresent = firstId != null;
            if (firstId != null) {
                params.put("firstId", String.valueOf(firstId));
            }

            SearchRequestBuilder requestBuilder = esClient.searchRequestBuilder(SUPPLIER_INDEX_NAME);
            QueryBuilder queryBuilder = SupplierSearchHelper.composeQuery(params);
            requestBuilder.setQuery(queryBuilder);
            SupplierSearchHelper.composeSort(requestBuilder, sort);
            requestBuilder.setFrom(pageInfo.getOffset()).setSize(pageInfo.getLimit());

            FacetBuilder thirdFacets = FacetBuilders.termsFacet(THIRD_FACETS).field("mainBusinessIds").size(20);
            FacetBuilder secondFacets = FacetBuilders.termsFacet(SECOND_FACETS).field("secondIds").size(20);
            FacetBuilder firstFacets = FacetBuilders.termsFacet(FIRST_FACETS).field("firstIds").size(20);

            requestBuilder.addFacet(thirdFacets).addFacet(secondFacets).addFacet(firstFacets);

            requestBuilder.addHighlightedField("companyName");

            RawSearchResult<RichSupplier> rawResult = esClient.facetSearchWithIndexType(SUPPLIER_INDEX_TYPE, RichSupplier.class, requestBuilder);

            SupplierFacetSearchResult facetSearchResult = refineSearchFacet(rawResult, isFirstPresent, isSecondPresent, isThirdPresent,
                    firstId, secondId);

            refineBreadCrumbs(thirdId, secondId, firstId, facetSearchResult);

            result.setResult(facetSearchResult);

            return result;

        } catch (Exception e) {
            log.error("fail to search supplier by pageNo={},size={},cn={},firstId={},secondId={},thirdId={},sort={},cause:{}",
                    pageNo, size, cn, firstId, secondId, thirdId, sort, Throwables.getStackTraceAsString(e));
            result.setError("supplier.search.fail");
            return result;
        }
    }

    private SupplierFacetSearchResult refineSearchFacet(RawSearchResult<RichSupplier> rawSearchResult,
                                                        boolean isFirstPresent, boolean isSecondPresent,
                                                        boolean isThirdPresent,Long firstId,
                                                        Long secondId) {
        SupplierFacetSearchResult result = new SupplierFacetSearchResult();
        Facets facets = rawSearchResult.getFacets();

        //set front category nav, if isFirstPresent, only need to show second facets
        //if isSecondPresent, show third facets
        //if isThirdPresent, show none
        //if neither, show both
        TermsFacet firstFacets = null;
        TermsFacet secondFacets = null;
        TermsFacet thirdFacets = null;

        if (!isThirdPresent) {
            if (isSecondPresent) {
                thirdFacets = facets.facet(THIRD_FACETS);
            } else {
                if (isFirstPresent) {
                    secondFacets = facets.facet(SECOND_FACETS);
                } else {
                    firstFacets = facets.facet(FIRST_FACETS);
                    secondFacets = facets.facet(SECOND_FACETS);
                    thirdFacets = facets.facet(THIRD_FACETS);
                }
            }
        }

        if (firstFacets != null) {
            result.setFirstNav(convertToSearchFacet(firstFacets, null));
        }
        if (secondFacets != null) {
            result.setSecondNav(convertToSearchFacet(secondFacets, firstId));
        }
        if (thirdFacets != null) {
            result.setThirdNav(convertToSearchFacet(thirdFacets, secondId));
        }

        result.setTotal(rawSearchResult.getTotal());
        result.setRichRequirements(rawSearchResult.getData());

        return result;
    }

    private List<SearchFacet> convertToSearchFacet(TermsFacet categoryFacets, Long currentCategoryId) {
        List<SearchFacet> categorySearchFacet = Lists.newArrayList();
        for (TermsFacet.Entry entry : categoryFacets) {
            Long id = Long.valueOf(entry.getTerm().string());
            long count = (long) entry.getCount();
            SearchFacet searchFacet = new SearchFacet(id, count);
            //get name
            Response<FrontendCategory> frontCategoryR = frontendCategoryService.findById(id);
            if (!frontCategoryR.isSuccess() || frontCategoryR.getResult() == null) {
                log.error("fail to find frontEndCategory by id={}, error code:{}",
                        id, frontCategoryR.getError());
                continue;
            }
            FrontendCategory fc = frontCategoryR.getResult();
            searchFacet.setName(fc.getName());

            //如果是1级类目，或者类目都为搜索类目的child，才加入到类目导航
            if(currentCategoryId == null || Objects.equal(fc.getParentId(), currentCategoryId)) {
                categorySearchFacet.add(searchFacet);
            }
        }
        return categorySearchFacet;
    }


    /**
     * 如果用户选择了某一个类目或者搜索结果只包含一个类目，显示面包屑，否则只显示根目录
     */
    private void refineBreadCrumbs(Long thirdId, Long secondId, Long firstId, SupplierFacetSearchResult result) {

        List<Pair> breadCrumbs = Lists.newArrayList();

        //选择了一个一级或者二级前台类目,或者结果
        if (thirdId != null || secondId != null || firstId != null
                || Objects.equal(result.getThirdNav().size(), 1) || Objects.equal(result.getSecondNav().size(), 1)
                || Objects.equal(result.getFirstNav().size(), 1)) {
            Long existId = thirdId != null ? thirdId : secondId;
            existId = existId != null ? existId : firstId;
            existId = existId != null ? existId : result.getThirdNav().get(0).getId();
            existId = existId != null ? existId : result.getSecondNav().get(0).getId();
            existId = existId != null ? existId : result.getFirstNav().get(0).getId();

            Response<List<FrontendCategory>> ancestorR = frontendCategoryService.ancestorsOf(existId);
            if (!ancestorR.isSuccess() || ancestorR.getResult().isEmpty()) {
                log.error("fail to find ancestor of frontendCategory id={}, error code={}, directly return",
                        existId, ancestorR.getError());
                return;
            }

            List<FrontendCategory> ancestor = ancestorR.getResult();
            for (FrontendCategory f : ancestor) {
                breadCrumbs.add(new Pair(f.getName(), f.getId()));
            }
            //add root
            breadCrumbs.add(new Pair("所有分类", 0L));

            //revert
            breadCrumbs = Lists.reverse(breadCrumbs);
            result.setBreadCrumbs(breadCrumbs);

        } else {
            result.setBreadCrumbs(ROOT_CATEGORY);
        }
    }
}
