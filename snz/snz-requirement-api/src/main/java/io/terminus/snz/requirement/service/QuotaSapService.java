package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.DetailQuota;

/**
 * Desc:配额信息进sap的管理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-25.
 */
public interface QuotaSapService {

    /**
     * 更改配额详细信息
     * @param detailQuota   配额详细信息
     * @return  Boolean
     * 返回更新是否成功
     */
    @Export(paramNames = {"detailQuota"})
    public Response<Boolean> updateQuota(DetailQuota detailQuota);

    /**
     * 批量的更新信息的配额信息内容
     * @param detailQuotas  详细的配额信息
     * @return Boolean
     * 返回批量更新是否成功
     */
    @Export(paramNames = {"detailQuotas"})
    public Response<Boolean> updateQuotaBatch(String detailQuotas);

    /**
     * 通过需求编号获取对应于某个需求的详细的模块配额信息
     * @param requirementId 需求编号
     * @param status        详细配额状态
     * @param pageNo        页面编号（默认0）
     * @param size          页数（默认20）
     * @return List
     * 返回需求模块的详细的配额数据信息
     */
    @Export(paramNames = {"requirementId", "status", "pageNo", "size"})
    public Response<Paging<DetailQuota>> findQuotaWithSap(Long requirementId, Integer status, Integer pageNo, Integer size);

    /**
     * 通过需求编号向sap系统传递详细的配额信息(这个系统实时调用非常耗时)
     * @param requirementId 需求编号
     * @return  Boolean
     * 返回传递数据到sap是否成功
     */
    @Export(paramNames = {"requirementId"})
    public Response<Boolean> setQuotaInfoToSAP(Long requirementId);
}
