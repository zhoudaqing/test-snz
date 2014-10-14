package io.terminus.snz.related.daos;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.related.models.CategoryFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:类目与工厂的关系
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-07.
 */
@Repository
public class CategoryFactoryDao extends SqlSessionDaoSupport {

    /**
     * 通过类目编号获取类目与工厂的关系
     * @param productId 类目编号
     * @param parkId    园区编号
     * @return  List
     * 返回类目与工厂的关系
     */
    public List<CategoryFactory> findByProductId(Long productId , Long parkId){
        return getSqlSession().selectList("CategoryFactory.findByProductId" , ImmutableMap.of("productId" , productId, "parkId", parkId));
    }

    /**
     * 通过类目编号以及工厂编号获取全部类目工厂关系信息
     * @param productId     类目编号
     * @param factoryIds    工厂编号列表
     * @return List
     * 返回全部类目工厂关系信息
     */
    public List<CategoryFactory> findByParams(Long productId , List<Long> factoryIds){
        return getSqlSession().selectList("CategoryFactory.findByParams" , ImmutableMap.of("productId" , productId, "factoryIds", factoryIds));
    }
 }
