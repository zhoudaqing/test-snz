package io.terminus.snz.sns.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户话题关联类
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-5-3
 */
public class TopicUser implements Serializable{

    private static final long serialVersionUID = 7041144221459943683L;

    @Getter
    @Setter
    private Long userId;
    @Getter
    @Setter
    private Long topicId;
    @Getter
    @Setter
    private Date createdAt;
    @Getter
    @Setter
    private Date updatedAt;
}
