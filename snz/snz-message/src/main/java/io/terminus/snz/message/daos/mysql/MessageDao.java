package io.terminus.snz.message.daos.mysql;

import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.message.models.Message;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-6-7
 */
@Repository
public class MessageDao extends SqlSessionDaoSupport {
    /**
     * 创建消息
     * @param m 消息对象
     * @return 创建记录数
     */
    public Integer create(Message m){
        return getSqlSession().insert("Message.create", m);
    }

    /**
     * 删除消息
     * @param id 消息id
     * @return 删除记录数
     */
    public Integer delete(Long id){
        return getSqlSession().delete("Message.delete", id);
    }

    /**
     * 批量删除消息
     * @param ids 消息id列表
     * @return 删除记录数
     */
    public Integer deletes(List<Long> ids){
        return getSqlSession().delete("Message.deletes", ids);
    }

    /**
     * 获取消息
     * @param id 消息主健
     * @return 消息对象
     */
    public Message findById(Long id){
        return getSqlSession().selectOne("Message.findById", id);
    }

    /**
     * 获取消息列表
     * @param ids 消息主健列表
     * @return 消息列表
     */
    public List<Message> findByIds(List<Long> ids){
        return getSqlSession().selectList("Message.findByIds", ids);
    }

    /**
     * 分页查找
     * @param criteria 查询条件
     * @param offset 起始偏移
     * @param limit 分页大小
     * @return 消息分页对象
     */
    public Paging<Message> paging(Map<String, Object> criteria, Integer offset, Integer limit){
        Map<String, Object> pagingParam = Maps.newHashMap();
        if (criteria != null){
            pagingParam.putAll(criteria);
        }

        Long total = getSqlSession().selectOne("Message.count", pagingParam);
        if(total == 0) {
            return new Paging<Message>(0L, Collections.<Message>emptyList());
        }

        pagingParam.put("offset", offset);
        pagingParam.put("limit", limit);
        List<Message> data = getSqlSession().selectList("Message.pagination", pagingParam);
        return new Paging<Message>(total, data);
    }
}
