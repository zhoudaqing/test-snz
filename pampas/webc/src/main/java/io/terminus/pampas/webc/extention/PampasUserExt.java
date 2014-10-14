/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.extention;

import io.terminus.pampas.client.AgentUserService;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.engine.utils.DubboHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-9
 */
@Component
public class PampasUserExt implements UserExt {
    @Autowired
    private DubboHelper dubboHelper;

    @Override
    public BaseUser getUser(String appKey, Long userId) {
        AgentUserService agentUserService = dubboHelper.getReference(AgentUserService.class, appKey);
        return agentUserService.getUser(userId);
    }
}
