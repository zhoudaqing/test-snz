package io.terminus.snz.requirement.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.NewProductImport;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 提供对表 `snz_new_product_imports`  的增删改查操作
 *
 * Created by wanggen
 */
@Repository
public class NewProductImportDao extends SqlSessionDaoSupport {

    private static final String namespace = "NewProductImport.";

    /**
     * 新增
     *
     * @param newProductImport add bean
     * @return 新增后的自增序列号
     */
    public long create(NewProductImport newProductImport) {
        getSqlSession().insert(namespace + "create", newProductImport);
        return newProductImport.getId();
    }


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 newProductImport
     */
    public NewProductImport findById(Long id) {
        return getSqlSession().selectOne(namespace + "findById", id);
    }


    /**
     * 根据 id 列表查询多个结果集
     * @param ids 多个id
     * @return NewProductImport 列表
     */
    public List<NewProductImport> findByIds(List<Long> ids){
        return getSqlSession().selectList(namespace + "findByIds", ids);
    }


    /**
     * 根据 moduleNum 查询 NewProductImport 列表
     *
     * @param moduleNum   模块编号
     * @return 结果列
     */
    public List<NewProductImport> findByModuleNum(String moduleNum){
        return getSqlSession().selectList(namespace+"findByModuleNum", moduleNum);
    }



    /**
     * 根据 moduleNum,supplierName 查询 NewProductImport 列表
     *
     * @param moduleNum   		模块编号
     * @param supplierName    供应商代码
     * @return 结果列
     */
    public List<NewProductImport> findByModuleNumAndSupplierName(String moduleNum, String supplierName) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("moduleNum", moduleNum);
        param.put("supplierName", supplierName);
        return getSqlSession().selectList(namespace + "findByModuleNumAndSupplierName", param);
    }


    /**
     * 更新操作
     *
     * @param newProductImport 更新操作参数
     * @return 影响行数
     */
    public int update(NewProductImport newProductImport) {
        return getSqlSession().update(namespace + "update", newProductImport);
    }


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public int deleteByIds(List<Long> ids) {
        return getSqlSession().delete(namespace + "deleteByIds", ids);
    }


}
