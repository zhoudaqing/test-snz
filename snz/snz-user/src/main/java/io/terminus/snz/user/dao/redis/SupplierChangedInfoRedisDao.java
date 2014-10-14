package io.terminus.snz.user.dao.redis;

import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.snz.user.tool.ChangedInfoKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;


/**
 * Author:Guo Chaopeng
 * Created on 14-7-15.
 */
@Repository
public class SupplierChangedInfoRedisDao {

    private JedisTemplate jedisTemplate;

    @Autowired
    public SupplierChangedInfoRedisDao(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public void addChangedInfos(final Long userId, final Map<String, String> changedInfos) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hmset(ChangedInfoKeys.changedInfos(userId), changedInfos);
            }
        });
    }

    public void removeChangedInfos(final Long userId) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(ChangedInfoKeys.changedInfos(userId));
            }
        });
    }

    public Map<String, String> getChangedInfos(final Long userId) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis jedis) {
                return jedis.hgetAll(ChangedInfoKeys.changedInfos(userId));
            }
        });
    }

    /**
     * 保存修改了重要信息的tab
     *
     * @param userId 用户编号
     * @param tabKey tab的key
     */
    public void addChangedTab(final Long userId, final String tabKey) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.sadd(ChangedInfoKeys.changedTabs(userId), tabKey);
            }
        });
    }

    /**
     * 判断某个tab的重要信息是否有更改
     *
     * @param tabKey tab的key
     * @return 有更改返回true，否则返回false
     */
    public boolean tabInfoChanged(final Long userId, final String tabKey) {
        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.sismember(ChangedInfoKeys.changedTabs(userId), tabKey);
            }
        });
    }

    public void removeChangedTabs(final Long userId) {
        jedisTemplate.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.del(ChangedInfoKeys.changedTabs(userId));
            }
        });
    }
}
