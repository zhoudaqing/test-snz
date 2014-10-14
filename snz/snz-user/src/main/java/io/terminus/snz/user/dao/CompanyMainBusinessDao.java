package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.snz.user.model.CompanyMainBusiness;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Guo Chaopeng
 * Created on 14-5-8.
 */
@Repository
public class CompanyMainBusinessDao extends SqlSessionDaoSupport {

    /**
     * 创建主营业务
     */
    public Long create(CompanyMainBusiness companyMainBusiness) {
        getSqlSession().insert("CompanyMainBusiness.insert", companyMainBusiness);
        return companyMainBusiness.getId();
    }

    /**
     * 根据user id删除主营业务信息
     */
    public void deleteByUserId(Long userId) {
        getSqlSession().delete("CompanyMainBusiness.deleteByUserId", userId);
    }

    /**
     * 根据user id查询主营业务信息
     */
    public List<CompanyMainBusiness> findByUserId(Long userId) {
        return getSqlSession().selectList("CompanyMainBusiness.findByUserId", userId);
    }

    /**
     * 在userIds范围内查询主营业务信息
     */
    public List<CompanyMainBusiness> findByUserIds(List<Long> userIds) {
        return getSqlSession().selectList("CompanyMainBusiness.findByUserIds", userIds);
    }

    /**
     * 根据主营业务编号查询主营业务信息
     */
    public List<CompanyMainBusiness> findByMainBusinessId(Long mainBusinessId) {
        return getSqlSession().selectList("CompanyMainBusiness.findByMainBusinessId", mainBusinessId);
    }

    /**
     * 根据公司编号查询主营业务信息
     */
    public List<CompanyMainBusiness> findByCompanyId(Long companyId) {
        return getSqlSession().selectList("CompanyMainBusiness.findByCompanyId", companyId);
    }

    /**
     * 根据业务id列表查询公司id列表
     *
     * @param bussinessIds 主营业务ids列表，即二级类目id列表
     * @return 对应的公司id列表
     */
    public List<CompanyMainBusiness> findCompanyIdsByMainBusinessIds(List<Long> bussinessIds) {
        return getSqlSession().selectList("CompanyMainBusiness.findCompanyIdsByMainBusinessIds", bussinessIds);
    }

    /**
     * 更加一级类目编号统计供应商数量
     */
    public Long countSupplierByFirstLevelId(Long firstLevelId) {
        Long count = getSqlSession().selectOne("CompanyMainBusiness.countSupplierByFirstLevelId", firstLevelId);
        return count == null ? 0 : count;
    }

    /**
     * 在mainBusinessIds范围内查询供应商编号
     */
    public List<Long> findUserIdsByMainBusinessIds(List<Long> mainBussinessIds) {
        return getSqlSession().selectList("CompanyMainBusiness.findUserIdsByMainBusinessIds", mainBussinessIds);
    }

    public List<Long> findUserIdsByFirstLevelId(Long firstLevelId) {
        return getSqlSession().selectList("CompanyMainBusiness.findUserIdsByFirstLevelId", firstLevelId);
    }

    /**
     * 根据类目查询供应商编号
     */
    public List<Long> findUserIdsByFcIds(Long firstLevelId, Long mainBusinessId, List<Long> mainBusinessIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("firstLevelId", firstLevelId);
        params.put("mainBusinessId", mainBusinessId);
        params.put("list", mainBusinessIds);

        return getSqlSession().selectList("CompanyMainBusiness.findUserIdsByFcIds", params);
    }

}
