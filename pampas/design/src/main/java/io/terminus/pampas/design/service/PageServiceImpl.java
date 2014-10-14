/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import io.terminus.pampas.design.dao.PageDao;
import io.terminus.pampas.design.dao.TemplatePageDao;
import io.terminus.pampas.design.manager.PageManager;
import io.terminus.pampas.design.model.FullPage;
import io.terminus.pampas.design.model.Page;
import io.terminus.pampas.design.model.TemplatePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
@Service
public class PageServiceImpl implements PageService {
    private static Logger log = LoggerFactory.getLogger(PageServiceImpl.class);

    @Autowired
    private PageManager pageManager;
    @Autowired
    private PageDao pageDao;
    @Autowired
    private TemplatePageDao templatePageDao;

    @Override
    public FullPage findBy(Long siteId, Long instanceId, String path) {
        return findBy0(siteId, instanceId, path, true);
    }

    @Override
    public FullPage findByWithoutCache(Long siteId, Long instanceId, String path) {
        return findBy0(siteId, instanceId, path, false);
    }

    private FullPage findBy0(Long siteId, Long instanceId, String path, boolean findCache) {
        checkArgument(instanceId != null && siteId != null, "site id and instanceId both required");
        path = !Strings.isNullOrEmpty(path) ? path : "index"; // 如果没有指定path,则默认访问首页
        return pageManager.findBy(siteId, instanceId, path, findCache);
    }

    @Override
    public Page findPageById(Long id) {
        checkNotNull(id, "page id can not be null");
        return pageDao.findById(id);
    }

    @Override
    public TemplatePage findTemplatePageById(Long id) {
        checkNotNull(id, "page id can not be null");
        return templatePageDao.findById(id);
    }

    /**
     * 根据站点实例id来查找所有的页面id
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面id列表
     */
    @Override
    public List<Long> findPageIds(Long siteId, Long instanceId) {
        checkArgument(instanceId != null && siteId != null, "site id and instanceId both required");
        return pageManager.findPageIds(siteId, instanceId);
    }

    /**
     * 根据站点实例id来查找所有的页面
     *
     * @param siteId     站点id
     * @param instanceId 站点实例id
     * @return 页面列表
     */
    @Override
    public List<? extends Page> findPages(Long siteId, Long instanceId) {
        checkArgument(instanceId != null && siteId != null, "site id and instanceId both required");
        return pageManager.findPages(siteId, instanceId);
    }

    /**
     * 创建子站页面
     *
     * @param templatePage 子站页面
     * @return 新创建的子站页面id
     */
    @Override
    public Long createSubSitePage(TemplatePage templatePage) {
        checkArgument(templatePage.getInstanceId() != null && !Strings.isNullOrEmpty(templatePage.getPath()),
                "instanceId and page path should be specified");
        templatePage.setName(MoreObjects.firstNonNull(templatePage.getName(), "新页面"));
        return pageManager.create(templatePage);
    }

    @Override
    public void updateSubSitePage(TemplatePage templatePage) {
        checkNotNull(templatePage.getId(), "page id can not be null");
        checkArgument(templatePage.getInstanceId() != null && !Strings.isNullOrEmpty(templatePage.getPath()),
                "instanceId and page path should be specified");
        templatePageDao.update(templatePage);
    }

    /**
     * 删除子站页面
     *
     * @param pageId 子站页面id
     */
    @Override
    public void deleteSubSitePage(Long pageId) {
        checkNotNull(pageId, "page id can not be null");
        pageManager.deleteTemplatePage(pageId);
    }

    @Override
    public void updatePage(Page page) {
        checkArgument(page.getInstanceId() != null && !Strings.isNullOrEmpty(page.getPath()),
                "instanceId and page path should be specified");
        pageDao.update(page);
    }
}
