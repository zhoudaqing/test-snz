package io.terminus.snz.sns.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * 功能描述: 话题朋友圈<br>
 *
 * @author wanggen on 14-9-16.
 */
@Data
public class TopicFriend implements Serializable{
    private static final long serialVersionUID = 2620944247726816840L;

    private Long userId;        //圈内朋友用户ID

    private String nick;        //圈内朋友用户昵称

    private String name;        //圈内朋友用户名称

    private Long topicId;       //话题ID

    private String corporation; //若是供应商，显示公司名称

}
