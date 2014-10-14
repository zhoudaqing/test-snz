/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.io.Resources;
import io.terminus.pampas.engine.utils.Protocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-28
 */
@Component
@Slf4j
public class ClasspathFileLoader implements FileLoader {
    @Override
    public Resp load(String path) {
        path = Protocol.removeProtocol(path, Protocol.CLASSPATH);
        URL url = Resources.getResource(path);
        Resp resp = new Resp();
        try {
            resp.setContext(Resources.toByteArray(url));
            resp.setSign("UNSUPPORTED");
        } catch (Exception e) {
            log.error("error when load classpath file: {}", path, e);
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
