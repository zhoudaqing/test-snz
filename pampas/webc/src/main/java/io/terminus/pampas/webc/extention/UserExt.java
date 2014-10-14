/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.extention;

import io.terminus.pampas.common.BaseUser;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-9
 */
public interface UserExt {
    /**
     * 按照 appKey 和 userKey 去获取一个用户
     * @param appKey appKey
     * @param userId userId
     * @return BaseUser
     */
    BaseUser getUser(String appKey, Long userId);
}
