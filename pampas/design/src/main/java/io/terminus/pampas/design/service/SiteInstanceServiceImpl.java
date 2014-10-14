/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.service;

import io.terminus.pampas.design.dao.SiteInstanceDao;
import io.terminus.pampas.design.dao.TemplateInstanceDao;
import io.terminus.pampas.design.manager.SiteInstanceManager;
import io.terminus.pampas.design.model.SiteInstance;
import io.terminus.pampas.design.model.TemplateInstance;
import io.terminus.pampas.design.model.TemplatePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-11-28
 */
@Service
@Slf4j
public class SiteInstanceServiceImpl implements SiteInstanceService {
    @Autowired
    private SiteInstanceDao siteInstanceDao;
    @Autowired
    private TemplateInstanceDao templateInstanceDao;

    @Autowired
    private SiteInstanceManager siteInstanceManager;

    @Override
    public Long createSiteInstance(SiteInstance siteInstance) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<SiteInstance> findSiteInstanceBySiteId(Long siteId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SiteInstance findSiteInstanceById(Long id) {
        return siteInstanceDao.findById(id);
    }

    @Override
    public Long createTemplateInstance(TemplateInstance templateInstance) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateTemplateInstance(TemplateInstance templateInstance) {
        templateInstanceDao.update(templateInstance);
    }

    @Override
    public TemplateInstance findTemplateInstanceById(Long id) {
        return templateInstanceDao.findById(id);
    }

    @Override
    public Long saveTemplateInstanceWithPage(TemplateInstance templateInstance, TemplatePage templatePage) {
        siteInstanceManager.saveTemplateInstanceWithPage(templateInstance, templatePage);
        return templatePage.getId();
    }
}
