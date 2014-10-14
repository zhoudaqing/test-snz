package io.terminus.snz.category.dto;

import io.terminus.snz.category.model.FrontendCategory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangzefeng on 14-6-25
 */
@ToString @EqualsAndHashCode
public class FrontEndCategoryNav implements Serializable {
    private static final long serialVersionUID = 3326588387457174190L;

    @Getter
    @Setter
    private FrontendCategory firstLevel;

    @Getter
    @Setter
    private List<FrontendCategory> thirdLevel;
}
