/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@ToString
public class FrontConfig extends BaseConfig {
    private static final long serialVersionUID = 7453802881470194536L;
    public static final String HREF_BASE = "base";
    public static final String HREF_MAIN = "main";
    public static final String HREF_LOGIN = "login";

    @Getter
    @Setter
    private Map<String, Component> components;
    @Getter
    @Setter
    private Map<Component.ComponentCategory, List<Component>> componentCategoryListMap = Maps.newHashMap();
    @Getter
    @Setter
    private List<Mapping> mappings;
    @Getter
    @Setter
    private Auths auths;
    @Getter
    @Setter
    private Render render;
    @Getter
    @Setter
    private Map<String, String> hrefs = Maps.newHashMap();

    public Map<String, String> getCurrentHrefs(String currentDomain) {
        Map<String, String> currentHrefs = Maps.newHashMap(hrefs);
        if (currentHrefs.get(FrontConfig.HREF_BASE) == null) {
            currentHrefs.put(FrontConfig.HREF_BASE, currentDomain);
        }
        if (currentHrefs.get(FrontConfig.HREF_MAIN) == null) {
            currentHrefs.put(FrontConfig.HREF_MAIN, "http://" + currentDomain);
        }
        if (currentHrefs.get(FrontConfig.HREF_LOGIN) == null) {
            currentHrefs.put(FrontConfig.HREF_LOGIN, currentHrefs.get(FrontConfig.HREF_MAIN) + "/login");
        }
        return currentHrefs;
    }
}
