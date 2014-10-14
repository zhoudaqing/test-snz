/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import java.util.Map;

/**
 * 商品详情装修相关的 service
 * <p/>
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-12-18
 */
public interface ItemCustomService {
    /**
     * 保存对应商品的装修内容
     *
     * @param itemId 商品id
     * @param html   商品html
     * @return 是否成功，内部的result没有意义
     */
    Boolean save(Long itemId, String html);

    /**
     * 保存对应商品的详情模板
     *
     * @param spuId 模板关联的spuId
     * @param html  详情html
     * @return 是否成功，内部的result没有意义
     */
    Boolean saveTemplate(Long spuId, String html);

    /**
     * 渲染对应的商品的装修内容
     *
     * @param itemId 商品id
     * @param spuId 商品的spuId
     * @param context 调用上下文，因为需要渲染handlebars 所以需要上下文
     * @return 渲染后的html
     */
    String render(Long itemId, Long spuId, Map<String, String> context);

    /**
     * 渲染对应的商品的详情模板内容
     *
     * @param spuId 模板关联的spuId
     * @return 渲染后的html
     */
    String renderTemplate(Long spuId, Map<String, String> context);
}
