package io.terminus.snz.user.model;

import lombok.Data;

/**
 * 供应商基础简单信息描述
 * <BR>
 * <pre>
 *  supplierName    String      供应商名称
 *  companyId       Long        注册公司ID
 *  userId          Long        注册用户ID
 *  module          String      模块
 * </pre>
 * @author wanggen on 14-9-1.
 */
@Data
public class SupplierBaseInfo {

    private String supplierName;    //供应商名称

    private String supplierCode;    //供应商编码-V码

    private Long companyId;         //注册公司ID

    private Long userId;            //注册用户ID

    private String module;          //模块

}
