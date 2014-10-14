package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.SupplierCountByIndustry;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-30.
 */
@Repository
public class SupplierCountByIndustryDao extends SqlSessionDaoSupport {

    /**
     * 创建统计信息
     */
    public Long create(SupplierCountByIndustry supplierCountByIndustry) {
        getSqlSession().insert("SupplierCountByIndustry.insert", supplierCountByIndustry);
        return supplierCountByIndustry.getId();
    }

    /**
     * 查询最近limit条统计信息
     */
    public List<SupplierCountByIndustry> findLastBy(Long industry, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("industry", industry);
        params.put("limit", limit);

        return getSqlSession().selectList("SupplierCountByIndustry.findLastBy", params);
    }

}
