package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.CompanyMainBusinessTmp;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author:Guo Chaopeng
 * Created on 14-8-26.
 */
@Repository
public class CompanyMainBusinessTmpDao extends SqlSessionDaoSupport {

    public Long create(CompanyMainBusinessTmp companyMainBusinessTmp) {
        getSqlSession().insert("CompanyMainBusinessTmp.insert", companyMainBusinessTmp);
        return companyMainBusinessTmp.getId();
    }

    public void deleteByUserId(Long userId) {
        getSqlSession().delete("CompanyMainBusinessTmp.deleteByUserId", userId);
    }

    public List<CompanyMainBusinessTmp> findByUserId(Long userId) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findByUserId", userId);
    }

    public List<CompanyMainBusinessTmp> findByUserIds(List<Long> userIds) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findByUserIds", userIds);
    }

    public List<CompanyMainBusinessTmp> findByMainBusinessId(Long mainBusinessId) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findByMainBusinessId", mainBusinessId);
    }

    public List<CompanyMainBusinessTmp> findByCompanyId(Long companyId) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findByCompanyId", companyId);
    }

    /**
     * 根据业务id列表查询公司id列表
     *
     * @param bussinessIds 主营业务ids列表，即二级类目id列表
     * @return 对应的公司id列表
     */
    public List<CompanyMainBusinessTmp> findCompanyIdsByMainBusinessIds(List<Long> bussinessIds) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findCompanyIdsByMainBusinessIds", bussinessIds);
    }

    public Long countSupplierByFirstLevelId(Long firstLevelId) {
        Long count = getSqlSession().selectOne("CompanyMainBusinessTmp.countSupplierByFirstLevelId", firstLevelId);
        return count == null ? 0 : count;
    }

    public List<Long> findUserIdsByMainBusinessIds(List<Long> mainBussinessIds) {
        return getSqlSession().selectList("CompanyMainBusinessTmp.findUserIdsByMainBusinessIds", mainBussinessIds);
    }

}
