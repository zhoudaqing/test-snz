package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierCountBySupplyPark;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
@Repository
public class SupplierCountBySupplyParkDao extends SqlSessionDaoSupport {

    /**
     * 创建统计信息
     */
    public Long create(SupplierCountBySupplyPark supplierCountBySupplyPark) {
        getSqlSession().insert("SupplierCountBySupplyPark.insert", supplierCountBySupplyPark);
        return supplierCountBySupplyPark.getId();
    }

    /**
     * 查询最近limit条统计信息
     */
    public List<SupplierCountBySupplyPark> findLastBy(Long supplyParkId, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplyParkId", supplyParkId);
        params.put("limit", limit);

        return getSqlSession().selectList("SupplierCountBySupplyPark.findLastBy", params);
    }

}
