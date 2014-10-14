/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.base.Defaults;
import com.google.common.collect.Maps;
import io.terminus.pampas.client.ParamUtil;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.InnerCookie;
import io.terminus.pampas.common.UserNotLoginException;
import io.terminus.pampas.common.UserUtil;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-18
 */
public class ParamConverter {
    public static interface UnKnowClass {}

    public static Object convertParam(String paramName, Class<?> paramClass, Map<String, Object> params) {
        if (BaseUser.class.isAssignableFrom(paramClass)) {
            Object user = UserUtil.getCurrentUser();
            if (user == null) {
                throw new UserNotLoginException("user not login.");
            }
            return user;
        }
        if (paramClass == InnerCookie.class) {
            return UserUtil.getInnerCookie();
        }
        Object param = params.get(paramName);
        // 如果没有同名的上下文并且类型是 map ，就把整个上下文丢进去
        if (param == null && Map.class.isAssignableFrom(paramClass)) {
            // filter custom object to prevent possible deserialize exception when rmi
            return filterCustomObject(params);
        }
        // 如果是非原始类型 直接原样返回
        if (!ParamUtil.isPrimitive(paramClass)) {
            return param;
        }
        if (param == null) {
            return Defaults.defaultValue(paramClass);
        }

        return param;
    }

    private static Map<String, String> filterCustomObject(Map<String, Object> params) {
        Map<String, String> targetParam = Maps.newHashMapWithExpectedSize(params.size());
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value == null) continue;
            if (ParamUtil.isPrimitive(value)) {
                targetParam.put(key, String.valueOf(value));
            }
        }
        return targetParam;
    }
}
