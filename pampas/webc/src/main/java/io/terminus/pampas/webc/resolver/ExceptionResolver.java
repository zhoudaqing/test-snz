/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.resolver;

import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.UserNotLoginException;
import io.terminus.pampas.engine.MessageSources;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-5
 */
public class ExceptionResolver extends ExceptionHandlerExceptionResolver {

    private String defaultErrorView;

    private static final String ERROR_DEFAULT = "失败了";

    private static final String ERROR_ARGS = "非法参数";

    private static final String ERROR_USER_NOT_LOGIN = "用户未登录";

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    @Autowired
    private MessageSources messageSources;

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

        if (handlerMethod == null) {
            return null;
        }

        Method method = handlerMethod.getMethod();

        if (method == null) {
            return null;
        }

        Error error = buildError(exception);

        //处理特殊的status code
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            error.setStatus(responseStatus.value().value());
        }

        // ajax
        ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        if (Objects.equal(request.getHeader(HttpHeaders.X_REQUESTED_WITH), "XMLHttpRequest")
                || responseBodyAnn != null) {   //ajax or restful request
            PrintWriter out = null;
            try {
                response.setContentType(MediaType.JSON_UTF_8.toString());
                response.setStatus(error.getStatus());
                out = response.getWriter();
                out.print(error.getMsg());
                return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
        return new ModelAndView(defaultErrorView, ImmutableMap.of("error", error));
    }

    /**
     * 获取异常的错误信息
     * @param exception 异常对象
     * @return messageSources.get后的内容
     */
    private Error buildError(Exception exception) {
        if (exception instanceof JsonResponseException) {
            JsonResponseException jsonEx = (JsonResponseException) exception;
            Integer status = MoreObjects.firstNonNull(jsonEx.getStatus(), 500);
            return new Error(status, MoreObjects.firstNonNull(messageSources.get(jsonEx.getMessage()), ERROR_DEFAULT));
        } else if (exception instanceof ServiceException) {
            ServiceException serviceEx = (ServiceException) exception;
            return new Error(500, MoreObjects.firstNonNull(messageSources.get(serviceEx.getMessage()), ERROR_DEFAULT));
        } else if (exception instanceof BindException) {
            BindException bindException = (BindException) exception;
            BindingResult result = bindException.getBindingResult();
            return new Error(400, MoreObjects.firstNonNull(messageSources.get(result.getFieldError().getDefaultMessage()), ERROR_DEFAULT));
        } else if(exception instanceof UserNotLoginException){
            return new Error(500, ERROR_USER_NOT_LOGIN);
        } else if(exception instanceof IllegalArgumentException){
            return new Error(400, MoreObjects.firstNonNull(messageSources.get(exception.getMessage()), ERROR_ARGS));
        } else if(exception instanceof RpcException){
            RpcException rpcEx = (RpcException)exception;
            if (rpcEx.getCause() instanceof ConstraintViolationException){
                ConstraintViolationException cve = (ConstraintViolationException)exception.getCause();
                Set<ConstraintViolation<?>> violations = cve
                        .getConstraintViolations();
                String firstError = messageSources.get(violations.iterator().next().getMessage());
                return new Error(400, firstError);
            }
        }
        return new Error(500, ERROR_DEFAULT);
    }

    @Data @AllArgsConstructor
    private static class Error{

        private Integer status;     //状态码

        private String msg;       //错误信息
    }
}
