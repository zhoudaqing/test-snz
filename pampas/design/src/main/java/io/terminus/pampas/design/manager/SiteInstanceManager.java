/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.manager;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.pampas.design.dao.PageDao;
import io.terminus.pampas.design.dao.SiteInstanceDao;
import io.terminus.pampas.design.dao.TemplateInstanceDao;
import io.terminus.pampas.design.dao.TemplatePageDao;
import io.terminus.pampas.design.model.Page;
import io.terminus.pampas.design.model.SiteInstance;
import io.terminus.pampas.design.model.TemplateInstance;
import io.terminus.pampas.design.model.TemplatePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-11-29
 */
@Component
@Slf4j
public class SiteInstanceManager {
    @Autowired
    private JedisTemplate template;
    @Autowired
    private SiteInstanceDao siteInstanceDao;
    @Autowired
    private TemplateInstanceDao templateInstanceDao;
    @Autowired
    private TemplatePageDao templatePageDao;
    @Autowired
    private PageDao pageDao;

    public void saveTemplateInstanceWithPage(final TemplateInstance templateInstance, final TemplatePage templatePage) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                templateInstanceDao.create(t, templateInstance);
                templatePageDao.create(t, templatePage);
                t.exec();
            }
        });
    }

    public void saveSiteInstanceWithPage(final SiteInstance siteInstance, final Page page) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                siteInstanceDao.create(t, siteInstance);
                pageDao.create(t, page);
                t.exec();
            }
        });
    }
}
