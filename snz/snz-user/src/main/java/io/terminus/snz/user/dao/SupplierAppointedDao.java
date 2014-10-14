package io.terminus.snz.user.dao;

import com.google.common.collect.ImmutableMap;
import io.terminus.snz.user.model.SupplierAppointed;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Author:Grancy Guo
 * Created on 14-9-16.
 */
@Repository
public class SupplierAppointedDao extends SqlSessionDaoSupport{

    /**
     * 创建甲指库
     */
    public Long create(SupplierAppointed supplierAppointed) {
        getSqlSession().insert("SupplierAppointed.insert", supplierAppointed);
        return supplierAppointed.getId();
    }

    /**
     * 获得满足条件的甲指供应商总数
     */
    public Long countSupplierAppointed(Map<String, Object> params) {
        //分页总数量
        return getSqlSession().selectOne("SupplierAppointed.countSupplierAppointed", params);
    }

    /**
     * 获得满足条件的甲指供应商信息
     */
    public List<SupplierAppointed> findSupplierAppiontedByParams(Map<String, Object> params){
        return getSqlSession().selectList("SupplierAppointed.findSupplierAppiontedByParams",params);
    }

    /**
     * 根据条件审核确认或者取消确认某条甲指供应商记录
     */
    public Boolean updateStatus(Map<String, Object> params){
        return getSqlSession().update("SupplierAppointed.updateStatus", params) == 1;
    }

    /**
     * 根据id查询供应商公司
     */
    public SupplierAppointed findCompanyById(Long id){
        return getSqlSession().selectOne("SupplierAppointed.findCompanyById", id);
    }

    /**
     * 根据需求id查询供应商公司
     */
    public SupplierAppointed findCompany(Long requirementId){
        return getSqlSession().selectOne("SupplierAppointed.findCompany", requirementId);
    }

    /**
     * 根据公司名称,需求ID查询甲指供应商公司
     */
    public SupplierAppointed findCompanyByCorp(String corporation, Long requirementId){
        return getSqlSession().selectOne("SupplierAppointed.findCompanyByCorp", ImmutableMap.of("corporation", corporation, "requirementId", requirementId));
    }

    /**
     * 根据公司名称更新甲指库供应商companyId
     */
    public boolean updateCompanyIdByCorp(Long companyId, String corporation){
        return getSqlSession().update("SupplierAppointed.updateCompanyIdByCorp", ImmutableMap.of("companyId", companyId, "corporation", corporation)) == 1;
    }

    /**
     * 根据需求ID查询甲指供应商公司
     */
    public SupplierAppointed findCompanyByReq(Long requirementId){
        return getSqlSession().selectOne("SupplierAppointed.findCompanyByReq", requirementId);
    }

    /**
     * 根据需求ID删除甲指供应商公司
     */
    public boolean deleteCompanyByReq(Long requirementId){
        return getSqlSession().delete("SupplierAppointed.deleteCompanyByReq", requirementId) == 1;
    }

}
