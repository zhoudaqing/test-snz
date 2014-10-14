/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

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
public class Mapping implements Serializable {
    private static final long serialVersionUID = 61453140428465964L;
    @Getter
    @Setter
    private String pattern;
    @Getter
    @Setter
    private Set<HttpMethod> methods;
    @Getter
    @Setter
    private String service;
    @Getter
    @Setter
    private String desc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mapping)) return false;

        Mapping mapping = (Mapping) o;

        if (methods != null ? !methods.equals(mapping.methods) : mapping.methods != null) return false;
        if (!pattern.equals(mapping.pattern)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pattern.hashCode();
        result = 31 * result + (methods != null ? methods.hashCode() : 0);
        return result;
    }
}
