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
 * Created by yangzefeng on 13-12-16
 */
@Repository
public class ItemCustomRedisDao {

    private final JedisTemplate jedisTemplate;

    @Autowired
    public ItemCustomRedisDao(@Qualifier("pampasJedisTemplate") JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public void createOrUpdate(final long itemId, final String htmlCode) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.set(keyByItemId(itemId), htmlCode);
            }
        });
    }

    public String findById(final long itemId) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(keyByItemId(itemId));
            }
        });
    }

    public void delete(final long itemId) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(keyByItemId(itemId));
            }
        });
    }

    public static String keyByItemId(Long itemId) {
        return "item-custom:" + itemId;
    }
}
