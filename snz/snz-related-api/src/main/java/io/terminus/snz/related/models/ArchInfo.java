package io.terminus.snz.related.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:  wenhaoli
 * Date: 2014-08-14
 */
public class ArchInfo implements Serializable {
    private static final long serialVersionUID = 6980160658093713863L;

    @Getter
    @Setter
    private String name;            //主营业务名称

    @Getter
    @Setter
    private Integer compositeScore;     //绩效得分

    @Getter
    @Setter
    private Integer numOfClaim;         //近一个月索赔次数

    @Getter
    @Setter
    private Integer sumOfClaim;         //损失（当前供应商一个月索赔金额总和）

    @Getter
    @Setter
    private String manager;             //采购经理

    @Getter
    @Setter
    private String members;             //利共体成员


}
