package io.terminus.snz.user.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.model.Company;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
public interface SupplierGroupService {

    /**
     * 增加关联（将小B同学加入小A同学的组群）
     * @param baseUser 当前登录用户
     * @param alphaId 小A同学(supplierId)
     * @param betaId 小B同学(supplierId)
     * @return 是否关联成功
     */
    @Export(paramNames = {"baseUser", "alphaId", "betaId"})
    Response<Boolean> createRelation(BaseUser baseUser, Long alphaId, Long betaId);

    /**
     * 删除关联（将小B同学从小A同学的组群中删除）
     * @param baseUser 当前登录用户
     * @param alphaId 小A同学(supplierId)
     * @param betaId 小B同学(supplierId)
     * @return 是否删除关联成功
     */
    @Export(paramNames = {"baseUser", "alphaId", "betaId"})
    Response<Boolean> deleteRelation(BaseUser baseUser, Long alphaId, Long betaId);

    /**
     * 获取和当前供应商同组的其他供应商
     * @param baseUser 当前登录的供应商
     * @param alphaId 小A同学，供应商id
     * @return 和当前供应商同组的其他供应商列表
     */
    @Export(paramNames = {"baseUser", "alphaId"})
    Response<List<Company>> getRelatedSuppliers(BaseUser baseUser,Long alphaId);
}
