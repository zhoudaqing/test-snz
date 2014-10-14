/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.console.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-07-25
 */
public class User {
    private static final long serialVersionUID = 7373231749129134927L;

    @Getter
    @Setter
    protected Long id;  //用户id

    @Getter
    @Setter
    protected String name;

    @Getter
    @Setter
    private String password;  //登陆密码

    @Getter
    @Setter
    private Integer status;   //状态 1:正常 -1: 禁用

    @Getter
    @Setter
    private String email; //email

    @Getter
    @Setter
    private String realName;  //真实名称

    @Getter
    @Setter
    private Date loginAt;     //上次登陆时间

    @Getter
    @Setter
    private Date createdAt;    //创建时间

    @Getter
    @Setter
    private Date updatedAt;     //更新时间

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User that = (User) o;

        return Objects.equal(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("password", password)
                .add("status", status)
                .add("email", email)
                .add("realName", realName)
                .add("loginAt", loginAt)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .omitNullValues()
                .toString();
    }
}
