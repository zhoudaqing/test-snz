package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.requirement.model.RequirementTime;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:需求的每个阶段的时间状态记录&就时间更改的用户
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-05-02.
 */
@Repository
public class RequirementTimeDao extends SqlSessionDaoSupport {
    /**
     * 创建需求的每个阶段的时间
     * @param requirementTime  需求的每个阶段的时间
     * @return Long
     * 返回创建的阶段的时间信息编号
     */
    public Long create(RequirementTime requirementTime){
        getSqlSession().insert("RequirementTime.create" , requirementTime);
        return requirementTime.getId();
    }

    /**
     * 批量创建每个阶段的时间状态
     * @param timeList  时间列表
     * @return  Boolean
     * 返回创建结果
     */
    public Boolean createBatch(List<RequirementTime> timeList){
        return getSqlSession().insert("RequirementTime.createBatch" , timeList) > 0;
    }

    /**
     * 更新需求阶段时间
     * @param requirementTime  需求阶段时间
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(RequirementTime requirementTime){
        return getSqlSession().update("RequirementTime.update" , requirementTime) == 1;
    }

    /**
     * 通过阶段时间编号查询信息
     * @param requirementTimeId   阶段时间编号
     * @return  RequirementTime
     * 返回需求信息
     */
    public RequirementTime findById(Long requirementTimeId){
        return getSqlSession().selectOne("RequirementTime.findById" , requirementTimeId);
    }

    /**
     * 通过需求编号&时间阶段类型查询对应的需求阶段信息
     * @param requirementId 需求编号
     * @param status        阶段类型
     * @return RequirementTime
     * 返回需求的阶段时间信息
     */
    public RequirementTime findByStatus(Long requirementId , Integer status){
        return getSqlSession().selectOne("RequirementTime.findByType" , ImmutableMap.of("requirementId", requirementId, "type", status));
    }

    /**
     * 通过需求编号&时间段的类型数组查询多条时间阶段数据
     * @param requirementId 需求编号
     * @param statues       时间阶段
     * @return List
     * 返回需求的阶段时间信息
     */
    public List<RequirementTime> findByStatues(Long requirementId , Integer... statues){
        return getSqlSession().selectList("RequirementTime.findByTypes" , ImmutableMap.of("requirementId", requirementId, "types", statues));
    }

    /**
     * 通过需求编号查询该需求下的全部阶段时间信息
     * @param requirementId 需求编号
     * @return  List
     * 返回查询到的时间阶段信息
     */
    public List<RequirementTime> findByRequirementId(Long requirementId){
        return getSqlSession().selectList("RequirementTime.findByRequirementId" , requirementId);
    }

    /**
     * 通过阶段时间编号删除对象
     * @param requirementTimeId   阶段时间编号
     * @return  Boolean
     * 返回删除结果
     */
    public Boolean delete(Long requirementTimeId){
        return getSqlSession().delete("RequirementTime.delete" , requirementTimeId) == 1;
    }
}
