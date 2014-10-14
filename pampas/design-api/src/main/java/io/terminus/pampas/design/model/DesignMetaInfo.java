/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-17
 */
public class DesignMetaInfo implements Serializable {
    private static final long serialVersionUID = 8184462934297490842L;

    @Getter
    @Setter
    private String redisUrl;
    @Getter
    @Setter
    private List<String> apps;
}
