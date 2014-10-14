/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public class Page implements Serializable {

    private static final long serialVersionUID = 7083294628442613579L;

    @Getter
    @Setter
    private Long id;  //页面id

    @Getter
    @Setter
    private PageCategory pageCategory; //页面类型

    @Getter
    @Setter
    private Long instanceId; //站点实例id

    @Getter
    @Setter
    private String name;   //页面名称

    @Getter
    @Setter
    private String path; //页面path

    @Getter
    @Setter
    @JsonIgnore
    private Map<String, String> parts;  //用户可以修改的部分

    @Getter
    @Setter
    private String jsonParts;  //以json格式存储的可改变的内容,会持久化

    @Getter
    @Setter
    private String keywords;   //搜索关键字

    @Getter
    @Setter
    private String description; //搜索描述

    @Override
    public int hashCode() {
        return Objects.hashCode(instanceId, path);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Page)) {
            return false;
        }
        Page that = (Page) o;
        return Objects.equal(this.instanceId, that.instanceId)
                && Objects.equal(this.path, that.path);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("instanceId", instanceId)
                .add("pageCategory", pageCategory)
                .add("name", name)
                .add("path", path)
                .add("parts", parts)
                .toString();
    }
}
