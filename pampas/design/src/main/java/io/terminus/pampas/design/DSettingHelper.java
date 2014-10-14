/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design;

import io.terminus.pampas.design.service.SiteService;
import io.terminus.pampas.engine.SettingHelper;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.utils.Domains;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-3
 */
@Component
@Primary
@Slf4j
public class DSettingHelper extends SettingHelper {
    @Autowired
    private SiteService siteService;

    @Override
    public App findByDomain(String domain) {
        App app = super.findByDomain(domain);
        if (app != null) {
            return app;
        }
        app = appService.findByDomain(Domains.removeSubDomain(domain));
        if (app == null) {
            log.error("app not found for domain: {}", domain);
        }
        return app;
//        Site site = siteService.findByDomain(domain);
//        if (site == null) {
//            log.error("site not found for domain: {}", domain);
//            return null;
//        }
//        app = appService.findByKey(site.getApp());
//        if (app == null) {
//            log.error("app not found for key: {}, with domain: {}", site.getApp(), domain);
//            return null;
//        }
//        return app;
    }
}
