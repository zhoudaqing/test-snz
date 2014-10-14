/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.dao;

import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.pampas.design.model.InstanceStatus;
import io.terminus.pampas.design.model.SiteInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

import static io.terminus.common.redis.utils.KeyUtils.entityId;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Repository
public class SiteInstanceDao extends RedisBaseDao<SiteInstance> {

    @Autowired
    public SiteInstanceDao(@Qualifier("pampasJedisTemplate") JedisTemplate template) {
        super(template);
    }

    public Long create(final SiteInstance siteInstance) {
        Long id = newId();
        siteInstance.setId(id);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                create(t, siteInstance);
                t.exec();
            }
        });
        return id;
    }


    public void create(Transaction t, SiteInstance siteInstance) {
        Long id = siteInstance.getId();
        t.hmset(entityId(SiteInstance.class, id), stringHashMapper.toHash(siteInstance));
        //如果站点实例是装修中,则加入站点的装修实例集合
        if (InstanceStatus.DESIGN == siteInstance.getStatus()) {
            t.sadd(keyBySiteId(siteInstance.getSiteId()), id.toString());
        }
    }


    public Set<String> designInstanceIds(final long siteId) {
        return template.execute(new JedisTemplate.JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(keyBySiteId(siteId));
            }
        });
    }

    public SiteInstance findById(long siteInstanceId) {
        SiteInstance siteInstance = findByKey(siteInstanceId);
        return siteInstance.getId() != null ? siteInstance : null;
    }

    public List<SiteInstance> findDesignInstanceBySiteId(long siteId) {
        Set<String> ids = designInstanceIds(siteId);
        return findByIds(ids);
    }

    public static String keyBySiteId(Long siteId) {
        return "site:" + siteId + ":instances";
    }
}
