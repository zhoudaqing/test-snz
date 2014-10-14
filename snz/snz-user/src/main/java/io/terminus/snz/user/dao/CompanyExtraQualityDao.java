package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyExtraQuality;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class CompanyExtraQualityDao extends SqlSessionDaoSupport {

    /**
     * 插入操作
     * @param companyExtraQuality
     * @return
     */
    public Long create(CompanyExtraQuality companyExtraQuality) {
        getSqlSession().insert("CompanyExtraQuality.create", companyExtraQuality);
        return companyExtraQuality.getId();
    }

    /**
     * 更新操作
     * @param companyExtraQuality
     * @return
     */
    public boolean update(CompanyExtraQuality companyExtraQuality) {
        return getSqlSession().update("CompanyExtraQuality.update", companyExtraQuality) == 1;
    }

    /**
     * 查询操作，根据用户id
     * @param userId
     * @return
     */
    public CompanyExtraQuality findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyExtraQuality.findByUserId", userId);
    }

}
