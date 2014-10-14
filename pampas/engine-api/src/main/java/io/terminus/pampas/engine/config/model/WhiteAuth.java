/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-30
 */
@ToString
public class WhiteAuth implements Serializable {
    private static final long serialVersionUID = 3696648541366561019L;
    @Getter
    @Setter
    private String pattern;
    @Getter
    @Setter
    private Pattern regexPattern;
    @Getter
    @Setter
    private Set<HttpMethod> methods;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteAuth)) return false;

        WhiteAuth whiteAuth = (WhiteAuth) o;

        if (!pattern.equals(whiteAuth.pattern)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
