package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.ModuleSolution;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:供应商提供的模块的实际数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@Repository
public class ModuleSolutionDao extends SqlSessionDaoSupport {
    /**
     * 创建需求的模块的方案
     * @param moduleSolution  模块的方案
     * @return Long
     * 返回创建的模块的方案编号
     */
    public Long create(ModuleSolution moduleSolution){
        getSqlSession().insert("ModuleSolution.create" , moduleSolution);
        return moduleSolution.getId();
    }

    /**
     * 通过需求模块方案
     * @param solutions 需求模块的方案列表
     * @return Integer
     * 返回批量创建的方案的数量
     */
    public Integer createBatch(List<ModuleSolution> solutions){
        return getSqlSession().insert("ModuleSolution.createBatch" , solutions);
    }

    /**
     * 更新需求模块的方案
     * @param moduleSolution  模块的方案
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(ModuleSolution moduleSolution){
        return getSqlSession().update("ModuleSolution.update" , moduleSolution) == 1;
    }

    /**
     * 根据需求方案编号删除方案下的全部模块数据信息
     * @param solutionId    需求编号
     * @return Boolean
     * 返回批量删除是否成功
     */
    public Boolean deleteBySolutionId(Long solutionId){
        return getSqlSession().delete("ModuleSolution.deleteBySolutionId" , solutionId) > 0;
    }

    /**
     * 通过模块的方案编号查询信息
     * @param moduleSolutionId   模块的方案编号
     * @return  ModuleSolution
     * 返回模块方案信息
     */
    public ModuleSolution findById(Long moduleSolutionId){
        return getSqlSession().selectOne("ModuleSolution.findById" , moduleSolutionId);
    }

    /**
     * 通过需求方案编号查询该方案下创建的模块方案数量
     * @param solutionId  整体方案编号
     * @return  Integer
     * 返回数量
     */
    public Integer countBySolutionId(Long solutionId){
        return getSqlSession().selectOne("ModuleSolution.countBySolutionId" , solutionId);
    }

    /**
     * 采购商通过方案编号查询供应商已经提交的模块方案
     * @param solutionId    方案编号
     * @param params        查询参数
     * @return 返回分页数据信息
     */
    public Paging<ModuleSolution> findSubmitted(Long solutionId, Map<String, Object> params){
        params.put("solutionId" , solutionId);

        Paging<ModuleSolution> paging;
        Long count = getSqlSession().selectOne("ModuleSolution.findByPurchaserCount" , params);
        if(count == 0){
            paging = new Paging<ModuleSolution>(0l , new ArrayList<ModuleSolution>());
        }else{
            List<ModuleSolution> solutionList = getSqlSession().selectList("ModuleSolution.findByPurchaser" , params);
            paging = new Paging<ModuleSolution>(count , solutionList);
        }

        return paging;
    }

    /**
     * 通过需求方案编号&具体模块需求编号查询模块方案
     * @param solutionId    需求方案编号
     * @param moduleId      具体模块需求编号
     * @return  ModuleSolution
     * 返回模块方案信息
     */
    public ModuleSolution findExist(Long solutionId, Long moduleId){
        return getSqlSession().selectOne("ModuleSolution.findExist" , ImmutableMap.of("solutionId" , solutionId, "moduleId", moduleId));
    }

    /**
     * 通过需求方案编号查询方案下的全部的模块报价信息
     * @param solutionId
     * @return
     */
    public List<ModuleSolution> findAllSolutions(Long solutionId){
        return getSqlSession().selectList("ModuleSolution.findAllSolutions" , solutionId);
    }
}
