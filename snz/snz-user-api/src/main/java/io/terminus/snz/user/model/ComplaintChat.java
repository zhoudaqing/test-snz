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
 * 抱怨信息聊天记录<BR>
 *
 * Created by wanggen 2014-09-20 13:44:55
 * <PRE>
 * id                       Long        主健
 * parentId                 Long        抱怨信息ID
 * senderId                 Long        消息发送者ID
 * senderName               String      消息发送者姓名或nick
 * message                  String      消息内容
 * createdAt                Date        创建时间
 * updatedAt                Date        更新时间
 * </PRE>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintChat implements Serializable {

    private static final long serialVersionUID = 4531413951958998885L;

    private Long id;                               //主健

    private Long parentId;                         //抱怨信息ID

    private Long senderId;                         //消息发送者ID

    private String senderName;                     //消息发送者姓名或nick

    private String message;                        //消息内容

    private Date createdAt;                        //创建时间

    private Date updatedAt;                        //更新时间


}
