package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyRank;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午8:22
 */
@Repository
public class CompanyRankDao extends SqlSessionDaoSupport {

    /**
     * 创建公司排名信息
     */
    public Long create(CompanyRank companyRank) {
        getSqlSession().insert("CompanyRank.insert", companyRank);
        return companyRank.getId();
    }

    /**
     * 删除公司排名信息
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("CompanyRank.delete", id) == 1;
    }

    /**
     * 更新公司排名信息
     */
    public boolean update(CompanyRank companyRank) {
        return getSqlSession().update("CompanyRank.update", companyRank) == 1;
    }

    /**
     * 根据编号查询公司排名信息
     */
    public CompanyRank findById(Long id) {
        return getSqlSession().selectOne("CompanyRank.findById", id);
    }

    /**
     * 根据用户编号查询公司排名信息
     */
    public CompanyRank findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyRank.findByUserId", userId);
    }
}
