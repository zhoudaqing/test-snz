package io.terminus.snz.user.dto;

import io.terminus.search.Pair;
import io.terminus.search.SearchFacet;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-24
 */
public class SupplierFacetSearchResult implements Serializable {
    private static final long serialVersionUID = 4661483879188491725L;

    @Getter
    @Setter
    private Long total;

    @Getter
    @Setter
    private List<RichSupplier> richRequirements;

    @Getter
    @Setter
    private List<Pair> breadCrumbs;

    @Getter
    @Setter
    private List<SearchFacet> firstNav;

    @Getter
    @Setter
    private List<SearchFacet> secondNav;

    @Getter
    @Setter
    private List<SearchFacet> thirdNav;
}
