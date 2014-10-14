/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.util;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/7
 */
public class AuthUtil {
    public static String admin() {
        return "admin";
    }

    public static String allGroup() {
        return "group:*";
    }

    public static String group(String group) {
        return "group:" + group;
    }

    public static String app(String group, String app) {
        return "group:" + group + ":app:" + app;
    }

    public static String site(String group, String app, Long siteId) {
        return "group:" + group + ":app:" + app + ":site:" + siteId;
    }
}
