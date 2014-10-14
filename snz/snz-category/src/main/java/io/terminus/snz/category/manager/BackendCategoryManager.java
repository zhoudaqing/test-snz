package io.terminus.snz.category.manager;

import io.terminus.snz.category.dao.BackendCategoryDao;
import io.terminus.snz.category.dao.BaseCategoryDao;
import io.terminus.snz.category.model.BackendCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Component
public class BackendCategoryManager extends BaseCategoryManager<BackendCategory>{

    @Autowired
    private BackendCategoryDao backendCategoryDao;

    @Override
    public BaseCategoryDao<BackendCategory> getCategoryDao() {
        return backendCategoryDao;
    }
}
