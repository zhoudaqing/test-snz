package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.ModuleQuotation;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:供应商提供的模块的报价数据信息
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-10.
 */
@Repository
public class ModuleQuotationDao extends SqlSessionDaoSupport {
    /**
     * 创建需求的模块的报价方案
     * @param moduleQuotation  模块的报价方案
     * @return Long
     * 返回创建的模块的报价方案编号
     */
    public Long create(ModuleQuotation moduleQuotation){
        getSqlSession().insert("ModuleQuotation.create" , moduleQuotation);
        return moduleQuotation.getId();
    }

    /**
     * 批量创建模块的报价方案
     * @param quotations 模块报价方案列表
     * @return  Integer
     * 返回模块方案创建的条数
     */
    public Integer createBatch(List<ModuleQuotation> quotations){
        return getSqlSession().insert("ModuleQuotation.createBatch" , quotations);
    }

    /**
     * 更新需求模块的报价方案
     * @param moduleQuotation  模块的报价方案
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(ModuleQuotation moduleQuotation){
        return getSqlSession().update("ModuleQuotation.update" , moduleQuotation) == 1;
    }

    /**
     * 根据需求方案编号删除方案下的全部模块报价数据信息
     * @param solutionId    需求编号
     * @return Boolean
     * 返回批量删除是否成功
     */
    public Boolean deleteBySolutionId(Long solutionId){
        return getSqlSession().delete("ModuleQuotation.deleteBySolutionId" , solutionId) > 0;
    }

    /**
     * 通过模块的报价方案编号查询信息
     * @param quotationId   模块的报价方案编号
     * @return  ModuleQuotation
     * 返回模块方案信息
     */
    public ModuleQuotation findById(Long quotationId){
        return getSqlSession().selectOne("ModuleQuotation.findById" , quotationId);
    }

    /**
     * 通过需求方案编号查询该方案下创建的模块报价方案数量
     * @param solutionId  整体方案编号
     * @return  Integer
     * 返回数量
     */
    public Integer countBySolutionId(Long solutionId){
        return getSqlSession().selectOne("ModuleQuotation.countBySolutionId" , solutionId);
    }

    /**
     * 通过方案编号查询方案下的全部模块报价方案
     * @param solutionId   方案编号
     * @return  List
     * 返回模块报价方案信息列表
     */
    public List<ModuleQuotation> findAllQuotations(Long solutionId){
        return getSqlSession().selectList("ModuleQuotation.findAllQuotations" , solutionId);
    }

    /**
     * 通过方案编号查询供应商已经提交的模块报价信息
     * @param solutionId    方案编号
     * @param params        查询参数
     * @return 返回分页数据信息
     */
    public Paging<ModuleQuotation> findSubmitted(Long solutionId, Map<String, Object> params){
        params.put("solutionId" , solutionId);

        Paging<ModuleQuotation> paging;
        Long count = getSqlSession().selectOne("ModuleQuotation.findByPurchaserCount" , params);
        if(count == 0){
            paging = new Paging<ModuleQuotation>(0l , new ArrayList<ModuleQuotation>());
        }else{
            List<ModuleQuotation> quotations = getSqlSession().selectList("ModuleQuotation.findByPurchaser" , params);
            paging = new Paging<ModuleQuotation>(count , quotations);
        }

        return paging;
    }

    /**
     * 通过编号查询供应商已经提交的模块报价信息
     * @param solutionIds   提交报价方案的列表
     * @param params        查询参数
     * @return 返回分页数据信息
     */
    public Paging<ModuleQuotation> findByTransact(List<Long> solutionIds , Map<String, Object> params){
        params.put("solutionIds" , solutionIds);

        Paging<ModuleQuotation> paging;
        Long count = getSqlSession().selectOne("ModuleQuotation.findTransactCount" , params);
        if(count == 0){
            paging = new Paging<ModuleQuotation>(0l , new ArrayList<ModuleQuotation>());
        }else{
            List<ModuleQuotation> quotations = getSqlSession().selectList("ModuleQuotation.findTransacts" , params);
            paging = new Paging<ModuleQuotation>(count , quotations);
        }

        return paging;
    }

    /**
     * 通过需求方案编号&具体模块需求编号查询模块报价方案
     * @param solutionId    需求方案编号
     * @param moduleId      具体模块需求编号
     * @return  ModuleSolution
     * 返回模块方案信息
     */
    public ModuleQuotation findExist(Long solutionId, Long moduleId){
        return getSqlSession().selectOne("ModuleQuotation.findExist" , ImmutableMap.of("solutionId" , solutionId, "moduleId", moduleId));
    }
}
