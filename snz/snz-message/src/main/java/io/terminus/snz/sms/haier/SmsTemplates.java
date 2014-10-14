package io.terminus.snz.sms.haier;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.terminus.snz.message.utils.HandlebarsUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class SmsTemplates {

    private static final Handlebars handlebars = HandlebarsUtil.HANDLEBARS;

    private static final LoadingCache<String, Template> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(5L, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Template>() {
                public Template load(String key) throws Exception {
                    return handlebars.compile(getTemplatePath(key));
                }
            });

    private static String getTemplatePath(String type) {
        return "templates/sms/" + type;
    }

    public static String build(String templateFile, Object data){
        try {
            Template template = cache.get(templateFile);
            return template.apply(data);
        } catch (Exception e) {
            log.error("failed to build sms content(type={}, data={}), cause:{}",
                    templateFile, data, Throwables.getStackTraceAsString(e));
            throw new RuntimeException("failed to build mail content", e);
        }
    }
}
