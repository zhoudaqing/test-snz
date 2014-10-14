/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.BaseUser;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14/7/25
 */
public class HandlebarTest {
    private static final Handlebars HANDLEBARS = new Handlebars();

    @Test
    public void testExtendClassGetter() throws Exception {
        String template = "{{_USER_.extend}}";

        ExtUser extUser = new ExtUser();
        extUser.setExtend("wtf_123");
        BaseUser baseUser = (BaseUser) extUser;
        Template t = HANDLEBARS.compileInline(template);
        Map<String, Object> context = Maps.newHashMap();
        context.put("_USER_", baseUser);
        assertThat("wtf_123", equalTo(t.apply(context)));
    }

    public static class ExtUser extends BaseUser {
        private static final long serialVersionUID = -4118919909823614823L;

        @Getter
        @Setter
        private String extend;
    }
}
