/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import io.terminus.pampas.common.InnerCookie;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-4-30
 */
public class WrapResp implements Serializable {
    private static final long serialVersionUID = 1025854443836742035L;
    @Getter
    @Setter
    private Object result;
    @Getter
    @Setter
    private InnerCookie cookie;
}
