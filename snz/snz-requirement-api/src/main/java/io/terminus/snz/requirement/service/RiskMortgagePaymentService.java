package io.terminus.snz.requirement.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.model.RiskMortgagePayment;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/21/14
 */
public interface RiskMortgagePaymentService {

    /**
     * 创建风险抵押金
     * @param payment    风险抵押金信息
     * @return id
     */
    @Export(paramNames = {"payment"})
    Response<Long> create(RiskMortgagePayment payment);

    /**
     * 修改风险抵押金
     * @param payment    风险抵押金信息
     * @return 是否更新成功
     */
    @Export(paramNames = {"payment"})
    Response<Boolean> update(RiskMortgagePayment payment);

    /**
     * 删除风险抵押金
     * @param id    风险抵押金id
     * @return 是否删除成功
     */
    @Export(paramNames = {"id"})
    Response<Boolean> delete(Long id);

    /**
     * 根据id查询风险抵押金id
     * @param id    风险抵押金id
     * @return 风险抵押金
     */
    @Export(paramNames = {"id"})
    Response<RiskMortgagePayment> findById(Long id);

    /**
     * 查询风险抵押金
     * @param payment    风险抵押金信息
     * @return 所要查询的风险抵押金
     */
    @Export(paramNames = {"payment"})
    Response<RiskMortgagePayment> findBy(RiskMortgagePayment payment);

    /**
     * 通过供应商V码查询供应商风险抵押金金额（一个供应商可能有多条数据，算总）
     * @param supplierCode    供应商V码
     * @return 供应商风险抵押金
     */
    Response<Long> getRiskMortgageAmountOfSupplier(String supplierCode);
}
