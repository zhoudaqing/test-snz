package io.terminus.snz.related.services;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.models.FactoryOrgan;

import java.util.List;

/**
 * 工厂组织对应关系服务
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-25
 */
public interface FactoryOrganService {
    /**
     * 批量添加
     * @param factoryOrgans
     * @return
     */
    @Export(paramNames = {"factoryOrgans"})
    Response<Integer> batchAdd(List<FactoryOrgan> factoryOrgans);

    /**
     * 根据组织找共
     * @param factory 工厂
     * @return 对应的组织
     */
    @Export(paramNames = {"factory"})
    Response<List<String>> findOrganByFactory(String factory);
}
