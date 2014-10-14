/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import io.terminus.pampas.engine.exception.NotFound404Exception;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Author: Anson Chan
 * Date: 8/17/12 3:30 PM
 */
@Component
@Slf4j
public class PageRender {
    @Autowired
    protected HandlebarEngine handlebarEngine;

    /**
     * 按路径渲染一个页面，用于非装修页面的渲染
     */
    public String render(String domain, String path, Map<String, Object> context) {
        return naiveRender(path, context);
    }

    /**
     * 直接按照path找到layout来渲染
     *
     * @param path    路径
     * @param context 上下文
     * @return 渲染结果
     */
    protected String naiveRender(String path, Map<String, Object> context) throws NotFound404Exception {
        context.put(RenderConstants.PATH, path);
        try {
            return handlebarEngine.execPath(path, context, false);
        } catch (FileNotFoundException e) {
            log.error("failed to find page {},cause:{}", path, e.getMessage());
            throw new NotFound404Exception("page not found");
        }
    }
}