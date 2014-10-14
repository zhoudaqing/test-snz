package io.terminus.snz.category.manager;

import io.terminus.snz.category.dao.BaseCategoryDao;
import io.terminus.snz.category.dao.FrontendCategoryDao;
import io.terminus.snz.category.model.FrontendCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 前后台类目管理类
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Component
public class FrontendCategoryManager extends BaseCategoryManager<FrontendCategory> {
    @Autowired
    private FrontendCategoryDao frontendCategoryDao;

    @Override
    public BaseCategoryDao<FrontendCategory> getCategoryDao() {
        return frontendCategoryDao;
    }
}
