package io.terminus.snz.user.dao;

import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierCreditQualify;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * todo: test
 * Date: 7/31/14
 * Time: 12:19
 * Author: 2014年 <a href="mailto:d@terminus.io">张成栋</a>
 */

@Repository
public class SupplierCreditQualifyDao extends SqlSessionDaoSupport {
    public Long create(SupplierCreditQualify insert) {
        getSqlSession().insert("SupplierCreditQualify.create", insert);
        return insert.getId();
    }

    public Boolean update(SupplierCreditQualify update) {
        return getSqlSession().update("SupplierCreditQualify.update", update) == 1;
    }

    public SupplierCreditQualify findById(Long id) {
        return getSqlSession().selectOne("SupplierCreditQualify.findById", id);
    }

    public SupplierCreditQualify findBy(SupplierCreditQualify params) {
        return getSqlSession().selectOne("SupplierCreditQualify.findBy", params);
    }

    public SupplierCreditQualify findByUserId(Long id) {
        return getSqlSession().selectOne("SupplierCreditQualify.findByUserId", id);
    }

    public List<SupplierCreditQualify> findByUserIds(List<Long> userIds) {
        return getSqlSession().selectList("SupplierCreditQualify.findByUserIds", userIds);
    }

    public Paging<SupplierCreditQualify> pagingForQualify(Integer type, Integer offset, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("type", type);
        params.put("offset", offset);
        params.put("limit", limit);

        Long count = getSqlSession().selectOne("SupplierCreditQualify.countForQualify", params);
        if (count==0l) {
            return new Paging<SupplierCreditQualify>(0l, Collections.<SupplierCreditQualify>emptyList());
        }
        List<SupplierCreditQualify> supplierCreditQualifies =
                getSqlSession().selectList("SupplierCreditQualify.pagingForQualify", params);
        return new Paging<SupplierCreditQualify>(count, supplierCreditQualifies);
    }

    public Paging<SupplierCreditQualify> upCommingIn(Date startAt, Date endAt, Integer offset, Integer limit) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("startAt", startAt);
        params.put("endAt", endAt);
        params.put("offset", offset);
        params.put("limit", limit);

        Long count = getSqlSession().selectOne("SupplierCreditQualify.countUpComingIn", params);
        if (count==0) {
            return new Paging<SupplierCreditQualify>(0l, Collections.<SupplierCreditQualify>emptyList());
        }
        List<SupplierCreditQualify> supplierCreditQualifies =
                getSqlSession().selectList("SupplierCreditQualify.pagingForUpComingIn", params);
        return new Paging<SupplierCreditQualify>(count, supplierCreditQualifies);
    }
}
