package io.terminus.snz.statistic.model;

import com.google.common.base.Objects;

/**
 * Desc:需求阶段状态枚举（抽出来在统计模块里）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-01.
 */
public enum RequirementStatus {
    //根据不同的业务场景需求的阶段状态会不同(业务场景－》模块属性＋模块策略)
    DELETE(-1 , "已删除"),
    WAIT_SEND(0 , "等待发布"),
    RES_INTERACTIVE(1 , "需求交互"),
    RES_LOCK(2 , "需求锁定"),
    SOL_INTERACTIVE(3 , "方案交互|承诺底线"),
    SOL_END(4 , "方案终投|谈判|竞标"),
    SUP_SOL(5 , "选定供应商与方案"),
    TENDER_END(6 , "招标结束");

    private final Integer value;

    private final String description;

    private RequirementStatus(Integer value, String description){
        this.value = value;

        this.description = description;
    }

    public static RequirementStatus from(Integer value){
        for(RequirementStatus requirementStatus : RequirementStatus.values()){
            if(Objects.equal(value, requirementStatus.value)){
                return requirementStatus;
            }
        }

        return null;
    }

    public Integer value(){
        return value;
    }

    @Override
    public String toString(){
        return description;
    }
}