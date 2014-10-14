package io.terminus.snz.user.dao;

import io.terminus.snz.user.model.BlackList;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description：
 * Author：Guo Chaopeng
 * Created on 14-6-23
 */
@Repository
public class BlackListDao extends SqlSessionDaoSupport {

    /**
     * 创建黑名单
     */
    public Long create(BlackList blackList) {
        getSqlSession().insert("BlackList.insert", blackList);
        return blackList.getId();
    }

    /**
     * 根据id查询黑名单信息
     */
    public BlackList findById(Long id) {
        return getSqlSession().selectOne("BlackList.findById", id);
    }

    /**
     * 查询所有黑名单信息
     */
    public List<BlackList> findAll() {
        return getSqlSession().selectList("BlackList.findAll");
    }

    /**
     * 根据名称查询黑名单信息
     */
    public BlackList findByName(String name) {
        return getSqlSession().selectOne("BlackList.findByName", name);
    }

}
