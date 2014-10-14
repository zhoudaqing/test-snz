package io.terminus.snz.user.dao;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.Company;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午5:39
 */
@Repository
public class CompanyDao extends SqlSessionDaoSupport {

    /**
     * 创建公司
     */
    public Long create(Company company) {
        getSqlSession().insert("Company.insert", company);
        return company.getId();
    }

    /**
     * 删除公司
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("Company.delete", id) == 1;
    }

    /**
     * 更新公司
     */
    public boolean update(Company company) {
        return getSqlSession().update("Company.update", company) == 1;
    }

    /**
     * 查询公司
     */
    public Company findById(Long id) {
        return getSqlSession().selectOne("Company.findById", id);
    }

    /**
     * 查询公司
     */
    public List<Company> findByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList("Company.findByIds", ids);
    }

    /**
     * 查询公司
     */
    public List<Company> findByCorporation(String corporation) {
        if (Strings.isNullOrEmpty(corporation)) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList("Company.findByCorporation", corporation.trim());
    }

    /**
     * 查询公司
     */
    public Company findByUserId(Long userId) {
        return getSqlSession().selectOne("Company.findByUserId", userId);
    }

    /**
     * 更新公司
     */
    public boolean updateParticipateCount(Long id) {
        return getSqlSession().update("Company.updateParticipateCount", id) == 1;
    }

    /**
     * 分页查询公司
     */
    public Paging<Company> findSuppliersForQualify(String corporation, Integer stage, Integer offset, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        if (corporation != null && !corporation.isEmpty()) {
            params.put("corporation", corporation);
        }
        params.put("stage", stage);
        params.put("offset", offset);
        params.put("limit", limit);

        Long count = getSqlSession().selectOne("countSuppliersForQualify", params);
        if (count == 0l) {
            return new Paging<Company>(0L, Collections.<Company>emptyList());
        }
        List<Company> companies = getSqlSession().selectList("findSuppliersForQualify", params);
        return new Paging<Company>(count, companies);
    }

    /**
     * 公司计数
     */
    public Long countSupplier() {
        Long count = getSqlSession().selectOne("Company.countSupplier");
        return count == null ? 0 : count;
    }

    /**
     * 公司计数(参与的)
     */
    public Long countParticipatedSupplier() {
        Long count = getSqlSession().selectOne("Company.countParticipatedSupplier");
        return count == null ? 0 : count;
    }

    public Long countSupplierHasSupplierCode() {
        Long count = getSqlSession().selectOne("Company.countSupplierHasSupplierCode");
        return count == null ? 0 : count;
    }

    /**
     * 根据供应商 Code 查询
     *
     * @param supplierCode 供应商代码，snz_companies 表应该保证 supplierCode 唯一
     * @return 查询到的唯一供应商
     */
    public Company findBySupplierCode(String supplierCode) {
        List<Company> companies = getSqlSession().selectList("Company.findBySupplierCode", supplierCode);
        if (companies != null && companies.size() > 0) {
            return companies.get(0);
        }
        return null;
    }


    /**
     * 根据供应商名称查询公司信息
     *
     * @param supplierName 供应商 V 码
     */
    public List<Company> findCompanyBySupplierName(String supplierName) {
        return getSqlSession().selectList("Company.findCompanyBySupplierName", supplierName);
    }

    /**
     * 查找老供应商并分页
     *
     * @param companyName 按名字查找供应商
     * @param offset      查找的便宜
     * @param size        查找的分页的大小
     * @return 公司的分页
     */
    public Paging<Company> pagingCompanyHasSupplierCode(String companyName, Integer offset, Integer size) {
        Long count = getSqlSession().selectOne("Company.countSupplierHasSupplierCode");
        if (count == 0l) {
            return new Paging<Company>(0l, Lists.<Company>newArrayList());
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("companyName", companyName);
        params.put("offset", offset);
        params.put("limit", size);
        return new Paging<Company>(count, getSqlSession().<Company>selectList("Company.pagingCompanyHasSupplierCode", params));
    }

    public List<Company> findCompanyHasVCode(Map param) {
        return getSqlSession().selectList("Company.findCompanyHasVCode", param);
    }

    /**
     * 在userIds范围内通过公司名称模糊搜索user id
     */
    public List<Long> findUserIdsByFuzzyCorporation(String corporation, List<Long> userIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("corporation", corporation);
        params.put("list", userIds);
        return getSqlSession().selectList("Company.findUserIdsByFuzzyCorporation", params);
    }
}
