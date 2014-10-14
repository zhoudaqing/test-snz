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
public class CategoryBinding implements Serializable {
    private static final long serialVersionUID = 8447716375650800088L;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private Long frontId;      //前台类目id

    @Getter
    @Setter
    private String bcs;    // 以json存储的后台类目id和name集合, 见List<CategoryPair>的json格式

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

        CategoryBinding that = (CategoryBinding) o;
        return Objects.equal(this.frontId, that.frontId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(frontId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("frontId", frontId)
                .add("bcs", bcs)
                .omitNullValues()
                .toString();
    }
}
