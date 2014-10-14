/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.manager;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.terminus.common.model.Paging;
import io.terminus.common.redis.utils.JedisTemplate;
import io.terminus.common.redis.utils.KeyUtils;
import io.terminus.pampas.design.dao.*;
import io.terminus.pampas.design.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Component
public class SiteManager {
    private final static Logger log = LoggerFactory.getLogger(SiteManager.class);

    @Autowired
    private JedisTemplate template;

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SiteInstanceDao siteInstanceDao;

    @Autowired
    private TemplateInstanceDao templateInstanceDao;

    @Autowired
    private TemplatePageDao templatePageDao;

    @Autowired
    private PageDao pageDao;

    /**
     * 创建模板站点
     *
     * @param site 模板站点
     * @return 新模板的id
     */
    public Long createTemplateSite(final Site site) {
        //做一些初始化
        site.setCategory(SiteCategory.TEMPLATE);
        Date now = new Date();
        site.setCreatedAt(now);
        site.setUpdatedAt(now);
        //创建模板站点实例
        final TemplateInstance instance = new TemplateInstance();
        instance.setStatus(InstanceStatus.DESIGN);//设置站点实例为装修中
        final Long siteId = siteDao.newId();
        site.setId(siteId);
        final Long instanceId = templateInstanceDao.newId();
        instance.setId(instanceId);
        //子站默认创建不发布
        site.setDesignInstanceId(instanceId);
        instance.setSiteId(siteId);
        //创建模板页面
        final TemplatePage[] templatePages = buildTemplatePages(instanceId);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                siteDao.create(t, site);
                templateInstanceDao.create(t, instance);
                templatePageDao.create(t, templatePages);
                t.rpush(SiteDao.keyAllTemplateSitesByApp(site.getApp()), String.valueOf(siteId));
                t.exec();
            }
        });

        return siteId;
    }

    private void checkDomain(Site site) {
        if (!Strings.isNullOrEmpty(site.getDomain())) {
            Site domainSite = siteDao.findByDomain(site.getDomain());
            if (domainSite != null && !Objects.equal(domainSite.getId(), site.getId())) {
                throw new IllegalArgumentException("domain exists");
            }
        }

        if (!Strings.isNullOrEmpty(site.getSubdomain())) {
            Site subDomainSite = siteDao.findBySubdomain(site.getApp(), site.getSubdomain());
            if (subDomainSite != null && !Objects.equal(subDomainSite.getId(), site.getId())) {
                throw new IllegalArgumentException("subDomain exists");
            }
        }
    }

    /**
     * 创建3个模板页面
     *
     * @return 3个模板页面
     */
    private TemplatePage[] buildTemplatePages(long instanceId) {
        List<TemplatePage> pages = Lists.newArrayListWithCapacity(3);
        for (PageCategory pageCategory : PageCategory.values()) {
            if (pageCategory != PageCategory.OTHER) {
                TemplatePage page = new TemplatePage();
                page.setId(templatePageDao.newId());
                page.setPageCategory(pageCategory);
                page.setName(pageCategory.getName());
                page.setPath(pageCategory.getPath());
                page.setInstanceId(instanceId);
                pages.add(page);
            }
        }
        return pages.toArray(new TemplatePage[pages.size()]);
    }

    /**
     * 创建店铺实例
     *
     * @param site       店铺站点
     * @param templateId 模板id
     * @return 新站点的id
     */
    public Long createShopSite(final Site site, final Long templateId) { // TODO 不应该叫 shop
        //判断域名是否重复
        checkDomain(site);
        //做一些初始化
        site.setCategory(SiteCategory.SHOP);
        Date now = new Date();
        site.setCreatedAt(now);
        site.setUpdatedAt(now);
        //创建店铺站点实例
        final SiteInstance instance = new SiteInstance();
        instance.setStatus(InstanceStatus.RELEASE);//设置站点实例为发布
        final Long siteId = siteDao.newId();
        site.setId(siteId);
        final Long instanceId = siteInstanceDao.newId();
        instance.setId(instanceId);
        //创建店铺站点时直接发布
        site.setReleaseInstanceId(instanceId);
        instance.setSiteId(siteId);
        //设置店铺站点实例的模板id
        instance.setTemplateId(templateId);
        //创建店铺页面
        final Page[] pages = buildPages(instanceId);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                siteDao.create(t, site);
                siteInstanceDao.create(t, instance);
                pageDao.create(t, pages);
                t.exec();
            }
        });
        return siteId;
    }

    /**
     * 站点使用模板进行装修,只有店铺站点才能使用模板,如果站点之前已经使用过该模板进行装修,这直接返回该实例
     *
     * @param siteId     站点id
     * @param templateId 模板id
     * @return 使用该模版的装修实例id
     */
    public Long useTemplate(final Long siteId, final Long templateId) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("site(id={}) not found", siteId);
            throw new IllegalArgumentException("site not found");
        }
        if (site.getCategory() != SiteCategory.SHOP) {
            log.error("only shop site can apply template,but is a {}", site.getCategory());
            throw new IllegalArgumentException("only shop site can apply template");
        }
        //如果站点之前已经使用过该模板进行装修,则直接返回该实例
        List<SiteInstance> designInstances = siteInstanceDao.findDesignInstanceBySiteId(siteId);
        for (SiteInstance designInstance : designInstances) {
            if (Objects.equal(designInstance.getTemplateId(), templateId)) {
                return designInstance.getId();
            }
        }

        //创建店铺站点实例
        final SiteInstance instance = new SiteInstance();
        instance.setStatus(InstanceStatus.DESIGN);//设置站点实例为装修中
        final Long instanceId = siteInstanceDao.newId();
        instance.setId(instanceId);
        site.setDesignInstanceId(instanceId);
        instance.setSiteId(siteId);
        //设置店铺站点实例的模板id
        instance.setTemplateId(templateId);
        //创建店铺页面
        final Page[] pages = buildPages(instanceId);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                siteInstanceDao.create(t, instance);
                pageDao.create(t, pages);
                t.exec();
            }
        });
        return instanceId;
    }

    /**
     * 重置一个站点的模板，会把自定义部分全都删除
     * @param siteId 站点id
     * @param templateId 店铺id
     */
    public void resetTemplate(final Long siteId, final Long templateId) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("site(id={}) not found", siteId);
            throw new IllegalArgumentException("site not found");
        }
        if (site.getCategory() != SiteCategory.SHOP) {
            log.error("only shop site can apply template,but is a {}", site.getCategory());
            throw new IllegalArgumentException("only shop site can apply template");
        }
        Long oldId = null;
        List<SiteInstance> designInstances = siteInstanceDao.findDesignInstanceBySiteId(siteId);
        for (SiteInstance designInstance : designInstances) {
            if (Objects.equal(designInstance.getTemplateId(), templateId)) {
                oldId = designInstance.getId();
                break;
            }
        }
        // 没有旧的实例就直接返回
        if (oldId == null) return;
        final List<Page> pages = pageDao.findBySiteInstanceId(oldId);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                // clean all json parts in every page
                for (Page page: pages) {
                    pageDao.delParts(t, page.getId());
                }
                t.exec();
            }
        });
    }

    private Page[] buildPages(Long instanceId) {
        List<Page> pages = Lists.newArrayListWithCapacity(3);
        for (PageCategory pageCategory : PageCategory.values()) {
            if (pageCategory != PageCategory.OTHER) {
                Page page = new Page();
                page.setId(pageDao.newId());
                page.setPageCategory(pageCategory);
                page.setName(pageCategory.getName());
                page.setPath(pageCategory.getPath());
                page.setInstanceId(instanceId);
                pages.add(page);
            }
        }
        return pages.toArray(new Page[pages.size()]);
    }

    /**
     * 创建子站点,不需要为子站创建页面
     *
     * @param site 子站
     * @return 新创建的站点id
     */
    public Long createSite(final Site site) {
        //判断域名是否重复
        checkDomain(site);
        //做一些初始化
        site.setCategory(SiteCategory.OFFICIAL);
        Date now = new Date();
        site.setCreatedAt(now);
        site.setUpdatedAt(now);
        //创建模板站点实例
        final TemplateInstance instance = new TemplateInstance();
        instance.setStatus(InstanceStatus.DESIGN);//设置站点实例为装修中
        final Long siteId = siteDao.newId();
        site.setId(siteId);
        final Long instanceId = templateInstanceDao.newId();
        instance.setId(instanceId);
        //子站默认创建不发布
        site.setDesignInstanceId(instanceId);
        instance.setSiteId(siteId);
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                siteDao.create(t, site);
                templateInstanceDao.create(t, instance);
                t.rpush(SiteDao.keyAllSubSitesByApp(site.getApp()), String.valueOf(siteId));
                t.exec();
            }
        });

        return siteId;
    }

    /**
     * 根据用户id查找属于他的站点列表
     *
     * @param userId 用户id
     * @return 站点列表
     */
    public List<Site> findByUserId(long userId) {
        return siteDao.findByUserId(userId);
    }

    /**
     * 物理删除站点,需要删除所有的instance,page
     *
     * @param userId 用户id
     * @param site   站点
     * @return 是否删除成功
     */
    public boolean delete(final Long userId, final Site site) {

        final Long siteId = site.getId();
        //查找站点的所有(装修中和发布后)实例
        Iterable<String> instanceIds = siteInstanceDao.designInstanceIds(siteId);
        if (site.getReleaseInstanceId() != null) {
            instanceIds = Iterables.concat(instanceIds, ImmutableList.of(site.getReleaseInstanceId().toString()));
        }
        final Iterable<String> allInstanceIds = instanceIds;
        Set<String> pageIds;
        if (site.getCategory() == SiteCategory.SHOP) { //寻找店铺实例的页面id列表
            final List<SiteInstance> instances = siteInstanceDao.findByIds(allInstanceIds);

            //根据站点实例寻找所有的page实例
            pageIds = Sets.newHashSetWithExpectedSize(instances.size() * 3);
            for (String instanceId : instanceIds) {
                pageIds.addAll(pageDao.pageIdsByInstanceId(Long.parseLong(instanceId)));
            }
        } else {  //寻找模板或者子站实例的页面id列表
            final List<TemplateInstance> instances = templateInstanceDao.findByIds(allInstanceIds);

            //根据站点实例寻找所有的page实例
            pageIds = Sets.newHashSetWithExpectedSize(instances.size() * 3);
            for (String instanceId : instanceIds) {
                pageIds.addAll(templatePageDao.pageIdsByInstanceId(Long.parseLong(instanceId)));
            }
        }

        final Set<String> allPageIds = pageIds;

        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                //删除instances
                deleteInstances(t, allPageIds, allInstanceIds, site.getCategory());

                //删除site到instance的索引
                t.del(SiteInstanceDao.keyBySiteId(siteId));

                //删除site
                t.del(KeyUtils.entityId(Site.class, siteId));

                //删除user 到site的索引,以及domain和subdomain的索引
                t.srem(SiteDao.keyUserSites(userId), String.valueOf(siteId));

                if (!Strings.isNullOrEmpty(site.getDomain())) {
                    t.del(SiteDao.keyByDomain(site.getDomain()));
                }

                if (!Strings.isNullOrEmpty(site.getSubdomain())) {
                    t.del(SiteDao.keyBySubDomain(site.getApp(), site.getSubdomain()));
                }
                final String allSitesKey = switchAllSitesKey(site.getCategory(), site.getApp());
                if (!Strings.isNullOrEmpty(allSitesKey)) {
                    t.lrem(allSitesKey, 1, String.valueOf(siteId));
                }
                t.exec();
            }
        });
        return Boolean.TRUE;
    }

    private String switchAllSitesKey(SiteCategory siteCategory, String app) {
        switch (siteCategory) {
            case TEMPLATE:
                return SiteDao.keyAllTemplateSitesByApp(app);
            case OFFICIAL:
                return SiteDao.keyAllSubSitesByApp(app);
            default:
                return null;
        }
    }

    /**
     * 删除各种站点实例
     *
     * @param t              redis事务
     * @param pageIds        页面id列表
     * @param allInstanceIds 实例列表
     * @param siteCategory   是模板或者子站实例,还是店铺实例
     */
    private void deleteInstances(Transaction t, Set<String> pageIds, Iterable<String> allInstanceIds, final SiteCategory siteCategory) {

        if (!pageIds.isEmpty()) {
            //删除pages
            t.del(Iterables.toArray(Iterables.transform(pageIds, new Function<String, String>() {
                @Override
                public String apply(String pageId) {
                    if (siteCategory == SiteCategory.SHOP) {
                        return KeyUtils.entityId(Page.class, Long.parseLong(pageId));
                    } else {
                        return KeyUtils.entityId(TemplatePage.class, Long.parseLong(pageId));
                    }
                }
            }), String.class));
        }

        if (!Iterables.isEmpty(allInstanceIds)) {
            //删除instance到page的索引
            t.del(Iterables.toArray(Iterables.transform(allInstanceIds, new Function<String, String>() {
                @Override
                public String apply(String instanceId) {
                    if (siteCategory == SiteCategory.SHOP) {
                        return PageDao.keyByInstanceId(Long.parseLong(instanceId));
                    } else {
                        return TemplatePageDao.keyByInstanceId(Long.parseLong(instanceId));
                    }
                }
            }), String.class));

            //删除instances
            t.del(Iterables.toArray(Iterables.transform(allInstanceIds, new Function<String, String>() {
                @Override
                public String apply(String instanceId) {
                    if (siteCategory == SiteCategory.SHOP) {
                        return KeyUtils.entityId(SiteInstance.class, Long.parseLong(instanceId));
                    } else {
                        return KeyUtils.entityId(TemplateInstance.class, Long.parseLong(instanceId));
                    }
                }
            }), String.class));
        }
    }

    /**
     * 更新站点信息
     *
     * @param site 站点
     * @return 是否更新成功
     */
    public boolean update(Site site) {
        checkDomain(site);
        return siteDao.update(site);
    }

    /**
     * 发布站点实例,这里需要做站点实例的拷贝,并只保留一个发布后的实例
     *
     * @param siteId           站点id
     * @param designInstanceId 装修中站点实例id
     * @return 发布后站点实例的id
     */
    public Long release(final Long siteId, Long designInstanceId) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("no site found where id={}", siteId);
            throw new IllegalArgumentException("site not found");
        }
        if (site.getCategory() == SiteCategory.SHOP) {//发布店铺站点实例
            return releaseShopInstance(site, designInstanceId);
        } else { //发布模板或者子站装修实例
            return releaseTemplateInstance(site, designInstanceId);
        }
    }

    /**
     * 发布店铺站点实例
     *
     * @param site             站点
     * @param designInstanceId 待发布的装修实例id
     * @return 发布后的站点实例id
     */
    private Long releaseShopInstance(Site site, Long designInstanceId) {
        final Long siteId = site.getId();
        SiteInstance dsi = siteInstanceDao.findById(designInstanceId);
        if (dsi == null) {
            log.error("no shop site instance found where id ={}", designInstanceId);
            throw new IllegalArgumentException("shop site instance not found");
        }
        //查找以前的发布后的站点实例以及页面id以备删除
        final Long oldInstanceId = site.getReleaseInstanceId();

        Set<String> opids = null;
        if (oldInstanceId != null) {
            opids = pageDao.pageIdsByInstanceId(oldInstanceId);
        }

        final Set<String> oldPageIds = opids;

        //复制实例
        final SiteInstance rsi = new SiteInstance();
        rsi.setId(siteInstanceDao.newId());
        rsi.setSiteId(dsi.getSiteId());
        rsi.setTemplateId(dsi.getTemplateId());
        rsi.setStatus(InstanceStatus.RELEASE);
        Date now = new Date();
        rsi.setCreatedAt(now);

        //复制页面
        List<Page> pages = pageDao.findBySiteInstanceId(designInstanceId);
        final List<Page> rps = Lists.newArrayListWithCapacity(pages.size());
        for (Page page : pages) {
            Page rp = new Page();
            rp.setId(pageDao.newId());
            rp.setPageCategory(page.getPageCategory());
            rp.setName(page.getName());
            rp.setPath(page.getPath());
            rp.setKeywords(page.getKeywords());
            rp.setDescription(page.getDescription());
            rp.setInstanceId(rsi.getId());
            rp.setJsonParts(page.getJsonParts());
            rps.add(rp);
        }


        //保存实例及页面
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                //持久化siteInstanceId
                siteInstanceDao.create(t, rsi);
                //持久化instance的页面
                for (Page rp : rps) {
                    pageDao.create(t, rp);
                }
                //删除原有的releaseSiteInstanceId
                if (oldInstanceId != null) {
                    deleteInstances(t, oldPageIds, ImmutableList.of(oldInstanceId.toString()), SiteCategory.SHOP);
                }

                //更新site的releaseInstanceId
                siteDao.release(t, siteId, rsi.getId());

                t.exec();
            }
        });
        return rsi.getId();
    }


    private Long releaseTemplateInstance(final Site site, final Long designInstanceId) {
        final TemplateInstance dsi = templateInstanceDao.findById(designInstanceId);
        if (dsi == null) {
            log.error("no template or sub site instance (id={}) found", designInstanceId);
            throw new IllegalArgumentException("template site instance not found");
        }

        //查找以前的发布后的站点实例以及页面id以备删除
        final Long oldInstanceId = site.getReleaseInstanceId();

        Set<String> opids = null;
        if (oldInstanceId != null) {
            opids = templatePageDao.pageIdsByInstanceId(oldInstanceId);
        }

        final Set<String> oldPageIds = opids;

        //复制实例
        final TemplateInstance rsi = new TemplateInstance();
        rsi.setId(templateInstanceDao.newId());
        rsi.setSiteId(dsi.getSiteId());
        rsi.setStatus(InstanceStatus.RELEASE);
        rsi.setHeader(dsi.getHeader());
        rsi.setFooter(dsi.getFooter());
        rsi.setIndexPath(dsi.getIndexPath());
        Date now = new Date();
        rsi.setCreatedAt(now);

        //复制页面
        List<TemplatePage> pages = templatePageDao.findBySiteInstanceId(designInstanceId);
        final List<TemplatePage> rps = Lists.newArrayListWithCapacity(pages.size());
        for (TemplatePage page : pages) {
            TemplatePage rp = new TemplatePage();
            rp.setId(templatePageDao.newId());
            rp.setPageCategory(page.getPageCategory());
            rp.setName(page.getName());
            rp.setPath(page.getPath());
            rp.setKeywords(page.getKeywords());
            rp.setDescription(page.getDescription());
            rp.setInstanceId(rsi.getId());
            rp.setJsonParts(page.getJsonParts());
            rp.setFixed(page.getFixed());
            rps.add(rp);
        }


        //保存实例及页面
        template.execute(new JedisTemplate.JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                Transaction t = jedis.multi();
                //持久化siteInstanceId
                templateInstanceDao.create(t, rsi);
                //持久化instance的页面
                for (TemplatePage rp : rps) {
                    templatePageDao.create(t, rp);
                }
                //删除原有的发布实例
                if (oldInstanceId != null) {
                    deleteInstances(t, oldPageIds, ImmutableList.of(oldInstanceId.toString()), site.getCategory());
                }

                //更新site的releaseInstanceId
                siteDao.release(t, site.getId(), rsi.getId());

                t.exec();
            }
        });
        return rsi.getId();
    }


    public Site findById(Long siteId) {
        return siteDao.findById(siteId);
    }

    public List<Site> listSites(String app, SiteCategory siteCategory) {
        final String key = switchAllSitesKey(siteCategory, app);
        List<String> ids = template.execute(new JedisTemplate.JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.lrange(key, 0, -1);
            }
        });
        return siteDao.findByIds(ids);
    }

    /**
     * 分页查询所有站点
     *
     * @param siteCategory 站点类型
     * @param offset       起始偏移位置
     * @param size         每页返回条数
     * @return 分页结果
     */
    public Paging<Site> pagination(String app, SiteCategory siteCategory, final Integer offset, final Integer size) {
        final String key = switchAllSitesKey(siteCategory, app);
        if (key == null) {
            throw new IllegalArgumentException("unsupported siteCategory: " + siteCategory);
        }
        Long count = template.execute(new JedisTemplate.JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(key);
            }
        });
        if (count == 0L) {
            return new Paging<Site>(0L, Collections.<Site>emptyList());
        }
        List<String> ids = template.execute(new JedisTemplate.JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.lrange(key, offset, offset + size - 1);
            }
        });
        List<Site> sites = siteDao.findByIds(ids);
        return new Paging<Site>(count, sites);

    }

    /**
     * 根据顶级域名找站点
     *
     * @param domain 顶级域名
     * @return 站点
     */
    public Site findByDomain(String domain) {
        return siteDao.findByDomain(domain);
    }

    /**
     * 根据二级域名找站点
     *
     * @param subdomain 二级域名
     * @return 站点
     */
    public Site findBySubdomain(String app, String subdomain) {
        return siteDao.findBySubdomain(app, subdomain);
    }

}
