package io.terminus.snz.user.dao;

import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierResource;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail: houly@terminus.io
 * author: Hou Luyao
 * Date: 14-9-1.
 */
@Repository
public class SupplierResourceDao extends SqlSessionDaoSupport {
    private static final String NAMESPACE = "SupplierResource.";


    public Paging<SupplierResource> findByPaging(Map<String, Object> param) {
        Integer total = getSqlSession().selectOne(NAMESPACE + "countBy", param);
        if (total == null || total == 0)
            return new Paging<SupplierResource>(0L, Collections.<SupplierResource>emptyList());
        if (param.get("offset") == null)
            param.put("offset", 0);
        if (param.get("limit") == null)
            param.put("limit", total);
        List<SupplierResource> userComplaintList = getSqlSession().selectList(NAMESPACE + "findBy", param);
        return new Paging<SupplierResource>(total.longValue(), userComplaintList);
    }
}

