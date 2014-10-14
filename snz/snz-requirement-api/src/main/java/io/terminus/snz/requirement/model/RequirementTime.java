package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:需求的每个阶段的预定时间设定
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
@ToString
@EqualsAndHashCode
public class RequirementTime implements Serializable {
    private static final long serialVersionUID = -6661796591248991554L;

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
    private Long userId;        //阶段提交操作的用户编号

    @Setter
    @Getter
    private String userName;    //阶段提交操作的用户姓名

    @Setter
    @Getter
    private Date actualStart;   //实际开始时间

    @Setter
    @Getter
    private Date actualEnd;     //实际结束时间

    @Setter
    @Getter
    private Date createdAt;     //创建时间

    @Setter
    @Getter
    private Date updatedAt;     //修改时间
}
