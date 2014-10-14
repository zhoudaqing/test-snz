package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:采购商模块数据信息
 * Mail:v@terminus.io
 * author Michael Zhao
 * Date:2014-04-30.
 */
@ToString
@EqualsAndHashCode
public class Module implements Serializable {

    private static final long serialVersionUID = 3114992617834973210L;

    @Getter
    @Setter
    private Long id;                //自增主键

    @Getter
    @Setter
    private String moduleFlowNum;   //模块流水码

    @Getter
    @Setter
    private Long requirementId;     //需求编号

    @Getter
    @Setter
    private Integer type;           //模块类型（1:老品，2:新品，3:衍生号，4:甲指，5:专利）

    @Getter
    @Setter
    private String moduleName;      //模块名称系列名称

    @Setter
    @Getter
    private Long oldModuleId;       //老品模块Id

    @Getter
    @Setter
    private String moduleNum;       //模块专用号(海尔的系统生成的编号)

    @Getter
    @Setter
    private Long seriesId;          //系列编号

    @Getter
    @Setter
    private String seriesName;      //系列名称

    @Getter
    @Setter
    private Long propertyId;        //类目属性编号（针对于整体的模块类目属性）

    @Getter
    @Setter
    private Integer total;          //模块需求的整体数量统计

    @Getter
    @Setter
    private Integer quality;        //质量目标（可以有创建者or团队人员填写）

    @Getter
    @Setter
    private Integer cost;           //成本目标

    @Getter
    @Setter
    private Integer delivery;       //产能要求

    @Getter
    @Setter
    private String attestations;    //认证要求[{id:10,name:'EU认证'}, {id:10,name:'EU认证'}]

    @Getter
    @Setter
    private Date supplyAt;          //批量供货时间

    @Getter
    @Setter
    private String units;           //各种单位的json字段{cost:{salesId:1, name:EA, quantityId:1, quantityName:百},delivery:{salesId:2, name:EA, quantityId:4, salesName:十}}

    @Getter
    @Setter
    private Integer selectNum;      //每个模块选取的供应商数量

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间

    public enum Type{
        NEW_TYPE(1 , "新品"), OLD_TYPE(2, "老品"), DERIVE_TYPE(3 , "衍生号"), JIA_ZHI(4 , "甲指"), PATENT(5 , "专利");

        private final Integer value;

        private final String description;

        private Type(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Type from(Integer value){
            for(Type type : Type.values()){
                if(Objects.equal(value, type.value)){
                    return type;
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

    public static class Unit {
        @Getter
        @Setter
        private Unit cost;

        @Getter
        @Setter
        private Unit delivery;

        @Getter
        @Setter
        private String salesId;

        @Getter
        @Setter
        private String salesName;

        @Getter
        @Setter
        private String quantityId;

        @Getter
        @Setter
        private Integer quantityName;
    }
}
