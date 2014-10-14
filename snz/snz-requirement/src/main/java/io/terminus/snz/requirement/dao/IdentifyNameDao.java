package io.terminus.snz.requirement.dao;

import io.terminus.snz.requirement.model.IdentifyName;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:认证信息处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-14.
 */
@Repository
public class IdentifyNameDao extends SqlSessionDaoSupport {
    /**
     * 获取所有的认证信息内容
     * @return List
     * 返回所有的认证信息列表
     */
    public List<IdentifyName> findAllName(){
        return getSqlSession().selectList("IdentifyName.findAllName");
    }
}
