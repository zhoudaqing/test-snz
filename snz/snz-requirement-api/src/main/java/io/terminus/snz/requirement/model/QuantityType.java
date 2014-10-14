package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;

/**
 * Desc:数量单位
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-15.
 */
public enum QuantityType {
    ONE(1 , "一"),
    TEN(2 , "十"),
    HUNDREDS(3 , "百"),
    THOUSANDS(4 , "千");

    private final Integer value;

    private final String description;

    private QuantityType(Integer value , String description){
        this.value = value;

        this.description = description;
    }

    public static QuantityType from(Integer value){
        for(QuantityType quantityType : QuantityType.values()){
            if(Objects.equal(value, quantityType.value)){
                return quantityType;
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
