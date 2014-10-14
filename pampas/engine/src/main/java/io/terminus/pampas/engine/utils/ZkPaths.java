/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.utils;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
public class ZkPaths {
    public static String rootPath() {
        return "/pampas/groups";
    }

    public static String groupPath(String group) {
        return rootPath() + "/" + group;
    }

    public static String cellPath(String group, String cell) {
        return groupPath(group) + "/" + cell;
    }
}
