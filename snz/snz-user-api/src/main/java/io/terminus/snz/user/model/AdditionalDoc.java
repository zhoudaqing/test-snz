package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * Description：财务证明材料
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午14:27
 */
@ToString
public class AdditionalDoc implements Serializable {

    private static final long serialVersionUID = -886445988476492783L;

    @Getter
    @Setter
    private Long id;                 //自增主键

    @Getter
    @Setter
    private Long userId;             //用户编号

    @Getter
    @Setter
    private Long financeId;         //公司财务编号

    @Getter
    @Setter
    private String name;             //材料名

    @Getter
    @Setter
    private String files;            //附件

    @Getter
    @Setter
    private String comment;          //描述

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

}