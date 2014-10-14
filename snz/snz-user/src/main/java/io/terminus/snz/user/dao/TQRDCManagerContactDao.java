/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.TQRDCManagerContact;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 提供对表 `snz_tqrdc_manager_contacts`  的增删改查操作<BR>
 * <p/>
 * Created by wanggen 2014-09-11 21:09:51
 */
@Repository
public class TQRDCManagerContactDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "TQRDCManagerContact.";

    /**
     * 新增
     *
     * @param tQRDCManagerContact add bean
     * @return 新增后的自增序列号
     */
    public long create(TQRDCManagerContact tQRDCManagerContact) {
        getSqlSession().insert(NAMESPACE + "create", tQRDCManagerContact);
        return tQRDCManagerContact.getId();
    }


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 tQRDCManagerContact
     */
    public TQRDCManagerContact findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 更新操作
     *
     * @param tQRDCManagerContact 更新操作参数
     * @return 影响行数
     */
    public int update(TQRDCManagerContact tQRDCManagerContact) {
        return getSqlSession().update(NAMESPACE + "update", tQRDCManagerContact);
    }


    /**
     * 根据序列 id 删除记录
     *
     * @param ids 序列 id 列表
     * @return 删除行数
     */
    public int deleteByIds(List<Long> ids) {
        return getSqlSession().delete(NAMESPACE + "deleteByIds", ids);
    }


    /**
     * 查询出所有供应商绩效管理者联系方式
     * @return  供应商绩效管理者联系方式
     */
    public List<TQRDCManagerContact> listAll(){
        return getSqlSession().selectList(NAMESPACE+"listAll");
    }

}
