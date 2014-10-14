/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.auth;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.console.model.User;
import io.terminus.pampas.console.service.UserService;
import io.terminus.pampas.webc.extention.UserExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/8
 */
@Component
@Primary
public class ConsoleUserExt implements UserExt {
    @Autowired
    private UserService userService;

    @Override
    public BaseUser getUser(String appKey, Long userId) {
        User user = userService.findById(userId);
        return LoginUser.fromUser(user);
    }
}
