/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.mapping;


import io.terminus.common.exception.ServiceException;
import io.terminus.pampas.common.Response;
import io.terminus.pampas.engine.MessageSources;
import io.terminus.pampas.engine.config.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-12
 */
@Slf4j
public abstract class Executor<O> {
    @Autowired(required = false)
    protected MessageSources messageSources;

    public abstract O exec(Service service, Map<String, Object> params);

    protected Object unwrapResponse(O result) {
        if (result == null) {
            return null;
        }
        if (result instanceof Response) {
            Response resp = (Response) result;
            if (resp.isSuccess()) {
                return resp.getResult();
            }
            log.error("failed to executor service, error code:{}", resp.getError());
            if (messageSources != null) {
                throw new ServiceException(messageSources.get(resp.getError()));
            } else {
                throw new ServiceException(resp.getError());
            }
        }
        if (result instanceof io.terminus.common.model.Response) {
            io.terminus.common.model.Response response = (io.terminus.common.model.Response) result;
            if (response.isSuccess()) {
                return response.getResult();
            }
            log.error("failed to executor service, error code:{}", response.getError());
            if (messageSources != null) {
                throw new ServiceException(messageSources.get(response.getError()));
            } else {
                throw new ServiceException(response.getError());
            }
        }
        return result;
    }
}
