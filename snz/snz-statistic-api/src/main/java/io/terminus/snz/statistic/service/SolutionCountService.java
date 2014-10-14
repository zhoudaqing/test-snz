package io.terminus.snz.statistic.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.model.SolutionCountType;
import io.terminus.snz.statistic.model.SupplierSolutionCount;

import java.util.List;
import java.util.Map;

/**
 * Desc:方案统计数据管理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-15.
 */
public interface SolutionCountService {
    /**
     * 创建供应商的统计数据
     * @param solutionCount 供应商的统计数据信息
     * @return  Boolean
     * 返回写入供应商统计数据
     */
    public Boolean setSupCount(SupplierSolutionCount solutionCount);

    /**
     * 实现批量的更改用户的需求方案数据的统计信息(这个是需求跳转时实现整体需求下的供应商统计数据的更改)
     * @param userIds   供应商用户Id列表
     * @param oldStatus     久的需求状态
     * @param newStatus     新的需求状态
     * @return  Boolean
     * 返回更新供应商的统计数据
     */
    public Boolean updateBatchCounts(List<Long> userIds , Integer oldStatus, Integer newStatus);

    /**
     * 根据供应商编号获取供应商的不同阶段的方案统计数据
     * @param user    供应商用户对象
     * @return  SupplierSolutionCount
     * 返回供应商统计数据信息
     */
    @Export(paramNames = {"user"})
    public Response<SupplierSolutionCount> findSupplierCount(BaseUser user);

    /**
     * 设置某个供应商的方案的统计信息
     * @param userId        供应商用户编号
     * @param countType     统计类型
     * @param addCount      统计增量
     * @return  Boolean
     * 返回方案统计信息写入是否成功
     */
    public Boolean setSolCountInfo(Long userId , SolutionCountType countType, Integer addCount);

    /**
     * 根据供应商编号获取供应商的方案状态统计数据
     * @param userId    供应商用户信息
     * @param countTypes 需要查询的统计数据类型
     * @return  Map
     * 返回供应商的方案的不同状态的数据的统计
     */
    public Map<SolutionCountType , Integer> findSupSolCount(Long userId , SolutionCountType... countTypes);
}
