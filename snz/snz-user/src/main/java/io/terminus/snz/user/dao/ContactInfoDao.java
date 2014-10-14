package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.ContactInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午3:31
 */
@Repository
public class ContactInfoDao extends SqlSessionDaoSupport {

    /**
     * 创建联系人
     */
    public Long create(ContactInfo contactInfo) {
        getSqlSession().insert("ContactInfo.insert", contactInfo);
        return contactInfo.getId();
    }

    /**
     * 删除联系人
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("ContactInfo.delete", id) == 1;
    }

    /**
     * 更新联系人
     */
    public boolean update(ContactInfo contactInfo) {
        return getSqlSession().update("ContactInfo.update", contactInfo) == 1;
    }

    /**
     * 根据id查询联系人
     */
    public ContactInfo findById(Long id) {
        return getSqlSession().selectOne("ContactInfo.findById", id);
    }

    /**
     * 根据user id查询联系人
     */
    public ContactInfo findByUserId(Long userId) {
        return getSqlSession().selectOne("ContactInfo.findByUserId", userId);
    }

    /**
     * 在userIds范围内查询联系人
     */
    public List<ContactInfo> findByUserIds(List<Long> userIds) {
        return getSqlSession().selectList("ContactInfo.findByUserIds", userIds);
    }


}
