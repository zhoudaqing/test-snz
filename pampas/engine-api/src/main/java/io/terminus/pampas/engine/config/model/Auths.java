/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine.config.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-30
 */
@ToString
public class Auths implements Serializable {
    private static final long serialVersionUID = 6554743011603841486L;
    @Getter
    @Setter
    private Set<ProtectedAuth> protectedList;
    @Getter
    @Setter
    private Set<WhiteAuth> whiteList;
}
