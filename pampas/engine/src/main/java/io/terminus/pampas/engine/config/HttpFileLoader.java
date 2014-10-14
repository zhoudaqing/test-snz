/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.net.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-25
 */
@Component
public class HttpFileLoader implements FileLoader {
    @Override
    public Resp load(String path) {
        HttpRequest request = HttpRequest.get(path);
        if (request.notFound()) {
            return Resp.NOT_FOUND;
        }
        Resp resp = new Resp();
        resp.setContext(request.bytes());
        resp.setSign(String.valueOf(request.lastModified()));
        return resp;
    }

    @Override
    public Resp load(String path, String sign) {
        HttpRequest request = HttpRequest.get(path);
        request.header(HttpHeaders.IF_MODIFIED_SINCE, sign);
        if (request.notFound()) {
            return Resp.NOT_FOUND;
        }
        if (request.notModified()) {
            return Resp.NOT_MODIFIED;
        }
        Resp resp = new Resp();
        resp.setContext(request.bytes());
        resp.setSign(String.valueOf(request.lastModified()));
        return resp;
    }
}
