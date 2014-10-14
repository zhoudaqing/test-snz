package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierApproveLog;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-22.
 */
@Repository
public class SupplierApproveLogDao extends SqlSessionDaoSupport {

    /**
     * 创建审核日志
     */
    public Long create(SupplierApproveLog supplierApproveLog) {
        getSqlSession().insert("SupplierApproveLog.insert", supplierApproveLog);
        return supplierApproveLog.getId();
    }

    /**
     * 更新审核日志
     */
    public boolean update(SupplierApproveLog supplierApproveLog) {
        return getSqlSession().update("SupplierApproveLog.update", supplierApproveLog) == 1;
    }

    /**
     * 根据user id查询审核日志
     */
    public SupplierApproveLog findByUserId(Long userId) {
        return getSqlSession().selectOne("SupplierApproveLog.findByUserId", userId);
    }

    /**
     * 删除审核日志
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("SupplierApproveLog.delete", id) == 1;
    }

    public SupplierApproveLog findLastByUserIdAndApproveType(Long userId, Integer approveType) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        params.put("approveType", approveType);
        return getSqlSession().selectOne("SupplierApproveLog.findLastByUserIdAndApproveType", params);
    }

}
