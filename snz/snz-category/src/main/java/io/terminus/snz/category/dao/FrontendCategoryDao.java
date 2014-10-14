package io.terminus.snz.category.dao;

import io.terminus.snz.category.model.FrontendCategory;
import org.springframework.stereotype.Repository;

/**
 * 前台类目dao
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Repository
public class FrontendCategoryDao extends BaseCategoryDao<FrontendCategory>{

    @Override
    public String entityName() {
        return FrontendCategory.class.getSimpleName();
    }
}
