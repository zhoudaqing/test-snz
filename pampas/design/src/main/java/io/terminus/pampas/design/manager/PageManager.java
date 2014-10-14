/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.manager;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.utils.BeanMapper;
import io.terminus.pampas.design.dao.*;
import io.terminus.pampas.design.model.*;
import io.terminus.pampas.engine.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-22
 */
@Component
public class PageManager {
    private final static Logger log = LoggerFactory.getLogger(PageManager.class);


    private final SiteDao siteDao;

    private final PageDao pageDao;

    private final TemplatePageDao templatePageDao;

    private final SiteInstanceDao siteInstanceDao;

    private final TemplateInstanceDao templateInstanceDao;

    private final LoadingCache<String, Optional<FullPage>> pageCache;

    private final static Splitter keySplitter = Splitter.on('-').limit(3).omitEmptyStrings().trimResults();

    @Autowired
    public PageManager(final Setting setting,
                       final SiteDao siteDao,
                       final PageDao pageDao,
                       final TemplatePageDao templatePageDao,
                       final SiteInstanceDao siteInstanceDao,
                       final TemplateInstanceDao templateInstanceDao) {
        this.siteDao = siteDao;
        this.pageDao = pageDao;
        this.templatePageDao = templatePageDao;
        this.siteInstanceDao = siteInstanceDao;
        this.templateInstanceDao = templateInstanceDao;
        this.pageCache = optionalCache(!setting.isDevMode());

    }

    /**
     * 创建从siteId,instanceId,path到 full page的缓存
     * @return 缓存
     */
    private LoadingCache<String, Optional<FullPage>> optionalCache(boolean buildCache) {
        if (buildCache) {
            return CacheBuilder.newBuilder()
                    .maximumSize(100000)
                    .expireAfterWrite(30, TimeUnit.MINUTES)
                    .build(
                            new CacheLoader<String, Optional<FullPage>>() {
                                @Override
                                public Optional<FullPage> load(String key) throws Exception {
                                    List<String> parts = keySplitter.splitToList(key);
                                    Long siteId = Long.parseLong(parts.get(0));
                                    Long instanceId = Long.parseLong(parts.get(1));
                                    String path = parts.get(2);
                                    FullPage fp = buildFullPage(siteId, instanceId, path);
                                    return Optional.fromNullable(fp);
                                }
                            });
        }
        return null;
    }

    /**
     * 根据参数查询页面渲染所需要的全部信息
     *
     * @param siteId     站点id
     * @param instanceId 实例id
     * @param path       页面路径
     * @param findCache  是否去cache里面找一发
     * @return 页面
     */
    public FullPage findBy(Long siteId, Long instanceId, String path, boolean findCache) {
        path = path.toLowerCase();
        if (pageCache != null && findCache) {
            Optional<FullPage> fp = pageCache.getUnchecked(siteId + "-" + instanceId + "-" + path);
            return fp.orNull();
        } else {
            return buildFullPage(siteId, instanceId, path);
        }
    }

    /**
     * 创建模板或者子站页面
     *
     * @param page 页面
     * @return 新页面id
     */
    public Long create(TemplatePage page) {
        return templatePageDao.create(page);
    }

    /**
     * 删除模板或者子站页面
     *
     * @param id 页面id
     */
    public void deleteTemplatePage(Long id) {
        templatePageDao.delete(id);
    }

