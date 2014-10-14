/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.dao;

import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.KeyUtils;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.design.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Repository
public class PageDao extends RedisBaseDao<Page> {

    public static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();
    public static final com.fasterxml.jackson.databind.JavaType TYPE = JSON_MAPPER.createCollectionType(Map.class, String.class, String.class);

    @Autowired
    public PageDao(@Qualifier("pampasJedisTemplate") JedisTemplate template) {
        super(template);
    }

    public Set<String> pageIdsByInstanceId(final long siteInstanceId) {
        return template.execute(new JedisTemplate.JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(keyByInstanceId(siteInstanceId));
            }
        });
    }

    public List<Page> findBySiteInstanceId(Long instanceId) {
        Set<String> pageIds = pageIdsByInstanceId(instanceId);
        List<Page> pages = findByIds(pageIds);

        for (Page page : pages) {
            Map<String, String> parts = JSON_MAPPER.fromJson(page.getJsonParts(), TYPE);
            page.setParts(parts);
        }
        return pages;
    }

    public Long create(final Page page) {
        Long id = newId();
        page.setId(id);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                create(t, page);
                t.exec();
            }
        });
        return id;
    }

    public void create(Transaction t, Page... pages) {
        for (Page page : pages) {
            //持久化page
            t.hmset(KeyUtils.entityId(Page.class, page.getId()), stringHashMapper.toHash(page));
            //添加实例到页面的索引
            t.sadd(keyByInstanceId(page.getInstanceId()), page.getId().toString());
        }
    }

    public void update(final Page page) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                create(t, page);
                t.exec();
            }
        });
    }

    public Page findById(Long id) {
        Page p = findByKey(id);
        return p.getId() != null ? p : null;
    }

    public void delParts(Transaction t, Long id) {
        t.hdel(KeyUtils.entityId(Page.class, id), "jsonParts");
    }

    public static String keyByInstanceId(Long instanceId) {
        return "site-instance:" + instanceId + ":pages";
    }
}
