package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;

/**
 * Desc:货币的类型
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-21.
 */
public enum CoinType {
    CNY(1 , "CNY"),
    USD(2 , "USD"),
    EUR(3 , "EUR"),
    JPY(4 , "JPY");

    private final Integer value;

    private final String description;

    private CoinType(Integer value , String description){
        this.value = value;

        this.description = description;
    }

    public static CoinType from(Integer value){
        for(CoinType coinType : CoinType.values()){
            if(Objects.equal(value, coinType.value)){
                return coinType;
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
