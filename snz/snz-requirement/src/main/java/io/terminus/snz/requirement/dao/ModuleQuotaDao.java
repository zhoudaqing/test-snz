package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.ModuleQuota;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:具体的模块的分配信息处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@Repository
public class ModuleQuotaDao extends SqlSessionDaoSupport {
    /**
     * 创建具体的模块的分配信息
     * @param moduleQuota  具体的模块的分配信息
     * @return Long
     * 返回具体的模块的分配信息编号
     */
    public Long create(ModuleQuota moduleQuota){
        getSqlSession().insert("ModuleQuota.create" , moduleQuota);
        return moduleQuota.getId();
    }

    /**
     * 创建具体的模块配额信息
     * @param moduleQuotaList 模块配额信息
     * @return  Integer
     * 返回模块的配额创建条数
     */
    public Integer createBatch(List<ModuleQuota> moduleQuotaList){
        return getSqlSession().insert("ModuleQuota.createBatch", moduleQuotaList);
    }

    /**
     * 更新具体的模块的分配信息
     * @param moduleQuota  具体的模块的分配信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(ModuleQuota moduleQuota){
        return getSqlSession().update("ModuleQuota.update" , moduleQuota) == 1;
    }

    /**
     * 通过具体的模块的分配信息编号查询信息
     * @param moduleQuotaId   具体的模块的分配信息编号
     * @return  ModuleQuota
     * 返回具体的模块的分配信息
     */
    public ModuleQuota findById(Long moduleQuotaId){
        return getSqlSession().selectOne("ModuleQuota.findById" , moduleQuotaId);
    }

    /**
     * 通过需求编号获取需求编号下的所有模块配额
     * @param requirementId 需求编号
     * @param status        配额状态(null:显示还未跟标的数据)
     * @return  List
     * 所有的模块配额信息
     */
    public List<ModuleQuota> findByRequirementId(Long requirementId , Integer status){
        Map<String , Object> params = Maps.newHashMap();
        params.put("requirementId" , requirementId);
        params.put("status" , status);
        return getSqlSession().selectList("ModuleQuota.findByRequirementId" , params);
    }

    /**
     * 通过需求编号获取所有的需求模块还未跟标的以及已经跟标的数据
     * @param requirementId 需求编号
     * @return  ModuleQuota
     * 返回跟标数据
     */
    public List<ModuleQuota> findAgreeQuotas(Long requirementId){
        return getSqlSession().selectList("ModuleQuota.findAgreeQuotas" , requirementId);
    }

    /**
     * 通过需求编号&模块编号获取其它的模块的配额信息
     * @param requirementId 需求编号
     * @param moduleId      模块编号
     * @param quotaId       模块配额编号
     * @return  List
     * 返回具体的模块配额信息
     */
    public List<ModuleQuota> findOtherQuota(Long requirementId , Long moduleId, Long quotaId){
        return getSqlSession().selectList("ModuleQuota.findOtherQuota", ImmutableMap.of("requirementId", requirementId, "moduleId", moduleId, "quotaId", quotaId));
    }

    /**
     * 根据模块编号&工厂编号获取全部的模块下的某个工厂的全部配额数据
     * @param moduleId      模块编号
     * @param moduleFactoryId     模块工厂编号
     * @return  List
     * 返回全部的工厂配额数据
     */
    public List<ModuleQuota> findQuotaByFactoryId(Long moduleId , Long moduleFactoryId){
        return getSqlSession().selectList("ModuleQuota.findQuotaByFactoryId" , ImmutableMap.of("moduleId" , moduleId, "moduleFactoryId", moduleFactoryId));
    }

    /**
     * 根据模块编号以及方案编号获取模块的配额数据
     * @param moduleId      模块编号
     * @param supplierId    供应商编号
     * @return  List
     * 返回模块的工厂的配额数据
     */
    public List<ModuleQuota> findQuotaBySolutionId(Long moduleId , Long supplierId){
        return getSqlSession().selectList("ModuleQuota.findQuotaBySolutionId" , ImmutableMap.of("moduleId" , moduleId, "supplierId", supplierId));
    }

    /**
     * 根据配额编号查询配额的详细信息
     * @param quotaIds  配额编号
     * @return  List
     * 返回配额的详细信息
     */
    public List<ModuleQuota> findByIds(String[] quotaIds){
       return getSqlSession().selectList("ModuleQuota.findByIds" , ImmutableMap.of("quotaIds" , quotaIds));
    }

    /**
     * 通过需求编号&一些参数查询模块配额信息
     * @param requirementId    需求编号
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<ModuleQuota> findByParams(Long requirementId , Map<String , Object> params){
        params.put("requirementId" , requirementId);
        Paging<ModuleQuota> paging;

        Long count = getSqlSession().selectOne("ModuleQuota.findQuotaCount" , params);
        if(count == 0){
            paging = new Paging<ModuleQuota>(0l , new ArrayList<ModuleQuota>());
        }else{
            List<ModuleQuota> moduleQuotaList = getSqlSession().selectList("ModuleQuota.findByParams" , params);
            paging = new Paging<ModuleQuota>(count , moduleQuotaList);
        }

        return paging;
    }
}
