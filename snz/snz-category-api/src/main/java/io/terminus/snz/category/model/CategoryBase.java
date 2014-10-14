package io.terminus.snz.category.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public abstract class CategoryBase implements Serializable{
    private static final long serialVersionUID = -1710133633122366451L;

    public static enum Type{

        Frontend(1),
        Backend(2);

        public final int value;
        Type(int value) {
            this.value = value;
        }

        public static Type from(int v){
            switch (v){
                case 1:
                    return Frontend;
                case 2:
                    return Backend;
                default:
                    throw new IllegalArgumentException("unknown category type: "+ v);
            }
        }
    }

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;      //类目名称

    @Getter
    @Setter
    private Long parentId;     //上级类目id, 如果没有上级类目, 则置0

    @Getter
    @Setter
    private Integer level;     //当前是第几级,从1开始编号

    @Getter
    @Setter
    private Boolean hasChildren;   //是否有下级类目

    @Getter
    @Setter
    private Date createdAt;

    @Getter
    @Setter
    private Date updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryBase that = (CategoryBase) o;

        return Objects.equal(parentId, that.parentId)
                && Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, parentId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("parentId", parentId)
                .add("level", level)
                .add("hasChildren", hasChildren)
                .omitNullValues()
                .toString();
    }
}
