package io.terminus.snz.category.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 7/7/14
 */
public class CategoryPairWithBcName implements Serializable {
    private static final long serialVersionUID = -8732069784195488393L;

    @Getter
    @Setter
    private CategoryPair categoryPair;

    @Getter
    @Setter
    private String bcName; // 后台类目名称
}
