package io.terminus.snz.user.service;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;

/**
 * Author:  wenhaoli
 * Date: 2014-08-16
 */
public interface SupplierTQRDCInfoTmpService {

    /**
     * 根据当前用户找到对应的综合绩效得分
     * @param user 当前用户
     * @return 综合绩效得分
     * */
    Response<SupplierTQRDCInfoTmp> findByUserId(BaseUser user);
 }
