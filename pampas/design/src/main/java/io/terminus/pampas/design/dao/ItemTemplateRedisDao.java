/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.dao;

import io.terminus.common.redis.utils.JedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

/**
 * Created by yangzefeng on 13-12-18
 */
@Repository
public class ItemTemplateRedisDao {

    private final JedisTemplate jedisTemplate;

    @Autowired
    public ItemTemplateRedisDao(@Qualifier("pampasJedisTemplate") JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public void createOrUpdate(final long spuId, final String htmlCode) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(keyBySpuId(spuId), htmlCode);
            }
        });
    }

    public String findBySpuId(final long spuId) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(keyBySpuId(spuId));
            }
        });
    }

    public void delete(final long spuId) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(keyBySpuId(spuId));
            }
        });
    }

    public static String keyBySpuId(Long spuId) {
        return "item-template:" + spuId;
    }
}
