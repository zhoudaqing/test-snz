/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 供应商物料明细<BR>
 *
 * Created by wanggen 2014-09-22 22:06:26
 * <PRE>
 * id                       Long        主健
 * moduleNum                String      物料号
 * moduleName               String      物料名称
 * supplierCode             String      供应商编码
 * supplierName             String      供应商名称
 * purchOrg                 String      采购组织
 * purchGroup               String      采购组
 * moduleGroup              String      物料组
 * moduleGroupDesc          String      物料组描述
 * taxCode                  String      税码
 * validityStart            String      有效期开始
 * validityEnd              String      有效期结束
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierModuleDetail implements Serializable {

    private static final long serialVersionUID = -3299182263989787504L;

    private Long id;                               //主健

    private String moduleNum;                      //物料号

    private String moduleName;                     //物料名称

    private String supplierCode;                   //供应商编码

    private String supplierName;                   //供应商名称

    private String purchOrg;                       //采购组织

    private String purchGroup;                     //采购组

    private String moduleGroup;                    //物料组

    private String moduleGroupDesc;                //物料组描述

    private String taxCode;                        //税码

    private String validityStart;                  //有效期开始

    private String validityEnd;                    //有效期结束


}
