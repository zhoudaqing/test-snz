package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;


/**
 * Description：联系人信息
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午11:30
 */
@ToString
public class ContactInfo implements Serializable {

    private static final long serialVersionUID = 6724520881049806455L;

    @Getter
    @Setter
    private Long id;                       //自增主键

    @Getter
    @Setter
    @NotBlank
    private String name;                   //联系人

    @Getter
    @Setter
    private String department;             //联系人部门

    @Getter
    @Setter
    private String duty;                   //联系人职务

    @Getter
    @Setter
    @NotBlank
    private String mobile;                 //联系人手机

    @Getter
    @Setter
    @NotBlank
    private String officePhone;            //联系人办公电话

    @Getter
    @Setter
    @NotBlank
    private String email;                  //联系人邮箱

    @Getter
    @Setter
    private Long companyId;                //企业编号

    @Getter
    @Setter
    private Long userId;                   //用户编号

    @Getter
    @Setter
    private Date createdAt;                //创建时间

    @Getter
    @Setter
    private Date updatedAt;                //修改时间

}