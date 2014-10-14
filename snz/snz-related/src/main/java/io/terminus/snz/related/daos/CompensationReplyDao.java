package io.terminus.snz.related.daos;

import com.google.common.collect.ImmutableMap;
import io.terminus.common.model.PageInfo;
import io.terminus.common.model.Paging;
import io.terminus.snz.related.models.CompensationReply;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Author:  wenhaoli
 * Date: 2014-08-11
 */
@Repository
public class CompensationReplyDao extends SqlSessionDaoSupport {

    /**
     * 根据回复id查找回复
     * @param id
     * @return 对应回复
     * */
    public CompensationReply findById(Long id){
        return getSqlSession().selectOne("CompensationReply.findById", id);
    }

    /**
     * 根据索赔记录id查找索赔记录
     * @param re 回复对象
     * @return 对应索赔记录的id
     * */
    public Long findDetailById(CompensationReply re) {
        return getSqlSession().selectOne("CompensationReply.findDetailById", re);
    }

    /**
     * 创建回复
     * @param  resp 回复对象
     * @return 回复对象的id
     * */
    public Long create(CompensationReply resp){
        getSqlSession().insert("CompensationReply.create", resp);
        return resp.getId();
    }

    /**
     * 根据索赔记录id查找回复
     * @param listId 索赔记录id
     * @return replys 多个回复对象
     * */
    public List<CompensationReply> findByDid(Long listId){
        return getSqlSession().selectList("CompensationReply.findByDid", listId);
    }

    /**
     *根据索赔记录id删除回复
     * @param listId 索赔记录id
     * @return Boolean 删除是否成功
     * */
    public Boolean deleteByDid(Long listId){
        getSqlSession().delete("CompensationReply.deleteByDid", listId);
        return Boolean.TRUE;
    }

    /**
     * 分页获取目标索赔记录相关的所有回复信息
     * @param listId 索赔记录id
     * @param pageNo 起始偏移
     * @param pageSize 分页大小
     * @return 索赔交互回话页
     * */
    public Paging<CompensationReply> allPagingForDid (Long listId, Integer pageNo, Integer pageSize) {

        long num = getSqlSession().selectList("CompensationReply.findByDid", listId).size();
        Long count = num;
        if(count == 0) {
            return new Paging<CompensationReply>(0L, Collections.<CompensationReply>emptyList());
        }
        PageInfo page = new PageInfo(pageNo,pageSize);
        List<CompensationReply> replyList = getSqlSession().selectList("CompensationReply.findAllReply",
                ImmutableMap.of("listId", listId, "offset", page.getOffset(), "size", page.getLimit()));
        return new Paging<CompensationReply>(count,replyList);
    }

 }
