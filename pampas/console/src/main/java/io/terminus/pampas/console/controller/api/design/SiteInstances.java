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
import io.terminus.pampas.design.model.Site;
import io.terminus.pampas.design.model.SiteInstance;
import io.terminus.pampas.design.model.TemplateInstance;
import io.terminus.pampas.design.model.TemplatePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
* Created by IntelliJ IDEA.
* User: AnsonChan
* Date: 13-11-29
*/
@Controller
@RequestMapping("/api/design/instances")
public class SiteInstances {
    private final static Logger log = LoggerFactory.getLogger(SiteInstance.class);

    @Autowired
    private SiteAgentService siteAgentService;
    @Autowired
    private PageAgentService pageAgentService;
    @Autowired
    private AuthExecutor authExecutor;

    @RequestMapping(value = "/template/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void saveTemplateInstance(@RequestParam String group,
                                     @PathVariable Long id, @RequestParam Long pageId,
                                     @RequestParam String header, @RequestParam String body, @RequestParam String footer,
                                     @RequestParam(required = false) String parts) {
        try {
            TemplateInstance templateInstance = siteAgentService.findTemplateInstanceById(group, id);
            Site site = siteAgentService.findById(group, templateInstance.getSiteId());
            if (!authExecutor.hasPermission(UserUtil.getUserId(), AuthUtil.site(group, site.getApp(), site.getId()))) {
                throw new JsonResponseException(500, "permission denied");
            }
            templateInstance.setHeader(header);
            templateInstance.setFooter(footer);
            TemplatePage templatePage = pageAgentService.findPageById(group, pageId);
            templatePage.setJsonParts(parts);
            templatePage.setFixed(body);
            siteAgentService.saveTemplateInstanceWithPage(group, templateInstance, templatePage);
        } catch (Exception e) {
            log.error("failed to save template instance for template(id={}) and page(id={}),error code:{} ", id, pageId,e.getMessage());
            throw new JsonResponseException(500, e.getMessage());
        }
    }

//    @RequestMapping(value = "/site/{instanceId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public void saveSiteInstance(@PathVariable Long instanceId, @RequestParam Long pageId, @RequestParam String parts) {
//        if (parts.length()>10240){
//            log.error("parts length is {},and long than 10k",parts.length());
//            throw new JsonResponseException(500, "内容过长");
//        }
//        try {
//            SiteInstance siteInstance = siteInstanceService.findSiteInstanceById(instanceId);
//            Site site = siteService.findById(siteInstance.getSiteId());
//            BaseUser user = UserUtil.getCurrentUser();
//            if (!MoreObjects.equal(site.getUserId(), user.getId())) {
//                log.error("site(id={}) not belong to user(id={})", site.getId(), user.getId());
//                throw new JsonResponseException(403, "site not belong to u");
//            }
//
//            Page page = pageService.findPageById(pageId);
//            page.setJsonParts(parts);
//            pageService.updatePage(page);
//        } catch (Exception e) {
//            log.error("failed to save site instance for site(instanceId={}) and page(id={}),error code:{} ", instanceId, pageId, e.getMessage());
//            throw new JsonResponseException(500, e.getMessage());
//        }
//    }
}
