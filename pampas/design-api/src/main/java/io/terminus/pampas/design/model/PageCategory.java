/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import com.google.common.base.Objects;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public enum PageCategory {
    INDEX("index", "店铺首页"),
    LIST("list", "商品列表"),
    DETAIL("detail", "商品详情页"),
    OTHER("wtf...", "wtf...");

    private final String path;
    private final String name;

    private PageCategory(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public static PageCategory fromPath(String path) {
        for (PageCategory pageCategory : PageCategory.values()) {
            if (Objects.equal(path, pageCategory.getPath())) {
                return pageCategory;
            }
        }
        return OTHER;
    }
}
