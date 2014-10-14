/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-3
 */
@Component
public class SettingHelper {
    @Autowired
    private Setting setting;
    @Autowired
    protected AppService appService;

    public App findByDomain(String domain) {
        if (setting.getMode() == Setting.Mode.IMPLANT) {
            return setting.getImplantApp();
        } else {
            return appService.findByDomain(domain);
        }
    }
}
