package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.MDMBaseCompanyDto;
import io.terminus.snz.requirement.model.SupplierQualification;

/**
 * MDM对接时，操作供应商v码申请信息的相关服务类
 *
 * Date: 7/22/14
 * Time: 14:01
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */
public interface MDMSupplierService {

    /**
     * 用于为采购经理和财务展示对应操作步骤的供应商列表，
     * 采购经理为stage 1，财务为stage 2.
     *
     * @param currentUser     当前登录用户
     * @param supplierName    供应商名字
     * @param pageNo          当前分页
     * @param size            分页大小
     * @return                对应操作步骤的供应商列表
     */
    @Export(paramNames = {"user", "supplierName", "pageNo", "size"})
    public Response<Paging<MDMBaseCompanyDto>> findBaseSupplierInfoBy(
            BaseUser currentUser, String supplierName, Integer pageNo, Integer size);


    // unused yet //
    public Response<SupplierQualification> findQualificationBySupplierId(Long supplierId);

    /**
     * 用于为采购经理和财务展示将要操作的供应商的基本信息
     *
     * @param supplierId    采购商id
     * @return              采购商基本信息
     */
    @Export(paramNames = {"supplierId"})
    public Response<MDMBaseCompanyDto> findBaseSupplierInfoById(Long supplierId);

    /**
     * 更新或者新建一条记录，根据当前用户的额角色
     * 采购经理和财务只能操作自己角色相关的供应商信息
     *
     * @param currentUser    当前登录用户，自动注入
     * @param supplierId     供应商id
     * @return               操作是否成功
     */
    @Export(paramNames = {"user", "supplierId", "qualification"})
    public Response<Boolean> updateSupplierInfoById(BaseUser currentUser, Long supplierId, SupplierQualification supplierQualification);
}
