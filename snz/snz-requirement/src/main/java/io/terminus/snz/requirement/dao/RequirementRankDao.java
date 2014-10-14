package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.requirement.model.RequirementRank;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:采购商对于供应商的排名
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-03.
 */
@Repository
public class RequirementRankDao extends SqlSessionDaoSupport {
   /**
    * 创建对于需求的供应商排名信息操作
    * @param requirementRank  供应商排名信息
    * @return Long
    * 返回创建的排名的编号
    */
    public Long create(RequirementRank requirementRank){
        getSqlSession().insert("RequirementRank.create" , requirementRank);
        return requirementRank.getId();
    }

    /**
     * 批量创建供应商排名信息
     * @param rankList  供应商排名列表
     * @return  Integer
     * 返回批量排名创建的数据条数
     */
    public Integer createBatch(List<RequirementRank> rankList){
        return getSqlSession().insert("RequirementRank.createBatch" , rankList);
    }

    /**
     * 更新供应商排名信息
     * @param requirementRank  供应商排名信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(RequirementRank requirementRank){
        return getSqlSession().update("RequirementRank.update" , requirementRank) == 1;
    }

    /**
     * 通过供应商排名信息编号查询信息
     * @param requirementRankId   供应商排名信息编号
     * @return  RequirementRank
     * 返回供应商排名信息
     */
    public RequirementRank findById(Long requirementRankId){
        return getSqlSession().selectOne("RequirementRank.findById" , requirementRankId);
    }

    /**
     * 通过需求编号获取该需求下的所有排名信息
     * @param requirementId 需求编号
     * @param type          类型（1:正选供应商，2:备选供应商，3:显示全部数据－》默认3）
     * @return  List
     * 返回排名信息
     */
    public List<RequirementRank> findAllRanks(Long requirementId, Integer type){
        return getSqlSession().selectList("RequirementRank.findAllRanks" , ImmutableMap.of("requirementId" , requirementId, "type", type));
    }
}
