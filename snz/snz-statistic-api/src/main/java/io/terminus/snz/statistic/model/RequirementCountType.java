package io.terminus.snz.statistic.model;

import com.google.common.base.Objects;

/**
 * Desc:需求统计数据类型
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-24.
 */
public enum RequirementCountType {
    SEND_SU("send_su" , "推送的供应商数量"),
    ANSWER_SU("answer_su", "响应的供应商数量"),
    SEND_SO("send_so" , "提交方案的供应商数量"),
    TOPIC_NUM("topic_num" , "话题数");

    private final String value;

    private final String description;

    private RequirementCountType(String value , String description){
        this.value = value;

        this.description = description;
    }

    public static RequirementCountType from(String value){
        for(RequirementCountType countType : RequirementCountType.values()){
            if(Objects.equal(value, countType.value)){
                return countType;
            }
        }

        return null;
    }

    public String value(){
        return value;
    }

    @Override
    public String toString(){
        return description;
    }
}
