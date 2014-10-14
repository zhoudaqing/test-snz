package io.terminus.snz.category.dao;

import io.terminus.snz.category.model.BackendCategory;
import org.springframework.stereotype.Repository;

/**
 * 前台类目dao
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Repository
public class BackendCategoryDao extends BaseCategoryDao<BackendCategory>{


    @Override
    public String entityName() {
        return BackendCategory.class.getSimpleName();
    }
}
