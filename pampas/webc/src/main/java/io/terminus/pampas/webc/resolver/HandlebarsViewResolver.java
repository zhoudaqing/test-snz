/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.resolver;

import com.github.jknack.handlebars.io.ServletContextTemplateLoader;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2012-08-25
 */
public class HandlebarsViewResolver extends AbstractTemplateViewResolver {

    /**
     * The default content type.
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * The handlebars instance.
     */
    private HandlebarEngine handlebarEngine;

    /**
     * Creates a new {@link HandlebarsViewResolver}.
     *
     * @param handlebarEngine The handlebars object. Required.
     */
    public HandlebarsViewResolver(final HandlebarEngine handlebarEngine) {
        checkNotNull(handlebarEngine, "The handlebarEngine object is required.");

        this.handlebarEngine = handlebarEngine;
        setViewClass(HandlebarsView.class);
        setContentType(DEFAULT_CONTENT_TYPE);
    }

    @Override
    public void setPrefix(final String prefix) {
        throw new UnsupportedOperationException("Use "
                + ServletContextTemplateLoader.class.getName() + "#setPrefix");
    }

    @Override
    public void setSuffix(final String suffix) {
        throw new UnsupportedOperationException("Use "
                + ServletContextTemplateLoader.class.getName() + "#setSuffix");
    }

    /**
     * Creates a new {@link HandlebarsViewResolver}.
     */
    public HandlebarsViewResolver() {
        throw new UnsupportedOperationException("operation not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractUrlBasedView buildView(final String viewName)
            throws Exception {
        return configure((HandlebarsView) super.buildView(viewName));
    }

    /**
     * Configure the handlebars view.
     *
     * @param view The handlebars view.
     * @return The configured view.
     * @throws java.io.IOException If a resource cannot be loaded.
     */
    protected AbstractUrlBasedView configure(final HandlebarsView view)
            throws IOException {
        String url = view.getUrl();
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        view.init(handlebarEngine, url);
        return view;
    }

    /**
     * The required view class.
     *
     * @return The required view class.
     */
    @Override
    protected Class<?> requiredViewClass() {
        return HandlebarsView.class;
    }

}


