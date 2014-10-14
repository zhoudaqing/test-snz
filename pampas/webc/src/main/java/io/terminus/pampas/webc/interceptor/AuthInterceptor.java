/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.interceptor;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.*;
import io.terminus.pampas.engine.exception.UnAuthorize401Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;

/*
 * 应该被插入在 LoginInterceptor 之后
 * Author: jlchen
 * Date: 2013-01-23
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private ConfigManager configManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI().substring(request.getContextPath().length());
        BaseUser user = UserUtil.getCurrentUser();
        Auths auths = configManager.getFrontConfig().getAuths();
        // 没有设置权限就全部返回 true
        if (auths == null) {
            return true;
        }

        HttpMethod method = HttpMethod.valueOf(request.getMethod().toUpperCase());
        if (auths.getWhiteList() != null) {
            for (WhiteAuth whiteAuth : auths.getWhiteList()) {
                // url 匹配，method 是 ALL 或者匹配
                if (whiteAuth.getRegexPattern().matcher(requestURI).matches()
                        && (whiteAuth.getMethods().contains(HttpMethod.ALL) || whiteAuth.getMethods().contains(method))) {
                    return true;
                }
            }
        }

        boolean inProtectedList = false;
        if (auths.getProtectedList() != null) {
            for (ProtectedAuth protectedAuth : auths.getProtectedList()) {
                if (protectedAuth.getRegexPattern().matcher(requestURI).matches()) {
                    inProtectedList = true;
                    if (user != null) {    //用户已登陆
                        if (typeMatch(protectedAuth.getTypes(), user.getTypeEnum())) { //用户类型匹配
                            return true;
                        }
                        if (roleMatch(protectedAuth.getRoles(), user.getRoles())) { //用户角色匹配
                            return true;
                        }
                    } else { //用户未登陆
                        redirectToLogin(request, response);
                        return false;
                    }
                }
            }
        }

        if (inProtectedList) {
            //能进入这里的,说明接口受到保护,用户已登陆,但用户角色不匹配,因此抛出鉴权失败的异常
            throw new UnAuthorize401Exception("您无权进行此操作");
        }

        if (!Objects.equal(method, "get") && user == null) { //write operation need login
            redirectToLogin(request, response);
            return false;
        }
        return true;
    }

    private boolean typeMatch(Set<BaseUser.TYPE> expectedType, BaseUser.TYPE actualType) {
        return (expectedType.contains(BaseUser.TYPE.ALL) || expectedType.contains(actualType));
    }

    private boolean roleMatch(Set<String> expectedRoles, List<String> actualRoles) {
        for (String actualRole : actualRoles) {
            if(expectedRoles.contains(actualRole)){
                return true;
            }
        }
        return false;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAjaxRequest(request)) {
            throw new JsonResponseException(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
        }
        String currentUrl = request.getRequestURL().toString();

        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            currentUrl = currentUrl + "?" + request.getQueryString();
        }

        FrontConfig frontConfig = configManager.getFrontConfig();
        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString(frontConfig.getCurrentHrefs(Setting.getCurrentDomain()).get(FrontConfig.HREF_LOGIN) + "?target={target}").build();
        URI uri = uriComponents.expand(currentUrl).encode().toUri();
        response.sendRedirect(uri.toString());
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return Objects.equal(request.getHeader(HttpHeaders.X_REQUESTED_WITH), "XMLHttpRequest");
    }
}
