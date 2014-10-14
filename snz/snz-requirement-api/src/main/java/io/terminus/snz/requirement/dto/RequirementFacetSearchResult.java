package io.terminus.snz.requirement.dto;

import io.terminus.search.Pair;
import io.terminus.search.SearchFacet;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-22
 */
public class RequirementFacetSearchResult implements Serializable {
    private static final long serialVersionUID = -3538116085179458318L;

    @Getter
    @Setter
    private Long total;

    @Getter
    @Setter
    private List<RichRequirement> richRequirements;

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
