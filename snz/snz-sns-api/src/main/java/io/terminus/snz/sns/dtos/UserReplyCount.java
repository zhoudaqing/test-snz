package io.terminus.snz.sns.dtos;

import io.terminus.snz.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户及回复数DTO
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-28
 */
@AllArgsConstructor @NoArgsConstructor
public class UserReplyCount implements Serializable{

    private static final long serialVersionUID = -5675144933440124521L;

    @Getter @Setter
    private User user;

    @Getter @Setter
    private Long replyCount;
}
