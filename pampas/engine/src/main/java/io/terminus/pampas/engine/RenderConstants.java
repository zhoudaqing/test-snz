/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.engine;

/**
 * 在Context中标识的常量
 * Date: 8/18/12 5:24 PM
 */
public interface RenderConstants {

    String COMPONENT_PATH = "_COMP_PATH_";
    String COMPONENT_NAME = "_COMP_NAME_";
    String COMPONENT_DATA = "_COMP_DATA_";

    String DESIGN_MODE = "_DESIGN_MODE_";

    String PATH = "_PATH_";

    //用户、站点、实例的位置
    String USER = "_USER_";
    String PAGE = "_PAGE_";

    // 配置数据的key，用于嵌套调用时清理上下文
    String CDATA_KEYS = "_CDATA_KEYS_";
    //服务调用数据
    String DATA = "_DATA_";
    String ERROR = "_ERROR_";

    // 样式
    String STYLE = "_STYLE_";
    // css class
    String CLASS = "_CLASS_";

    String HREF = "_HREF_";
}