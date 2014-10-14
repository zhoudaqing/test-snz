package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierAppointedDto;
import io.terminus.snz.user.model.SupplierAppointed;

import java.util.List;

/**
 * Author: Grancy Guo
 * Created on 14-9-15.
 */
public interface SupplierAppointedService {

    /**
     * 根据条件查询甲指库信息
     * @param status 甲指供应商审核状态
     * @param supplierName 甲指供应商模糊搜索名称
     * @param pageNo 当前分页
     * @param size 分页大小
     * @return Paging<SupplierAppointedDto> 符合条件的甲指列表
     */
    @Export(paramNames = {"status", "supplierName", "pageNo", "size"})
    public Response<Paging<SupplierAppointedDto>> findbyParams(Integer status, String supplierName,
                                                       Integer pageNo, Integer size);

    /**
     * 初审或终审确认通过甲指库供应商
     * 终审通过之前，需要校验该供应商是否入驻
     * 如果供应商已经入驻，审核通过后，赋予该供应商全部需求权
     * @param jiaZhiId 甲指供应商id
     * @param flag 初审为0 终审为1
     * @return true or false
     */
    @Export(paramNames = {"baseUser", "jiaZhiId", "flag"})
    public Response<Boolean> confirmJiaZhi(BaseUser baseUser,Long jiaZhiId, Integer flag);

    /**
     * 初审不通过或终审不通过甲指库供应商，并推送消息至相关需求发布者
     * @param jiaZhiId 甲指供应商id
     * @param flag 初审不通过为0 终审不通过为1
     * @param advice 不通过原因
     * @return true or false
     */
    @Export(paramNames = {"baseUser", "jiaZhiId", "flag", "advice"})
    public Response<Boolean> rejectJiaZhi(BaseUser baseUser, Long jiaZhiId, Integer flag, String advice);

    /**
     * 根据传入的Model SupplierAppointed处理新增还是插入
     * @param supplierAppointed 甲指供应商model
     * @return id 甲指供应商id
     */
    @Export(paramNames = {"supplierAppointed"})
    public Response<Boolean> batchSupplierAppointed(SupplierAppointed supplierAppointed);

    /**
     * 甲指库供应商可能存在这种情况，甲方强行指定的供应商，该供应商并未在我们系统注册
     * 这个供应商在甲指需求发布的时候已经被指定进入系统甲指库
     * 以后这个供应商入驻我们系统，注册结束后，需要调用本方法将公司id写入系统甲指库
     * @param companyId 供应商公司id
     * @param corporation 供应商公司全名
     * @return true or false
     */
    @Export(paramNames = {"companyId", "corporation"})
    public Response<Boolean> insertSupplierAppointedId(Long companyId, String corporation);

    /**
     * 根据id查询甲指供应商信息
     * @param requirementId 需求id
     * @return SupplierAppointed
     */
    @Export(paramNames = {"requirementId"})
    public Response<SupplierAppointed> findbyId(Long requirementId);

}
