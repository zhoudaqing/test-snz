package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.User;

import java.util.List;

/**
 * Created by yangzefeng on 14-6-25
 */
public interface SupplierIndexService {

    /**
     * 供应商全量脚本
     */
    @Export
    Response<Boolean> fullDump();

    /**
     * 供应商增量脚本
     * @param interval 时间间隔，单位分钟，一般为15分钟
     */
    @Export(paramNames = {"interval"})
    Response<Boolean> deltaDump(int interval);

    /**
     *
     * @param ids 供应商id列表
     * @param status 供应商状态
     */
    Response<Boolean> realTimeIndex(List<Long> ids, User.SearchStatus status);
}
