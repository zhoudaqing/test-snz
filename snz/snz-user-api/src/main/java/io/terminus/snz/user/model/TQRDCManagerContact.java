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
 * 供应商绩效管理者联系方式<BR>
 *
 * Created by wanggen 2014-09-11 21:09:51
 * <PRE>
 * id                       Long        主健
 * name                     String      发送人姓名
 * phone                    String      联系方式
 * template                 String      短信模板
 * remark                   String      备注
 * createdAt                Date        创建时间
 * updatedAt                Date        更新时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TQRDCManagerContact implements Serializable {

    private static final long serialVersionUID = 2306944663714303555L;

    private Long id;                               //主健

    private String name;                           //发送人姓名

    private String phone;                          //联系方式

    private String template;                       //短信模板

    private String remark;                         //备注

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //更新时间


}
