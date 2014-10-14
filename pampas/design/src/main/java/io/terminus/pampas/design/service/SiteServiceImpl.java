/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.design.dao.SiteDao;
import io.terminus.pampas.design.dao.SiteInstanceDao;
import io.terminus.pampas.design.manager.SiteManager;
import io.terminus.pampas.design.model.Site;
import io.terminus.pampas.design.model.SiteCategory;
import io.terminus.pampas.design.model.SiteInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Service
@Slf4j
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteManager siteManager;
    @Autowired
    private SiteInstanceDao siteInstanceDao;
    @Autowired
    private SiteDao siteDao;

    /**
     * 创建模板站点
     *
     * @param userId 用户id
     * @param site   模板站点
     * @return 新创建的站点id
     */
    @Override
    public Long createTemplateSite(Long userId, Site site) {
        return siteManager.createTemplateSite(site);
    }


    /**
     * 根据模板id创建店铺站点
     *
     * @param userId     卖家id
     * @param site       店铺站点
     * @param templateId 模板id
     * @return 新创建的站点id
     */
    @Override
    public Long createShopSite(Long userId, Site site, Long templateId) {
        site.setUserId(userId);
        //必须要指定一个顶级或者二级域名
        checkArgument(!Strings.isNullOrEmpty(site.getDomain()) || !Strings.isNullOrEmpty(site.getSubdomain()),
                "no domain or subDomain specified");
        List<Site> exist = siteDao.findByUserId(userId);
        if(exist.size() != 0) {
            throw new IllegalStateException("seller already has a site");
        }
        return siteManager.createShopSite(site, templateId);
    }

    /**
     * 创建子站站点
     *
     * @param userId 站点管理员id
     * @param site   子站站点
     * @return 新创建的站点id
     */
    @Override
    public Long createSubSite(Long userId, Site site) {
        site.setUserId(userId);
        checkNotNull(site.getApp(), "app 必须传入");
        //必须要指定一个顶级或者二级域名 这条规则暂时去掉
//        checkArgument(!Strings.isNullOrEmpty(site.getDomain()) || !Strings.isNullOrEmpty(site.getSubdomain()),
//                "顶级域名或者二级域名最少需选填其一");
        return siteManager.createSite(site);
    }

    /**
     * 站点使用模板进行装修,只有店铺站点才能使用模板
     *
     * @param siteId     店铺站点id
     * @param templateId 模板id
     * @return 使用该模版的装修实例id
     */
    @Override
    public Long useTemplate(Long siteId, Long templateId) {
        return siteManager.useTemplate(siteId, templateId);
    }

    /**
     * 重置一个站点的模板，会把自定义部分全都删除
     * @param siteId 站点id
     * @param templateId 店铺id
     */
    @Override
    public void resetTemplate(Long siteId, Long templateId) {
        siteManager.resetTemplate(siteId, templateId);
    }

    /**
     * 删除站点,如果删除的是模板站点,则只有未发布过的模板才可以删除
     *
     * @param userId 用户id
     * @param siteId 站点id
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteSite(Long userId, Long siteId) {
        //查找站点
        final Site site = siteManager.findById(siteId);
        if (site == null) {
            return true;
        }
        // 子站没有 owner
        if (site.getCategory() != SiteCategory.OFFICIAL) {
            checkState(Objects.equal(userId, site.getUserId()), "note site owner");
        }
        checkState(site.getCategory() != SiteCategory.TEMPLATE || site.getReleaseInstanceId() == null,
                "site(category=template) has been released ,so it can not be deleted");
        return siteManager.delete(userId, site);
    }


    /**
     * 更新站点信息,不允许更改站点的类型
     *
     * @param site 站点
     * @return 是否更新成功
     */
    @Override
    public Boolean update(Site site) {
        site.setCategory(null); //不允许更改站点的类型
        return siteManager.update(site);
    }

    /**
     * 发布站点实例,这里需要做站点实例的拷贝,并只保留一个发布后的实例
     *
     * @param siteId           站点id
     * @param designInstanceId 装修中站点实例id
     * @return 发布后站点实例id
     */
    @Override
    public Long release(Long siteId, Long designInstanceId) {
        return siteManager.release(siteId, designInstanceId);
    }

    @Override
    public Long useAndRelease(Long siteId, Long templateId) {
        Long used = useTemplate(siteId, templateId);
        return release(siteId, used);
    }

    /**
     * 根据domain查找site
     *
     * @param domain 域名
     * @return 站点信息
     */
    @Override
    public Site findByDomain(String domain) {
        checkArgument(!Strings.isNullOrEmpty(domain), "domain can not be empty");
        return siteManager.findByDomain(domain);
    }

    /**
     * 根据二级域名查找site
     *
     * @param subdomain 二级域名
     * @return 站点信息
     */
    @Override
    public Site findBySubdomain(String app, String subdomain) {
        checkArgument(!Strings.isNullOrEmpty(subdomain), "subdomain can not be empty");
        return siteManager.findBySubdomain(app, subdomain);
    }

    /**
     * 根据用户id查找所有的站点列表
     *
     * @param userId 用户id
     * @return 站点列表
     */
    @Override
    public List<Site> findByUserId(Long userId) {
        return siteManager.findByUserId(userId);
    }

    @Override
    public Site findShopByUserId(Long userId) {
        List<Site> sites = siteManager.findByUserId(userId);
        Site s = null;
        for (Site site : sites) {
            if (site.getCategory() == SiteCategory.SHOP) {
                s = site;
                break;
            }
        }
        return s;
    }

    /**
     * 根据站点id查找站点
     *
     * @param siteId 站点id
     * @return 站点信息
     */
    @Override
    public Site findById(Long siteId) {
        return siteManager.findById(siteId);
    }

    @Override
    public List<Site> listSubSites(String app) {
        return siteManager.listSites(app, SiteCategory.OFFICIAL);
    }

    /**
     * 分页查询所有站点
     *
     * @param siteType 站点类型 1-模板,2-子站,3-店铺
     * @param pageNo   起始页码
     * @param size     每页返回条数
     * @return 分页结果
     */
    @Override
    public Paging<Site> pagination(String app, Integer siteType, Integer pageNo, Integer size) {
        pageNo = MoreObjects.firstNonNull(pageNo, 1);
        size = MoreObjects.firstNonNull(size, 20);
        size = size > 0 ? size : 20;
        int offset = (pageNo - 1) * size;
        offset = offset > 0 ? offset : 0;

        SiteCategory siteCategory = SiteCategory.TEMPLATE;//默认查模板
        if (Objects.equal(siteType, 2)) {
            siteCategory = SiteCategory.OFFICIAL;
        } else if (Objects.equal(siteType, 3)) {
            siteCategory = SiteCategory.SHOP;
        }
        return siteManager.pagination(app, siteCategory, offset, size);
    }

    @Override
    public Map<String, Object> listShopTemplates(BaseUser user, String app) {
        // TODO 暂时不管分页
        Map<String, Object> context = Maps.newHashMap();
        Paging<Site> sites = siteManager.pagination(app, SiteCategory.TEMPLATE, 0, 20);
        // 过滤出已经release的模板
        List<Site> availableSites = Lists.newArrayList();
        for (Site site : sites.getData()) {
            if (site.getReleaseInstanceId() != null) {
                availableSites.add(site);
            }
        }
        context.put("sites", availableSites);
        // 卖家只可能有一个店铺 site
        Site exist = findShopByUserId(user.getId());
        SiteInstance siteInstance = siteInstanceDao.findById(exist.getReleaseInstanceId());
        // 拿到 release 的实例所使用的模板 id
        context.put("usingTemplate", siteInstance.getTemplateId());
        return context;
    }
}
