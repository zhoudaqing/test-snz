package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyExtraScaleAndCost;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class CompanyExtraScaleAndCostDao extends SqlSessionDaoSupport {

    /**
     * 插入操作
     * @param companyExtraScaleAndCost
     * @return
     */
    public Long create(CompanyExtraScaleAndCost companyExtraScaleAndCost) {
        getSqlSession().insert("CompanyExtraScaleAndCost.create", companyExtraScaleAndCost);
        return companyExtraScaleAndCost.getId();
    }

    /**
     * 更新操作
     * @param companyExtraScaleAndCost
     * @return
     */
    public boolean update(CompanyExtraScaleAndCost companyExtraScaleAndCost) {
        return getSqlSession().update("CompanyExtraScaleAndCost.update", companyExtraScaleAndCost) == 1;
    }

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    public CompanyExtraScaleAndCost findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyExtraScaleAndCost.findByUserId", userId);
    }

}
