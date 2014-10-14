/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design;

import com.alibaba.dubbo.config.spring.ServiceBean;
import io.terminus.pampas.design.service.DesignMetaService;
import io.terminus.pampas.design.service.PageService;
import io.terminus.pampas.design.service.SiteInstanceService;
import io.terminus.pampas.design.service.SiteService;
import io.terminus.pampas.engine.Register;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-22
 */
@Configuration
public class DRegister {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Register register;

    @Bean
    public ServiceBean<DesignMetaService> dubboDesignMetaService() {
        return register.createServiceBean(DesignMetaService.class);
    }

    @Bean
    public ServiceBean<SiteService> dubboSiteService() {
        return register.createServiceBean(SiteService.class);
    }

    @Bean
    public ServiceBean<SiteInstanceService> dubboSiteInstanceService() {
        return register.createServiceBean(SiteInstanceService.class);
    }

    @Bean
    public ServiceBean<PageService> dubboPageService() {
        return register.createServiceBean(PageService.class);
    }
}
