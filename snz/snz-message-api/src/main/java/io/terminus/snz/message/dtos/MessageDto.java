package io.terminus.snz.message.dtos;

import io.terminus.snz.message.models.Message;
import lombok.*;

import java.util.List;

/**
 * 消息传输对象, 用户消息推送(服务内部使用)
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-8
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @Getter @Setter
    private Message message;            //消息对象

    @Getter @Setter
    private List<Long> receiverIds;     //接收者id列表

    @Getter @Setter
    private Object datas;               //模版数据, 用于消息模版生成消息内容
}
