package io.terminus.snz.related.daos;

import io.terminus.snz.related.models.AddressPark;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Desc:园区查询处理
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-06-26.
 */
@Repository
public class AddressParkDao extends SqlSessionDaoSupport {
    /**
     * 查询全部的园区信息
     *
     * @return List
     * 返回全部的园区信息
     */
    public List<AddressPark> findAllPark() {
        return getSqlSession().selectList("AddressPark.findAllPark");
    }

    /**
     * 根据产品编号获取全部的园区信息
     *
     * @param productId 产品编号
     * @return List
     * 返回园区信息
     */
    public List<AddressPark> findParkByProductId(Long productId) {
        return getSqlSession().selectList("AddressPark.findByProductId", productId);
    }

    public List<AddressPark> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList("AddressPark.findByIds", ids);
    }

}
