/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.auth;

import io.terminus.auth.api.AuthorizeRealm;
import io.terminus.auth.api.UserIdProvider;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.console.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
@Component
public class AuthSupport implements UserIdProvider, AuthorizeRealm {

    @Autowired
    private UserService userService;

    @Override
    public List<String> getRoles(Long userId) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getPermissions(Long userId) {
        return userService.findPermissions(userId);
    }

    @Override
    public Long getUserId() {
        return UserUtil.getUserId();
    }
}
