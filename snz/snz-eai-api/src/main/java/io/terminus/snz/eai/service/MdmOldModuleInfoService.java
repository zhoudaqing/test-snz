package io.terminus.snz.eai.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.eai.model.MDMOldModuleInfo;

/**
 * Date: 8/31/14
 * Time: 21:56
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface MdmOldModuleInfoService {

    @Export(paramNames = "moduleNum")
    public Response<MDMOldModuleInfo> findByModuleNum(String moduleNum);
}
