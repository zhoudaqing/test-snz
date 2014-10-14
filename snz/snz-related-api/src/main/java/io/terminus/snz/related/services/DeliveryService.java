package io.terminus.snz.related.services;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.related.dto.ParkFactoryDto;
import io.terminus.snz.related.models.AddressFactory;
import io.terminus.snz.related.models.AddressPark;

import java.util.List;

/**
 * Desc:配送服务信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
public interface DeliveryService {

    /**
     * 获取全部的园区信息
     *
     * @return List
     * 返回园区信息
     */
    @Export
    public Response<List<AddressPark>> findAllPark();

    /**
     * 获取所有的园区信息
     *
     * @param productId 产品线编号
     * @return List
     * 返回园区信息内容
     */
    @Export(paramNames = {"productId"})
    public Response<List<AddressPark>> findParkByProductId(Long productId);

    /**
     * 根据园区编号查询工厂信息
     * @param productId 产品线编号（后台二级类目）
     * @param parkId 园区编号
     * @return  List
     * 返回工厂的信息
     */
    @Export(paramNames = {"productId", "parkId"})
    public Response<List<AddressFactory>> findFactories(Long productId, Long parkId);

    /**
     * 根据工厂编号集合查询工厂信息
     * @param factoryIds 工厂编号集合
     * @return  List
     * 返回工厂的信息列表
     */
    public Response<List<AddressFactory>> findFactoryByIds(List<Long> factoryIds);

    /**
     * 根据id集合获取园区信息
     *
     * @return List
     * 返回园区信息
     */
    @Export(paramNames = {"ids"})
    public Response<List<AddressPark>> findParksByIds(List<Long> ids);

    /**
     * 根据类目编号以及工厂编号集合获取工厂以及园区的详细信息
     * @param productId     类目编号
     * @param factoryIds    工厂编号集合
     * @return  List
     * 返回工厂以及园区的信息集合
     */
    public Response<List<ParkFactoryDto>> findDetailFactories(Long productId, List<Long> factoryIds);
}
