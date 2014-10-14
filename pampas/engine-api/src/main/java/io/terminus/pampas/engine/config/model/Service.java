/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-24
 */
@ToString
public class Service implements Serializable {
    private static final long serialVersionUID = 6022055077761299899L;
    @Getter
    @Setter
    private String app;
    @Getter
    @Setter
    private ServiceType type = ServiceType.DUBBO; // 默认 DUBBO
    @Getter
    @Setter
    private String uri;
    @Getter
    @Setter
    private String desc;

    public enum ServiceType {
        HTTP, DUBBO, SPRING
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;

        Service service = (Service) o;

        if (!app.equals(service.app)) return false;
        if (type != service.type) return false;
        if (!uri.equals(service.uri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = app.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + uri.hashCode();
        return result;
    }
}
