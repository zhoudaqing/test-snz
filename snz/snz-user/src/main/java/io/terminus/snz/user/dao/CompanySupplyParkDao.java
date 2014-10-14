package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.CompanySupplyPark;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-7-4.
 */
@Repository
public class CompanySupplyParkDao extends SqlSessionDaoSupport {

    /**
     * 创建可供货园区信息
     */
    public Long create(CompanySupplyPark companySupplyPark) {
        getSqlSession().insert("CompanySupplyPark.insert", companySupplyPark);
        return companySupplyPark.getId();
    }

    /**
     * 根据user id删除可供货园区信息
     */
    public void deleteByUserId(Long userId) {
        getSqlSession().delete("CompanySupplyPark.deleteByUserId", userId);
    }

    /**
     * 根据user id查询可供货园区信息
     */
    public List<CompanySupplyPark> findByUserId(Long userId) {
        return getSqlSession().selectList("CompanySupplyPark.findByUserId", userId);
    }

    /**
     * 根据company id查询可供货园区信息
     */
    public List<CompanySupplyPark> findByCompanyId(Long companyId) {
        return getSqlSession().selectList("CompanySupplyPark.findByCompanyId", companyId);
    }

    /**
     * 根据可供货园区编号统计供应商数量
     */
    public Long countBySupplyParkId(Long supplyParkId) {
        Long count = getSqlSession().selectOne("CompanySupplyPark.countBySupplyParkId", supplyParkId);
        return (count == null ? 0 : count);
    }

    /**
     * 在userIds范围内通过supplier park id查询user id
     */
    public List<Long> findUserIdsBySupplyParkId(Long supplyParkId, List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("supplyParkId", supplyParkId);
        params.put("list", userIds);

        return getSqlSession().selectList("CompanySupplyPark.findUserIdsBySupplyParkId", params);
    }

}
