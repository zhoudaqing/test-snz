package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierLevelCountDto;
import io.terminus.snz.user.dto.SupplierModuleCountDto;
import io.terminus.snz.user.model.SupplierTQRDCInfo;

import java.util.Map;

/**
 * 供应商 TQRDC 信息
 *
 * @author wanggen on 14-7-24.
 */
public interface SupplierTQRDCInfoService {

    /**
     * 根据月份统计在各个分值区间的供应商总数
     *
     * @param month 月份
     * @return 统计数据
     */
    @Export(paramNames = {"month"})
    Response<SupplierLevelCountDto> countCompositeScoreOfMonth(String month);

    /**
     * 插入 TQRDC 信息
     *
     * @param supplierTQRDCInfo 实例
     * @return 是否插入成功
     */
    Response<Boolean> create(SupplierTQRDCInfo supplierTQRDCInfo);

    /**
     * 统计在某一月份中，各个产品线在各个分值区间的供应商数量，key 是产品线ID
     * <pre>
     *  1:{
     *      bestCount:1380,
     *      standardCount:308,
     *      ...
     *      badCount:50
     *  },
     *  2:{
     *      bestCount:789,
     *      ...
     *      badCount:10
     *  }
     * </pre>
     *
     * @param month 统计数据的月份，格式为 YYYY-MM
     * @return 各个产品线分值区间的供应商数量
     */
    @Export(paramNames = {"month"})
    Response<Map<Integer, SupplierLevelCountDto>> countCompositeScoreByProductLineOfMonth(String month);

    /**
     * 数据转换接口
     *
     * @param params 日期参数 {date:'20140801', month:'2014-08'}
     * @return 实际转换多少条数据
     */
    @Export(paramNames = "params")
    Response<Integer> execTransfer(Map<String, Object> params);

    /**
     * 根据总和得分修改供应商状态
     *
     * @param month 月份
     * @return 是否更新成功
     */
    Response<Boolean> updateSupplierStatus(String month);

    /**
     * 按模块统计供应商
     *
     * @return 是否统计成功
     */
    @Export
    public Response<Boolean> supplierSummaryByModule();

    /**
     * 获取按模块统计供应商的结果
     *
     * @return 统计结果
     */
    @Export
    public Response<SupplierModuleCountDto> getSupplierSummaryByModule();

}
