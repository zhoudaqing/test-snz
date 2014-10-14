package io.terminus.snz.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 信息完善检查结果
 * Author:Guo Chaopeng
 * Created on 14-8-5.
 */
public class InfoCompleteCheckResult implements Serializable{

    private static final long serialVersionUID = 7965423902442178994L;

    @Setter
    @Getter
    private Boolean isComplete;           //信息是否完善

    @Setter
    @Getter
    private Long infoCompletePercent;   //已完善信息的百分比

}
