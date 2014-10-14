package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:最终采购商确认的供应商名次&相对于整体需求的分配比例
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@ToString
@EqualsAndHashCode
public class RequirementRank implements Serializable {
    private static final long serialVersionUID = -6037995437237605989L;

    @Setter
    @Getter
    private Long id;            //自增主键

    @Setter
    @Getter
    private Long requirementId; //需求编号

    @Setter
    @Getter
    private Integer rank;       //名次（由采购商设定名次:1,2,3...名次）

    @Setter
    @Getter
    private Integer type;       //名次类型（1:正选供应商，2:备选供应商）

    @Setter
    @Getter
    private Long supplierId;    //供应商编号

    @Setter
    @Getter
    private String supplierName;//供应商名称

    @Setter
    @Getter
    private Integer quotaScale; //配额比例数据信息

    @Setter
    @Getter
    private String selectReason;//选择该方案的原因

    @Setter
    @Getter
    private String selectFile;  //选择该方案的上传文档

    @Setter
    @Getter
    private Date createdAt;     //创建时间

    @Setter
    @Getter
    private Date updatedAt;     //修改时间

    //供应商名次排名类型
    public enum Type{
        OFFICIAL(1 , "正选供应商"), REPLACE(2 , "备选供应商");

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
}
