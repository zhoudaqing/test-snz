/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
public class UserPermission implements Serializable {

    @Getter
    @Setter
    private Long id;     //数据库主键id

    @Getter
    @Setter
    private Long userId;  //用户id , 一个用户只能有一条授权记录, 整体保存和更新的


    @Getter
    @Setter
    private String permissions;   //以json形式存储的授权列表 如 ["app:pampas", "site:2", "group:wtf"]


    @Getter
    @Setter
    private Date createdAt;    //创建时间

    @Getter
    @Setter
    private Date updatedAt;     //更新时间

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPermission)) return false;

        UserPermission that = (UserPermission) o;

        return Objects.equal(this.userId, that.userId)
                && Objects.equal(this.permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, permissions);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("permissions", permissions)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .omitNullValues()
                .toString();
    }
}
