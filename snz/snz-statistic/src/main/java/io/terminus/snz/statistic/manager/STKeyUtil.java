package io.terminus.snz.statistic.manager;

/**
 * Desc:redis中保存的数据key
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-29.
 */
public class STKeyUtil {
    /**
     * 所用供应商的方案的统计数据的key
     * @param supplierId    供应商编号
     * @return String
     * 返回所有供应商的方案的统计key
     */
    public static String solCountKey(Long supplierId){
        return "solCountBySupplier:"+supplierId;
    }

    /**
     * 供应商的方案的入选情况的统计数据
     * @param supplierId    供应商编号
     * @return  String
     * 返回供应商的方案的状态的统计key
     */
    public static String supSolCountKey(Long supplierId){
        return "supplierSolCount:"+supplierId;
    }

    /**
     * 采购商对应于需求不同阶段的需求数量
     * @param userId 供应商编号
     * @return  String
     * 返回采购商的所有方案统计key
     */
    public static String reqCountKey(Long userId){
        return "reqCountByPurchaser:"+userId;
    }

    /**
     * 所有需求的具体的数据统计
     * @param requirementId 需求编号
     * @return  String
     * 返回供应商的需求的统计key
     */
    public static String reqInfoCountKey(Long requirementId){
        return "reqInfoCountView:"+requirementId;
    }

    /**
     * 某个需求下的所有供应商回复（记录供应商编号）
     * @param requirementId 需求编号
     * @return  String
     * 返回key
     */
    public static String reqTopicCountKey(Long requirementId){
        return "reqTopicCount:"+requirementId;
    }
}
