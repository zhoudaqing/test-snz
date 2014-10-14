/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import com.google.common.base.Strings;
import io.terminus.pampas.design.model.FullPage;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-1
 */
@Component
@Slf4j
public class DRenderHelpers {
    @Autowired
    private HandlebarEngine handlebarEngine;

    @PostConstruct
    private void init() {
        handlebarEngine.registerHelper("render", new Helper<String>() {
            @Override
            public CharSequence apply(String type, Options options) throws IOException {
                FullPage fullpage = options.get(RenderConstants.PAGE);
                String templateStr = null;
                if (type.equals("header")) {
                    templateStr = fullpage.getHeader();
                } else if (type.equals("footer")) {
                    templateStr = fullpage.getFooter();
                } else if (type.equals("body")) {
                    templateStr = fullpage.getFixed();
                } else if (type.equals("part")) {
                    Object partKey = options.param(0);
                    if (partKey == null) {
                        log.error("part key not found when rendering fullPage: ({})", fullpage);
                    } else {
                        if (fullpage.getParts() != null) {
                            templateStr = fullpage.getParts().get(String.valueOf(partKey));
                        } else {
                            log.warn("part ({}) not found for fullPage: ({})", partKey, fullpage);
                        }
                    }
                }
                if (Strings.isNullOrEmpty(templateStr)) {
                    log.warn("can't find template when render {}, {} - {}", type, fullpage.getInstanceId(), fullpage.getPath());
                    return options.tagType == TagType.SECTION ? options.fn() : "";
                }
                Map<String, Object> context = null;
                if (options.context.model() instanceof Map) {
                    //noinspection unchecked
                    context = (Map<String, Object>) options.context.model();
                }
                // TODO add cache key for inline template
                return new Handlebars.SafeString(handlebarEngine.execInline(templateStr, context));
            }
        });
    }
}
