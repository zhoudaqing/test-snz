package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyExtraRD;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class CompanyExtraRDDao extends SqlSessionDaoSupport {

    /**
     * 插入操作
     * @param companyExtraRD
     * @return
     */
    public Long create(CompanyExtraRD companyExtraRD) {
        getSqlSession().insert("CompanyExtraRD.create", companyExtraRD);
        return companyExtraRD.getId();
    }

    /**
     * 更新操作
     * @param companyExtraRD
     * @return
     */
    public boolean update(CompanyExtraRD companyExtraRD) {
        return getSqlSession().update("CompanyExtraRD.update", companyExtraRD) == 1;
    }

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    public CompanyExtraRD findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyExtraRD.findByUserId", userId);
    }
}
