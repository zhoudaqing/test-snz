package io.terminus.snz.related.daos;

import io.terminus.common.mysql.dao.MyBatisDao;
import io.terminus.snz.related.models.FactoryOrgan;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工厂组织对应关系Dao
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-25
 */
@Repository
public class FactoryOrganDao extends MyBatisDao<FactoryOrgan> {

    /**
     * 根据工厂查找组织
     * @param factory 工厂
     * @return 对应组织
     */
    public List<String> findOrganByFactory(String factory) {
        return getSqlSession().selectList(nameSpace + ".findOrganByFactory", factory);
    }
}
