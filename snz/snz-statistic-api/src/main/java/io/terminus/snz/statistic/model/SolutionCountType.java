package io.terminus.snz.statistic.model;

import com.google.common.base.Objects;

/**
 * Desc:供应商方案统计数据类型
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-04.
 */
public enum SolutionCountType {
    MUTUAL_SOL("mutual_sol" , "交互过的需求数量"),
    ACCEPT_SOL("accept_sol" , "承诺的方案数量"),
    SELECT_SOL("select_sol" , "选中的方案数量"),
    ALTERNATE_SOL("alternate_sol", "备选的方案数量"),
    ENTER_SOL("enter_sol" , "入围的方案数量");

    private final String value;

    private final String description;

    private SolutionCountType(String value , String description){
        this.value = value;

        this.description = description;
    }

    public static SolutionCountType from(String value){
        for(SolutionCountType countType : SolutionCountType.values()){
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
