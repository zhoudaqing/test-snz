package io.terminus.snz.requirement.service;

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
import io.terminus.snz.requirement.dto.RequirementFacetSearchResult;
import io.terminus.snz.requirement.dto.RichRequirement;
import io.terminus.snz.requirement.service.search.RequirementSearchHelper;
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
 * Created by yangzefeng on 14-6-20
 */
@Service
@Slf4j
public class RequirementSearchServiceImpl implements RequirementSearchService {

    private static final String REQUIREMENT_INDEX_NAME = "requirements";

    private static final String REQUIREMENT_INDEX_TYPE = "requirement";

    private static final String THIRD_FACETS = "third_front_category_facets";

    private static final String SECOND_FACETS = "second_front_category_facets";

    private static final String FIRST_FACETS = "first_front_category_facets";

    public static final ImmutableList<Pair> rootCategory = ImmutableList.of(new Pair("所有分类", 0L));

    private static final Iterable<Integer> FILTER_STATUS = Lists.newArrayList(1,2,3,4,5,6);

    private static final Joiner joiner = Joiner.on("_").skipNulls();

    @Autowired
    private ESClient esClient;

    @Autowired
    private FrontendCategoryService frontendCategoryService;

    @Override
    public Response<RequirementFacetSearchResult> facetSearchRequirement(Integer pageNo, Integer size, String q, Long thirdId,
                                                                         Long secondId,Long firstId, String sort) {

        Response<RequirementFacetSearchResult> result = new Response<RequirementFacetSearchResult>();

        try {
            PageInfo pageInfo = new PageInfo(pageNo, size);
            Map<String, String> params = Maps.newHashMap();

            //所有可出现在search中的状态集合
            params.put("status", joiner.join(FILTER_STATUS));

            if(!Strings.isNullOrEmpty(q)) {
                params.put("q", q);
            }

            boolean isThirdPresent = thirdId != null;
            if(thirdId != null) {
                params.put("thirdId", String.valueOf(thirdId));
            }

            boolean isSecondPresent = secondId != null;
            if(secondId != null) {
                params.put("secondId", String.valueOf(secondId));
            }

            boolean isFirstPresent = firstId != null;
            if(firstId != null) {
                params.put("firstId", String.valueOf(firstId));
            }

            SearchRequestBuilder requestBuilder = esClient.searchRequestBuilder(REQUIREMENT_INDEX_NAME);
            QueryBuilder queryBuilder = RequirementSearchHelper.composeQuery(params);
            requestBuilder.setQuery(queryBuilder);
            RequirementSearchHelper.composeSort(requestBuilder, sort);
            requestBuilder.setFrom(pageInfo.getOffset()).setSize(pageInfo.getLimit());

            FacetBuilder thirdFacets = FacetBuilders.termsFacet(THIRD_FACETS).field("thirdLevelIds").size(20);
            FacetBuilder secondFacets = FacetBuilders.termsFacet(SECOND_FACETS).field("secondLevelIds").size(20);
            FacetBuilder firstFacets = FacetBuilders.termsFacet(FIRST_FACETS).field("firstLevelIds").size(20);

            requestBuilder.addFacet(thirdFacets).addFacet(secondFacets).addFacet(firstFacets);

            requestBuilder.addHighlightedField("name");

            RawSearchResult<RichRequirement> rawResult = esClient.facetSearchWithIndexType(REQUIREMENT_INDEX_TYPE, RichRequirement.class, requestBuilder);

            RequirementFacetSearchResult refineResult = refineSearchFacet(rawResult, isFirstPresent, isSecondPresent, isThirdPresent,
                    firstId, secondId);

            //refine breadCrumb
            refineBreadCrumbs(thirdId, secondId, firstId, refineResult);

            result.setResult(refineResult);

            return result;

        }catch (Exception e) {
            log.error("fail to facet search requirement by thirdId={} secondId={} firstId={} sort={}, cause:{}",
                    thirdId, secondId, firstId, sort, Throwables.getStackTraceAsString(e));
            result.setError("requirement.search.fail");
            return result;
        }
    }

    private RequirementFacetSearchResult refineSearchFacet(RawSearchResult<RichRequirement> rawSearchResult,
                                                           boolean isFirstPresent,
                                                           boolean isSecondPresent,
                                                           boolean isThirdPresent,
                                                           Long firstId, Long secondId) {
        RequirementFacetSearchResult result = new RequirementFacetSearchResult();
        Facets facets = rawSearchResult.getFacets();

        //set front category nav, if isFirstPresent, only need to show second facets
        //if isSecondPresent, show third facets
        //if isThirdPresent, show none
        //if neither, show both
        TermsFacet firstFacets = null;
        TermsFacet secondFacets = null;
        TermsFacet thirdFacets = null;

        if(!isThirdPresent) {
            if(isSecondPresent) {
                thirdFacets = facets.facet(THIRD_FACETS);
            }else {
                if(isFirstPresent) {
                    secondFacets = facets.facet(SECOND_FACETS);
                }else {
                    firstFacets = facets.facet(FIRST_FACETS);
                    secondFacets = facets.facet(SECOND_FACETS);
                    thirdFacets = facets.facet(THIRD_FACETS);
                }
            }
        }

        if(firstFacets != null) {
            result.setFirstNav(convertToSearchFacet(firstFacets, null));
        }
        if(secondFacets != null) {
            result.setSecondNav(convertToSearchFacet(secondFacets, firstId));
        }
        if(thirdFacets != null) {
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
    private void refineBreadCrumbs(Long thirdId,Long secondId,Long firstId, RequirementFacetSearchResult result) {

        List<Pair> breadCrumbs = Lists.newArrayList();

        //选择了一个一级或者二级前台类目,或者结果只属于某一个前台类目
        if(thirdId != null || secondId != null || firstId != null
                || Objects.equal(result.getThirdNav().size(), 1) || Objects.equal(result.getSecondNav().size(), 1)
                || Objects.equal(result.getFirstNav().size(), 1)) {
            Long existId = thirdId != null ? thirdId : secondId;
            existId = existId != null ? existId : firstId;
            existId = existId != null ? existId : result.getThirdNav().get(0).getId();
            existId = existId != null ? existId : result.getSecondNav().get(0).getId();
            existId = existId != null ? existId : result.getFirstNav().get(0).getId();

            Response<List<FrontendCategory>> ancestorR = frontendCategoryService.ancestorsOf(existId);
            if(!ancestorR.isSuccess() || ancestorR.getResult().isEmpty()) {
                log.error("fail to find ancestor of frontendCategory id={}, error code={}, directly return",
                        existId, ancestorR.getError());
                return;
            }

            List<FrontendCategory> ancestor = ancestorR.getResult();
            for(FrontendCategory f : ancestor) {
                breadCrumbs.add(new Pair(f.getName(), f.getId()));
            }
            //add root
            breadCrumbs.add(new Pair("所有分类", 0L));

            //revert
            breadCrumbs = Lists.reverse(breadCrumbs);
            result.setBreadCrumbs(breadCrumbs);

        }else {
            result.setBreadCrumbs(rootCategory);
        }
    }
}
