package io.terminus.snz.requirement.event;

import io.terminus.snz.requirement.dto.RequirementDto;
import io.terminus.snz.requirement.model.AuditMessage;
import io.terminus.snz.requirement.model.CountMessage;
import io.terminus.snz.requirement.model.Requirement;
import io.terminus.snz.requirement.model.RequirementMessage;

/**
 * Desc:需求eventBus处理事件
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-09.
 */
public interface RequirementEvents {
    /**
     * 通过订阅requirement的create事件，当创建成功时会回调解析excel。并且将数据导入DB
     * @param requirementDto 需求详细信息对象
     */
    public void analyzeEvent(RequirementDto requirementDto);

    /**
     * 通过订阅需求的状态切换消息对象实现异步向消息中心推送消息
     * @param requirementMessage 需求的消息对象
     */
    public void transitionEvent(RequirementMessage requirementMessage);

    /**
     * 订阅需求审核处理流程的实现异步向消息中心推送信息
     * @param auditMessage 审核的消息对象
     */
    public void auditEvent(AuditMessage auditMessage);

    /**
     * 计算某个需求的正选供应商数量&备选供应商(并保存到需求中)
     * @param requirement 需求供应商
     */
    public void countSelectNum(Requirement requirement);

    /**
     * 写入需求的统计数据信息
     * @param countMessage  需求统计数据
     */
    public void countEvent(CountMessage countMessage);
}
