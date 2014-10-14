package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.Paging;
import io.terminus.snz.requirement.model.DerivativeDiff;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Desc:场景差异信息处理
 * Created by jiaoyuan on 14-7-7.
 * 代码交接给侯璐瑶
 */
@Repository
public class DerivativeDiffDao extends SqlSessionDaoSupport {
    /**
     * 创建具体的场景差异信息
     * @param derivativeDiff  具体的场景差异信息
     * @return Long
     * 返回具体的场景的信息编号
     */
    public Long create(DerivativeDiff derivativeDiff){
        getSqlSession().insert("DerivativeDiff.create" , derivativeDiff);
        return derivativeDiff.getId();
    }
    /**
     * 更新具体的场景差异信息
     * @param derivativeDiff  具体的场景差异信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(DerivativeDiff derivativeDiff){
        return  getSqlSession().update("DerivativeDiff.update",derivativeDiff)==1;
    }
    public  DerivativeDiff findByModuleId(long moduleId){
        return  getSqlSession().selectOne("DerivativeDiff.findByModuleId",moduleId);
    }
    /**
     * 通过需求编号&一些参数查询具体的场景差异信息
     * @param requirementId    需求编号
     * @param params        查询操作处理
     * @return  List
     * 返回查询得到的详细信息列表
     */
    public Paging<DerivativeDiff> findByParams(Long requirementId, Map<String, Object> params){
        params.put("requirementId",requirementId);
        Paging<DerivativeDiff> paging;
        Long count = getSqlSession().selectOne("DerivativeDiff.findByRequirementCount" , params);
        if(count == 0){

            paging = new Paging<DerivativeDiff>(0l , new ArrayList<DerivativeDiff>());
        }else{
            List<DerivativeDiff> derivativeDiffs = getSqlSession().selectList("DerivativeDiff.findByRequirementId" , params);
            paging = new Paging<DerivativeDiff>(count , derivativeDiffs);

        }

        return paging;
    }

    public Boolean createBash(List<DerivativeDiff> derivativeDiffList){
        return  getSqlSession().insert("DerivativeDiff.createBatch",derivativeDiffList)>0;
    }

    /**
     * 通过需求编号查询全部的衍生品差异信息
     * @param requirementId 需求编号
     * @return
     * 衍生品差异详细信息列表
     */
    public List<DerivativeDiff> findByRequirementId(Long requirementId){
        return getSqlSession().selectList("DerivativeDiff.findByRequirementIdWithOutPaging",requirementId);
    }

    /**
     * 通过需求编号、模块编号查询差异信息是否存在
     * @param requirementId 需求编号 ,  moduleId 模块编号
     * @return
     * 衍生品差异详细信息列表
     */
    public List<DerivativeDiff> checkDiffExists(Long requirementId , Long moduleId){
        return getSqlSession().selectList("DerivativeDiff.checkDiffExists", ImmutableMap.of("requirementId",requirementId, "moduleId",moduleId));
    }

    public Boolean delete(Long requirementId, Long moduleId){
        return getSqlSession().delete("DerivativeDiff.delete", ImmutableMap.of("requirementId",requirementId, "moduleId",moduleId)) <= 1;
    }
}
