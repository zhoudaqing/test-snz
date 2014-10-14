package io.terminus.snz.requirement.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.requirement.model.ReqPredictTime;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:需求的创建时的预期时间
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-08-19.
 */
@Repository
public class ReqPredictTimeDao extends SqlSessionDaoSupport {
    /**
     * 创建需求的每个阶段的预期时间
     * @param reqPredictTime  需求的每个阶段的预期时间
     * @return Long
     * 返回创建的阶段的预期时间信息编号
     */
    public Long create(ReqPredictTime reqPredictTime){
        getSqlSession().insert("ReqPredictTime.create" , reqPredictTime);
        return reqPredictTime.getId();
    }

    /**
     * 批量创建每个阶段的预期时间状态
     * @param timeList  预期时间列表
     * @return  Boolean
     * 返回创建结果
     */
    public Boolean createBatch(List<ReqPredictTime> timeList){
        return getSqlSession().insert("ReqPredictTime.createBatch" , timeList) > 0;
    }

    /**
     * 更新需求预期阶段时间
     * @param reqPredictTime  需求预期阶段时间
     * @return Boolean
     * 返回更新是否成功
     */
    public Boolean update(ReqPredictTime reqPredictTime){
        return getSqlSession().update("ReqPredictTime.update" , reqPredictTime) == 1;
    }

    /**
     * 通过预期阶段时间编号查询信息
     * @param reqPredictTimeId   预期阶段时间编号
     * @return  ReqPredictTime
     * 返回需求信息
     */
    public ReqPredictTime findById(Long reqPredictTimeId){
        return getSqlSession().selectOne("ReqPredictTime.findById" , reqPredictTimeId);
    }

    /**
     * 通过需求编号&预期时间阶段类型查询对应的需求阶段信息
     * @param requirementId 需求编号
     * @param status        阶段类型
     * @return RequirementTime
     * 返回需求的阶段时间信息
     */
    public ReqPredictTime findByStatus(Long requirementId , Integer status){
        return getSqlSession().selectOne("ReqPredictTime.findByType" , ImmutableMap.of("requirementId", requirementId, "type", status));
    }

    /**
     * 通过需求编号&预期时间段的类型数组查询多条预期时间阶段数据
     * @param requirementId 需求编号
     * @param statues       预期时间阶段
     * @return List
     * 返回需求的阶段时间信息
     */
    public List<ReqPredictTime> findByStatues(Long requirementId , Integer... statues){
        return getSqlSession().selectList("ReqPredictTime.findByTypes" , ImmutableMap.of("requirementId", requirementId, "types", statues));
    }

    /**
     * 通过需求编号查询该需求下的全部预期阶段时间信息
     * @param requirementId 需求编号
     * @return  List
     * 返回查询到的预期时间阶段信息
     */
    public List<ReqPredictTime> findByRequirementId(Long requirementId){
        return getSqlSession().selectList("ReqPredictTime.findByRequirementId" , requirementId);
    }

    /**
     * 通过阶段预期时间编号删除对象
     * @param reqPredictTimeId   预期阶段时间编号
     * @return  Boolean
     * 返回删除结果
     */
    public Boolean delete(Long reqPredictTimeId){
        return getSqlSession().delete("ReqPredictTime.delete" , reqPredictTimeId) == 1;
    }
}
