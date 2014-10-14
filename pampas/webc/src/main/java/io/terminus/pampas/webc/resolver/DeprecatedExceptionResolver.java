/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.resolver;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.UserNotLoginException;
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
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-5
 */
public class DeprecatedExceptionResolver extends ExceptionHandlerExceptionResolver {

    private String defaultErrorView;

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

        if (handlerMethod == null) {
            return null;
        }

        Method method = handlerMethod.getMethod();

        if (method == null) {
            return null;
        }

        ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
        if (Objects.equal(request.getHeader(HttpHeaders.X_REQUESTED_WITH), "XMLHttpRequest")
                || responseBodyAnn != null) {   //ajax or restful request
            response.setContentType(MediaType.JSON_UTF_8.toString());

            PrintWriter out = null;
            try {
                out = response.getWriter();
                if (exception instanceof JsonResponseException) {
                    JsonResponseException jsonEx = (JsonResponseException) exception;
                    response.setStatus(Objects.firstNonNull(jsonEx.getStatus(), 500));
                    out.print(Objects.firstNonNull(jsonEx.getMessage(), "失败了"));
                } else if (exception instanceof ServiceException) {
                    ServiceException serviceEx = (ServiceException) exception;
                    response.setStatus(500);
                    out.print(Objects.firstNonNull(serviceEx.getMessage(), "失败了"));
                } else if (exception instanceof BindException) {
                    BindException bindException = (BindException) exception;
                    BindingResult result = bindException.getBindingResult();
                    response.setStatus(400);
                    out.print(Objects.firstNonNull(result.getFieldError().getDefaultMessage(), "失败了"));
                } else if (exception instanceof UserNotLoginException) {
                    response.setStatus(500);
                    out.println("用户未登录");
                } else {
                    response.setStatus(500);
                    out.println("失败了");
                }
                return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }

        //处理特殊的status code
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            try {
                return resolveResponseStatus(responseStatus, response, exception);
            } catch (Exception resolveEx) {
                logger.warn("Handling of @ResponseStatus resulted in Exception", resolveEx);
            }
        }
        response.setStatus(500);
        return new ModelAndView(defaultErrorView, ImmutableMap.of("ex", exception));

    }


    protected ModelAndView resolveResponseStatus(ResponseStatus responseStatus,
                                                 HttpServletResponse response, Exception ex) throws Exception {

        int statusCode = responseStatus.value().value();
        /*String reason = responseStatus.reason();
        if (!StringUtils.hasLength(reason)) {
            response.sendError(statusCode);
        }else {
            response.sendError(statusCode, reason);
        }*/
        response.setStatus(statusCode);
        return new ModelAndView(defaultErrorView, ImmutableMap.of("ex", ex));
    }
}

