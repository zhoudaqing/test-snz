package io.terminus.snz.message.components;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.snz.message.models.Message;
import io.terminus.snz.message.utils.HandlebarsUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 消息模版
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-8
 */
@Slf4j
public class MessageTemplates {

    private static final Handlebars handlebars = HandlebarsUtil.HANDLEBARS;

    // 消息类型到消息模版的映射
    private static Map<Integer, String> messageTypeMapping;

    static {
        // init message type and template key mapping
        Map<Integer, String> temp = Maps.newHashMap();
        temp.put(Message.Type.APPLIER_PASS.value(), "templates/applier_pass");
        temp.put(Message.Type.APPLIER_UNPASS.value(), "templates/applier_unpass");
        temp.put(Message.Type.APPLIER_INFO_PASS.value(), "templates/applier_info_pass");
        temp.put(Message.Type.APPLIER_INFO_UNPASS.value(), "templates/applier_info_unpass");
        temp.put(Message.Type.TOPIC_CREATE.value(), "templates/topic_create");
        temp.put(Message.Type.REPLY_CREATE.value(), "templates/topic_reply_create");
        temp.put(Message.Type.SUPPLIER_APPOINTED_UNPASS.value(),"templates/applier_appointed_unpass");

        //自交互消息
        temp.put(Message.Type.REQUIREMENT_APPLY.value(), "templates/requirement_applying");
        temp.put(Message.Type.REQUIREMENT_APPROVE_PASS.value(), "templates/requirement_approve_pass");
        temp.put(Message.Type.REQUIREMENT_APPROVE_UNPASS.value(), "templates/requirement_approve_unpass");
        temp.put(Message.Type.REQUIREMENT_PUBLISH.value(), "templates/requirement_publish");
        temp.put(Message.Type.REQUIREMENT_SOLUTION_END.value(), "templates/requirement_solution_end");
        temp.put(Message.Type.REQUIREMENT_SOLUTION_SELECTED.value(), "templates/requirement_solution_selected");
        temp.put(Message.Type.REQUIREMENT_CREATE.value(), "templates/requirement_create");
        temp.put(Message.Type.REQUIREMENT_WARNING.value(), "templates/requirement_warning");
        temp.put(Message.Type.REQUIREMENT_LOCK.value(), "templates/requirement_lock");
        temp.put(Message.Type.REQUIREMENT_INFO_EMPTY.value(), "templates/requirement_info_empty");

        // 资质交互相关推送
        temp.put(Message.Type.SUPPLIER_QUALIFY_DEADLINE.value(), "templates/supplier_qualify_deadline");

        // make immutable
        messageTypeMapping = ImmutableMap.copyOf(temp);
    }

    /**
     * 消息模版缓存
     */
    private static LoadingCache<String, Template> templatesCache
            = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES)
              .build(new CacheLoader<String, Template>() {
                  @Override
                  public Template load(String templatePath) throws Exception {
                      return handlebars.compile(templatePath);
                  }
              });


    /**
     * 构建消息内容
     * @param t 消息类型
     * @param datas 数据: map, 对象, 参考模版
     * @return 消息内容
     * @throws Exception
     */
    public static String buildMessageContent(Message.Type t, Object datas) throws Exception {
        String templatePath = messageTypeMapping.get(t.value());
        try {
            if (Strings.isNullOrEmpty(templatePath)){
                log.error("message template(type={}, path={}) can't be found", t, templatePath);
                throw new Exception("the message template can't be found");
            }
            Template template = templatesCache.get(templatePath);
            return template.apply(datas);
        } catch (Exception e) {
            log.error("failed to build message content(template={}, datas={}), cause:{}",
                    templatePath, datas, Throwables.getStackTraceAsString(e));
            throw new Exception("failed to build message content:"+Throwables.getStackTraceAsString(e));
        }
    }
}
