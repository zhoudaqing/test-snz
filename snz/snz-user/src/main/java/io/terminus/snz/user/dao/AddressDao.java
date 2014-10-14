package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.Address;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-6
 */
@Repository
public class AddressDao extends SqlSessionDaoSupport {

    /**
     * 根据id查询地区信息
     */
    public Address findById(Integer id) {
        return getSqlSession().selectOne("Address.findById", id);
    }

    /**
     * 根据上级id查询其所有下级地区信息
     */
    public List<Address> findByParentId(Integer parentId) {
        return getSqlSession().selectList("Address.findByParentId", parentId);
    }

    /**
     * 根据地区级别查询地区信息
     */
    public List<Address> findByLevel(Integer level) {
        return getSqlSession().selectList("Address.findByLevel", level);
    }

    /**
     * 创建地区信息
     */
    public Integer create(Address address) {
        getSqlSession().insert("Address.create", address);
        return address.getId();
    }
}
