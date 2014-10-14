package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:需求的消息中心传递数据对象
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-02.
 */
@ToString
@EqualsAndHashCode
public class RequirementMessage implements Serializable{

    private static final long serialVersionUID = 4062871073264319626L;

    @Setter
    @Getter
    private Long requirementId;         //需求编号

    @Setter
    @Getter
    private String requirementName;     //需求名称

    @Setter
    @Getter
    private Integer requirementStatus;  //需求状态

    @Setter
    @Getter
    private Long senderId;              //消息发送者

    @Setter
    @Getter
    private String companyScope;        //需求的前台类目列表(用于查询供应商范围)

    @Setter
    @Getter
    private List<Long> receiverIds;     //消息接受者

    public RequirementMessage(){}

    public RequirementMessage(Requirement requirement , Long senderId, List<Long> receiverIds){
        this.requirementId = requirement.getId();
        this.requirementName = requirement.getName();
        this.requirementStatus = requirement.getStatus();
        this.companyScope = requirement.getCompanyScope();
        this.senderId = senderId;
        this.receiverIds = receiverIds;
    }
}
