package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.requirement.model.RequirementTeam;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:采购商需求对于团队的人员的操作
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
@Repository
public class RequirementTeamDao extends SqlSessionDaoSupport {
    /**
     * 创建需求的团队人员信息
     * @param requirementTeam  团队人员信息
     * @return Long
     * 返回创建的团队人员信息编号
     */
    public Long create(RequirementTeam requirementTeam){
        getSqlSession().insert("RequirementTeam.create" , requirementTeam);
        return requirementTeam.getId();
    }

    /**
     * 批量创建需求的团队信息
     * @param teamList    团队信息列表
     * @return Boolean
     * 返回创建是否成功
     */
    public Boolean createBatch(List<RequirementTeam> teamList){
        return getSqlSession().insert("RequirementTeam.createBatch" , teamList) > 0;
    }

    /**
     * 更新需求团队人员信息
     * @param requirementTeam  团队人员信息
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(RequirementTeam requirementTeam){
        return getSqlSession().update("RequirementTeam.update" , requirementTeam) == 1;
    }

    /**
     * 通过团队人员信息编号查询信息
     * @param requirementTeamId   团队人员信息编号
     * @return  RequirementTeam
     * 返回需求信息
     */
    public RequirementTeam findById(Long requirementTeamId){
        return getSqlSession().selectOne("RequirementTeam.findById" , requirementTeamId);
    }

    /**
     * 通过团队人员信息编号删除对象
     * @param requirementTeamId   团队人员信息编号
     * @return  Boolean
     * 返回删除结果
     */
    public Boolean delete(Long requirementTeamId){
        return getSqlSession().delete("RequirementTeam.delete" , requirementTeamId) == 1;
    }

    /**
     * 通过需求编号查询改需求下的全部团队人员信息
     * @param requirementId 需求编号
     * @return  List
     * 返回一个需求团队的信息
     */
    public List<RequirementTeam> findByRequirementId(Long requirementId){
        return getSqlSession().selectList("RequirementTeam.findByRequirementId" , requirementId);
    }

    /**
     * 根据用户编号一级用户的团队类型判断用户是否存在
     * @param userId    用户编号
     * @param teamType  团队类型
     * @return  RequirementTeam
     * 需求团队信息
     */
    public RequirementTeam findByIdType(Long userId , Integer teamType){
        return getSqlSession().selectOne("RequirementTeam.findByIdType" , ImmutableMap.of("userId", userId, "teamType", teamType));
    }
}
