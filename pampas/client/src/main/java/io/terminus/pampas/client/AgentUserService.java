/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import io.terminus.pampas.common.BaseUser;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-6
 */
public interface AgentUserService {
    BaseUser getUser(Long userId);
}
