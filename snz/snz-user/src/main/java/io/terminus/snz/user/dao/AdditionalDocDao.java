package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.AdditionalDoc;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-5-3-下午9:39
 */
@Repository
public class AdditionalDocDao extends SqlSessionDaoSupport {

    /**
     * 创建文档信息
     */
    public Long create(AdditionalDoc additionalDoc) {
        getSqlSession().insert("AdditionalDoc.insert", additionalDoc);
        return additionalDoc.getId();
    }

    /**
     * 删除文档信息
     */
    public boolean delete(Long id) {
        return getSqlSession().delete("AdditionalDoc.delete", id) == 1;
    }

    /**
     * 更新文档信息
     */
    public boolean update(AdditionalDoc additionalDoc) {
        return getSqlSession().update("AdditionalDoc.update", additionalDoc) == 1;
    }

    /**
     * 根据编号查询文档信息
     */
    public AdditionalDoc findById(Long id) {
        return getSqlSession().selectOne("AdditionalDoc.findById", id);
    }

    /**
     * 根据财务编号查询相关文档信息
     */
    public List<AdditionalDoc> findByFinanceId(Long financeId) {
        return getSqlSession().selectList("AdditionalDoc.findByFinanceId", financeId);
    }

}
