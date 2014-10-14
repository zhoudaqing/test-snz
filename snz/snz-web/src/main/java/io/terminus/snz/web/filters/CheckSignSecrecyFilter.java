package io.terminus.snz.web.filters;

import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.RequirementSolution;
import io.terminus.snz.requirement.service.RequirementSolutionService;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import io.terminus.snz.web.interceptors.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Desc:对保密协议的签订的过滤（判断用户是否已经签订保密协议,当访问某些需求信息时）
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-11.
 */
@Slf4j
public class CheckSignSecrecyFilter implements Filter {
    //重定向页面跳转
    private String redirectURL = null;

    private RequirementSolutionService requirementSolutionService;

    private AccountService accountService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());

        requirementSolutionService = (RequirementSolutionService) context.getBean("requirementSolutionServiceImpl");
        accountService = (AccountService) context.getBean("accountService");
        redirectURL = filterConfig.getInitParameter("redirectURL");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;

        HttpServletResponse response = (HttpServletResponse)res;

        HttpSession session = request.getSession();

        try{
            //用户编号
            Long userId = (Long)session.getAttribute(LoginInterceptor.SESSION_USER_ID);

            //需求编号
            Long requirementId = Long.parseLong(request.getParameter("requirementId"));

            if(userId == null){
                //还未登入的此处不检测，让loginFilter去处理
                chain.doFilter(req , res);
                return;
            }else{
                //校验用户数据是否符合
                Response<User> userRes = accountService.findUserById(userId);
                if(!userRes.isSuccess()){
                    log.error("query user info failed, userId={}, error code={}", userId, userRes.getError());
                    response.sendRedirect(request.getContextPath() + redirectURL + "?requirementId="+requirementId);
                }else{
                    User user = userRes.getResult();
                    //是否是供应商
                    if(Objects.equal(User.Type.from(user.getType()) , User.Type.SUPPLIER)){
                        Response<RequirementSolution> solutionResponse = requirementSolutionService.findSolutionBySupplier(requirementId , user);

                        if(!solutionResponse.isSuccess() || solutionResponse.getResult() == null){
                            //还未签订保密协议返回保密协议页面
                            response.sendRedirect(request.getContextPath() + redirectURL + "?requirementId="+requirementId);
                        }else {
                            chain.doFilter(req , res);
                        }
                    }else{
                        chain.doFilter(req , res);
                    }
                }
            }
        }catch (NumberFormatException f){
            log.error("find requirementId & userId failed , error code={}", Throwables.getStackTraceAsString(f));
        }catch (Exception e){
            log.error("filter requirement failed , error code={}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void destroy() {

    }
}
