package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyExtraResponse;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class CompanyExtraResponseDao extends SqlSessionDaoSupport {

    /**
     * 插入操作
     * @param companyExtraResponse
     * @return
     */
    public Long create(CompanyExtraResponse companyExtraResponse) {
        getSqlSession().insert("CompanyExtraResponse.create", companyExtraResponse);
        return companyExtraResponse.getId();
    }

    /**
     * 更新操作
     * @param companyExtraResponse
     * @return
     */
    public boolean update(CompanyExtraResponse companyExtraResponse) {
        return getSqlSession().update("CompanyExtraResponse.update", companyExtraResponse) == 1;
    }

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    public CompanyExtraResponse findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyExtraResponse.findByUserId", userId);
    }

}
