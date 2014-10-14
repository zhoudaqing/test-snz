package io.terminus.snz.requirement.dto;

import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementSend;
import io.terminus.snz.requirement.model.RequirementTeam;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Desc:详细需求信息现实页面（需求信息&模块信息等）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-09.
 */
public class RequirementDetailDto implements Serializable {
    private static final long serialVersionUID = -4194533907847595860L;

    @Setter
    @Getter
    private Boolean isAcceptQuestionnaire;     //供应商是否已经参与该需求的调查问卷

    @Setter
    @Getter
    private Requirement requirement;            //需求信息

    @Setter
    @Getter
    private List<RequirementTeam> teamList;     //需求的团队人员数据信息(用于团队成员的管理处理)

    @Setter
    @Getter
    private Date finishDay;                     //阶段结束时间

    @Setter
    @Getter
    private Boolean statusType;                 //是否已超过预计的设定时间

    @Setter
    @Getter
    private Date solEndTime;                    //方案终投的开始时间

    @Setter
    @Getter
    private Map<Integer, Integer> deferDays;   //延期天数信息


    //采购商对于需求的各种数据信息
    @Setter
    @Getter
    private Integer depositStatus;              //保证金状态

    @Setter
    @Getter
    private Integer solutionStatus;             //需求方案状态

    @Setter
    @Getter
    private List<ParkFactoryDto> parkFactoryDtoList;    //工厂园区信息

    @Setter
    @Getter
    private RequirementSend requirementSend; //需求的其他状态标志位
}
