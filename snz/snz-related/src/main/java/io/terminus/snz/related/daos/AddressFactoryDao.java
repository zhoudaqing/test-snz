package io.terminus.snz.related.daos;

import io.terminus.snz.related.models.AddressFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:工厂信息数据
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
@Repository
public class AddressFactoryDao extends SqlSessionDaoSupport {
    /**
     * 通过园区编号集合返回工厂信息
     * @param factoryIds   工厂编号集合
     * @return  List
     * 返回工厂信息列表
     */
    public List<AddressFactory> findByFactoryIds(List<Long> factoryIds){
        return getSqlSession().selectList("AddressFactory.findByFactoryIds" , factoryIds);
    }

    /**
     * 获取全部的工厂信息
     * @return  List
     * 返回工厂信息
     */
    public List<AddressFactory> findAllFactory(){
        return getSqlSession().selectList("AddressFactory.findAllFactory");
    }
}
