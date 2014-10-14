package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;

/**
 * Desc:采购单位
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-14.
 */
public enum SalesType {
    EA(1 , "EA"),
    JUA(2 , "JUA"),
    KG(3 , "KG"),
    BAG(4 , "BAG"),
    BOT(5 , "BOT"),
    G(6 , "G"),
    L(7 , "L"),
    M(8 , "M"),
    M2(9 , "M2"),
    M3(10 , "M3"),
    ML(11 , "ML"),
    MM(12 , "MM"),
    PIA(13 , "PIA"),
    TAO(14 , "TAO"),
    TEN(15 , "TEN"),
    TIA(16 , "TIA"),
    TO(17 , "TO"),
    ZHA(18 , "ZHA");

    private final Integer value;

    private final String description;

    private SalesType(Integer value , String description){
        this.value = value;

        this.description = description;
    }

    public static SalesType from(Integer value){
        for(SalesType salesType : SalesType.values()){
            if(Objects.equal(value, salesType.value)){
                return salesType;
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
