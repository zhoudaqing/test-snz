/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.client;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: AnsonChan
 * Date: 14-5-11
 */
public abstract class Action implements Serializable{
    @Getter
    @Setter
    private Object data;
}
