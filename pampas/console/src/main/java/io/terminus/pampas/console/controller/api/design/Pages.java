/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.api.design;

import io.terminus.auth.core.AuthExecutor;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.console.service.PageAgentService;
import io.terminus.pampas.console.service.SiteAgentService;
import io.terminus.pampas.console.util.AuthUtil;
import io.terminus.pampas.design.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* Created by IntelliJ IDEA.
* User: AnsonChan
* Date: 13-12-2
*/
@Controller
@RequestMapping("/api/design/pages")
public class Pages {
    @Autowired
    private PageAgentService pageAgentService;
    @Autowired
    private SiteAgentService siteAgentService;
    @Autowired
    private AuthExecutor authExecutor;

    /**
     * 新建一个页面，只有子站可以随意新建页面
     *
     * @param instanceId 站点实例id
     * @param name       名字
     * @param path       路径
     * @param keywords SEO 关键字
     * @param description SEO 描述信息
     * @return id
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Long create(@RequestParam String group,
                       @RequestParam Long instanceId,
                       @RequestParam String name,
                       @RequestParam String path,
                       @RequestParam(required = false) String keywords,
                       @RequestParam(required = false) String description) {
        path = path.toLowerCase();
        TemplateInstance templateInstance = authCheck(group, instanceId);
        List<? extends Page> pages = pageAgentService.findPages(group, templateInstance.getSiteId(), templateInstance.getId());
        for (Page existedPage : pages) {
            if (existedPage.getPath().equals(path)) {
                throw new JsonResponseException(400, "路径已经存在，不能重复");
            }
        }
        TemplatePage page = new TemplatePage();
        page.setInstanceId(instanceId);
        page.setPageCategory(PageCategory.OTHER);
        page.setName(name);
        page.setPath(path);
        page.setKeywords(keywords);
        page.setDescription(description);
        return pageAgentService.createPage(group, page);
    }

    @RequestMapping(value = "/set-index", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void setIndex(@RequestParam String group, @RequestParam Long instanceId, @RequestParam String path) {
        TemplateInstance templateInstance = authCheck(group, instanceId);
        templateInstance.setIndexPath(path);
        siteAgentService.updateTemplateInstance(group, templateInstance);
    }

    /**
     * 修改子站的页面信息，子站页面可以修改名字路径以及关键字和描述
     *
     * @param id   页面id
     * @param name 名字
     * @param path 路径
     * @param keywords SEO 关键字
     * @param description SEO 描述信息
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void update(@RequestParam String group,
                       @PathVariable Long id,
                       @RequestParam String name,
                       @RequestParam String path,
                       @RequestParam(required = false) String keywords,
                       @RequestParam(required = false) String description) {
        // always lower case
        path = path.toLowerCase();
        TemplatePage templatePage = pageAgentService.findPageById(group, id);
        TemplateInstance templateInstance = authCheck(group, templatePage.getInstanceId());
        List<? extends Page> pages = pageAgentService.findPages(group, templateInstance.getSiteId(), templateInstance.getId());
        if (!templatePage.getPath().equals(path)) {
            for (Page existedPage : pages) {
                if (existedPage.getPath().equals(path)) {
                    throw new JsonResponseException(400, "路径已经存在，不能重复");
                }
            }
        }
        templatePage.setName(name);
        templatePage.setPath(path);
        templatePage.setKeywords(keywords);
        templatePage.setDescription(description);
        pageAgentService.updatePage(group, templatePage);
    }

    /**
     * 修改店铺的页面信息，店铺页面只可以修改关键字和描述
     *
     * @param id   页面id
     * @param keywords SEO 关键字
     * @param description SEO 描述信息
     */
//    @RequestMapping(value = "/shop/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public void updateSitePage(@RequestParam String group,
//                               @PathVariable Long id,
//                               @RequestParam(required = false) String keywords,
//                               @RequestParam(required = false) String description) {
//        Page page = pageAgentService.findPageById(group, id);
//        SiteInstance siteInstance = siteInstanceService.findSiteInstanceById(page.getInstanceId());
//        Site site = siteAgentService.findById(group, siteInstance.getSiteId());
//        if (!site.getUserId().equals(UserUtil.getUserId())) {
//            throw new JsonResponseException(403, "site isnt belong to u");
//        }
//        page.setKeywords(keywords);
//        page.setDescription(description);
//        pageAgentService.updatePage(group, page);
//    }

    /**
     * 删除一个页面，只有子站的页面可以删除
     *
     * @param id 页面id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void delete(@RequestParam String group, @PathVariable Long id) {
        TemplatePage templatePage = pageAgentService.findPageById(group, id);
        if (templatePage == null) {
            throw new JsonResponseException(400, "page not found");
        }
        authCheck(group, templatePage.getInstanceId());

        pageAgentService.deletePage(group, id);
    }

    private TemplateInstance authCheck(String group, Long instanceId) {
        TemplateInstance templateInstance = siteAgentService.findTemplateInstanceById(group, instanceId);
        Site site = siteAgentService.findById(group, templateInstance.getSiteId());
        // only official site can set index
        if (site.getCategory() != SiteCategory.OFFICIAL) {
            throw new JsonResponseException(500, "category illegal");
        }
        if (!authExecutor.hasPermission(UserUtil.getUserId(), AuthUtil.site(group, site.getApp(), site.getId()))) {
            throw new JsonResponseException(500, "permission denied");
        }
        return templateInstance;
    }
}
