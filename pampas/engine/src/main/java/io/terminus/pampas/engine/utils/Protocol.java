/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.utils;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-28
 */
public enum Protocol {
    HTTP("http://"), HTTPS("https://"), SERVLET("resource:"), CLASSPATH("classpath:"), FILE(""), ZK("zookeeper://");

    private String prefix;

    private Protocol(String prefix) {
        this.prefix = prefix;
    }

    public static Protocol analyze(String uri) {
        String lowerUri = uri.toLowerCase();
        if (lowerUri.startsWith(HTTP.prefix)) {
            return HTTP;
        }
        if (lowerUri.startsWith(HTTPS.prefix)) {
            return HTTPS;
        }
        if (lowerUri.startsWith(SERVLET.prefix)) {
            return SERVLET;
        }
        if (lowerUri.startsWith(CLASSPATH.prefix)) {
            return CLASSPATH;
        }
        if (lowerUri.startsWith(ZK.prefix)) {
            return ZK;
        }
        return FILE;
    }

    public static String removeProtocol(String uri) {
        return removeProtocol(uri, analyze(uri));
    }

    public static String removeProtocol(String uri, Protocol protocol) {
        return uri.substring(protocol.prefix.length());
    }
}
