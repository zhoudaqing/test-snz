/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.base.Charsets;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
public interface FileLoader {
    Resp load(String path);

    Resp load(String path, String sign);

    class Resp {
        public static final Resp NOT_FOUND = new Resp();
        public static final Resp NOT_MODIFIED = new Resp();

        static {
            NOT_FOUND.notFound = true;
            NOT_MODIFIED.notModified = true;
        }

        @Getter
        @Setter
        private boolean notFound;
        @Getter
        @Setter
        private boolean notModified;
        @Getter
        @Setter
        private String sign;
        @Getter
        @Setter
        private byte[] context;

        public String asString() {
            return new String(context, Charsets.UTF_8);
        }
    }
}
