/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import io.terminus.pampas.common.BaseUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@ToString
public class ProtectedAuth implements Serializable {
    private static final long serialVersionUID = 6649344563529265570L;
    @Getter
    @Setter
    private String pattern;
    @Getter
    @Setter
    private Pattern regexPattern;
    @Getter
    @Setter
    private Set<BaseUser.TYPE> types;
    @Getter
    @Setter
    private Set<String> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectedAuth)) return false;

        ProtectedAuth protectedAuth = (ProtectedAuth) o;

        if (!pattern.equals(protectedAuth.pattern)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
