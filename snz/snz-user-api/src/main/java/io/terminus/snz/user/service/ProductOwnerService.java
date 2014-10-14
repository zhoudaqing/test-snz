package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.ProductOwner;

import java.util.List;


/**
 * 产品线负责人及相关工厂信息<P>
 *
 * @author wanggen 2014-08-18 10:50:18
 */
public interface ProductOwnerService {

    /**
     * 新增
     *
     * @param productOwner add bean
     * @return 新增后的自增序列号
     */
    @Export(paramNames = {"productOwner"})
    public Response<Long> create(ProductOwner productOwner);


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 productOwner
     */
    @Export(paramNames = {"id"})
    public Response<ProductOwner> findById(Long id);


    /**
     * 根据 id 列表查询多个结果集
     * @param ids 多个id
     * @return ProductOwner 列表
     */
    @Export(paramNames = {"ids"})
    public Response<List<ProductOwner>> findByIds(List<Long> ids);


    /**
     * 根据 productLineId 查询 ProductOwner 列表
     *
     * @param productLineId   产品线ID
     * @return 结果列
     */
    @Export(paramNames = {"productLineId"})
    Response<List<ProductOwner>> findByProductLineId(Integer productLineId);


    /**
     * 根据 productLineName 查询 ProductOwner 列表
     *
     * @param productLineName   产品线名称
     * @return 结果列
     */
    @Export(paramNames = {"productLineName"})
    Response<List<ProductOwner>> findByProductLineName(String productLineName);


    /**
     * 更新操作
     *
     * @param productOwner 更新操作参数
     * @return 影响行数
     */
    public Response<Integer> update(ProductOwner productOwner);


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public Response<Integer> deleteByIds(List<Long> ids);


}
