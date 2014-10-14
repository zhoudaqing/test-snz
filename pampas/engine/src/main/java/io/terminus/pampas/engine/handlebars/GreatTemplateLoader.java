/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.handlebars;

import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.utils.Protocol;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-28
 */
public class GreatTemplateLoader extends AbstractTemplateLoader {
    private ServletContext servletContext;

    public GreatTemplateLoader(ServletContext servletContext, String prefix, String suffix) {
        this.servletContext = servletContext;
        this.setPrefix(prefix);
        this.setSuffix(suffix);
    }

    @Override
    public TemplateSource sourceAt(String location) throws IOException {
        notEmpty(location, "The uri is required.");
        URL resource = getResource(location);
        if (resource == null) {
            throw new FileNotFoundException(location);
        }
        return new URLTemplateSource(resource.toString(), resource);
    }

    private URL getResource(String location) throws IOException {
        // 如果 location 上就带了 resource 的协议就是直接由 controller 返回的，去 servlet 上下文找
        if (Protocol.analyze(location) == Protocol.SERVLET) {
            String uri = resolve(Protocol.removeProtocol(location, Protocol.SERVLET));
            return servletContext.getResource(uri);
        }
        String home = Setting.getCurrentApp().getAssetsHome();
        // 找组件和view的路径是不同的
        if (location.startsWith("component:")) {
            location = "components/" + normalize(location.substring(10)) + "/view"; // 10 is the length of "component:"
        } else {
            location = "views/" + normalize(location);
        }
        String uri = home + location + getSuffix();
        switch (Protocol.analyze(uri)) {
            case HTTP:
            case HTTPS:
                return new URL(uri);
            case FILE:
                File file = new File(uri);
                return file.exists() ? file.toURI().toURL() : null;
            case SERVLET:
                return servletContext.getResource(Protocol.removeProtocol(uri, Protocol.SERVLET));
            default:
                throw new UnsupportedOperationException("template loader only support [HTTP, FILE, SERVLET] protocol");
        }
    }
}
