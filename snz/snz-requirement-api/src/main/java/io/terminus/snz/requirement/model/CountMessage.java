package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:统计详细信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-29.
 */
@ToString
@EqualsAndHashCode
public class CountMessage implements Serializable{

    private static final long serialVersionUID = 2430317228601324065L;

    @Setter
    @Getter
    private Requirement requirement;//需求信息

    @Setter
    @Getter
    private Integer oldStatus;      //上一个阶段

    @Setter
    @Getter
    private Integer newStatus;      //下一个阶段

    public CountMessage(){}

    public CountMessage(Requirement requirement, Integer oldStatus, Integer newStatus){
        this.requirement = requirement;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}
