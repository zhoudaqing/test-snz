package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.OldModule;

/**
 * Created by jiaoyuan on 14-7-10.
 */
public interface OldModuleService {
    /**
     * 通过筛选条件查询老品
     * @param moduleNum
     * @param timeTotalStart
     * @param timeTotalEnd
     * @param seriesName
     * @return
     * 返回老品列表
     */
    @Export(paramNames = {"moduleNum","timeTotalStart","timeTotalEnd","seriesName"})
    public Response<Paging<OldModule>> findByFilters(String moduleNum,Integer timeTotalStart,Integer timeTotalEnd,String seriesName);

    /**
     * 通过模块专用号查询老品
     * @param moduleNum
     * @return
     * 返回老品信息
     */
    @Export(paramNames = {"moduleNum"})
    public Response<OldModule> findByModuleNum(String moduleNum);

    /**
     * 根据采购商id删除
     * @param purchaserId
     * @return
     * 返回删除结果
     */
    @Export(paramNames = {"purchaserId"})
    public Response<Boolean> delete(Long purchaserId);

    /**
     * 通过老品id查询老品详细信息
     * @param purchaserId
     * @return
     * 返回老品信息
     */
    @Export(paramNames = {"purchaserId"})
    public Response<OldModule> findById(Long purchaserId);


    /**
     *更新老品信息
     * @param oldModule
     * @return
     * 返回更新结果
     */
    @Export(paramNames = {"oldModule"})
    public Response<Boolean> update(OldModule oldModule);

    /**
     * 创建老品信息
     * @param oldModule
     * @return
     */
    @Export(paramNames = {"oldModule"})
    public Response<Boolean> create(OldModule oldModule);


}
