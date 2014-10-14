/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.utils;

import javax.servlet.http.Cookie;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-03-07
 */
public class CookieBuilder {
    private final String name;

    private final String value;

    private final String domain;

    private String path = "/";

    private boolean httpOnly = false;

    private int maxAge = -1;

    private CookieBuilder(String name, String value, String domain, String path, boolean httpOnly, int maxAge) {
        this.name = name;
        this.value = value;
        this.domain = domain;
        this.path = path;
        this.httpOnly = httpOnly;
        this.maxAge = maxAge;
    }

    private CookieBuilder(String name, String value, String domain) {
        this.name = name;
        this.value = value;
        this.domain = domain;
    }

    public CookieBuilder path(String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder httpOnly() {
        this.httpOnly = true;
        return this;
    }

    public CookieBuilder maxAge(int seconds) {
        this.maxAge = seconds;
        return this;
    }

    public static CookieBuilder from(String name, String value, String domain) {
        return new CookieBuilder(name, value, domain);
    }

    public Cookie build() {
        Cookie cookie = new Cookie(this.name, this.value);
        if (this.domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(this.path);
        cookie.setHttpOnly(this.httpOnly);
        cookie.setMaxAge(this.maxAge);
        return cookie;
    }

}
