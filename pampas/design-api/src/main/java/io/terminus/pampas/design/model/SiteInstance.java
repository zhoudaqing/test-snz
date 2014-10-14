/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package io.terminus.pampas.design.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 这是要持久化的信息 店铺实例
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-20
 */
public class SiteInstance extends BaseSiteInstance implements Serializable {
    private static final long serialVersionUID = -642501665364787674L;

    @Getter
    @Setter
    private Long templateId; //模板id,对应TemplateSiteInstance的site id ,如果本身即为模板,则为null

    @Override
    public int hashCode() {
        return Objects.hashCode(siteId, templateId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof SiteInstance)) {
            return false;
        }
        SiteInstance that = (SiteInstance) o;
        return Objects.equal(this.siteId, that.siteId)
                && Objects.equal(this.templateId, that.templateId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("siteId", siteId)
                .add("status", status)
                .add("templateId", templateId)
                .omitNullValues()
                .toString();
    }
}
