/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-25
 */
@ToString
public abstract class BaseConfig implements Serializable {
    private static final long serialVersionUID = -3815877052270311523L;
    @Getter
    @Setter
    private String app;
    @Getter
    @Setter
    private String sign;
    @Getter
    @Setter
    private Date loadedAt;
    @Getter
    @Setter
    private String location;
}
