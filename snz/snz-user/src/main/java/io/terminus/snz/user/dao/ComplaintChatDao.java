/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */
package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.ComplaintChat;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 提供对表 `snz_complaint_chats`  的增删改查操作<BR>
 *
 * Created by wanggen 2014-09-20 13:44:55
 */
@Repository
public class ComplaintChatDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "ComplaintChat.";
    /**
     * 新增
     *
     * @param complaintChat add bean
     * @return 新增后的自增序列号
     */
    public long create(ComplaintChat complaintChat) {
        getSqlSession().insert(NAMESPACE + "create", complaintChat);
        return complaintChat.getId();
    }


    /**
     * 根据ID查询单条记录
     *
     * @return 返回的 complaintChat
     */
    public ComplaintChat findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }


    /**
     * 根据 id 列表查询多个结果集
	  *
     * @param ids 多个id
     * @return ComplaintChat 列表
     */
    public List<ComplaintChat> findByIds(List<Long> ids){
        return getSqlSession().selectList(NAMESPACE + "findByIds", ids);
    }


    /**
     * 分页查询
     *
     * @param param 查询参数
     * @return 分页查询结果
     */
    public Paging<ComplaintChat> findByPaging(Map<String, Object> param) {
        Integer total = getSqlSession().selectOne(NAMESPACE + "countBy", param);
        if (total == null || total == 0)
            return new Paging<ComplaintChat>(0L, Collections.<ComplaintChat>emptyList());
        List<ComplaintChat> complaintChatList = getSqlSession().selectList(NAMESPACE + "findBy", param);
        return new Paging<ComplaintChat>(total.longValue(), complaintChatList);
    }


    /**
     * 根据条件无分页查询
     *
     * @param param 查询条件
     * @return 结果集
     */
    public List<ComplaintChat> findAllBy(Map<String, Object> param) {
        return getSqlSession().selectList(NAMESPACE + "findBy", param);
    }


    /**
     * 根据 parentId 查询 ComplaintChat 列表
     *
     * @param parentId   抱怨信息ID
     * @return 结果列
     */
    public List<ComplaintChat> findByParentId(Long parentId){
        return getSqlSession().selectList(NAMESPACE+"findByParentId",parentId);
    }


    /**
     * 更新操作
     *
     * @param complaintChat 更新操作参数
     * @return 影响行数
     */
    public int update(ComplaintChat complaintChat) {
        return getSqlSession().update(NAMESPACE + "update", complaintChat);
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


}
