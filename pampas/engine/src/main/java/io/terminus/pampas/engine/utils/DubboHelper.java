/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.utils;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-11
 */
@Component
@Slf4j
public class DubboHelper {
    private static final String DEFAULT_VERSION = "DEFAULT";

    @Autowired
    private ApplicationContext applicationContext;

    private LoadingCache<ReferenceKey, Optional<ReferenceObj>> referenceCache;
    private LoadingCache<ReferenceKey, ServiceBean<?>> providerCache;

    @PostConstruct
    private void init() {
        referenceCache = CacheBuilder.newBuilder().build(new CacheLoader<ReferenceKey, Optional<ReferenceObj>>() {
            @Override
            public Optional<ReferenceObj> load(ReferenceKey key) throws Exception {
                ReferenceBean<Object> referenceBean = new ReferenceBean<Object>();
                referenceBean.setApplicationContext(applicationContext);
                referenceBean.setInterface(key.clazz);
                if (!Strings.isNullOrEmpty(key.version) && !Objects.equal(key.version, DEFAULT_VERSION)) {
                    referenceBean.setVersion(key.version);
                }
                if (!Strings.isNullOrEmpty(key.url)) {
                    referenceBean.setUrl(key.url);
                }
                try {
                    referenceBean.afterPropertiesSet();
                    return Optional.of(new ReferenceObj(referenceBean, referenceBean.get()));
                } catch (Exception e) {
                    log.error("error when init dubbo reference bean. class {}, version {}", key.clazz, key.version, e);
                    return Optional.absent();
                }
            }
        });
        providerCache = CacheBuilder.newBuilder().build(new CacheLoader<ReferenceKey, ServiceBean<?>>() {
            @Override
            public ServiceBean<?> load(ReferenceKey key) throws Exception {
                ServiceBean<Object> serviceBean = new ServiceBean<Object>();
                serviceBean.setApplicationContext(applicationContext);
                serviceBean.setInterface(key.clazz);
                serviceBean.setRef(applicationContext.getBean(key.clazz));
                if (!Strings.isNullOrEmpty(key.version) && !Objects.equal(key.version, DEFAULT_VERSION)) {
                    serviceBean.setVersion(key.version);
                }
                serviceBean.afterPropertiesSet();
                return serviceBean;
            }
        });
    }

    @PreDestroy
    private void destroy() {
        for (Optional<ReferenceObj> objOptional : referenceCache.asMap().values()) {
            if (objOptional.isPresent()) {
                objOptional.get().referenceBean.destroy();
            }
        }
        for (ServiceBean serviceBean : providerCache.asMap().values()) {
            try {
                serviceBean.destroy();
            } catch (Exception e) {
                log.warn("error when destroy dubbo serviceBean", e);
            }
        }
    }

    public <T> T getReference(Class<T> clazz, String version) {
        return getReference(new ReferenceKey(clazz, version));
    }

    public <T> T getReference(Class<T> clazz, String version, String ip, Integer port) {
        port = MoreObjects.firstNonNull(port, 20880);
        String url = "dubbo://" + ip + ":" + port + "/" + clazz.getName();
        ReferenceKey key = new ReferenceKey(clazz, version);
        key.url = url;
        return getReference(key);
    }

    public <T> void exportProvider(Class<T> clazz, String version) {
        providerCache.getUnchecked(new ReferenceKey(clazz, version));
    }

    private <T> T getReference(ReferenceKey key) {
        Optional<ReferenceObj> referenceOptional = referenceCache.getUnchecked(key);
        if (referenceOptional.isPresent()) {
            //noinspection unchecked
            return (T) referenceOptional.get().bean;
        } else {
            return null;
        }
    }

    private static class ReferenceKey {
        private Class clazz;
        private String version = DEFAULT_VERSION;
        private String url;

        private ReferenceKey(Class clazz, String version) {
            this.clazz = clazz;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReferenceKey)) return false;

            ReferenceKey that = (ReferenceKey) o;

            if (!clazz.equals(that.clazz)) return false;
            if (url != null ? !url.equals(that.url) : that.url != null) return false;
            if (!version.equals(that.version)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz.hashCode();
            result = 31 * result + version.hashCode();
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }
    }

    private static class ReferenceObj {
        private ReferenceBean<?> referenceBean;
        private Object bean;

        private ReferenceObj(ReferenceBean<?> referenceBean, Object bean) {
            this.referenceBean = referenceBean;
            this.bean = bean;
        }
    }
}
