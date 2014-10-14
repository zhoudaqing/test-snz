/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.container;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import io.terminus.pampas.design.model.*;
import io.terminus.pampas.design.service.PageService;
import io.terminus.pampas.design.service.SiteInstanceService;
import io.terminus.pampas.design.service.SiteService;
import io.terminus.pampas.engine.PageRender;
import io.terminus.pampas.engine.RenderConstants;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.Render;
import io.terminus.pampas.engine.exception.NotFound404Exception;
import io.terminus.pampas.engine.exception.Server500Exception;
import io.terminus.pampas.engine.model.App;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Author: Anson Chan
 * Date: 8/17/12 3:30 PM
 */
@Component
@Primary // will override PageRender in core module
@Slf4j
public class DPageRender extends PageRender {
    @Autowired
    private SiteService siteService;
    @Autowired
    private SiteInstanceService siteInstanceService;
    @Autowired
    private PageService pageService;
    @Autowired
    private ConfigManager configManager;

    /**
     * 按域名和路径渲染一个页面，用于子站和非装修页面的渲染；肯定不会是编辑模式的渲染
     */
    @Override
    public String render(String domain, String path, Map<String, Object> context) {
        App currentApp = Setting.getCurrentApp();
        String currentDomain = Setting.getCurrentDomain();
        if (isNaiveRender(currentApp.getKey(), path)) {
            return naiveRender(path, context);
        }
        Site site;
//        if (domain.endsWith(currentDomain) && !MoreObjects.equal(domain, currentDomain)) { // 如果是以 siteDomain 结尾的，就认为是二级域名
//            String subDomain = domain.substring(0, domain.length() - currentApp.getDomain().length() - 1);
//            site = siteService.findBySubdomain(subDomain);
//        } else {
//            site = siteService.findByDomain(domain);
//        }
        if (Objects.equal(domain, currentDomain)) { // 如果相同，就去找 root 子站
            site = siteService.findBySubdomain(currentApp.getKey(), "/");
        } else { // 否则移除后面的域，只保留第一个子域去找
            String subDomain = domain.substring(0, domain.length() - currentDomain.length() - 1);
            site = siteService.findBySubdomain(currentApp.getKey(), subDomain);
        }
        if (site == null) {
            log.error("site not found by domain: {}", domain);
            throw new NotFound404Exception();
        }

        FullPage fullpage = pageService.findBy(site.getId(), site.getReleaseInstanceId(), path);
        if (fullpage == null) {
            throw new NotFound404Exception();
        }
        return designRender(fullpage, context, false);
    }

    /**
     * 按站点实例id去渲染子站或者模板内容，肯定是编辑模式的渲染
     */
    public String renderTemplate(Long siteInstanceId, String path, Map<String, Object> context) {
        TemplateInstance templateInstance = siteInstanceService.findTemplateInstanceById(siteInstanceId);
        return renderInstance(templateInstance, path, context, true);
    }

    /**
     * 按实例id去渲染个人站点（店铺），可能是正常显示也可能是编辑模式
     */
    public String renderSite(Long siteInstanceId, String path, Map<String, Object> context, boolean isDesign) {
        SiteInstance siteInstance = siteInstanceService.findSiteInstanceById(siteInstanceId);
        return renderInstance(siteInstance, path, context, isDesign);
    }

    private String renderInstance(BaseSiteInstance siteInstance, String path, Map<String, Object> context, boolean isDesign) {
        // always lower case
        path = path.toLowerCase();
        FullPage fullPage;
        if (isDesign) {
            fullPage = pageService.findByWithoutCache(siteInstance.getSiteId(), siteInstance.getId(), path);
        } else {
            fullPage = pageService.findBy(siteInstance.getSiteId(), siteInstance.getId(), path);
        }
        if (fullPage == null) {
            throw new NotFound404Exception();
        }
        return designRender(fullPage, context, isDesign);
    }

    /**
     * 渲染装修出来的页面，如果 isDesign 为 true ，则会多渲染一些额外信息用于装修
     *
     * @param page     页面
     * @param context  上下文
     * @param isDesign 是否装修模式
     * @return 渲染结果
     */
    private String designRender(FullPage page, Map<String, Object> context, boolean isDesign) {
        if (isDesign) {
            context.put(RenderConstants.DESIGN_MODE, true);
        }
        context.put(RenderConstants.PATH, page.getPath());
        context.put(RenderConstants.PAGE, page);

        Render renderConfig = configManager.getFrontConfig().getRender();
        if (renderConfig == null || Strings.isNullOrEmpty(renderConfig.getEeveeLayout())) {
            throw new Server500Exception("no [render] or [render.eeveeLayout] config found in front_config file");
        }
        return naiveRender(configManager.getFrontConfig().getRender().getEeveeLayout(), context);
    }

    private boolean isNaiveRender(String app, String path) {
        Render render = configManager.findRender(app);
        if (render == null) {
            return true;
        }
        if (render.getPrefixs() != null) {
            for (String prefix : render.getPrefixs()) {
                if (path.toLowerCase().startsWith(prefix.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
