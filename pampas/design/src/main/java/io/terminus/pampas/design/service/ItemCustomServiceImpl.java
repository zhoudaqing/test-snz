/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.terminus.pampas.design.dao.ItemCustomRedisDao;
import io.terminus.pampas.design.dao.ItemTemplateRedisDao;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-12-18
 */
@Service
@Slf4j
public class ItemCustomServiceImpl implements ItemCustomService {
    @Autowired
    private ItemCustomRedisDao itemCustomRedisDao;
    @Autowired
    private ItemTemplateRedisDao itemTemplateRedisDao;
    @Autowired
    private HandlebarEngine handlebarEngine;

    private final LoadingCache<String, Optional<String>> cache;

    private final static Splitter keySplitter = Splitter.on('-').limit(2).omitEmptyStrings().trimResults();

    @Autowired
    public ItemCustomServiceImpl(Setting setting) {
        this.cache = optionalCache(!setting.isDevMode());
    }

    private LoadingCache<String, Optional<String>> optionalCache(boolean buildCache) {
        if (buildCache) {
            return CacheBuilder.newBuilder()
                    .maximumSize(100000)
                    .expireAfterWrite(5, TimeUnit.MINUTES)
                    .build(
                            new CacheLoader<String, Optional<String>>() {
                                @Override
                                public Optional<String> load(String key) throws Exception {
                                    List<String> parts = keySplitter.splitToList(key);
                                    String type = parts.get(0);
                                    Long id = Long.parseLong(parts.get(1));
                                    String hbs;
                                    if (Objects.equal(type, "custom")) {
                                        hbs = itemCustomRedisDao.findById(id);
                                    } else {
                                        hbs = itemTemplateRedisDao.findBySpuId(id);
                                    }
                                    return Optional.fromNullable(hbs);
                                }
                            });
        }
        return null;
    }

    @Override
    public Boolean save(Long itemId, String html) {
        itemCustomRedisDao.createOrUpdate(itemId, html);
        return true;
    }

    @Override
    public Boolean saveTemplate(Long spuId, String html) {
        itemTemplateRedisDao.createOrUpdate(spuId, html);
        return true;
    }

    @Override
    public String render(Long itemId, Long spuId, Map<String, String> context) {
        boolean designMode = context.containsKey(RenderConstants.DESIGN_MODE);
        String hbs;
        if (cache == null || designMode) {
            hbs = itemCustomRedisDao.findById(itemId);
        } else {
            hbs = cache.getUnchecked("custom-" + itemId).orNull();
        }
        if (hbs != null) {
            Map<String, Object> renderContext = Maps.newHashMap();
            renderContext.put("spuId", spuId);
            if (designMode) {
                renderContext.put(RenderConstants.DESIGN_MODE, true);
            }
            return handlebarEngine.execInline(hbs, renderContext);
        }
        log.error("item content not found. itemId [{}], spuId [{}]", itemId, spuId);
        return "";
    }

    @Override
    public String renderTemplate(Long spuId, Map<String, String> context) {
        boolean designMode = context.containsKey(RenderConstants.DESIGN_MODE);
        String hbs;
        if (cache == null || designMode) {
            hbs = itemTemplateRedisDao.findBySpuId(spuId);
        } else {
            hbs = cache.getUnchecked("template-" + spuId).orNull();
        }
        if (hbs != null) {
            Map<String, Object> renderContext = Maps.newHashMap();
            if (designMode) {
                renderContext.put(RenderConstants.DESIGN_MODE, true);
            }
            return handlebarEngine.execInline(hbs, renderContext);
        }
        log.error("item template content not found. spuId [{}]", spuId);
        return "";
    }
}
