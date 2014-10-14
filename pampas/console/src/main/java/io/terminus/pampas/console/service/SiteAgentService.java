/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import io.terminus.common.utils.MapBuilder;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.console.ServiceHelper;
import io.terminus.pampas.console.auth.AuthHelpers;
import io.terminus.pampas.console.util.AuthUtil;
import io.terminus.pampas.design.model.Site;
import io.terminus.pampas.design.model.SiteCategory;
import io.terminus.pampas.design.model.TemplateInstance;
import io.terminus.pampas.design.model.TemplatePage;
import io.terminus.pampas.design.service.SiteInstanceService;
import io.terminus.pampas.design.service.SiteService;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-19
 */
@Component
public class SiteAgentService {
    private static final Pattern subDomainPattern = Pattern.compile("(/|[\\w\\-]+)");

    @Autowired
    private ServiceHelper serviceHelper;
    @Autowired
    private AuthHelpers authHelpers;

    @Export(paramNames = {"group", "app"})
    public Map<String, Object> listSitesWithApp(String group, String app) {
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        List<Site> sites = siteService.listSubSites(app);
        ConfigService configService = serviceHelper.getService(ConfigService.class, group);
        App appModel = configService.getApp(app);

        return MapBuilder.<String, Object>of().put("app", appModel, "sites", sites).map();
    }

    @Export(paramNames = {"group", "app", "site"})
    public void createSite(String group, String app, Site site) {
        checkNotNull(site.getName());
        checkNotNull(site.getSubdomain());
        authHelpers.checkPermission(AuthUtil.app(group, app));
        checkArgument(subDomainPattern.matcher(site.getSubdomain()).matches(), "二级域名只能为 [/] 或者 [由数字字母下划线和横线组成]");
        site.setApp(app);
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        siteService.createSubSite(0L, site);
    }

    @Export(paramNames = {"group", "site"})
    public void updateSite(String group, Site site) {
        checkNotNull(site.getId());
        checkNotNull(site.getSubdomain());
        authHelpers.checkPermission(AuthUtil.site(group, site.getApp(), site.getId()));
        checkArgument(subDomainPattern.matcher(site.getSubdomain()).matches(), "二级域名只能为 [/] 或者 [由数字字母下划线和横线组成]");
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        siteService.update(site);
    }

    @Export(paramNames = {"group", "siteId"})
    public void deleteSite(String group, Long siteId) {
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        Site site = siteService.findById(siteId);
        if (site == null) return; // ignore
        authHelpers.checkPermission(AuthUtil.app(group, site.getApp()));
        siteService.deleteSite(0L, siteId);
    }

    @Export(paramNames = {"group", "siteId"})
    public void publishSite(String group, Long siteId) {
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        Site site = siteService.findById(siteId);
        if (site == null) return; // ignore
        authHelpers.checkPermission(AuthUtil.site(group, site.getApp(), site.getId()));
        checkArgument(site.getCategory() == SiteCategory.OFFICIAL);
        siteService.release(siteId, site.getDesignInstanceId());
    }

    public Site findById(String group, Long siteId) {
        SiteService siteService = serviceHelper.getService(SiteService.class, group);
        return siteService.findById(siteId);
    }

    public TemplateInstance findTemplateInstanceById(String group, Long instanceId) {
        SiteInstanceService siteInstanceService = serviceHelper.getService(SiteInstanceService.class, group);
        return siteInstanceService.findTemplateInstanceById(instanceId);
    }

    public void updateTemplateInstance(String group, TemplateInstance templateInstance) {
        SiteInstanceService siteInstanceService = serviceHelper.getService(SiteInstanceService.class, group);
        siteInstanceService.updateTemplateInstance(templateInstance);
    }

    public void saveTemplateInstanceWithPage(String group, TemplateInstance templateInstance, TemplatePage templatePage) {
        SiteInstanceService siteInstanceService = serviceHelper.getService(SiteInstanceService.class, group);
        siteInstanceService.saveTemplateInstanceWithPage(templateInstance, templatePage);
    }
}
