/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;

import com.fasterxml.jackson.databind.JavaType;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import io.terminus.common.exception.ServiceException;
import io.terminus.common.utils.JsonMapper;
import io.terminus.pampas.engine.config.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-13
 */
@Component
@Slf4j
public class HttpExecutor extends Executor<Map<String, Object>> {
    private static final Splitter splitter = Splitter.on(':').trimResults().omitEmptyStrings();

    private static final JsonMapper jsonMapper = JsonMapper.JSON_NON_EMPTY_MAPPER;
    private static final JavaType collectionType = jsonMapper.createCollectionType(HashMap.class, String.class, Object.class);

    @Override
    public Map<String, Object> exec(Service service, Map<String, Object> params) {
        HttpTarget httpTarget = HttpTarget.parseFrom(service);
        HttpRequest httpRequest;
        switch (httpTarget.method) {
            case GET:
                httpRequest = HttpRequest.get(httpTarget.uri, params, true);
                break;
            case POST:
                httpRequest = HttpRequest.post(httpTarget.uri, params, true);
                break;
            case PUT:
                httpRequest = HttpRequest.put(httpTarget.uri, params, true);
                break;
            case DELETE:
                httpRequest = HttpRequest.delete(httpTarget.uri, params, true);
                break;
            default:
                throw new IllegalStateException("Unsupported http method");
        }
        if (!httpRequest.ok()) {
            log.error("http service error: code {}, params {}, body {}", httpRequest.code(), params, httpRequest.body());
            throw new ServiceException("http service error");
        }
        String response = httpRequest.body();
        return jsonMapper.fromJson(response, collectionType);
    }

    private static class HttpTarget {
        private static Map<String, HttpMethod> methodMapping = ImmutableMap.<String, HttpMethod>builder()
                .put("GET", HttpMethod.GET)
                .put("POST", HttpMethod.POST)
                .put("DELETE", HttpMethod.DELETE)
                .put("PUT", HttpMethod.PUT)
                .build();

        private HttpMethod method;
        private String uri;

        protected static HttpTarget parseFrom(Service service) {
            HttpTarget target = new HttpTarget();
            List<String> splitResult = splitter.splitToList(service.getUri());
            target.method = methodMapping.get(splitResult.get(0).toUpperCase());
            target.uri = splitResult.get(1);
            return target;
        }
    }
}
