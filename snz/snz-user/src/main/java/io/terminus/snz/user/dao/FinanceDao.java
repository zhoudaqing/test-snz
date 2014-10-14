package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.Finance;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午8:59
 */
@Repository
public class FinanceDao extends SqlSessionDaoSupport {

    /**
     * 创建财务
     */
    public Long create(Finance finance) {
        getSqlSession().insert("Finance.insert", finance);
        return finance.getId();
    }

    /**
     * 删除财务
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("Finance.delete", id) == 1;
    }

    /**
     * 更新财务
     */
    public boolean update(Finance finance) {
        return getSqlSession().update("Finance.update", finance) == 1;
    }

    /**
     * 根据Id查询财务
     */
    public Finance findById(Long id) {
        return getSqlSession().selectOne("Finance.findById", id);
    }

    /**
     * 根据user id查询财务
     */
    public Finance findByUserId(Long userId) {
        return getSqlSession().selectOne("Finance.findByUserId", userId);
    }
}
