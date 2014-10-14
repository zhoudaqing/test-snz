/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@ToString
public class Component implements Serializable {
    private static final long serialVersionUID = -1877549770928584021L;
    @Getter
    @Setter
    private String path;
    @Getter
    @Setter
    private ComponentCategory category = ComponentCategory.ADMIN; // 默认 ADMIN
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String desc;
    @Getter
    @Setter
    private String service;

    public static enum Mode {
        OFFICIAL(ImmutableSet.of(ComponentCategory.COMMON, ComponentCategory.OFFICIAL)),
        TEMPLATE(ImmutableSet.of(ComponentCategory.COMMON, ComponentCategory.OFFICIAL, ComponentCategory.SHOP)),
        SHOP(ImmutableSet.of(ComponentCategory.COMMON, ComponentCategory.SHOP)),
        ITEM(ImmutableSet.of(ComponentCategory.COMMON, ComponentCategory.ITEM)),
        ITEM_TEMPLATE(ImmutableSet.of(ComponentCategory.COMMON));

        @Getter
        private Set<ComponentCategory> categories;

        private Mode(Set<ComponentCategory> categories) {
            this.categories = categories;
        }
    }

    public static enum ComponentCategory {
        COMMON("通用", true), ADMIN("后台", false), OFFICIAL("官方", true), SHOP("店铺", true), ITEM("商品", true);

        @Getter
        private String name;
        @Getter
        private boolean designable;

        private ComponentCategory(String name, boolean designable) {
            this.name = name;
            this.designable = designable;
        }
    }
}
