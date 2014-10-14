/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.client.ParamUtil;
import io.terminus.pampas.engine.config.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-12
 */
@Component
@Slf4j
public class SpringExecutor extends Executor<Object> {
    private static final Splitter splitter = Splitter.on(':').trimResults().omitEmptyStrings();

    protected static class ServiceInfo {
        private final Class<?> klass;   //类名

        private final Method method;    //方法

        private final Class<?>[] types;  //参数类型数组

        private final String[] paramNames;    //参数名称数组

        public ServiceInfo(Class<?> klass, Method method, Class<?>[] types, String[] paramNames) {
            this.klass = klass;
            this.method = method;
            this.types = types;
            this.paramNames = paramNames;
        }

        public Class<?> getKlass() {
            return klass;
        }

        public Method getMethod() {
            return method;
        }

        public Class<?>[] getTypes() {
            return types;
        }

        public String[] getParamNames() {
            return paramNames;
        }
    }

    /**
     * api string to service info cache
     */
    private final LoadingCache<String, ParamUtil.MethodInfo> methodInfos;

    private DefaultConversionService converter = new DefaultConversionService();

    @Autowired
    private ApplicationContext applicationContext;

    public SpringExecutor() {
        CacheLoader<String, ParamUtil.MethodInfo> loader = new CacheLoader<String, ParamUtil.MethodInfo>() {

            @Override
            public ParamUtil.MethodInfo load(String key) throws Exception {

                List<String> parts = splitter.splitToList(key);
                if (parts.size() != 2) {
                    throw new IllegalArgumentException("bad api format,should be interfaceName:methodName,but is: " + key);
                }
                Class<?> klass = Class.forName(parts.get(0));
                Object bean = applicationContext.getBean(klass);
                Method method = findMethodByName(klass, parts.get(1));
                if (method == null) {
                    throw new NoSuchMethodException("failed to find method: " + key);
                }

                return ParamUtil.getMethodInfo(bean, method);
            }
        };

        this.methodInfos = CacheBuilder.newBuilder().build(loader);
    }


    public Object exec(Service service, Map<String, Object> params) {
        String api = service.getUri();
        if (Strings.isNullOrEmpty(api)) return null;

        ParamUtil.MethodInfo methodInfo = methodInfos.getUnchecked(api);

        LinkedHashMap<String, ParamUtil.ParamInfo> paramsInfo = methodInfo.getParams();
        Object[] concernedParams = new Object[paramsInfo.size()];
        int index = 0;
        for (String paramName : paramsInfo.keySet()) {
            ParamUtil.ParamInfo paramInfo = paramsInfo.get(paramName);
            // 先转一次
            Object param = ParamConverter.convertParam(paramName, paramInfo.getClazz(), params);
            // 再进行 json -> 对象和 params -> 对象的处理
            concernedParams[index++] = ParamUtil.convert(param, paramInfo.getClazz(), paramInfo.getJavaType(), params);
        }

        Object object;
        try {
            object = methodInfo.getMethod().invoke(methodInfo.getBean(), concernedParams);
        } catch (IllegalAccessException e) {
            log.error("illegal access method, service: {}", service, e);
            throw new ServiceException(e);
        } catch (InvocationTargetException e) {
            log.error("invocation target exception, service: {}", service, e);
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
            throw new ServiceException(e.getTargetException().getMessage(), e);
        }

        return unwrapResponse(object);
    }

    private Method findMethodByName(Class<?> beanClazz, String methodName) {
        Method[] methods = beanClazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
