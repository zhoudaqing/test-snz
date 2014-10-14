/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config;

import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-28
 */
@Component
@Slf4j
public class LocalFileLoader implements FileLoader {
    @Override
    public Resp load(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return Resp.NOT_FOUND;
        }
        Resp resp = new Resp();
        resp.setSign(String.valueOf(file.lastModified()));
        try {
            resp.setContext(Files.toByteArray(file));
        } catch (Exception e) {
            log.error("error when load local file: {}", path, e);
            return Resp.NOT_FOUND;
        }
        return resp;
    }

    @Override
    public Resp load(String path, String sign) {
        File file = new File(path);
        if (!file.exists()) {
            return Resp.NOT_FOUND;
        }
        if (file.lastModified() == Long.valueOf(sign)) {
            return Resp.NOT_MODIFIED;
        }
        Resp resp = new Resp();
        resp.setSign(String.valueOf(file.lastModified()));
        try {
            resp.setContext(Files.toByteArray(file));
        } catch (Exception e) {
            log.error("error when load local file: {}", path, e);
            return Resp.NOT_FOUND;
        }
        return resp;
    }
}
