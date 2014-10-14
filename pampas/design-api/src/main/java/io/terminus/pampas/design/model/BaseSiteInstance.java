/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 13-11-28
 */
public abstract class BaseSiteInstance implements Serializable {
    private static final long serialVersionUID = 2744979762662511280L;
    @Getter
    @Setter
    protected Long id;  //实例id

    @Getter
    @Setter
    protected Long siteId;  //站点id

    @Getter
    @Setter
    protected InstanceStatus status; //状态

    @Getter
    @Setter
    protected Date createdAt;
}
