/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.service;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.Setting;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.dao.AppDao;
import io.terminus.pampas.engine.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-22
 */
@Service
public class AppServiceImpl implements AppService {
    private final AppDao appDao;
    private final ConfigManager configManager;
    private final Setting setting;

    private final LoadingCache<String, Optional<App>> domainAppCache;

    @Override
    public List<App> listAll() {
        return configManager.listAllApp();
    }

    @Override
    public Map<String, Object> listAllWithSetting() {
        Map<String, Object> result = Maps.newHashMap();
        result.put("apps", listAll());
        result.put("implant", setting.getMode() == Setting.Mode.IMPLANT);
        if (setting.getMode() == Setting.Mode.CENTER) {
            result.put("rootPath", setting.getRootPath());
        }
        return result;
    }

    @Autowired
    public AppServiceImpl(final AppDao appDao, ConfigManager configManager, Setting setting) {
        this.appDao = appDao;
        this.configManager = configManager;
        this.setting = setting;
        if (!setting.isDevMode()) {
            this.domainAppCache = CacheBuilder.newBuilder().build(new CacheLoader<String, Optional<App>>() {
                @Override
                public Optional<App> load(String domain) throws Exception {
                    return Optional.fromNullable(appDao.findByDomain(domain));
                }
            });
        } else {
            this.domainAppCache = null;
        }
    }

    @Override
    public App findByDomain(String domain) {
        if (domainAppCache == null) {
            return appDao.findByDomain(domain);
        }
        Optional<App> optionalResult = domainAppCache.getUnchecked(domain);
        return optionalResult.orNull();
    }

    @Override
    public App findByKey(String key) {
        return appDao.findByKey(key);
    }

    @Override
    public Response<String> create(App app) {
        checkNotNull(app.getKey());
        checkNotNull(app.getDomain());
        if (appDao.findByKey(app.getKey()) != null) {
            return Response.fail("app with key [" + app.getKey() + "] is already exists");
        }
        if (appDao.findByDomain(app.getDomain()) != null) {
            return Response.fail("app with domain [" + app.getDomain() + "] is already exists");
        }
        appDao.create(app);
        return Response.ok(app.getKey());
    }

    @Override
    public Response<Void> update(App app) {
        checkNotNull(app.getKey());
        if (!Strings.isNullOrEmpty(app.getDomain())) {
            App exists = appDao.findByDomain(app.getDomain());
            if (exists != null && !Objects.equal(app.getKey(), exists.getKey())) {
                return Response.fail("app with domain [" + app.getDomain() + "] is already exists");
            }
        }
        appDao.update(app);
        return Response.ok();
    }

    @Override
    public Response<Void> updateExtraDomains(String key, Set<String> extraDomains) {
        checkNotNull(key);
        checkNotNull(extraDomains);
        for (String domain : extraDomains) {
            App exists = appDao.findByDomain(domain);
            if (exists != null && !Objects.equal(key, exists.getKey())) {
                return Response.fail("app with domain [" + domain + "] is already exists");
            }
        }
        appDao.updateExtraDomains(key, extraDomains);
        return Response.ok();
    }

    @Override
    public Set<String> getExtraDomains(String key) {
        checkNotNull(key);
        return appDao.getExtraDomains(key);
    }

    @Override
    public void delete(String key) {
        checkNotNull(key);
        appDao.delete(key);
    }
}
