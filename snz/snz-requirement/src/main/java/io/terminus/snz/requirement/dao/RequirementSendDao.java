package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.RequirementSend;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Desc:需求信息是否已完全写入
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-31.
 */
@Repository
public class RequirementSendDao extends SqlSessionDaoSupport {

    /**
     * 创建需求的写入状态信息
     * @param requirementSend   需求的写入状态
     * @return  Boolean
     * 返回创建写入状态是否成功
     */
    public Boolean create(RequirementSend requirementSend){
        return getSqlSession().insert("RequirementSend.create" , requirementSend) > 0;
    }

    /**
     * 更新需求的写入状态信息
     * @param requirementSend   需求的写入状态
     * @return  Boolean
     * 返回更新写入状态是否成功
     */
    public Boolean update(RequirementSend requirementSend){
        return getSqlSession().update("RequirementSend.update" , requirementSend) > 0;
    }

    /**
     * 根据信息编号查询
     * @param id    信息编号
     * @return RequirementSend
     * 返回详细的状态信息
     */
    public RequirementSend findById(Long id){
        return getSqlSession().selectOne("RequirementSend.findById" , id);
    }

    /**
     * 根据需求编号查询
     * @param requirementId 需求编号
     * @return RequirementSend
     * 返回详细的状态信息
     */
    public RequirementSend findByRequirementId(Long requirementId){
        return getSqlSession().selectOne("RequirementSend.findByRequirementId" , requirementId);
    }
}
