package io.terminus.snz.haier.event;

import io.terminus.snz.requirement.model.Requirement;

/**
 * Desc:海尔eventBus处理事件
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-16.
 */
public interface PLMEvents {
    /**
     * 通过订阅需求锁定事件时，想海尔的PLM系统中间表写入数据
     * @param requirement 需求详细信息对象
     */
    public void sendModuleToPLM(Requirement requirement);
}
