/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商联系方式<BR>
 *
 * Created by wanggen 2014-09-11 21:18:48
 * <PRE>
 * id                       Long        主健
 * supplierName             String      供应商名称
 * supplierCode             String      供应商编码
 * phone                    String      联系电话
 * createdAt                Date        创建时间
 * updatedAt                Date        更新时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContact implements Serializable {
    private static final long serialVersionUID = 4891074515363517861L;

    private Long id;                               //主健

    private String supplierName;                   //供应商名称

    private String supplierCode;                   //供应商编码

    private String phone;                          //联系电话

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //更新时间


}
