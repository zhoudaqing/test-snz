package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dream on 14-10-11.
 */

public class YzlCreditQualify implements Serializable {
    private static final long serialVersionUID = -1468074885540025575L;
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private Long supplierId;

    @Getter
    @Setter
    private Integer status; // -4 EJECT, -3 OTHERS, -2 C, -1 B, 1 BBB, 2 A, 3 AA, 4 AAA, 5 AUTO, 11 APPLYING,

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String appealMsg;

    @Getter
    @Setter
    private Long reviewer;

    @Getter
    @Setter
    private String reviewerName;

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;
}
