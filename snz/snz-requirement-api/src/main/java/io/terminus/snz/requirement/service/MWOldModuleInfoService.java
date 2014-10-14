/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.MWOldModuleInfo;

import java.util.List;


/**
 * 老品信息 服务类<BR>
 *
 * @author wanggen 2014-09-02 13:12:17
 */
public interface MWOldModuleInfoService {


    /**
     * 根据 moduleNum,moduleName 查询 MWOldModuleInfo 列表
     *
     * @param moduleNum   模块专用号、物料号
     * @param moduleName   模块描述
     * @return 结果列
     */
    @Export(paramNames = {"moduleNum", "moduleName"})
    Response<List<MWOldModuleInfo>> findByModuleNumOrModuleName(String moduleNum, String moduleName);


}
