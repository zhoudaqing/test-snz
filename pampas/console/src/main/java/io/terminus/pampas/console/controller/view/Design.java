/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.controller.view;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.net.MediaType;
import io.terminus.pampas.console.service.ConfigAgentService;
import io.terminus.pampas.console.service.DesignMetaAgentService;
import io.terminus.pampas.console.service.PageAgentService;
import io.terminus.pampas.console.service.SiteAgentService;
import io.terminus.pampas.design.model.*;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.exception.NotFound404Exception;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.webc.controller.ViewRender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* User: AnsonChan
* Date: 13-11-28
*/
@Controller
@RequestMapping("/design")
@Slf4j
public class Design {
    @Autowired
    private ViewRender viewRender;
    @Autowired
    private SiteAgentService siteAgentService;
    @Autowired
    private PageAgentService pageAgentService;
    @Autowired
    private DesignMetaAgentService designMetaAgentService;
    @Autowired
    private ConfigAgentService configAgentService;

    @RequestMapping(value = "/editor/group/{group}/app/{app}/{siteId}", method = RequestMethod.GET)
    public String designTemplate(@PathVariable String group,
                                 @PathVariable("app") String appKey,
                                 @PathVariable Long siteId,
                                 Map<String, Object> context) {
//        if (UserUtil.getCurrentUser().getTypeEnum() != BaseUser.TYPE.ADMIN) {
//            log.error("only admin can edit sub site and template, currentUser: {}", UserUtil.getCurrentUser().getId());
//            throw new UnAuthorize401Exception("管理员才能编辑子站和模板");
//        }
        Map<String, Object> editorContext = Maps.newHashMap();
        Site site = siteAgentService.findById(group, siteId);
        if (site == null) {
            log.error("can not find site when edit template, siteId: {}", siteId);
            throw new NotFound404Exception("未找到对应的站点");
        }
        editorContext.put("site", site);
        editorContext.put("mode", site.getCategory());
        TemplateInstance templateInstance = siteAgentService.findTemplateInstanceById(group, site.getDesignInstanceId());
        if (templateInstance == null) {
            log.error("can not find site instance when edit template, instanceId: {}", site.getDesignInstanceId());
            throw new NotFound404Exception("Can't find template instance.");
        }
        // clean unnecessary data
        templateInstance.setHeader(null);
        templateInstance.setFooter(null);
        editorContext.put("instance", templateInstance);
        List<? extends Page> pages = pageAgentService.findPages(group, siteId, templateInstance.getId());
        if (pages != null && !pages.isEmpty()) {
            Map<String, Page> pageMap = Maps.newHashMap();
            for (Page page : pages) {
                // clean unnecessary data
                page.setParts(null);
                page.setJsonParts(null);
                if (page instanceof TemplatePage) {
                    ((TemplatePage) page).setFixed(null);
                }
                pageMap.put(page.getPath(), page);
            }
            editorContext.put("pages", pageMap);
            Page indexPage = pageMap.get(Strings.isNullOrEmpty(templateInstance.getIndexPath()) ? "index" : templateInstance.getIndexPath());
            editorContext.put("indexPage", indexPage);
            editorContext.put("currentPage", MoreObjects.firstNonNull(indexPage, pages.get(0)));
        }
        App app = configAgentService.getApp(group, appKey);
        String configJsUrl = app.getProxyRoot() + app.getConfigJsFile();
        editorContext.put("configJs", configJsUrl);
        context.put("editorContext", editorContext);
        context.put("title", site.getCategory() == SiteCategory.OFFICIAL ? "子站编辑" : "模板编辑");
        context.put("group", group);
        context.put("app", appKey);
        return "design/editor";
    }

//    @RequestMapping(value = "/sites/{templateId}", method = RequestMethod.GET)
//    public String designSite(@PathVariable Long templateId, Map<String, Object> context) {
//        BaseUser user = UserUtil.getCurrentUser();
//        Map<String, Object> editorContext = Maps.newHashMap();
//        Response<Site> sr = siteService.findShopByUserId(user.getId());
//        if(!sr.isSuccess()){
//            log.error("failed to find shop site for user(id={}),error code:{}",user.getId(),sr.getError());
//            throw new NotFound404Exception(sr.getError());
//        }
//        Site site = sr.getResult();
//        editorContext.put("site", site);
//        editorContext.put("sellerId", user.getId());
//        editorContext.put("mode", site.getCategory());
//        Response<Long> useR = siteService.useTemplate(site.getId(), templateId);
//        if(!useR.isSuccess()){
//            log.error("failed to use template(id={}) for user(id={}),error code:{}",templateId,user.getId(),useR.getError());
//            throw new Server500Exception("failed to use template");
//        }
//        Long siteInstanceId = useR.getResult();
//        Response<SiteInstance> si = siteInstanceService.findSiteInstanceById(siteInstanceId);
//        if(!si.isSuccess()){
//            log.error("failed to find site instance(id={}),error code:{}",
//                    siteInstanceId,user.getId(),si.getError());
//            throw new NotFound404Exception(si.getError());
//        }
//        SiteInstance siteInstance = si.getResult();
//
//        // clean unnecessary data
//        editorContext.put("instance", siteInstance);
//        Response<List<? extends Page>> pr = pageService.findPages(site.getId(), siteInstance.getId());
//        Server500Exception.failToThrow(pr);
//        List<? extends Page> pages = pr.getResult();
//        if (pages != null && !pages.isEmpty()) {
//            Map<String, Page> pageMap = Maps.newHashMap();
//            for (Page page : pages) {
//                // clean unnecessary data
//                page.setParts(null);
//                page.setJsonParts(null);
//                pageMap.put(page.getPath(), page);
//            }
//            editorContext.put("pages", pageMap);
//            Page indexPage = pageMap.get("index");
//            editorContext.put("indexPage", indexPage);
//            editorContext.put("currentPage", indexPage);
//        }
//        context.put("editorContext", editorContext);
//        context.put("title", "店铺编辑");
//        return "views/" + editorLayout;
//    }

    @RequestMapping(value = "/pages/group/{group}/app/{app}", method = RequestMethod.GET)
    public void designPage(@RequestHeader("Host") String domain, @PathVariable String group, @PathVariable String app,
                           @RequestParam(required = false) Long instanceId, @RequestParam String path, @RequestParam boolean isSite,
                           HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> context) {
        context.put(RenderConstants.DESIGN_MODE, true);
//        if (instanceId == null) {
//            domain = domain.split(":")[0];
//            String subDomain = domain.substring(0, domain.length() - commonConstants.getDomain().length() - 1);
//            Site site = siteService.findBySubdomain(subDomain);
//            if (site == null) {
//                throw new NotFound404Exception("site not found for subDomain [" + subDomain + "]");
//            }
//            viewRender.viewTemplate(site.getReleaseInstanceId(), path, response, context);
//            return;
//        }
//        if (isSite) {
            // shop component need sellerId
//            context.put("sellerId", UserUtil.getCurrentUser().getId());
//            viewRender.viewSite(instanceId, path, response, true, context);
//        } else {
//            viewRender.viewTemplate(instanceId, path, response, context); // TODO
//        }
        String html = designMetaAgentService.renderTemplate(group, app, instanceId, path, context);
        response.setContentType(MediaType.HTML_UTF_8.toString());
        try {
            response.getWriter().write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
