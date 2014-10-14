package io.terminus.snz.message.dtos;

import io.terminus.common.model.Paging;
import io.terminus.snz.message.models.Message;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 该DTO用于显示消息中心首页, 如 /messages
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-25
 */
public class MessagePagingView implements Serializable{
    private static final long serialVersionUID = -7551355160776003215L;

    @Getter @Setter
    private Long totalNum = 0L;              //总消息数

    @Getter @Setter
    private Long newNum = 0L;                //未读消息总数

    @Getter @Setter
    private Integer status = 0;              //0表示所有消息, 1表示未读消息

    @Getter @Setter
    private Paging<? extends Message> msgs;  //带标记的消息分页对象
}
