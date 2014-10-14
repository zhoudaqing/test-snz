package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierCountByStatus;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
@Repository
public class SupplierCountByStatusDao extends SqlSessionDaoSupport {

    /**
     * 创建统计信息
     */
    public Long create(SupplierCountByStatus supplierCountByStatus) {
        getSqlSession().insert("SupplierCountByStatus.insert", supplierCountByStatus);
        return supplierCountByStatus.getId();
    }

    /**
     * 查询最近limit条统计信息
     */
    public List<SupplierCountByStatus> findLastBy(Integer status, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", status);
        params.put("limit", limit);

        return getSqlSession().selectList("SupplierCountByStatus.findLastBy", params);
    }

}
