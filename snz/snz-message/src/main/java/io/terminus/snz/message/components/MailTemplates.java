package io.terminus.snz.message.components;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.terminus.snz.message.models.Mail;
import io.terminus.snz.message.utils.HandlebarsUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2014 杭州端点网络科技有限公司
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/8/28
 */
@Slf4j
public class MailTemplates {
    private static final Handlebars handlebars = HandlebarsUtil.HANDLEBARS;

    private static final LoadingCache<Mail.Type, Template> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(5L, TimeUnit.MINUTES)
            .build(new CacheLoader<Mail.Type, Template>() {
                @Override
                public Template load(Mail.Type type) throws Exception {
                    return handlebars.compile(getTemplatePath(type));
                }
            });

    private static String getTemplatePath(Mail.Type type) {
        return "templates/mails/" + type.toString().toLowerCase();
    }

    public static String build(Mail.Type type, Object data) throws Exception {
        try {
            Template template = cache.get(type);
            return template.apply(data);
        } catch (Exception e) {
            log.error("failed to build mail content(type={}, data={}), cause:{}",
                    type, data, Throwables.getStackTraceAsString(e));
            throw new RuntimeException("failed to build mail content", e);
        }
    }
}
