/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.service;

import com.google.common.base.Strings;
import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.console.ServiceHelper;
import io.terminus.pampas.console.auth.AuthHelpers;
import io.terminus.pampas.console.util.AuthUtil;
import io.terminus.pampas.engine.model.App;
import io.terminus.pampas.engine.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-6-19
 */
@Component
public class AppAgentService {
    @Autowired
    private ServiceHelper serviceHelper;
    @Autowired
    private AuthHelpers authHelpers;

    @Export(paramNames = {"group", "app"})
    public void createApp(String group, App app) {
        checkArgument(!Strings.isNullOrEmpty(app.getKey()));
        checkArgument(!Strings.isNullOrEmpty(app.getDomain()));
        authHelpers.checkPermission(AuthUtil.group(group));
        AppService appService = serviceHelper.getService(AppService.class, group);
        Response<String> resp = appService.create(app);
        if (!resp.isSuccess()) {
            throw new ServiceException(resp.getError());
        }
    }

    @Export(paramNames = {"group", "app"})
    public void updateApp(String group, App app) {
        checkArgument(!Strings.isNullOrEmpty(app.getKey()));
        checkArgument(!Strings.isNullOrEmpty(app.getDomain()));
        authHelpers.checkPermission(AuthUtil.app(group, app.getKey()));
        AppService appService = serviceHelper.getService(AppService.class, group);
        Response<Void> resp = appService.update(app);
        if (!resp.isSuccess()) {
            throw new ServiceException(resp.getError());
        }
    }

    @Export(paramNames = {"group", "appKey", "extraDomains"})
    public void updateExtraDomains(String group, String appKey, Set<String> extraDomains) {
        authHelpers.checkPermission(AuthUtil.app(group, appKey));
        AppService appService = serviceHelper.getService(AppService.class, group);
        Response<Void> resp = appService.updateExtraDomains(appKey, extraDomains);
        if (!resp.isSuccess()) {
            throw new ServiceException(resp.getError());
        }
    }

    @Export(paramNames = {"group", "appKey"})
    public Set<String> getExtraDomains(String group, String appKey) {
        AppService appService = serviceHelper.getService(AppService.class, group);
        return appService.getExtraDomains(appKey);
    }

    @Export(paramNames = {"group", "appKey"})
    public void deleteApp(String group, String appKey) {
        checkNotNull(appKey);
        authHelpers.checkPermission(AuthUtil.group(group));
        AppService appService = serviceHelper.getService(AppService.class, group);
        appService.delete(appKey);
    }

    @Export(paramNames = {"group", "appKey"})
    public App getApp(String group, String appKey) {
        checkNotNull(appKey);
        AppService appService = serviceHelper.getService(AppService.class, group);
        return appService.findByKey(appKey);
    }

    @Export(paramNames = {"group"})
    public Map<String, Object> listAllAppsWithSetting(String group) {
        AppService appService = serviceHelper.getService(AppService.class, group);
        return appService.listAllWithSetting();
    }
}
