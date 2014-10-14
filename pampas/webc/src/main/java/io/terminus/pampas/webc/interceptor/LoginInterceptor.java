/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.interceptor;

import com.google.common.base.Strings;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.webc.extention.UserExt;
import io.terminus.pampas.webc.utils.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 应该被插入在 CookieInterceptor 之后
 * Author: jlchen
 * Date: 2013-01-22
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private final UserExt userExt;

    @Autowired
    public LoginInterceptor(UserExt userExt) {
        this.userExt = userExt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String sessionId = UserUtil.getInnerCookie().get(LoginInfo.SESSION_ID);
        if (Strings.isNullOrEmpty(sessionId)) {
            UserUtil.putCurrentUser(null);
            return true;
        }
        LoginInfo loginInfo = LoginInfo.fromCookieKey(sessionId);
        BaseUser user = userExt.getUser(Setting.getCurrentAppKey(), loginInfo.getUserId());
        UserUtil.putCurrentUser(user);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtil.clearCurrentUser();
    }
}
