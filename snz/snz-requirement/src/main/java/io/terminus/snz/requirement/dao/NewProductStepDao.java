package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.snz.requirement.model.NewProductStep;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 提供对表 `snz_new_product_steps`  的增删改查操作
 *
 * Created by wanggen
 */
@Repository
public class NewProductStepDao extends SqlSessionDaoSupport {

    private static final String namespace = "NewProductStep.";

    /**
     * 新增
     *
     * @param newProductStep add bean
     * @return 新增后的自增序列号
     */
    public long create(NewProductStep newProductStep) {
        getSqlSession().insert(namespace + "create", newProductStep);
        return newProductStep.getId();
    }


    /**
     * 根据ID查询但条记录
     *
     * @return 返回的 newProductStep
     */
    public NewProductStep findById(Long id) {
        return getSqlSession().selectOne(namespace + "findById", id);
    }


    /**
     * 根据 id 列表查询多个结果集
     * @param ids 多个id
     * @return NewProductStep 列表
     */
    public List<NewProductStep> findByIds(List<Long> ids){
        return getSqlSession().selectList(namespace + "findByIds", ids);
    }

    /**
     * 根据 moduleNum 查询 NewProductStep 列表
     *
     * @param mouldNumber   模块号
     * @return 结果列
     */
    public List<NewProductStep> findByModuleNum(String mouldNumber){
        return getSqlSession().selectList(namespace+"findByModuleNum",mouldNumber);
    }


    /**
     * 根据 moduleNum,supplierName 查询 NewProductStep 列表
     *
     * @param mouldNumber   模块号
     * @param supplierName   供应商名称
     * @return 结果列
     */
    public List<NewProductStep> findByModuleNumAndSupplierName(String mouldNumber, String supplierName){
        Map<String, Object> param = Maps.newHashMap();
        param.put("moduleNum", mouldNumber);
        param.put("supplierName", supplierName);
        return getSqlSession().selectList(namespace+"findByModuleNumAndSupplierName",param);
    }


    /**
     * 根据 moduleNum,supplierName,step 查询 NewProductStep 列表
     *
     * @param mouldNumber   模块号
     * @param supplierName   供应商名称
     * @param step 流程节点(1:原件创建 | 2:原件创建 | 3:装配完成 | 4:收样确认 | 5:检测收样 | 6:检测开始 | 7:检测计划时间 | 8:检测完成)
     * @return 结果
     */
    public NewProductStep findByMouldNumberAndSupplierNameAndStep(String mouldNumber, String supplierName, Integer step){
        Map<String, Object> param = Maps.newHashMap();
        param.put("moduleNum", mouldNumber);
        param.put("supplierName", supplierName);
        param.put("step", step);
        return getSqlSession().selectOne(namespace+"findByModuleNumAndSupplierNameAndStep",param);
    }


    /**
     * 根据 NPI 及步骤查询唯一步骤信息
     * @param parentId  NPI导入数据父ID
     * @param step      输入本次 NPI 的某一步
     * @return          唯一步骤
     */
    public NewProductStep findByParentIdAndStep(Long parentId, Integer step){
        return getSqlSession().selectOne(namespace+"findByParentIdAndStep",
                ImmutableMap.of("parentId", parentId, "step", step));
    }


    /**
     * 更新操作
     *
     * @param newProductStep 更新操作参数
     * @return 影响行数
     */
    public Boolean update(NewProductStep newProductStep) {
        return getSqlSession().update(namespace + "update", newProductStep)==1;
    }

    /**
     * 更新操作
     *
     * @param newProductStep 更新操作参数
     * @return 影响行数
     */
    public Boolean updateByParams(NewProductStep newProductStep) {
        return getSqlSession().update(namespace + "updateByParams", newProductStep)==1;
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
