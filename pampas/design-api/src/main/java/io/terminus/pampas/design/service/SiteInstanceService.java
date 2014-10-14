/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import io.terminus.pampas.design.model.SiteInstance;
import io.terminus.pampas.design.model.TemplateInstance;
import io.terminus.pampas.design.model.TemplatePage;

import java.util.List;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public interface SiteInstanceService {

    /**
     * 创建siteInstance
     *
     * @param siteInstance 被创建的siteInstance
     * @return 新创建siteInstance的id
     */
    Long createSiteInstance(SiteInstance siteInstance);

    /**
     * 根据siteId查找所有可装修的siteInstance
     *
     * @param siteId 站点id
     * @return 可装修的siteInstance列表
     */
    List<SiteInstance> findSiteInstanceBySiteId(Long siteId);

    /**
     * 根据id查找siteInstance
     *
     * @param id 站点id
     * @return siteInstance
     */
    SiteInstance findSiteInstanceById(Long id);

    /**
     * 创建template templateInstance
     *
     * @param templateInstance templateInstance
     * @return id
     */
    Long createTemplateInstance(TemplateInstance templateInstance);

    void updateTemplateInstance(TemplateInstance templateInstance);

    /**
     * 根据id获取templateInstance
     *
     * @param id id
     * @return templateInstance
     */
    TemplateInstance findTemplateInstanceById(Long id);

    /**
     * 同时保存template instance和一个属于它的页面
     *
     * @param templateInstance
     * @param templatePage
     * @return
     */
    Long saveTemplateInstanceWithPage(TemplateInstance templateInstance, TemplatePage templatePage);
}
