/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.dao;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import io.terminus.common.redis.dao.RedisBaseDao;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.KeyUtils;
import io.terminus.pampas.design.model.Site;
import io.terminus.pampas.design.model.SiteCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static io.terminus.common.redis.utils.KeyUtils.entityId;


/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Repository
public class SiteDao extends RedisBaseDao<Site> {
    @Autowired
    public SiteDao(@Qualifier("pampasJedisTemplate") JedisTemplate template) {
        super(template);
    }


    public void create(Transaction t, Site site) {
        Long id = site.getId();
        t.hmset(entityId(Site.class, id), stringHashMapper.toHash(site));
        //add to user-site 子站不需要这个索引
        if (site.getCategory() != SiteCategory.OFFICIAL) {
            t.sadd(keyUserSites(site.getUserId()), id.toString());
        }

        //add domain or subdomain index
        if (!Strings.isNullOrEmpty(site.getDomain())) {
            t.set(keyByDomain(site.getDomain()), id.toString());
        }
        if (!Strings.isNullOrEmpty(site.getSubdomain())) {
            t.set(keyBySubDomain(site.getApp(), site.getSubdomain()), id.toString());
        }
    }

    public Site findById(Long id) {
        Site site = findByKey(id);
        return site.getId() != null ? site : null;
    }

    public Site findByDomain(final String domain) {
        String siteId = template.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(keyByDomain(domain));
            }
        });
        if (!Strings.isNullOrEmpty(siteId)) {
            return findById(Long.parseLong(siteId));
        }
        return null;

    }

    public Site findBySubdomain(final String app, final String subdomain) {
        String siteId = template.execute(new JedisTemplate.JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.get(keyBySubDomain(app, subdomain));
            }
        });
        if (!Strings.isNullOrEmpty(siteId)) {
            return findById(Long.parseLong(siteId));
        }
        return null;
    }

    public boolean update(final Site site) {
        final long siteId = site.getId();
        final Site exist = findById(siteId);
        if (!Objects.equal(exist.getApp(), site.getApp())) {
            throw new IllegalArgumentException("can't change site's app");
        }
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                Long id = site.getId();
                t.hmset(entityId(Site.class, id), stringHashMapper.toHash(site));

                //add domain or subdomain index
                if (!Strings.isNullOrEmpty(site.getDomain()) && !Objects.equal(site.getDomain(), exist.getDomain())) {
                    t.del(keyByDomain(exist.getDomain()));
                    t.set(keyByDomain(site.getDomain()), id.toString());
                }
                if (!Strings.isNullOrEmpty(site.getSubdomain()) && !Objects.equal(site.getSubdomain(), exist.getSubdomain())) {
                    t.del(keyBySubDomain(exist.getApp(), exist.getSubdomain()));
                    t.set(keyBySubDomain(exist.getApp(), site.getSubdomain()), id.toString());
                }
                t.exec();
            }
        });
        return true;
    }

    public void release(Transaction t, final long siteId, final long releaseInstanceId) {
        Site site = new Site();
        site.setId(siteId);
        site.setReleaseInstanceId(releaseInstanceId);
        site.setUpdatedAt(new Date());
        t.hmset(KeyUtils.entityId(Site.class, siteId), stringHashMapper.toHash(site));
    }

    public List<Site> findByUserId(final long userId) {
        Set<String> ids = template.execute(new JedisTemplate.JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.smembers(keyUserSites(userId));
            }
        });
        return findByIds(ids);
    }

    public static String keyUserSites(Long userId) {
        return "user:" + userId + ":sites";
    }

    public static String keyByDomain(String domain) {
        return "site:domain:" + domain;
    }

    public static String keyBySubDomain(String app, String subDomain) {
        return "app:" + app + ":site:sub-domain:" + subDomain;
    }

    public static String keyAllTemplateSitesByApp(String app) {
        return "app:" + app + ":template-sites";
    }

    public static String keyAllSubSitesByApp(String app) {
        return "app:" + app + ":sub-sites";
    }
}
