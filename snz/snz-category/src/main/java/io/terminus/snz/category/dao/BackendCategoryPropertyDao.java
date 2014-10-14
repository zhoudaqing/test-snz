package io.terminus.snz.category.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.category.model.BackendCategoryProperty;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class BackendCategoryPropertyDao extends SqlSessionDaoSupport {

    public Long create(BackendCategoryProperty bcp) {
        getSqlSession().insert("BackendCategoryProperty.create", bcp);
        return bcp.getId();
    }

    public boolean update(BackendCategoryProperty bcp) {
        return getSqlSession().update("BackendCategoryProperty.update", bcp) == 1;
    }

    public boolean delete(Long id) {
        return getSqlSession().delete("BackendCategoryProperty.delete", id) == 1;
    }

    public BackendCategoryProperty findById(Long id) {
        return getSqlSession().selectOne("BackendCategoryProperty.findById", id);
    }

    public List<BackendCategoryProperty> findByBcId(Long bcId) {
        return getSqlSession().selectList("BackendCategoryProperty.findByBcId", bcId);
    }

    /**
     * 根据后台类目编号列表查询后台类目属性信息
     * @param bcIds 后台类目列表
     * @return  List
     * 返回后台类目属性信息
     */
    public List<BackendCategoryProperty> findByBcIds(List<Long> bcIds){
        return getSqlSession().selectList("BackendCategoryProperty.findByBcIds", bcIds);
    }

    /**
     * 更具后台类目编号和工厂编号列表查询后台类目属性信息
     * @param bcId           后台类目id
     * @param factoryNums    工厂编号列表
     * @return 后台类目属性信息List
     */
    public List<BackendCategoryProperty> findByBcIdWithFactoryNums(Long bcId, List<String> factoryNums) {
        return getSqlSession().selectList("BackendCategoryProperty.findByBcIdWithFactoryNums", ImmutableMap.of(
                "bcId", bcId, "factoryNums", factoryNums
        ));
    }
}
