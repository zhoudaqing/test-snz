package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.ProductLine;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-2.
 */
@Repository
public class ProductLineDao extends SqlSessionDaoSupport {

    /**
     * 创建产业线
     */
    public Long create(ProductLine productLine) {
        getSqlSession().insert("ProductLine.insert", productLine);
        return productLine.getId();
    }

    /**
     * 根据编号查询产业线
     */
    public ProductLine findById(Long id) {
        return getSqlSession().selectOne("ProductLine.findById", id);
    }

    /**
     * 查询所有产业线
     */
    public List<ProductLine> findAll() {
        return getSqlSession().selectList("ProductLine.findAll");
    }

    /**
     * 在ids范围内查询产业线
     */
    public List<ProductLine> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList("ProductLine.findByIds", ids);
    }

}
