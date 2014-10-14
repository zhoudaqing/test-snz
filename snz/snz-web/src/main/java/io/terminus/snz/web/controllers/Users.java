package io.terminus.snz.web.controllers;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.snz.user.dto.UserDto;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.model.YzlCreditQualify;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.user.service.YzlCreditQualifyService;
import io.terminus.snz.web.interceptors.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-6.
 */
@Controller
@RequestMapping("/api/user")
@Slf4j
public class Users {

    @Autowired
    private MessageSources messageSources;

    @Autowired
    private AccountService<User> accountService;

    @Autowired
    private YzlCreditQualifyService yzlCreditQualifyService;

    @Value(value = "#{app.domain}")
    private String domain;

    /**
     * 登录
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String login(@RequestParam("nick") String nick, @RequestParam("passwd") String passwd,
                        @RequestParam(value = "target", required = false) String target,
                        HttpServletRequest request) {
        Response<UserDto> result = accountService.login(nick, passwd);

//单条件查询
//        Response<List<YzlCreditQualify>> resp = yzlCreditQualifyService.findByStatus(4);
//        for(YzlCreditQualify temp : resp.getResult()) {
//            System.out.println("**************************************************");
//            System.out.println(temp.getId()+temp.getUserId()+temp.getMessage());
//            System.out.println("**************************************************");
//        }

//单条删除

//       Response<Boolean> resp = yzlCreditQualifyService.deleteById(97);

//插入一条数据

//        YzlCreditQualify yzlCreditQualify = new YzlCreditQualify();
//        yzlCreditQualify.setUserId(88l);
//        yzlCreditQualify.setSupplierId(88888l);
//        yzlCreditQualify.setStatus(4);
//        yzlCreditQualify.setMessage("OMG!!!");
//        Response<Integer> resp = yzlCreditQualifyService.create(yzlCreditQualify);
//更改一条数据
//        YzlCreditQualify yzlCreditQualify = new YzlCreditQualify();
//        yzlCreditQualify.setId(95);
//        yzlCreditQualify.setMessage("abcddalksjflksadjfl;kasdjf");
//        Response<Integer> resp = yzlCreditQualifyService.update(yzlCreditQualify);

//批量删除数据
//        List<Integer> ids = Lists.newArrayList();
//        ids.add(91);
//        ids.add(93);
//        ids.add(95);
//        Response<Integer> resp = yzlCreditQualifyService.deleteByIds(ids);

//批量插入数据
//        List<YzlCreditQualify> yzls = Lists.newArrayList();
//        YzlCreditQualify yzlCreditQualify = new YzlCreditQualify();
//        yzlCreditQualify.setUserId(1111l);
//        yzlCreditQualify.setSupplierId(1111l);
//        yzlCreditQualify.setStatus(4);
//        yzlCreditQualify.setMessage("hello world!");
//        yzls.add(yzlCreditQualify);
//        yzlCreditQualify.setUserId(2222l);
//        yzlCreditQualify.setSupplierId(2222l);
//        yzlCreditQualify.setStatus(4);
//        yzlCreditQualify.setMessage("the day you went away!");
//        yzls.add(yzlCreditQualify);
//        Response<Integer> resp = yzlCreditQualifyService.creates(yzls);


        //分页测试
//        Response<Paging<YzlCreditQualify>> resp = yzlCreditQualifyService.findByPage(4, "went", 1, 5);
//        System.out.println(resp.getResult());
//        System.out.println("***********************************************");

        if (!result.isSuccess()) {
            log.error("failed to login user by nickname={},error code:{}", nick, result.getError());
            throw new JsonResponseException(500, messageSources.get(result.getError()));
        }
        UserDto userDto = result.getResult();
        User user = userDto.getUser();
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER_ID, user.getId());
        if (!Strings.isNullOrEmpty(userDto.getSsoSessionId())) {
            request.getSession().setAttribute(LoginInterceptor.SSO_SESSION_ID, userDto.getSsoSessionId());
        }
        String redirectUrl = redirectTarget(target);
        return redirectUrl;
    }

    /**
     * TODO: 非常恶心的方法实现注册后自动登录，以后一定要重构下！
     */
    @RequestMapping(value = "/dirty_login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String dirtyLogin(@RequestParam("nick") String nick, @RequestParam("passwd") String passwd,
                             @RequestParam(value = "target", required = false) String target,
                             HttpServletRequest request) {
        Response<UserDto> result = accountService.dirtyLogin(nick, passwd);

        if (!result.isSuccess()) {
            log.error("failed to login user by nickname={},error code:{}", nick, result.getError());
            throw new JsonResponseException(500, messageSources.get(result.getError()));
        }
        UserDto userDto = result.getResult();
        User user = userDto.getUser();
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER_ID, user.getId());
        if (!Strings.isNullOrEmpty(userDto.getSsoSessionId())) {
            request.getSession().setAttribute(LoginInterceptor.SSO_SESSION_ID, userDto.getSsoSessionId());
        }
        //return "redirect:/";
        return redirectTarget(target);
    }

    private String redirectTarget(String target) {
        if (!Strings.isNullOrEmpty(target)) {
            return target;
        } else {
            //return "http://" + domain;
            return "/";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        String ssoSessionId = null;
        try {
            HttpSession session = request.getSession(false);

            if (session != null) {
                ssoSessionId = String.valueOf(session.getAttribute(LoginInterceptor.SESSION_USER_ID));
                session.invalidate();
            }
            if (request.getServerPort() == 80) {
                return "redirect:http://" + domain;
            }
            return "redirect:http://" + domain + ":" + request.getServerPort();
        } catch (Exception e) {
            log.error("failed to logout user,cause:", e);
            throw new JsonResponseException(500, messageSources.get("user.logout.fail"));
        } finally {
            if (!Strings.isNullOrEmpty(ssoSessionId)) {
                // logout from sso
                accountService.logout(ssoSessionId);
            }
        }
    }
}
