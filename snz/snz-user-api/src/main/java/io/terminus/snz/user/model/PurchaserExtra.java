package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 采购商额外信息表
 * Author:Guo Chaopeng
 * Created on 14-6-6.
 */
@ToString
public class PurchaserExtra implements Serializable {

    private static final long serialVersionUID = 6138868297631935912L;
    @Setter
    @Getter
    private Long id;                //主键

    @Setter
    @Getter
    private Long userId;            //对应用户编号

    @Setter
    @Getter
    private String employeeId;      //工号

    @Setter
    @Getter
    private String leader;          //领导

    @Setter
    @Getter
    private String department;      //所属部门

    @Setter
    @Getter
    private String position;        //职位

    @Getter
    @Setter
    private Date createdAt;         //创建时间

    @Getter
    @Setter
    private Date updatedAt;         //修改时间

}