    /**
     * 根据参数查询页面id列表
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面id列表
     */
    public List<Long> findPageIds(Long siteId, Long instanceId) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("site(id={}) not found", siteId);
            throw new IllegalArgumentException("site not found");
        }
        Set<String> ids;
        if (site.getCategory() == SiteCategory.SHOP) {
            ids = pageDao.pageIdsByInstanceId(instanceId);
        } else {
            ids = templatePageDao.pageIdsByInstanceId(instanceId);
        }
        List<Long> result = Lists.newArrayListWithCapacity(ids.size());
        for (String id : ids) {
            result.add(Long.parseLong(id));
        }
        return result;
    }

    /**
     * 根据参数查询页面列表
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面id列表
     */
    public List<? extends Page> findPages(Long siteId, Long instanceId) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("site(id={}) not found", siteId);
            throw new IllegalArgumentException("site not found");
        }
        Set<String> ids;
        if (site.getCategory() == SiteCategory.SHOP) {
            ids = pageDao.pageIdsByInstanceId(instanceId);
            return pageDao.findByIds(ids);
        } else {
            ids = templatePageDao.pageIdsByInstanceId(instanceId);
            return templatePageDao.findByIds(ids);
        }
    }


    private FullPage buildFullPage(Long siteId, Long instanceId, String path) {
        Site site = siteDao.findById(siteId);
        if (site == null) {
            log.error("failed to find site(id={})", siteId);
            return null;
        }
        SiteCategory type = site.getCategory();
        return buildFullPage(type, instanceId, path);
    }


    private FullPage buildFullPage(SiteCategory type, Long instanceId, String path) {

        if (type == SiteCategory.SHOP) { //是店铺页面

            return buildShopFullPage(instanceId, path);
        } else {//是模板页面或者子站页面
            return buildTemplateFullPage(instanceId, path);
        }
    }

    private FullPage buildShopFullPage(Long instanceId, String path) {
        SiteInstance instance = siteInstanceDao.findById(instanceId);
        if (instance == null) {
            log.error("no shop site instance(id={}) found.", instanceId);
            return null;
        }
        List<Page> pages = pageDao.findBySiteInstanceId(instanceId);
        for (Page page : pages) {
            if (Objects.equal(page.getPath(), path)) {
                FullPage fp = new FullPage();
                BeanMapper.copy(page, fp);

                //寻找模板
                Site t = siteDao.findById(instance.getTemplateId());
                if (t == null) {
                    log.error("no template(id={}) found ", instance.getTemplateId());
                    return null;
                }
                //寻找模板的 header 与 footer 部分
                long templateInstanceId = t.getReleaseInstanceId();
                TemplateInstance ti = templateInstanceDao.findById(templateInstanceId);
                if (ti == null) {
                    log.error("no template instance(id={}) found for template site(id={})", templateInstanceId, t.getId());
                    return null;
                }
                fp.setHeader(ti.getHeader());
                fp.setFooter(ti.getFooter());
                // 填充对应模板页面的 fix 部分和 parts 部分（如果需要的话）
                fillShopPage(templateInstanceId, fp);
                return fp;
            }
        }
        log.error("no shop page of site instance(id={}) match path({})", instance.getId(), path);
        return null;
    }

    private void fillShopPage(Long templateInstanceId, FullPage page) {
        List<TemplatePage> tps = templatePageDao.findBySiteInstanceId(templateInstanceId);
        for (TemplatePage tp : tps) {
            if (Objects.equal(tp.getPath(), page.getPath())) {
                page.setFixed(tp.getFixed());
                // 如果自己没有 parts 就直接用模板的 parts
                if (page.getParts() == null) {
                    page.setParts(tp.getParts());
                // 否则当模板的 parts 不为 null 时 merge 两者
                } else if (tp.getParts() != null) {
                    Map<String, String> mergedParts = Maps.newHashMap(tp.getParts());
                    mergedParts.putAll(page.getParts());
                    page.setParts(mergedParts);
                }
                break;
            }
        }
    }


    private FullPage buildTemplateFullPage(Long instanceId, String path) {
        if (Objects.equal(path, "index")) {
            TemplateInstance templateInstance = templateInstanceDao.findById(instanceId);
            path = Strings.isNullOrEmpty(templateInstance.getIndexPath()) ? path : templateInstance.getIndexPath();
        }
        List<TemplatePage> tps = templatePageDao.findBySiteInstanceId(instanceId);
        for (TemplatePage tp : tps) {
            if (path.equalsIgnoreCase(tp.getPath())) { // 大小写不敏感
                FullPage fp = new FullPage();
                BeanMapper.copy(tp, fp);
                //寻找模板实例
                TemplateInstance ti = templateInstanceDao.findById(instanceId);
                if (ti == null) {
                    log.error("no template instance(id={}) found", instanceId);
                    return null;
                }
                //设置头尾
                fp.setHeader(ti.getHeader());
                fp.setFooter(ti.getFooter());
                return fp;
            }
        }
        log.error("no template page found for template instance(id={}) and path={}", instanceId, path);
        return null;
    }


}
