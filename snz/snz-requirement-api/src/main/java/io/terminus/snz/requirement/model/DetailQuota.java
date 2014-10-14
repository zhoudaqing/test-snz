package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:详细的配额信息（抛给SAP的数据）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
@ToString
@EqualsAndHashCode
public class DetailQuota implements Serializable {
    private static final long serialVersionUID = -2545325467484663531L;

    @Getter
    @Setter
    private Long id;                    //自增主键

    @Getter
    @Setter
    private Long requirementId;         //需求编号

    @Getter
    @Setter
    private String requirementName;     //需求名称

    @Getter
    @Setter
    private Long solutionId;            //方案编号方便查询

    @Getter
    @Setter
    private Long moduleId;              //模块编号

    @Getter
    @Setter
    private String moduleNum;           //模块专用号

    @Getter
    @Setter
    private String moduleName;          //模块名称

    @Getter
    @Setter
    private String purchaseOrg;         //采购组织

    @Getter
    @Setter
    private String factoryNum;          //工厂编码

    @Getter
    @Setter
    private Long supplierId;            //供应商编号

    @Getter
    @Setter
    private String supplierVCode;       //供应商V码

    @Getter
    @Setter
    private String supplierName;        //供应商名称

    @Getter
    @Setter
    private String purchaseType;        //采购类型

    @Getter
    @Setter
    private Integer quantity;           //配额数量

    @Getter
    @Setter
    private Integer scale;              //模块配额的占总配额的比例70,50

    @Getter
    @Setter
    private Integer originalCost;       //用户的模块报价

    @Getter
    @Setter
    private Integer actualCost;         //实际模块跟标价格

    @Getter
    @Setter
    private Integer agencyFee;          //代理费

    @Getter
    @Setter
    private Integer duty;               //关税

    @Getter
    @Setter
    private Integer otherFee;           //杂费

    @Getter
    @Setter
    private Integer feeUnit;            //采购价单位

    @Getter
    @Setter
    private String purchaseUnit;        //采购单位

    @Getter
    @Setter
    private String coinType;            //采购币种

    @Getter
    @Setter
    private Integer purchaseDay;        //采购期

    @Getter
    @Setter
    private String purchaseTeam;        //采购组

    @Getter
    @Setter
    private String basicUnit;           //基本单位

    @Getter
    @Setter
    private String taxCode;             //税码

    @Getter
    @Setter
    private String dataType;            //配额的数据类型

    @Getter
    @Setter
    private Integer status;             //状态（0:待提交，1:已填写完整，2:提交到SAP）

    @Getter
    @Setter
    private Date createdAt;             //创建时间

    @Getter
    @Setter
    private Date updatedAt;             //修改时间

    public enum Status{
        WAIT_SUBMIT(0 , "待提交"), SUBMIT(1, "已填写完整"), SEND_SAP(2, "提交到SAP");

        private final Integer value;

        private final String description;

        private Status(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Status from(Integer value){
            for(Status status : Status.values()){
                if(Objects.equal(value, status.value)){
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
