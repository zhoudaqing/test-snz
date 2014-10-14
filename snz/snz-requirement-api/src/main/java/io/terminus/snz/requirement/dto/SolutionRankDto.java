package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.RequirementRank;
import io.terminus.snz.requirement.model.RequirementSolution;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:需求方案信息&对应的排名信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-28.
 */
@ToString
@EqualsAndHashCode
public class SolutionRankDto implements Serializable {
    private static final long serialVersionUID = -875143864329329868L;

    @Setter
    @Getter
    private RequirementSolution requirementSolution;    //需求方案对象

    @Setter
    @Getter
    private RequirementRank requirementRank;            //需求排名信息

}
