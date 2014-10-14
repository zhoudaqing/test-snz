/*
 * Copyright (c) 2013 杭州端点网络科技有限公司
 */

package io.terminus.snz.user.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-6
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1842929503746211283L;

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private Integer parentId;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Address)) {
            return false;
        }
        Address that = (Address) o;
        return Objects.equal(id, that.id) && Objects.equal(parentId, that.parentId)
                && Objects.equal(name, that.name) && Objects.equal(level, that.level);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id).add("parentId", parentId).add("name", name).add("level", level).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.parentId, this.name, this.level);
    }
}
