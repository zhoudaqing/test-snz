package io.terminus.snz.category.dao;

import com.fasterxml.jackson.databind.JavaType;
import io.terminus.common.utils.JsonMapper;
import io.terminus.snz.category.dto.CategoryPair;
import io.terminus.snz.category.model.CategoryBinding;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Author:  <a href="mailto:i@terminus.io">jlchen</a>
 * Date: 2014-06-11
 */
@Repository
public class CategoryBindingDao extends SqlSessionDaoSupport {

    public static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    private static final JavaType javaType = JSON_MAPPER.createCollectionType(List.class, CategoryPair.class);

    public Long create(CategoryBinding categoryBinding){
        getSqlSession().insert("CategoryBinding.create", categoryBinding);
        return categoryBinding.getId();
    }

    public boolean update(CategoryBinding categoryBinding){

        return getSqlSession().update("CategoryBinding.update", categoryBinding) ==1;
    }

    public boolean delete(Long id) {
        return getSqlSession().delete("CategoryBinding.delete", id) == 1;
    }

    public List<CategoryPair> findByFcid(Long fcid){
        CategoryBinding categoryBinding = getSqlSession().selectOne("CategoryBinding.findByFcid", fcid);
        if(categoryBinding == null){
            return Collections.emptyList();
        }
        return JSON_MAPPER.fromJson(categoryBinding.getBcs(), javaType);
    }

    public CategoryBinding findBy(Long fcid) {
        return getSqlSession().selectOne("CategoryBinding.findByFcid", fcid);
    }

    public CategoryBinding findById(Long id) {
        return getSqlSession().selectOne("CategoryBinding.findById", id);
    }

    public List<CategoryBinding> findAll() {
        return getSqlSession().selectList("CategoryBinding.findAll");
    }

}
