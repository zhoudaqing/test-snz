/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.SupplierModuleDetail;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提供对表 `snz_supplier_module_details`  的增删改查操作<BR>
 * <p/>
 * Created by wanggen 2014-09-22 22:06:26
 */
@Repository
public class SupplierModuleDetailDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierModuleDetail.";

    /**
     * 新增
     *
     * @param supplierModuleDetail add bean
     * @return 新增后的自增序列号
     */
    public long create(SupplierModuleDetail supplierModuleDetail) {
        getSqlSession().insert(NAMESPACE + "create", supplierModuleDetail);
        return supplierModuleDetail.getId();
    }


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 supplierModuleDetail
     */
    public SupplierModuleDetail findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 根据 supplierCode 查询 SupplierModuleDetail 列表
     *
     * @param supplierCode 供应商编码
     * @return 结果列
     */
    public List<SupplierModuleDetail> findBySupplierCode(String supplierCode) {
        return getSqlSession().selectList(NAMESPACE + "findBySupplierCode", supplierCode);
    }


    /**
     * 根据供应商V码统计该数量
     *
     * @param supplierCode 供应商V码
     * @return 存在的供应商数量
     */
    public Integer countBySupplierCode(String supplierCode) {
        return getSqlSession().<Integer>selectOne(NAMESPACE+"countBySupplierCode", supplierCode);
    }


    /**
     * 更新操作
     *
     * @param supplierModuleDetail 更新操作参数
     * @return 影响行数
     */
    public int update(SupplierModuleDetail supplierModuleDetail) {
        return getSqlSession().update(NAMESPACE + "update", supplierModuleDetail);
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
