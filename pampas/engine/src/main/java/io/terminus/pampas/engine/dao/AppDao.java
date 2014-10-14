/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.dao;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.pampas.engine.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-21
 */
@Repository
public class AppDao extends RedisBaseDao<App> {
    public AppDao() {
        this(null);
    }

    @Autowired(required = false) // 因为嵌入式可以不需要 redis ，所以这里是 required = false
    public AppDao(JedisTemplate template) {
        super(template);
    }

    public App findByKey(final String key) {
        return template.execute(new JedisTemplate.JedisAction<App>() {
            @Override
            public App action(Jedis jedis) {
                Map<String, String> hash = jedis.hgetAll(key(key));
                return stringHashMapper.fromHash(hash);
            }
        });
    }

    public App findByDomain(final String domain) {
        String key = template.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(keyByDomain(domain));
            }
        });
        if (key == null) {
            return null;
        }
        return findByKey(key);
    }

    public List<App> listAll() {
        Set<String> keys = template.execute(new JedisTemplate.JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(keyAll());
            }
        });
        return findByKeys(keys, new Function<String, String>() {
            @Override
            public String apply(String key) {
                return key(key);
            }
        });
    }

    public void create(final App app) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                t.hmset(key(app.getKey()), stringHashMapper.toHash(app));
                t.setnx(keyByDomain(app.getDomain()), app.getKey());
                t.sadd(keyAll(), app.getKey());
                t.exec();
            }
        });
    }

    public void update(final App app) {
        final App exists = findByKey(app.getKey());
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                t.hmset(key(app.getKey()), stringHashMapper.toHash(app));
                // 新的不为空并且和旧的不一样
                if (!Strings.isNullOrEmpty(app.getDomain()) && !Objects.equal(app.getDomain(), exists.getDomain())) {
                    t.del(keyByDomain(exists.getDomain()));
                    t.setnx(keyByDomain(app.getDomain()), app.getKey());
                }
                t.exec();
            }
        });
    }

    public void updateExtraDomains(final String appKey, final Set<String> extraDomains) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Set<String> existsDomains = MoreObjects.firstNonNull(jedis.smembers(keyExtraDomains(appKey)), Sets.<String>newHashSet());
                Transaction t = jedis.multi();
                for (String domain : extraDomains) {
                    if (existsDomains.contains(domain)) {
                        existsDomains.remove(domain);
                    } else {
                        t.setnx(keyByDomain(domain), appKey);
                        t.sadd(keyExtraDomains(appKey), domain);
                    }
                }
                for (String domain : existsDomains) {
                    t.del(keyByDomain(domain));
                    t.srem(keyExtraDomains(appKey), domain);
                }
                t.exec();
            }
        });
    }

    public Set<String> getExtraDomains(final String appKey) {
        return template.execute(new JedisTemplate.JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(keyExtraDomains(appKey));
            }
        });
    }

    public void delete(final String appKey) {
        final App exists = findByKey(appKey);
        if (exists == null) {
            return;
        }
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                t.del(key(appKey));
                t.del(keyByDomain(exists.getDomain()));
                t.srem(keyAll(), appKey);
                t.exec();
            }
        });
    }

    public static String keyAll() {
        return "apps";
    }

    public static String key(String key) {
        return "app:" + key;
    }

    public static String keyExtraDomains(String key) {
        return "app:" + key + ":extra-domains";
    }

    public static String keyByDomain(String domain) {
        return "app-domain:" + domain;
    }
}
