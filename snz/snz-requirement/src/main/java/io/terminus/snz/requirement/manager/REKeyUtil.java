package io.terminus.snz.requirement.manager;

import io.terminus.snz.requirement.model.Module;

/**
 * Desc:redis中保存的数据key
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-29.
 */
public class REKeyUtil {
    /**
     * 通过需求编号返回模块数量的key
     * @param requirementId 需求编号
     * @return  String
     * 返回模块数量redis的key
     */
    public static String moduleNumKey(Long requirementId){
        return "requirementId:"+requirementId+":"+"moduleNum";
    }

    /**
     * 通过需求编号返回模块总数的key
     * @param requirementId 需求编号
     * @return  String
     * 返回模块总数的redis的key
     */
    public static String moduleTotalKey(Long requirementId){
        return "requirementId:"+requirementId+":"+"moduleTotal";
    }

    /**
     * 签名的协议信息
     * @param requirementId 需求编号
     * @return String
     * 返回签名的redis的key
     */
    public static String signKey(Long requirementId){
        return "requirementId:"+requirementId+":"+"signKey";
    }

    /**
     * 所用供应商的方案的统计数据的key
     * @return String
     * 返回所有供应商的方案的统计key
     */
    public static String solCountKey(){
        return "solCountBySupplier";
    }

    /**
     * 供应商对应于需求不同阶段的方案数量
     * @param supplierId    供应商编号
     * @return String
     * 返回统计的redis的key
     */
    public static String supplierCountKey(Long supplierId , Integer status){
        return "supplierId:"+supplierId+":status:"+status;
    }

    /**
     * 采购商对应于需求不同阶段的需求数量
     * @return  String
     * 返回采购商的所有方案统计key
     */
    public static String reqCountKey(){
        return "reqCountByPurchaser";
    }

    /**
     * 采购商用户（海尔的人员）获取对应阶段的key
     * @param userId        采购商用户编号
     * @param status        需求的状态
     * @return String
     * 返回阶段的key
     */
    public static String purchaserStatusKey(Long userId , Integer status){
        return "purchaserUserId:"+userId+":status:"+status;
    }

    /**
     * 在不良中间表根据部门编号获取一个部门的名字
     *
     * @param departCode    市场不良或者现场不良的部门编号
     * @return 拼接后的部门编号key
     */
    public static final String mwDepartKey(String departCode) {
        return "mw-depart-code:" + departCode;
    }

    /**
     * 在不良中间表根据部门名字获取他的编号
     *
     * @param departName    市场不良或者现场不良的部门名字
     * @return
     */
    public static final String mwBusinessKey(String departName) {
        return "mw-depart-name:" + departName;
    }

    /**
     *
     * @param fac
     * @param module
     * @return
     */
    public static final String mwFacModuleKey(String fac, String module) {
        return "mw-fac-code:" + fac + ":module-num:" + module;
    }
}
