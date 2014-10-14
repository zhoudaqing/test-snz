package io.terminus.snz.requirement.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Desc:参与讨论的对象（包括需求的创建者&团队成员，供应商人员（公司名称））
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-18.
 */
@ToString
@EqualsAndHashCode
public class TopicUser implements Serializable {
    private static final long serialVersionUID = 5848217102912366259L;

    @Getter
    @Setter
    private Long userId;        //用户变好

    @Getter
    @Setter
    private String userName;    //用户名称

    @Getter
    @Setter
    private Integer userType;   //用户类型

    @Getter
    @Setter
    private Long companyId;     //公司编号

    @Getter
    @Setter
    private String companyName; //公司名称
}
