package io.terminus.snz.requirement.service.search;

import com.google.common.collect.ImmutableMap;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RequirementSearchHelperTest {

    @Test
    public void testComposeQuery() throws Exception {
        assertNotNull(RequirementSearchHelper.composeQuery(ImmutableMap.of("q" , "q", "status",
                "1", "firstId", "1", "secondId", "1", "thirdId", "1")));
    }

    @Test
    public void testComposeSort() throws Exception {
        RequirementSearchHelper.composeSort(new SearchRequestBuilder(new TransportClient()), "0_1");
    }
}