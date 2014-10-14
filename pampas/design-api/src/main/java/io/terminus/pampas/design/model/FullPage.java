/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 页面渲染时需要的所有信息
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-21
 */
public class FullPage extends TemplatePage {

    private static final long serialVersionUID = 377392446336742100L;

    @Getter
    @Setter
    private String header;   //页面头部

    @Getter
    @Setter
    private String footer;   //页面尾部
}
