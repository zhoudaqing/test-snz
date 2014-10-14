package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.Requirement;

/**
 * 将需求dump到搜索引擎
 * Created by yangzefeng on 14-6-23
 */
public interface RequirementIndexService {

    /**
     * 全量dump,每天一次
     */
    @Export
    Response<Boolean> fullDump();

    /**
     * 增量dump
     * @param interval 时间间隔,时间单位分钟,一般为15分钟
     */
    @Export(paramNames = {"interval"})
    Response<Boolean> deltaDump(int interval);

    /**
     * 准实时dump搜索引擎，在创建需求完成后调用，需求不会从搜索引擎中删除
     * @param id 需求id
     */
    Response<Boolean> realTimeIndex(Long id, Requirement.SearchStatus status);
}
