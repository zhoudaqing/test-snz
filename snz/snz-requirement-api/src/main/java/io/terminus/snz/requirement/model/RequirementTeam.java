package io.terminus.snz.requirement.model;

import com.google.common.base.Objects;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Desc:需求设定中对于TQRDC的管理人员设定
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-01.
 */
@ToString
@EqualsAndHashCode
public class RequirementTeam implements Serializable {
    private static final long serialVersionUID = 1902741443617218284L;

    @Setter
    @Getter
    private Long id;                //自增主键

    @Setter
    @Getter
    private Long requirementId;     //需求编号

    @Setter
    @Getter
    private String requirementName; //需求名称

    @Setter
    @Getter
    private Integer type;           //绑定的节点类型（1:T技术,2:Q质量,3:R互动,4:D产能,5:C成本）

    @Setter
    @Getter
    private Long userId;            //队员编号

    @Setter
    @Getter
    private String userName;        //队员名称

    @Setter
    @Getter
    private String userNumber;     //队员工号

    @Setter
    @Getter
    private String userPhone;       //队员联系电话

    @Setter
    @Getter
    private Date createdAt;          //创建时间

    @Setter
    @Getter
    private Date updatedAt;          //修改时间

    public enum Type{
        T(1 , "技术"), Q(2 , "质量"), R(3 , "互动"), D(4 , "产能"), C(5 , "成本");

        private final Integer value;

        private final String description;

        private Type(Integer value , String description){
            this.value = value;

            this.description = description;
        }

        public static Type from(Integer value){
            for(Type type : Type.values()){
                if(Objects.equal(value , type.value)){
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
