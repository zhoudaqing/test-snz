package io.terminus.snz.user.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 采购商权限细则，对应类目
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/21/14
 */
public class PurchaserAuthority implements Serializable {
    private static final long serialVersionUID = -6618226350468409992L;

    @Getter
    @Setter
    private Long id;            // 自增主键

    @Getter
    @Setter
    private Long userId;        // 用户id, @see User.id

    @Getter
    @Setter
    private Integer type;       // 暂定: 1. 后台类目, 2. 前台类目

    @Getter
    @Setter
    private String content;     // 当权限类型对应为类目时存放类目id, 其他情况视type另定

    @Getter
    @Setter
    private String richContent; // 保留字段，当需要存放更复杂信息时

    @Getter
    @Setter
    private Integer position;    // 职位; 暂定: 1. 小微主, 2. 小微成员

    @Getter
    @Setter
    private String role;        // 职责; @see User.JobRole

    @Getter
    @Setter
    private Date createdAt;     // 创建时间

    @Getter
    @Setter
    private Date updatedAt;     // 更新时间
}
