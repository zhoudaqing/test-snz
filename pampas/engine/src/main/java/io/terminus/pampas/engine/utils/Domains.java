/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.utils;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-20
 */
public class Domains {
    /**
     * 移除第一个域
     * www.mall.com -> mall.com
     * xxx.mall.com.cn -> mall.com.cn
     */
    public static String removeSubDomain(String domain) {
        int firstDotIndex = domain.indexOf(".");
        return domain.substring(firstDotIndex + 1);
    }
}
