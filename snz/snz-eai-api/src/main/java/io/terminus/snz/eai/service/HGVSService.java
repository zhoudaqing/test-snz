package io.terminus.snz.eai.service;

import io.terminus.pampas.common.Response;

import java.util.List;
import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/25/14
 */
public interface HGVSService {

    /**
     * 通过供应商v码获得余额
     * @param vCode v码
     * @return 余额
     */
    Response<Long> getBalanceBySupplierCode(String vCode);

    /**
     * 批量查询（快速）
     * @param vCodes 供应商v码列表
     * @return 所有查询供应商的余额
     */
    Response<Map<String, Long>> bulkGetBalanceBySupplierCodes(List<String> vCodes);
}
