/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import io.terminus.pampas.console.ServiceHelper;
import io.terminus.pampas.design.model.Page;
import io.terminus.pampas.design.model.TemplatePage;
import io.terminus.pampas.design.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-20
 */
@Service
public class PageAgentService {
    @Autowired
    private ServiceHelper serviceHelper;

    public Long createPage(String group, TemplatePage templatePage) {
        PageService pageService = serviceHelper.getService(PageService.class, group);
        return pageService.createSubSitePage(templatePage);
    }

    public void updatePage(String group, TemplatePage templatePage) {
        PageService pageService = serviceHelper.getService(PageService.class, group);
        pageService.updateSubSitePage(templatePage);
    }

    public void deletePage(String group, Long pageId) {
        PageService pageService = serviceHelper.getService(PageService.class, group);
        pageService.deleteSubSitePage(pageId);
    }

    public List<? extends Page> findPages(String group, Long siteId, Long instanceId) {
        PageService pageService = serviceHelper.getService(PageService.class, group);
        return pageService.findPages(siteId, instanceId);
    }

    public TemplatePage findPageById(String group, Long pageId) {
        PageService pageService = serviceHelper.getService(PageService.class, group);
        return pageService.findTemplatePageById(pageId);
    }
}
