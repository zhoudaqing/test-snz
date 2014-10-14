/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-20
 */
public class TemplatePage extends Page {
    private static final long serialVersionUID = -3926872603227674089L;


    @Getter
    @Setter
    private String fixed;  //页面body部分的不可改变的内容
}
