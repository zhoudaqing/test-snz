package io.terminus.snz.web.controllers;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import io.terminus.search.ESClient;
import io.terminus.search.ESHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * Created by yangzefeng on 14-6-30
 */

@Component
public class EsInitiator {

    private static final Set<String> indices = ImmutableSet.<String>builder()
            .add("requirements/requirement")
            .add("suppliers/supplier")
            .build();

    private final ESClient esClient;

    @Autowired
    public EsInitiator(ESClient esClient) {
        this.esClient = esClient;
    }

    @PostConstruct
    public void init() {
        for (String index : indices) {
            List<String> parts = Splitter.on('/').omitEmptyStrings().trimResults().splitToList(index);
            ESHelper.createIndexIfNeeded(esClient.getClient(), parts.get(0), parts.get(1));
        }
    }
}
