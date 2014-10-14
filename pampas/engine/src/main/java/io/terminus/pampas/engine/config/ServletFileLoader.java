/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.io.Resources;
import io.terminus.pampas.engine.utils.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-12
 */
@Component
@Slf4j
public class ServletFileLoader implements FileLoader {
    @Autowired
    private ServletContext servletContext;

    @Override
    public Resp load(String path) {
        path = Protocol.removeProtocol(path, Protocol.SERVLET);
        Resp resp = new Resp();
        try {
            URL url = servletContext.getResource(path);
            resp.setContext(Resources.toByteArray(url));
            resp.setSign("UNSUPPORTED");
        } catch (Exception e) {
            log.error("error when load servlet file: {}", path, e);
            return Resp.NOT_FOUND;
        }
        return resp;
    }

    @Override
    public Resp load(String path, String sign) {
        // always return NOT_MODIFIED
        return Resp.NOT_MODIFIED;
    }
}
