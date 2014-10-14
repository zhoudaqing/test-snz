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
 * Date: 14-4-25
 */
@ToString
public class Render implements Serializable {
    private static final long serialVersionUID = 2784212452907126587L;
    @Getter
    @Setter
    private String eeveeLayout;
    @Getter
    @Setter
    private Set<String> prefixs;
}
