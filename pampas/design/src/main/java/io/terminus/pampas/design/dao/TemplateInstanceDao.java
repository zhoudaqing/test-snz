/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.dao;

import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.pampas.design.model.InstanceStatus;
import io.terminus.pampas.design.model.TemplateInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import static io.terminus.common.redis.utils.KeyUtils.entityId;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-22
 */
@Repository
public class TemplateInstanceDao extends RedisBaseDao<TemplateInstance> {

    @Autowired
    public TemplateInstanceDao(@Qualifier("pampasJedisTemplate") JedisTemplate template) {
        super(template);
    }

    public TemplateInstance findById(long id) {
        TemplateInstance instance = findByKey(id);
        return instance.getId() != null ? instance : null;
    }

    public Long create(final TemplateInstance instance) {
        Long id = newId();
        instance.setId(id);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                create(t, instance);
                t.exec();
            }
        });
        return id;
    }

    public void create(Transaction t, TemplateInstance instance) {
        Long id = instance.getId();
        t.hmset(entityId(TemplateInstance.class, id), stringHashMapper.toHash(instance));
        //如果站点实例是装修中,则加入站点的装修实例集合
        if (InstanceStatus.DESIGN == instance.getStatus()) {
            t.sadd(keyBySiteId(instance.getSiteId()), id.toString());
        }
    }

    public void update(final TemplateInstance instance) {
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                create(t, instance);
                t.exec();
            }
        });
    }

    public static String keyBySiteId(Long siteId) {
        return "site:" + siteId + ":instances";
    }
}
