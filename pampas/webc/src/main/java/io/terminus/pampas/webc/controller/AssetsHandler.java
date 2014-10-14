/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.webc.controller;

import com.google.common.collect.Iterables;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.FileLoader;
import io.terminus.pampas.engine.config.FileLoaderHelper;
import io.terminus.pampas.webc.utils.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-13
 */
@Component
public class AssetsHandler {
    @Autowired
    private FileLoaderHelper fileLoaderHelper;

    public boolean handle(String path, HttpServletResponse response) {
        String lastPath = Iterables.getLast(Splitters.SLASH.split(path));
        List<String> fileInfo = Splitters.DOT.splitToList(lastPath);
        if (fileInfo.size() == 1) {
            return false;
        }
        response.setContentType(MimeTypes.getType(Iterables.getLast(fileInfo)));
        String realPath = Setting.getCurrentApp().getAssetsHome() + path;
        FileLoader.Resp resp = fileLoaderHelper.load(realPath);
        if (resp.isNotFound()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return true;
        }
        response.setContentLength(resp.getContext().length);
        try {
            response.getOutputStream().write(resp.getContext());
        } catch (IOException e) {
            // ignore this fucking exception
        }
        return true;
    }
}
