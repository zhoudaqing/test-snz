/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.google.common.collect.Maps;
import io.terminus.common.utils.Splitters;
import io.terminus.pampas.engine.config.ConfigManager;
import io.terminus.pampas.engine.config.model.HttpMethod;
import io.terminus.pampas.engine.config.model.Mapping;
import io.terminus.pampas.engine.config.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-13
 */
@Component
@Slf4j
public class Invoker {
    @Autowired
    private ConfigManager configManager;
    @Autowired
    private SpringExecutor springExecutor;
    @Autowired
    private HttpExecutor httpExecutor;
    @Autowired
    private DubboExecutor dubboExecutor;

    private Map<Service.ServiceType, Executor<?>> executorMap = Maps.newHashMap();

    @PostConstruct
    public void init() {
        executorMap.put(Service.ServiceType.SPRING, springExecutor);
        executorMap.put(Service.ServiceType.DUBBO, dubboExecutor);
        executorMap.put(Service.ServiceType.HTTP, httpExecutor);
    }

    public Object invoke(String serviceUri, Map<String, Object> params) {
        List<String> serviceInfo = Splitters.COLON.splitToList(serviceUri);
        return invoke(serviceInfo.get(0), serviceInfo.get(1), params);
    }

    public Object invoke(String app, String key, Map<String, Object> params) {
        checkNotNull(key, "Service key should not be null");
        Service service = configManager.findService(app, key);
        if (service == null) {
            log.error("Service not found: app {}, key {}", app, key);
            throw new NullPointerException("Service not found: " + app + ":" + key);
        }
        Executor<?> executor = executorMap.get(service.getType());
        return executor.exec(service, params);
    }

    public Mapping mappingMatch(String app, String url) {
        checkNotNull(url, "url should not be null");
        url = normalize(url);
        List<Mapping> mappings = configManager.findMappings(app);
        if (mappings == null) {
            return null;
        }
        List<String> urls = Splitters.COLON.splitToList(url);
        HttpMethod method = HttpMethod.valueOf(urls.get(0));
        url = urls.get(1);
        Mapping targetMapping = null;
        int targetMatchCount = Integer.MAX_VALUE;
        for (Mapping mapping : mappings) {
            int matchCount = match(method, url, mapping);
            if (matchCount == -1) {
                continue;
            }
            if (matchCount == 0) {
                targetMapping = mapping;
                break;
            }
            if (matchCount < targetMatchCount) {
                targetMatchCount = matchCount;
                targetMapping = mapping;
            }
        }
        return targetMapping;
    }

    public Object mappingInvoke(Mapping mapping, String url, Map<String, Object> params) {
        checkNotNull(url, "url should not be null");
        checkNotNull(mapping, "mapping should not be null");
        params.putAll(parse(url, mapping.getPattern()));
        return invoke(mapping.getService(), params);
    }

    private int match(HttpMethod method, String url, Mapping mapping) {
        // 如果不是 ALL 并且不在允许方法内 直接 return -1
        if (!mapping.getMethods().contains(HttpMethod.ALL) && !mapping.getMethods().contains(method)) {
            return -1;
        }
        String pattern = mapping.getPattern();
        String[] mappingSplits = url.split("/");
        String[] patternSplits = pattern.split("/");
        if (mappingSplits.length != patternSplits.length) {
            return -1;
        }
        int matchCount = 0;
        for (int i = 0; i < mappingSplits.length; i++) {
            if (patternSplits[i].startsWith("{")) {
                matchCount++;
                continue;
            }
            if (!patternSplits[i].equals(mappingSplits[i])) {
                return -1;
            }
        }
        return matchCount;
    }

    private Map<String, String> parse(String url, String pattern) {
        Map<String, String> patterns = Maps.newHashMap();
        if (!pattern.contains("{")) {
            return patterns;
        }
        List<String> mappingSplits = Splitters.SLASH.splitToList(url);
        List<String> patternSplits = Splitters.SLASH.splitToList(pattern);
        if (mappingSplits.size() != patternSplits.size()) {
            throw new IllegalArgumentException("not matched url");
        }

        for (int i = 0; i < mappingSplits.size(); i++) {
            String patternPart = patternSplits.get(i);
            String mappingPart = mappingSplits.get(i);
            if (patternPart.startsWith("{")) {
                patterns.put(patternPart.substring(1, patternPart.length() - 1), mappingPart);
                continue;
            }
            if (!patternPart.equals(mappingPart)) {
                throw new IllegalArgumentException("not matched url");
            }
        }
        return patterns;
    }

    private String normalize(String url) {
        if (url.startsWith("/")) {
            return url.substring(1);
        }
        return url;
    }
}
