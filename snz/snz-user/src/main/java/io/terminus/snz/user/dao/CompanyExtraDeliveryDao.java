package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyExtraDelivery;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/23/14
 */
@Repository
public class CompanyExtraDeliveryDao extends SqlSessionDaoSupport {

    /**
     * 插入操作
     * @param companyExtraDelivery
     * @return
     */
    public Long create(CompanyExtraDelivery companyExtraDelivery) {
        getSqlSession().insert("CompanyExtraDelivery.create", companyExtraDelivery);
        return companyExtraDelivery.getId();
    }

    /**
     * 更新操作
     * @param companyExtraDelivery
     * @return
     */
    public boolean update(CompanyExtraDelivery companyExtraDelivery) {
        return getSqlSession().update("CompanyExtraDelivery.update", companyExtraDelivery) == 1;
    }

    /**
     * 查询操作，根据用户id
     * @param userId
     * @return
     */
    public CompanyExtraDelivery findByUserId(Long userId) {
        return getSqlSession().selectOne("CompanyExtraDelivery.findByUserId", userId);
    }
}
