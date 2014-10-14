package io.terminus.snz.requirement.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Desc:审核详情信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-02.
 */
@ToString
@EqualsAndHashCode
public class AuditMessage implements Serializable{

    private static final long serialVersionUID = 2731377235105834105L;

    @Setter
    @Getter
    private Long requirementId;         //需求编号

    @Setter
    @Getter
    private String requirementName;     //需求名称

    @Setter
    @Getter
    private Integer status;             //审核状态

    @Setter
    @Getter
    private Long senderId;              //消息发送者

    @Setter
    @Getter
    private List<Long> receiverIds;     //消息接受者

    public AuditMessage(){}

    public AuditMessage(Requirement requirement , Long senderId, List<Long> receiverIds){
        this.requirementId = requirement.getId();
        this.requirementName = requirement.getName();
        this.status = requirement.getCheckResult();
        this.senderId = senderId;
        this.receiverIds = receiverIds;
    }
}
