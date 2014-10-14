package io.terminus.snz.user.service.search;

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
 * Created by yangzefeng on 14-6-24
 */
public class SupplierSearchHelper {

    private static final Splitter SPLITTER = Splitter.on("_").trimResults().omitEmptyStrings();

    public static QueryBuilder composeQuery(Map<String, String> params) {

        QueryBuilder queryBuilder;

        List<FilterBuilder> filterBuilders = Lists.newArrayList();

        String keywords = params.get("cn");
        if (!Strings.isNullOrEmpty(keywords)) {
            queryBuilder = QueryBuilders.matchQuery("companyName", keywords);
        } else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }

        String type = params.get("type");
        if (!Strings.isNullOrEmpty(type)) {
            filterBuilders.add(FilterBuilders.termFilter("type", Integer.valueOf(type)));
        }

        String approveStatus = params.get("approveStatus");
        if (!Strings.isNullOrEmpty(approveStatus)) {
            List<String> approveStatuses = SPLITTER.splitToList(approveStatus);
            filterBuilders.add(FilterBuilders.inFilter("approveStatus", approveStatuses.toArray(new String[approveStatuses.size()])));
        }

        String status = params.get("status");
        if (!Strings.isNullOrEmpty(status)) {
            filterBuilders.add(FilterBuilders.termFilter("status", Integer.valueOf(status)));
        }

        String firstIdstr = params.get("firstId");
        if (!Strings.isNullOrEmpty(firstIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("firstIds", Long.valueOf(firstIdstr)));
        }

        String secondIdstr = params.get("secondId");
        if (!Strings.isNullOrEmpty(secondIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("secondIds", Long.valueOf(secondIdstr)));
        }

        String thirdIdstr = params.get("thirdId");
        if (!Strings.isNullOrEmpty(thirdIdstr)) {
            filterBuilders.add(FilterBuilders.termFilter("mainBusinessIds", Long.valueOf(thirdIdstr)));
        }

        if (filterBuilders.isEmpty()) {
            return queryBuilder;
        } else {
            AndFilterBuilder and = FilterBuilders.andFilter();
            for (FilterBuilder filterBuilder : filterBuilders) {
                and.add(filterBuilder);
            }
            return QueryBuilders.filteredQuery(queryBuilder, and);
        }
    }

    public static void composeSort(SearchRequestBuilder requestBuilder, String sort) {
        if (!Strings.isNullOrEmpty(sort)) {
            List<String> parts = SPLITTER.splitToList(sort);
            //参与交互数
            String participateCount = Objects.firstNonNull(parts.get(0), "0");

            switch (Integer.valueOf(participateCount)) {
                case 1:
                    requestBuilder.addSort("participateCount", SortOrder.ASC);
                    break;
                case 2:
                    requestBuilder.addSort("participateCount", SortOrder.DESC);
                    break;
                default:
                    break;
            }
        }
    }
}
