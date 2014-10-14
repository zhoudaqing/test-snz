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
 * 子站和模板的实例
 * <p/>
 * Author:  <a href="mailto:jlchen.cn@gmail.com">jlchen</a>
 * Date: 2013-11-20
 */
public class TemplateInstance extends BaseSiteInstance implements Serializable {
    private static final long serialVersionUID = -7625369758389815558L;

    @Getter
    @Setter
    private String indexPath;     //首页 path

    @Getter
    @Setter
    private String header;        //模板的头部部分

    @Getter
    @Setter
    private String footer;        //模板的尾部部分

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof TemplateInstance)) {
            return false;
        }
        TemplateInstance that = (TemplateInstance) o;
        return Objects.equal(this.siteId, that.siteId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.siteId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("siteId", siteId)
                .add("indexPath", indexPath)
                .add("header", header)
                .add("footer", footer)
                .toString();
    }
}
