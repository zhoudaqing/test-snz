package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierTQRDCInfoTmp;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 提供对表 `snz_supplier_tqrdc_infos_tmp`  的增删改查操作
 * <p/>
 * Created by wanggen
 */
@Repository
public class SupplierTQRDCInfoTmpDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierTQRDCInfoTmp.";

    /**
     * 新增
     *
     * @param supplierTQRDCInfoTmp add bean
     * @return 新增后的自增序列号
     */
    public long create(SupplierTQRDCInfoTmp supplierTQRDCInfoTmp) {
        getSqlSession().insert(NAMESPACE + "create", supplierTQRDCInfoTmp);
        return supplierTQRDCInfoTmp.getId();
    }


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 supplierTQRDCInfoTmp
     */
    public SupplierTQRDCInfoTmp findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 根据 id 列表查询多个结果集
     *
     * @param ids 多个id
     * @return SupplierTQRDCInfoTmp 列表
     */
    public List<SupplierTQRDCInfoTmp> findByIds(List<Long> ids) {
        return getSqlSession().selectList(NAMESPACE + "findByIds", ids);
    }


    /**
     * 根据 date 查询 SupplierTQRDCInfoTmp 列表
     *
     * @param date 日期
     * @return 结果列
     */
    public List<SupplierTQRDCInfoTmp> findByDate(String date) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("date", date);
        return getSqlSession().selectList(NAMESPACE + "findByDate", param);
    }


    /**
     * 更新操作
     *
     * @param supplierTQRDCInfoTmp 更新操作参数
     * @return 影响行数
     */
    public int update(SupplierTQRDCInfoTmp supplierTQRDCInfoTmp) {
        return getSqlSession().update(NAMESPACE + "update", supplierTQRDCInfoTmp);
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
     * 根据用户id获取列表
     * @param id 用户id
     * @return supplierTQRDCInfoTmp
     * */
    public SupplierTQRDCInfoTmp findByUserId(Long id){
        return getSqlSession().selectOne(NAMESPACE + "findByUserId", id);
    }
}
