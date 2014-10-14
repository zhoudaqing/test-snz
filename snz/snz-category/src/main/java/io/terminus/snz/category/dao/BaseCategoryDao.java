package io.terminus.snz.category.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.category.model.CategoryBase;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
public  abstract class BaseCategoryDao<T extends CategoryBase> extends SqlSessionDaoSupport{


    public Long create(T category) {
        getSqlSession().insert(entityName()+".create", category);
        return category.getId();

    }

    public boolean update(T category){
        return getSqlSession().update(entityName()+".update", category) == 1;
    }

    public boolean delete(Long id) {
        return getSqlSession().delete(entityName()+".delete", id) == 1;
    }

    public T findById(Long id){
        return getSqlSession().selectOne(entityName()+".findById", id);
    }

    public List<T> findByIds(List<Long> ids){
        return getSqlSession().selectList(entityName()+".findByIds", ids);
    }

    public List<T> findChildrenOf(Long id){
        return getSqlSession().selectList(entityName()+".findChildrenOf", id);
    }

    public List<T> findByLevels(Integer level){
        return getSqlSession().selectList(entityName() + ".findByLevels" , level);
    }

    public List<T> findByParentIdAndLevel(Integer parentId, Integer level) {
        return getSqlSession().selectList(entityName() + ".findByParentIdAndLevel",
                ImmutableMap.of("parentId", parentId,
                                "level",    level));
    }

    public T findByLevelAndName(Integer level, String name) {
        return getSqlSession().selectOne(entityName() + ".findByLevelAndName", ImmutableMap.of(
                "level", level, "name", name
        ));
    }

    public abstract String entityName();
}
