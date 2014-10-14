package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierCountDto;
import io.terminus.snz.user.dto.SupplierReparationSummariesDto;
import io.terminus.snz.user.model.SupplierReparationSumaries;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
public interface SupplierSummaryService {

    /**
     * 从供货园区维度统计供应商数量
     *
     * @param supplyParkIds 可供货园区编号
     * @return 是否成功
     */
    public Response<Boolean> supplierSummaryBySupplyParks(List<Long> supplyParkIds);

    /**
     * 根据不同供货园区查询供应商数量（用于对比）
     *
     * @param supplyParkId1 供货园区1编号
     * @param supplyParkId2 供货园区2编号
     * @return 最近的供应商数量统计信息
     */
    @Export(paramNames = {"supplyParkId1", "supplyParkId2"})
    public Response<SupplierCountDto> findSupplierCountBySupplyParkIds(Long supplyParkId1, Long supplyParkId2);

    /**
     * 从状态维度统计供应商数量
     *
     * @return 是否成功
     */
    public Response<Boolean> supplierSummaryByStatus();

    /**
     * 根据不同状态(1：注册，2：参与交互，3：入围，4：合作)查询供应商数量（用于对比）
     *
     * @param status1 状态1
     * @param status2 状态2
     * @return 最近的供应商数量统计信息
     */
    @Export(paramNames = {"status1", "status2"})
    public Response<SupplierCountDto> findSupplierCountByStatus(Integer status1, Integer status2);

    /**
     * 从供应商层次维度统计供应商数量
     *
     * @return 是否成功
     */
    public Response<Boolean> supplierSummaryByLevel();

    /**
     * 根据不同层次(1：优选，2：合格，3：限制，4：淘汰)查询供应商数量（用于对比）
     *
     * @param level1 层次1
     * @param level2 层次2
     * @return 最近的供应商数量统计信息
     */
    @Export(paramNames = {"level1", "level2"})
    public Response<SupplierCountDto> findSupplierCountByLevel(Integer level1, Integer level2);

    /**
     * 从行业维度统计供应商数量
     *
     * @return 是否成功
     */
    public Response<Boolean> supplierSummaryByIndustry();

    /**
     * 根据不同行业（一级类目）查询供应商数量（用于对比）
     *
     * @param industryId1 行业1编号
     * @param industryId2 行业2编号
     * @return 最近的供应商数量统计信息
     */
    @Export(paramNames = {"industryId1", "industryId2"})
    public Response<SupplierCountDto> findSupplierCountByIndustries(Long industryId1, Long industryId2);

    /**
     * 根据供应商评价信息
     * @param baseUser
     * @param supplinerName
     * @return
     */
    @Export(paramNames = {"baseUser", "supplinerName","pageNo","size"})
    public Response<Paging<SupplierReparationSummariesDto>> findSupplierReparationSumaries(BaseUser baseUser, String supplinerName, Integer pageNo, Integer size);

    /**
     * 被全量dump调用
     *
     * @param reparationSumaries    插入或更新的记录
     * @return 操作完返回记录的id
     */
    public Response<Long> insertOrUpdateReparationSummary(SupplierReparationSumaries reparationSumaries);
}
