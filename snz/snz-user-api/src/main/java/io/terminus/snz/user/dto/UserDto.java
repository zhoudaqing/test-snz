package io.terminus.snz.user.dto;

import io.terminus.snz.user.model.User;
import lombok.*;

import java.io.Serializable;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-7
 */
@ToString @NoArgsConstructor @AllArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7150838413115681320L;

    @Getter @Setter
    private User user;

    @Getter @Setter
    private String ssoSessionId;            //hac中sso session id
}
