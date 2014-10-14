package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:具体到模块的配额分配情况
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@ToString
@EqualsAndHashCode
public class ModuleQuota implements Serializable {
    private static final long serialVersionUID = 8762336737995834432L;

    @Setter
    @Getter
    private Long id;            //自增主键

    @Setter
    @Getter
    private Long requirementId; //需求编号

    @Setter
    @Getter
    private Long solutionId;    //方案编号

    @Setter
    @Getter
    private Long moduleId;      //模块编号

    @Setter
    @Getter
    private String moduleNum;   //模块专用号

    @Setter
    @Getter
    private String moduleName;  //模块名称

    @Setter
    @Getter
    private Long moduleFactoryId; //模块工厂编号

    @Setter
    @Getter
    private String factoryNum;  //工厂编号

    @Setter
    @Getter
    private String factoryName; //工厂名称

    @Setter
    @Getter
    private Long supplierId;    //供应商编号

    @Setter
    @Getter
    private String supplierName;//供应商名称

    @Setter
    @Getter
    private Integer quantity;   //配额数量

    @Setter
    @Getter
    private Integer scale;      //配额比例

    @Setter
    @Getter
    private Integer originalCost;//用户的模块报价

    @Setter
    @Getter
    private Integer actualCost; //实际模块报价

    @Getter
    @Setter
    private String costUnit;    //价格单位的json字段{salesId:1, name:EA, quantityId:1, quantityName:1}

    @Setter
    @Getter
    private Integer status;     //模块配额的状态0:不同意,1:同意

    @Setter
    @Getter
    private Date createdAt;     //创建时间

    @Setter
    @Getter
    private Date updatedAt;     //修改时间

    public enum Status{
        DISAGREE(0 , "不同意"), ACCEPT(1 , "同意");

        private final Integer value;

        private final String description;

        private Status(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Status from(Integer value){
            for(Status status : Status.values()){
                if(Objects.equal(value , status.value)){
                    return status;
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
