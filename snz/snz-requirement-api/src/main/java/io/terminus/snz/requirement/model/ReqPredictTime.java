package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:需求的预计的时间信息（用于前台展示用的）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-19.
 */
@ToString
@EqualsAndHashCode
public class ReqPredictTime implements Serializable {
    private static final long serialVersionUID = -8489293484930868532L;

    @Setter
    @Getter
    private Long id;            //自增主键

    @Setter
    @Getter
    private Long requirementId; //需求编号

    @Setter
    @Getter
    private Integer type;       //交互状态时间（1:需求交互,2:需求锁定...）由RequirementStatus控制类型

    @Setter
    @Getter
    private Date predictStart;  //预计开始时间

    @Setter
    @Getter
    private Date predictEnd;    //预计结束时间

    @Setter
    @Getter
    private Date createdAt;     //创建时间

    @Setter
    @Getter
    private Date updatedAt;     //修改时间
}
