package io.terminus.snz.statistic.service;

import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.statistic.model.PurchaserRequirementCount;
import io.terminus.snz.statistic.model.RequirementCountType;

import java.util.Map;

/**
 * Desc:需求统计数据管理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-15.
 */
public interface RequirementCountService {
    /**
     * 设置某个需求的统计信息
     * @param requirementId 需求编号
     * @param countType     需求统计类型
     * @param addCount      统计增量
     * @return  Boolean
     * 返回需求统计信息写入是否成功
     */
    public Boolean setReqCountInfo(Long requirementId , RequirementCountType countType, Integer addCount);

    /**
     * 设置某个需求下的供应商响应信息
     * @param requirementId 需求编号
     * @param supplierId    供应商编号
     * @return  Boolean
     * 返回需求的话题的统计信息
     */
    public Boolean incTopicSuppliers(Long requirementId , Long supplierId);

    /**
     * 根据需求编号以及需要查询的统计数据类型查询统计数据信息
     * @param requirementId 需求编号
     * @param countTypes    统计数据类型
     * @return  Map<RequirementCountType , Integer>
     * 返回需求的统计数据
     */
    public Map<RequirementCountType , Integer> findReqCount(Long requirementId, RequirementCountType... countTypes);

    /**
     * 根据需求编号删除某个需求统计信息
     * @param requirementId 需求编号
     * @return  Boolean
     * 返回删除是否成功
     */
    public Boolean deleteReqCount(Long requirementId);

    /**
     * 写入采购商的需求统计信息
     * @param requirementCount  采购商需求统计数据
     * @return  Boolean
     * 返回写入是否成功
     */
    public Boolean setPurchaserReqCount(PurchaserRequirementCount requirementCount);

    /**
     * 通过用户编号实现统计信息的更改
     * @param userId    用户编号（需求创建者）
     * @param oldStatus 上一个阶段
     * @param newStatus 下一个阶段
     * @return  Boolean
     * 返回更新是否成功
     */
    public Boolean updatePurchaserReqCount(Long userId , Integer oldStatus, Integer newStatus);

    /**
     * 根据采购商用户编号获取全部的需求的统计数据
     * @param user    用户信息（需求创建者）
     * @return  PurchaserRequirementCount
     * 返回采购商用户的全部统计数据
     */
    @Export(paramNames = {"user"})
    public Response<PurchaserRequirementCount> findPurchaserReqCount(BaseUser user);
}
