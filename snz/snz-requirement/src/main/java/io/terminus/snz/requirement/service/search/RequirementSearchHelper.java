package io.terminus.snz.requirement.service.search;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by yangzefeng on 14-6-20
 */
public class RequirementSearchHelper {

    private static final Splitter splitter = Splitter.on("_").trimResults().omitEmptyStrings();

    public static QueryBuilder composeQuery(Map<String, String> params) {

        QueryBuilder queryBuilder;

        List<FilterBuilder> filterBuilders = Lists.newArrayList();

        String keywords = params.get("q");
        if(!Strings.isNullOrEmpty(keywords)) {
            queryBuilder = QueryBuilders.matchQuery("name", keywords);
        }else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }

        String joinedStatus = params.get("status");
        if(!Strings.isNullOrEmpty(joinedStatus)) {
            List<String> statuses = splitter.splitToList(joinedStatus);
            filterBuilders.add(FilterBuilders.inFilter("status", statuses.toArray(new String[statuses.size()])));
        }

        String firstIdstr = params.get("firstId");
        if(!Strings.isNullOrEmpty(firstIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("firstLevelIds", Long.valueOf(firstIdstr)));
        }

        String secondIdstr = params.get("secondId");
        if(!Strings.isNullOrEmpty(secondIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("secondLevelIds", Long.valueOf(secondIdstr)));
        }

        String thirdIdstr = params.get("thirdId");
        if(!Strings.isNullOrEmpty(thirdIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("thirdLevelIds", Long.valueOf(thirdIdstr)));
        }

        if(filterBuilders.isEmpty()) {
            return queryBuilder;
        }else {
            AndFilterBuilder and = FilterBuilders.andFilter();
            for(FilterBuilder filterBuilder : filterBuilders) {
                and.add(filterBuilder);
            }
            return QueryBuilders.filteredQuery(queryBuilder, and);
        }
    }

    public static void composeSort(SearchRequestBuilder requestBuilder, String sort) {
        if(!Strings.isNullOrEmpty(sort)) {
            List<String> parts = splitter.splitToList(sort);
            //发布时间
            String checkAt = Objects.firstNonNull(parts.get(0), "0");
            //模块数量
            String moduleNum = Objects.firstNonNull(parts.get(1), "0");

            switch (Integer.valueOf(checkAt)) {
                case 1:
                    requestBuilder.addSort("checkTime", SortOrder.ASC);
                    break;
                case 2:
                    requestBuilder.addSort("checkTime", SortOrder.DESC);
                    break;
                default:
                    break;
            }

            switch (Integer.valueOf(moduleNum)) {
                case 1:
                    requestBuilder.addSort("moduleNum", SortOrder.ASC);
                    break;
                case 2:
                    requestBuilder.addSort("moduleNum",SortOrder.DESC);
                    break;
                default:
                    break;
            }
        }
    }
}
