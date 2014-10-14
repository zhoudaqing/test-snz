package io.terminus.snz.web.interceptors;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Author: jlchen
 * Date: 2013-01-22
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private final static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    public static final String SESSION_USER_ID = "session_user_id";

    /**
     * sso session id from hac
     */
    public static final String SSO_SESSION_ID = "sso_session_id";

    /**
     * cas current hack, store it here
     */
    public static final String CAS_CURRENT_URL = "cas_current_url";

    private final AccountService<User> accountService;

    @Autowired
    public LoginInterceptor(AccountService<User> accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private ConfigManager configManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {

            Object urlObj = session.getAttribute(LoginInterceptor.CAS_CURRENT_URL);
            if(urlObj != null && !("/login").equals(request.getRequestURI())){
                configManager.getFrontConfig().getHrefs().put(LoginInterceptor.CAS_CURRENT_URL,(String)urlObj);
            }

            //假如本系统登入的用户通过获取SESSION_USER_ID获取用户信息
            Object userId = session.getAttribute(SESSION_USER_ID);
            if (userId != null) {
                Response<User> result = accountService.findUserById(Long.valueOf(userId.toString()));
                if (!result.isSuccess()) {
                    log.error("failed to find user where id={},error code:{}", userId, result.getError());

                    //在本系统未找到用户清空session数据(这步处理是解决user.ihaier.com有用户，在本系统无该用户的情况)
                    request.getSession().setAttribute(SESSION_USER_ID , null);

                    return true;
                }

                setUserView(result.getResult());
            }else {
                //还未登入的用供应商昵称获取用户数据
                String nick = request.getRemoteUser();
                if (nick != null) {
                    Response<User> userRes = accountService.findUserByNick(nick);
                    if (!userRes.isSuccess()) {
                        log.error("failed to find user where nick={},error code:{}", nick, userRes.getError());

                        //在本系统未找到用户清空session数据
                        request.getSession().setAttribute(SESSION_USER_ID, null);

                        return true;
                    }

                    setUserView(userRes.getResult());
                }
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtil.clearCurrentUser();
    }

    /**
     * 向UserUtil写入登入用户信息
     * @param user 用户数据
     */
    private void setUserView(User user){
        if (user != null) {
            BaseUser baseUser = new BaseUser(user.getId(), user.getName(), user.getType());
            baseUser.setRoles(user.buildRoles());
            baseUser.setParentId(user.getParentId());
            baseUser.setTags(user.getTags());
            baseUser.setMobile(user.getMobile());
            baseUser.setNickName(user.getNick());
            UserUtil.putCurrentUser(baseUser);
        }
    }
}
