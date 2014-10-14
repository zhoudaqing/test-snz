package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.ProductOwner;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提供对表 `snz_product_owners`  的增删改查操作<P>
 *
 * Created by wanggen 2014-08-13 22:17:08
 */
@Repository
public class ProductOwnerDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "ProductOwner.";

    /**
     * 新增
     *
     * @param productOwner add bean
     * @return 新增后的自增序列号
     */
    public long create(ProductOwner productOwner) {
        getSqlSession().insert(NAMESPACE + "create", productOwner);
        return productOwner.getId();
    }


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 productOwner
     */
    public ProductOwner findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 根据 id 列表查询多个结果集
     * @param ids 多个id
     * @return ProductOwner 列表
     */
    public List<ProductOwner> findByIds(List<Long> ids){
        return getSqlSession().selectList(NAMESPACE + "findByIds", ids);
    }


    /**
     * 根据 productLineId 查询 ProductOwner 列表
     *
     * @param productLineId   产品线ID
     * @return 结果列
     */
    public List<ProductOwner> findByProductLineId(Integer productLineId){
        return getSqlSession().selectList(NAMESPACE+"findByProductLineId",productLineId);
    }


    /**
     * 根据 productLineName 查询 ProductOwner 列表
     *
     * @param productLineName   产品线名称
     * @return 结果列
     */
    public List<ProductOwner> findByProductLineName(String productLineName){
        return getSqlSession().selectList(NAMESPACE+"findByProductLineName",productLineName);
    }


    /**
     * 更新操作
     *
     * @param productOwner 更新操作参数
     * @return 影响行数
     */
    public int update(ProductOwner productOwner) {
        return getSqlSession().update(NAMESPACE + "update", productOwner);
    }


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public int deleteByIds(List<Long> ids) {
        return getSqlSession().delete(NAMESPACE + "deleteByIds", ids);
    }


}
