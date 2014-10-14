package io.terminus.snz.user.service;

import com.google.common.collect.Lists;
import io.terminus.search.ESClient;
import io.terminus.search.RawSearchResult;
import io.terminus.snz.user.dto.RichSupplier;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by yangzefeng on 14-8-4
 */
public class SupplierSearchServiceTest {

    private RawSearchResult<RichSupplier> richSupplierR;

    private SearchRequestBuilder requestBuilder;

    @InjectMocks
    private SupplierSearchServiceImpl supplierSearchServiceImpl;

    @Mock
    private ESClient esClient;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        RichSupplier richSupplier = new RichSupplier();
        richSupplier.setId(1l);
        richSupplier.setCorpAddr("test");
        richSupplier.setFirstIds(Lists.newArrayList(1l,2l,3l));

        richSupplierR = new RawSearchResult<RichSupplier>(1l,null,Lists.newArrayList(richSupplier));

        requestBuilder = new SearchRequestBuilder(null);
    }


    @Test
    public void testSearch() {
        when(esClient.facetSearchWithIndexType("supplier", RichSupplier.class, requestBuilder)).thenReturn(richSupplierR);
        when(esClient.searchRequestBuilder("suppliers")).thenReturn(requestBuilder);
        assertNotNull(supplierSearchServiceImpl.facetSearchSupplier(1, 20, "test", anyLong(), null, null, "1_0"));
    }
}
