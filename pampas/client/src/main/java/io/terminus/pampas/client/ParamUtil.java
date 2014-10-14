/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.support.DefaultConversionService;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-7
 */
@Slf4j
public class ParamUtil {
    private static ObjectMapper mapper;
    private static JavaType mapType;
    private static Map<String, Class<?>> primitiveClassMap = new HashMap<String, Class<?>>();
    private static DefaultConversionService conversionService = new DefaultConversionService();

    static {
        mapper = new ObjectMapper();
        //设置输出时包含属性的风格
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapType = mapper.getTypeFactory().constructParametricType(HashMap.class, String.class, Object.class);

        primitiveClassMap.put("int", int.class);
        primitiveClassMap.put("long", long.class);
        primitiveClassMap.put("short", short.class);
        primitiveClassMap.put("byte", byte.class);
        primitiveClassMap.put("char", char.class);
        primitiveClassMap.put("boolean", boolean.class);
        primitiveClassMap.put("float", float.class);
        primitiveClassMap.put("double", double.class);
    }

    public static Class<?> getPrimitiveClass(String className) {
        return primitiveClassMap.get(className);
    }

    public static boolean isPrimitive(Object obj) {
        if (obj instanceof Class) {
            return (((Class) obj).isPrimitive()
                    || obj == String.class
                    || obj == Integer.class
                    || obj == Long.class
                    || obj == Short.class
                    || obj == Byte.class
                    || obj == Character.class
                    || obj == Boolean.class
                    || obj == Float.class
                    || obj == Double.class);
        } else {
            return (obj instanceof String
                    || obj instanceof Integer
                    || obj instanceof Long
                    || obj instanceof Short
                    || obj instanceof Byte
                    || obj instanceof Character
                    || obj instanceof Boolean
                    || obj instanceof Float
                    || obj instanceof Double);
        }
    }

    public static Object convert(Object param, Class clazz, JavaType javaType, Map<String, Object> context) {
        // 基本类型就转一下
        if (isPrimitive(clazz)) {
            return conversionService.convert(param, clazz);
        }
        // 非空且类型可访问就直接返回
        //noinspection unchecked
        if (param != null && clazz.isAssignableFrom(param.getClass())){
            return param;
        }
        // 有值并是 String ，则认为是 json 转一把
        if (param != null && param instanceof String) {
            try {
                if (javaType != null) { // 带范型的类型转换需要 javaType
                    return mapper.readValue(param.toString(), javaType);
                }
                return mapper.readValue(param.toString(), clazz);
            } catch (IOException e) {
                throw new RuntimeException("json 2 obj mapping error: " + param, e);
            }
        }
        // 否则就把整个 map 转一把
        if (context == null || context.isEmpty()) {
            return null;
        }
        return mapper.convertValue(context, clazz);
    }

    public static Object convertResult(Object result) {
        if (isPrimitive(result)) {
            return result;
        }
        Map convertedResult = mapper.convertValue(new ConvertWrap(result), mapType);
        return convertedResult.get("obj");
    }

    public static MethodInfo getMethodInfo(Object bean, Method method) {
        Export methodExport = method.getAnnotation(Export.class);
        if (methodExport == null) {
            return null;
        }
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.bean = bean;
        methodInfo.method = method;
        Class<?>[] paramClasses = method.getParameterTypes();
        Type[] paramTypes = method.getGenericParameterTypes();
        String[] paramNames = methodExport.paramNames();
        if (paramClasses.length != paramNames.length) {
            throw new IllegalArgumentException("param count not same: " + method.toString());
        }
        for (int i = 0; i < paramClasses.length; i++) {
            JavaType javaType = constructJavaType(paramTypes[i]);
            methodInfo.params.put(paramNames[i], new ParamInfo(paramClasses[i], javaType));
        }
        return methodInfo;
    }

    private static JavaType constructJavaType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            Type[] typeArgs = ptype.getActualTypeArguments();
            JavaType[] javaTypes = new JavaType[typeArgs.length];
            for (int i = 0; i < typeArgs.length; i++) {
                javaTypes[i] = constructJavaType(typeArgs[i]);
            }
            return mapper.getTypeFactory().constructParametricType((Class<?>) ptype.getRawType(), javaTypes);
        } else {
            return mapper.getTypeFactory().constructType(type);
        }
    }

    public static class MethodInfo {
        @Getter
        @Setter
        private Object bean;
        @Getter
        @Setter
        private Method method;
        @Getter
        @Setter
        private LinkedHashMap<String, ParamInfo> params = new LinkedHashMap<String, ParamInfo>();
    }

    public static class ParamInfo {
        @Getter
        @Setter
        private Class<?> clazz;
        @Getter
        @Setter
        private JavaType javaType;

        public ParamInfo(Class<?> clazz, JavaType javaType) {
            this.clazz = clazz;
            this.javaType = javaType;
        }
    }

    private static class ConvertWrap implements Serializable {
        private static final long serialVersionUID = -4890673040481403994L;

        @Getter
        @Setter
        private Object obj;

        private ConvertWrap(Object obj) {
            this.obj = obj;
        }
    }
}
