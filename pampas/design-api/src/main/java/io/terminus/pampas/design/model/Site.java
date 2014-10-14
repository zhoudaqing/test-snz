/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-20
 */
public class Site implements Serializable {
    private static final long serialVersionUID = -3824918914269756807L;

    @Getter
    @Setter
    private Long id;  //站点id

    @Getter
    @Setter
    private String app;

    @Getter
    @Setter
    private Long userId;  //所属的用户id

    @Getter
    @Setter
    private SiteCategory category;   //站点类型

    @Getter
    @Setter
    private String name;           //站点名称

    @Getter
    @Setter
    private String domain;        //站点顶级域名

    @Getter
    @Setter
    private String subdomain;     //站点二级域名

    @Getter
    @Setter
    private Long designInstanceId;  //装修中的实例id

    @Getter
    @Setter
    private Long releaseInstanceId;  //发布后的实例id

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    @Override
    public int hashCode() {
        return Objects.hashCode(userId, domain, subdomain);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Site)) {
            return false;
        }
        Site that = (Site) o;
        return Objects.equal(this.userId, that.userId)
                && Objects.equal(this.domain, that.domain)
                && Objects.equal(this.subdomain, that.subdomain);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("userId", userId)
                .add("name", name)
                .add("domain", domain)
                .add("subdomain", subdomain)
                .add("designInstanceId", designInstanceId)
                .add("releaseInstanceId", releaseInstanceId)
                .omitNullValues()
                .toString();
    }
}
