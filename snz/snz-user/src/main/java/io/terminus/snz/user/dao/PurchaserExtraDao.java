package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.PurchaserExtra;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-6
 */
@Repository
public class PurchaserExtraDao extends SqlSessionDaoSupport {

    /**
     * 创建采购商额外信息
     */
    public Long create(PurchaserExtra purchaserExtra) {
        getSqlSession().insert("PurchaserExtra.insert", purchaserExtra);
        return purchaserExtra.getId();
    }

    /**
     * 更新采购商额外信息
     */
    public boolean updateByUserId(PurchaserExtra purchaserExtra) {
        return getSqlSession().update("PurchaserExtra.updateByUserId", purchaserExtra) == 1;
    }

    /**
     * 根据编号查询采购商额外信息
     */
    public PurchaserExtra findById(Long id) {
        return getSqlSession().selectOne("PurchaserExtra.findById", id);
    }

    /**
     * 根据user id查询采购商额外信息
     */
    public PurchaserExtra findByUserId(Long userId) {
        return getSqlSession().selectOne("PurchaserExtra.findByUserId", userId);
    }

}
