/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.auth;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.terminus.auth.core.AuthExecutor;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/9
 */
@Component
public class AuthHelpers {
    @Autowired
    private HandlebarEngine handlebarEngine;
    @Autowired
    private AuthExecutor authExecutor;

    @PostConstruct
    public void init() {
        handlebarEngine.registerHelper("hasPermission", new Helper<String>() {
            @Override
            public CharSequence apply(String permission, Options options) throws IOException {
                Long userId = UserUtil.getUserId();
                if (authExecutor.hasPermission(userId, permission)) {
                    return options.fn();
                } else {
                    return options.inverse();
                }
            }
        });

        handlebarEngine.registerHelper("hasRole", new Helper<String>() {
            @Override
            public CharSequence apply(String role, Options options) throws IOException {
                Long userId = UserUtil.getUserId();
                if (authExecutor.hasRole(userId, role)) {
                    return options.fn();
                } else {
                    return options.inverse();
                }
            }
        });
    }

    public void checkPermission(String permission) {
        Long userId = UserUtil.getUserId();
        if (!authExecutor.hasPermission(userId, permission)) {
            throw new ServiceException("permission denied");
        }
    }
}
