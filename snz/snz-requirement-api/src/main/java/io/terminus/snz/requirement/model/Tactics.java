package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;

/**
 * Desc:技术策略枚举
 * Mail:v@terminus.io
 * author Michael Zhao
 * Date:2014-05-01.
 */
public enum Tactics {
    //后期还会添加更多的策略信息
    TECHNOLOGY_NEW(1 , "技术领先"),
    DIFFERENTIATION(2 , "差异化"),
    EXCELLENCE(3 , "卓越运营");

    private final Integer value;

    private final String description;

    private Tactics(Integer value , String description){

        this.value = value;

        this.description = description;
    }

    public static Tactics from(Integer value){
        for(Tactics tactics : Tactics.values()){
            if(Objects.equal(value , tactics.value)){
                return tactics;
            }
        }

        return null;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return description;
    }
}
