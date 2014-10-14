package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.MainBusinessApprover;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-31.
 */
@Repository
public class MainBusinessApproverDao extends SqlSessionDaoSupport {

    /**
     * 创建审核人
     */
    public Long create(MainBusinessApprover mainBusinessApprover) {
        getSqlSession().insert("MainBusinessApprover.insert", mainBusinessApprover);
        return mainBusinessApprover.getId();
    }

    /**
     * 根据组员id查询审核信息
     */
    public List<MainBusinessApprover> findByMemberId(String memberId) {
        return getSqlSession().selectList("MainBusinessApprover.findByMemberId", memberId);
    }

    /**
     * 根据组长id查询审核信息
     */
    public List<Long> findMainBusinessIdsByLeaderId(String leaderId) {
        return getSqlSession().selectList("MainBusinessApprover.findMainBusinessIdsByLeaderId", leaderId);
    }

    /**
     * 根据组员或组长查询主营业务编号
     */
    public List<Long> findMainBusinessIdsByMemberIdOrLeaderId(String memberOrLeaderId) {
        return getSqlSession().selectList("MainBusinessApprover.findMainBusinessIdsByMemberIdOrLeaderId", memberOrLeaderId);
    }

    /**
     * 根据主营业务编号查询审核信息
     */
    public List<MainBusinessApprover> findByMainBusinessIds(List<Long> mainBusinessIds) {
        return getSqlSession().selectList("MainBusinessApprover.findByMainBusinessIds", mainBusinessIds);
    }

}
