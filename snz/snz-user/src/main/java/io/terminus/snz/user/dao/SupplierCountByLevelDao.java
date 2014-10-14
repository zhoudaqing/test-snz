package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierCountByLevel;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-23.
 */
@Repository
public class SupplierCountByLevelDao extends SqlSessionDaoSupport {

    /**
     * 创建统计信息
     */
    public Long create(SupplierCountByLevel supplierCountByLevel) {
        getSqlSession().insert("SupplierCountByLevel.insert", supplierCountByLevel);
        return supplierCountByLevel.getId();
    }

    /**
     * 查询最近limit条统计信息
     */
    public List<SupplierCountByLevel> findLastBy(Integer level, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("level", level);
        params.put("limit", limit);

        return getSqlSession().selectList("SupplierCountByLevel.findLastBy", params);
    }

    public Integer countByMonth(String month) {
        Integer count = getSqlSession().selectOne("SupplierCountByLevel.countByMonth", month);
        return count == null ? 0 : count;

    }

}
