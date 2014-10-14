package io.terminus.snz.requirement.service;

import com.google.common.collect.Lists;
import io.terminus.search.ESClient;
import io.terminus.search.RawSearchResult;
import io.terminus.snz.requirement.dto.RichRequirement;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by yangzefeng on 14-8-4
 */
public class RequirementSearchServiceTest {

    @InjectMocks
    private RequirementSearchServiceImpl requirementSearchService;

    @Mock
    private ESClient esClient;

    private RawSearchResult<RichRequirement> richRequirementR;

    private SearchRequestBuilder requestBuilder;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        requestBuilder = new SearchRequestBuilder(null);

        RichRequirement r = new RichRequirement();
        r.setId(1l);
        r.setFirstLevelIds(Lists.newArrayList(1l,2l));
        r.setName("test");
        r.setStatus(1);

        richRequirementR = new RawSearchResult<RichRequirement>(1l,null,Lists.newArrayList(r));
    }

    @Test
    public void testSearchRequirement() {
        when(esClient.searchRequestBuilder(anyString())).thenReturn(requestBuilder);
        when(esClient.facetSearchWithIndexType("requirement", RichRequirement.class, requestBuilder)).thenReturn(richRequirementR);
        assertNotNull(requirementSearchService.facetSearchRequirement(anyInt(), anyInt(), anyString(), anyLong(), anyLong(), anyLong(), "1_1"));
    }
}
