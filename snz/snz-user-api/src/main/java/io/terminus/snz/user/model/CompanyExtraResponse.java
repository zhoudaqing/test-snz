package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
public class CompanyExtraResponse implements Serializable {
    private static final long serialVersionUID = -2120300953049430646L;

    @Getter
    @Setter
    private Long id;                                    // 自增主键

    @Getter
    @Setter
    private Long userId;                                // 用户id

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
