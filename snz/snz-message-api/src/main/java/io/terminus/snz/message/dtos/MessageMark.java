package io.terminus.snz.message.dtos;

import io.terminus.snz.message.models.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 可标记消息, 即未读或已读
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-25
 */
@NoArgsConstructor
public class MessageMark extends Message {
    private static final long serialVersionUID = 3359063312522783819L;

    @Getter @Setter
    private Integer isRead;     //0表示未读, 表示已读

    public MessageMark(Message m){
        setId(m.getId());
        setUserId(m.getUserId());
        setContent(m.getContent());
        setCreatedAt(m.getCreatedAt());
        setUpdatedAt(m.getUpdatedAt());
        setMtype(m.getMtype());
    }
}
