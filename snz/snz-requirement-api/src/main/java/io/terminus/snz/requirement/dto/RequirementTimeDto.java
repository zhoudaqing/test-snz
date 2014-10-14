package io.terminus.snz.requirement.dto;

import io.terminus.snz.requirement.model.ReqPredictTime;
import io.terminus.snz.requirement.model.RequirementTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:需求的时间信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-19.
 */
@ToString
@EqualsAndHashCode
public class RequirementTimeDto implements Serializable {
    private static final long serialVersionUID = 1515399280878323573L;

    @Setter
    @Getter
    private List<ReqPredictTime> reqPredictTimes;       //预期的时间

    @Setter
    @Getter
    private List<RequirementTime> requirementTimes;     //实际&延期时间信息
}
