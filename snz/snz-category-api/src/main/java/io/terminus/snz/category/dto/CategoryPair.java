package io.terminus.snz.category.dto;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public class CategoryPair implements Serializable {
    private static final long serialVersionUID = -8228056388532662255L;

    @Getter
    @Setter
    private Long id; //后台叶子类目id

    @Getter
    @Setter
    private String path; //后台叶子类目从,根类目到叶子类目的路径

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("path", path)
                .toString();
    }
}
