/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.collect.Maps;
import io.terminus.pampas.engine.utils.Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-28
 */
@Component
public class FileLoaderHelper {
    @Autowired
    private HttpFileLoader httpFileLoader;
    @Autowired
    private LocalFileLoader localFileLoader;
    @Autowired
    private ClasspathFileLoader classpathFileLoader;
    @Autowired
    private ServletFileLoader servletFileLoader;

    private Map<Protocol, FileLoader> loaderMap = Maps.newHashMap();

    @PostConstruct
    private void init() {
        loaderMap.put(Protocol.HTTP, httpFileLoader);
        loaderMap.put(Protocol.FILE, localFileLoader);
        loaderMap.put(Protocol.CLASSPATH, classpathFileLoader);
        loaderMap.put(Protocol.SERVLET, servletFileLoader);
    }

    public FileLoader.Resp load(String path) {
        Protocol protocol = Protocol.analyze(path);
        FileLoader fileLoader = loaderMap.get(protocol);
        if (fileLoader == null) {
            throw new UnsupportedOperationException("unsupported protocol: " + protocol);
        }
        return fileLoader.load(path);
    }

    public FileLoader.Resp load(String path, String sign) {
        Protocol protocol = Protocol.analyze(path);
        FileLoader fileLoader = loaderMap.get(protocol);
        if (fileLoader == null) {
            throw new UnsupportedOperationException("unsupported protocol: " + protocol);
        }
        return fileLoader.load(path, sign);
    }
}
