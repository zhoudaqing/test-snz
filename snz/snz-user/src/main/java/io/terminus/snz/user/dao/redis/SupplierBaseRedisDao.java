package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.StringHashMapper;
import redis.clients.jedis.Jedis;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-20.
 */
public abstract class SupplierBaseRedisDao<T> {

    public final StringHashMapper<T> stringHashMapper;
    protected final JedisTemplate template;
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public SupplierBaseRedisDao(JedisTemplate template) {
        this.template = template;
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        stringHashMapper = new StringHashMapper<T>(entityClass);
    }

    public T findByKey(final String key) {
        Map<String, String> hash = template.execute(new JedisTemplate.JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
        if (hash == null || hash.isEmpty()) {
            return null;
        }
        return stringHashMapper.fromHash(hash);
    }

    public void save(final String key, final T t) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hmset(key, stringHashMapper.toHash(t));
            }
        });
    }

    public void remove(final String key) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(key);
            }
        });
    }

}
