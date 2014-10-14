/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import com.google.common.collect.Lists;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.pampas.design.container.DPageRender;
import io.terminus.pampas.design.model.DesignMetaInfo;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.FileLoaderHelper;
import io.terminus.pampas.engine.handlebars.HandlebarEngine;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.model.AppWithConfigInfo;
import io.terminus.pampas.engine.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-17
 */
@Service
public class DesignMetaServiceImpl implements DesignMetaService {
    @Autowired
    @Qualifier("pampasJedisTemplate")
    private JedisTemplate jedisTemplate;
    @Autowired
    private ConfigService configService;
    @Autowired
    private DPageRender dPageRender;
    @Autowired
    private FileLoaderHelper fileLoaderHelper;
    @Autowired
    private HandlebarEngine handlebarEngine;

    @Override
    public DesignMetaInfo getMetaInfo() {
        final DesignMetaInfo metaInfo = new DesignMetaInfo();
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                metaInfo.setRedisUrl(jedis.getClient().getHost() + ":" + jedis.getClient().getPort());
            }
        });
        List<AppWithConfigInfo> apps = configService.listAllAppWithConfigInfo();
        List<String> appNames = Lists.newArrayList();
        // 过滤出前端配置非空的 app
        for (AppWithConfigInfo app : apps){
            if (app.getFrontConfig() != null) {
                appNames.add(app.getApp().getKey());
            }
        }
        metaInfo.setApps(appNames);
        return metaInfo;
    }

    @Override
    public String renderTemplate(String appKey, Long siteInstanceId, String path, Map<String, Object> context) {
        App app = configService.getApp(appKey);
        Setting.setCurrentApp(app);
        String result = dPageRender.renderTemplate(siteInstanceId, path, context);
        Setting.clearCurrentApp();
        return result;
    }

    @Override
    public String renderComponent(String appKey, String template, Map<String, Object> context) {
        App app = configService.getApp(appKey);
        Setting.setCurrentApp(app);
        String result = handlebarEngine.execInline(template, context);
        Setting.clearCurrentApp();
        return result;
    }
}
