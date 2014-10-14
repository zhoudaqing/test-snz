/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.user.model.SupplierContact;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提供对表 `snz_supplier_contacts`  的增删改查操作<BR>
 * <p/>
 * Created by wanggen 2014-09-11 21:18:48
 */
@Repository
public class SupplierContactDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierContact.";


    /**
     * 新增
     *
     * @param supplierContact add bean
     * @return 新增后的自增序列号
     */
    public long create(SupplierContact supplierContact) {
        getSqlSession().insert(NAMESPACE + "create", supplierContact);
        return supplierContact.getId();
    }


    /**
     * 更新操作
     *
     * @param supplierContact 更新操作参数
     * @return 影响行数
     */
    public int update(SupplierContact supplierContact) {
        return getSqlSession().update(NAMESPACE + "update", supplierContact);
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


    /**
     * 加载所有供应商联系信息
     *
     * @return 所有供应商联系信息
     */
    public List<SupplierContact> listAll() {
        return getSqlSession().selectList(NAMESPACE + "listAll");
    }


    /**
     * 通过供应商 V码 或 供应商名称 查询供应商联系方式
     *
     * @param supplierCode 供应商V码
     * @param supplierName 供应商名称
     * @return 供应商联系方式
     */
    public SupplierContact findBySupplierCodeOrName(String supplierCode, String supplierName) {
        return getSqlSession().selectOne(NAMESPACE + "findBySupplierCodeOrName",
                ImmutableMap.of("supplierCode", supplierCode, "supplierName", supplierName));
    }
}
