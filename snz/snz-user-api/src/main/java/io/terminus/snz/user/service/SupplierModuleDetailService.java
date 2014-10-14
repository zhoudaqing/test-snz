/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.SupplierModuleDetail;

import java.util.List;


/**
 * 供应商物料明细 服务类<BR>
 *
 * @author wanggen 2014-09-22 22:23:37
 */
public interface SupplierModuleDetailService {

    /**
     * 新增
     *
     * @param supplierModuleDetail add bean
     * @return 新增后的自增序列号
     */
    @Export(paramNames = {"supplierModuleDetail"})
    public Response<Long> create(SupplierModuleDetail supplierModuleDetail);


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 supplierModuleDetail
     */
    @Export(paramNames = {"id"})
    public Response<SupplierModuleDetail> findById(Long id);


    /**
     * 根据 supplierCode 查询 SupplierModuleDetail 列表
     *
     * @param supplierCode 供应商编码
     * @return 结果列
     */
    @Export(paramNames = {"supplierCode"})
    Response<List<SupplierModuleDetail>> findBySupplierCode(String supplierCode);


    /**
     * 更新操作
     *
     * @param supplierModuleDetail 更新操作参数
     * @return 影响行数
     */
    @Export(paramNames = {"supplierModuleDetail"})
    public Response<Integer> update(SupplierModuleDetail supplierModuleDetail);


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    @Export(paramNames = {"ids"})
    public Response<Integer> deleteByIds(List<Long> ids);


    /**
     * 推送供应商物料数据
     *
     * @param supplierCode 供应商编码
     * @return 实际推送数据量
     */
    @Export(paramNames = {"supplierCode"})
    public Response<Integer> postToHGVS(String supplierCode);


}
