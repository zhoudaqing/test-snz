package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jiaoyuan on 14-7-5.
 */
@ToString
@EqualsAndHashCode
public class OldModule implements Serializable{
    private static final long serialVersionUID = 8356726140358835267L;
    @Getter
    @Setter
    private Long id;                //自增主键

    @Getter
    @Setter
    private Long purchaserId;       //采购商编号(指的是注册公司的编号)

    @Getter
    @Setter
    private String purchaserName;   //采购商名称

    @Getter
    @Setter
    private Long requirementId;     //需求id

    @Getter
    @Setter
    private String requirementName; //需求名称

    @Getter
    @Setter
    private Long moduleId;       //模块编号（690系统的）

    @Getter
    @Setter
    private String moduleName;      //模块名称 物料描述

    @Getter
    @Setter
    private String moduleNum;       //模块专用号(海尔的系统生成的编号) 物料号

    @Getter
    @Setter
    private Integer total;          //全部的资源量

    @Getter
    @Setter
    private Long seriesId;          //系列编号（对应后台的二级类目）

    @Getter
    @Setter
    private String  seriesName;     //系列名称

    @Getter
    @Setter
    private String  quotaIds;       //配额ids 1,2,3... 供应商

    @Getter
    @Setter
    private Date overTime;          //反超期

    @Getter
    @Setter
    private Integer tacticsId;      //模块策略编号

    @Getter
    @Setter
    private String headDrop;        //引领点

    @Getter
    @Setter
    private String resourceNum;     //这个是设置不同工厂的不同需求量（json格式rs:[{fa:101,num:1000},{fa:102,num:2000}]）

    @Getter
    @Setter
    private Integer quality;        //质量目标（可以有创建者or团队人员填写）

    @Getter
    @Setter
    private Integer cost;           //成本目标 价格

    @Getter
    @Setter
    private Integer delivery;       //产能要求

    @Getter
    @Setter
    private String attestation;     //认证要求

    @Getter
    @Setter
    private Date supplyAt;          //批量供货时间

   

    @Getter
    @Setter
    private Integer resourceCount;  //资源计数

    @Getter
    @Setter
    private Integer timeTotal;      //计时（天）

    @Getter
    @Setter
    private Integer interactionStarts;//交互启动

    @Getter
    @Setter
    private Integer npiStatus;        //NPI状态

    @Getter
    @Setter
    private Integer prioritySelect;    //优选 PLM状态


    @Getter
    @Setter
    private Date createdAt;             //创建时间

    @Getter
    @Setter
    private Date updatedAt;             //修改时间

    public enum PrioritySelect{
        DISAGREE(0 , "不优选"), ACCEPT(1 , "优选");

        private final Integer value;

        private final String description;

        private PrioritySelect(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static PrioritySelect from(Integer value){
            for(PrioritySelect prioritySelect : PrioritySelect.values()){
                if(Objects.equal(value, prioritySelect.value)){
                    return prioritySelect;
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
}
