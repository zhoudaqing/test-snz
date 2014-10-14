package io.terminus.snz.sns.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 需求统计相关信息
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-23
 */
@NoArgsConstructor @AllArgsConstructor
public class RequirementCount implements Serializable{
    private static final long serialVersionUID = -3218429726367129641L;

    @Getter @Setter
    private Long reqId;         //需求id

    @Getter @Setter
    private Long topics;        //话题数

    @Getter @Setter
    private Long suppliers;     //参与供应商数
}
