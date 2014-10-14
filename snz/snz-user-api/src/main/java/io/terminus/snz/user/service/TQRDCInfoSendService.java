package io.terminus.snz.user.service;

import io.terminus.pampas.common.Response;

/**
 * 功能描述: 供应商绩效相关的短信发送服务
 *
 * @author wanggen on 14-9-12.
 */
public interface TQRDCInfoSendService {


    /**
     * 发送供应商绩效信息给所有供应商
     *
     * @return 发送结果
     */
    Response<Boolean> sendTQRDCInfoToSuppliers();


    /**
     * 发送供应商绩效数据给某个供应商
     *
     * @param supplierCode 供应商 V码
     * @param supplierName 供应商名称
     * @return 发送结果
     */
    Response<Boolean> sendTQRDCInfoToSupplier(String supplierCode, String supplierName);


    /**
     * 发送供应商绩效汇总信息给领导者
     *
     * @return 发送结果
     */
    Response<Boolean> sendTQRDCSummaryToManagers();


}
