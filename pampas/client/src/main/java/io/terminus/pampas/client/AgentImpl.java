/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import com.alibaba.dubbo.config.spring.ServiceBean;
import io.terminus.pampas.common.InnerCookie;
import io.terminus.pampas.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-29
 */
@Slf4j
public class AgentImpl implements Agent, InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private Map<String, ParamUtil.MethodInfo> methodInfoMap = new HashMap<String, ParamUtil.MethodInfo>();

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // find and register dubbo ServiceBean
        Map<String, ServiceBean> serviceBeans = applicationContext.getBeansOfType(ServiceBean.class);
        for (ServiceBean serviceBean : serviceBeans.values()) {
            Object bean = serviceBean.getRef();
            Class interfaceClass = serviceBean.getInterfaceClass();
            log.debug("dubbo serviceBean found: {}", interfaceClass.getName());
            registerMethod(bean, interfaceClass);
        }
        // find and register bean with annotation @Export
        Map<String, Object> annotationBeans = applicationContext.getBeansWithAnnotation(Export.class);
        for (Object bean : annotationBeans.values()) {
            Class clazz = bean.getClass();
            log.debug("bean with @Export found: {}", clazz.getName());
            registerMethod(bean, clazz);
        }
    }

    private void registerMethod(Object bean, Class clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            ParamUtil.MethodInfo methodInfo = ParamUtil.getMethodInfo(bean, method);
            if (methodInfo == null) {
                continue;
            }
            String key = clazz.getName() + ":" + method.getName();
            methodInfoMap.put(key, methodInfo);
            log.debug("method exported: {}", key);
        }
    }

    @Override
    public LinkedHashMap<String, String> getParamsInfo(String key) {
        ParamUtil.MethodInfo methodInfo = methodInfoMap.get(key);
        if (methodInfo == null) {
            throw new NullPointerException("method not found: " + key);
        }
        LinkedHashMap<String, String> paramsInfo = new LinkedHashMap<String, String>();
        for (String paramKey : methodInfo.getParams().keySet()) {
            paramsInfo.put(paramKey, methodInfo.getParams().get(paramKey).getClazz().getName());
        }
        return paramsInfo;
    }

    @Override
    public WrapResp call(String key, Map<String, Object> params, Map<String, Object> context) {
        ParamUtil.MethodInfo methodInfo = methodInfoMap.get(key);
        if (methodInfo == null) {
            throw new NullPointerException("method not found: " + key);
        }
        WrapResp resp = new WrapResp();
        Object[] args = new Object[methodInfo.getParams().size()];
        int index = 0;
        for (String paramName : methodInfo.getParams().keySet()) {
            ParamUtil.ParamInfo paramInfo = methodInfo.getParams().get(paramName);
            Object arg = params.get(paramName);
            // 如果是 InnerCookie ，得带回去，service 中可通过操作 InnerCookie 来达到操作 Cookie 的目的
            if (arg instanceof InnerCookie) {
                resp.setCookie((InnerCookie) arg);
            }
            args[index++] = ParamUtil.convert(arg, paramInfo.getClazz(), paramInfo.getJavaType(), context);
        }
        Object result;
        try {
            result = methodInfo.getMethod().invoke(methodInfo.getBean(), args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("error when invoke method: " + key, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("error when invoke method: " + key, e);
        }
        if (result == null) {
            return resp;
        }
        // 如果是 Action 的实例，则要转化内层的 data
        if (result instanceof Action) {
            Action action = (Action) result;
            action.setData(ParamUtil.convertResult(action.getData()));
            resp.setResult(action);
            return resp;
        }
        // if instanceof Response, convert inner result
        if (result instanceof Response) {
            Response response = (Response) result;
            Response<Object> realResponse = new Response<Object>();
            realResponse.setSuccess(response.isSuccess());
            realResponse.setError(response.getError());
            realResponse.setResult(ParamUtil.convertResult(response.getResult()));
            resp.setResult(realResponse);
            return resp;
        }
        resp.setResult(ParamUtil.convertResult(result));
        return resp;
    }
}
