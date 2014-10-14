package io.terminus.snz.user.dao;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.terminus.common.model.Paging;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
@Repository
public class SupplierResourceMaterialInfoDao extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "SupplierResourceMaterialInfo.";

    public Long create(SupplierResourceMaterialInfo info1) {
        getSqlSession().insert(NAMESPACE + "create", info1);
        return info1.getId();
    }

    public boolean update(SupplierResourceMaterialInfo info1) {
        return getSqlSession().update(NAMESPACE + "update", info1) == 1;
    }

    public SupplierResourceMaterialInfo findById(Long id) {
        return getSqlSession().selectOne(NAMESPACE + "findById", id);
    }

    public SupplierResourceMaterialInfo findOneBySupplierId(Long supplierId) {
        return getSqlSession().selectOne(NAMESPACE + "findOneBy", ImmutableMap.of("supplierId", supplierId));
    }

    public List<SupplierResourceMaterialInfo> findBy(Integer status, Date startAt, Date endAt) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("status", status);
        params.put("startAt", startAt);
        params.put("endAt", endAt);
        return getSqlSession().selectList(NAMESPACE + "findBy", params);
    }

    public Paging<SupplierResourceMaterialInfo> pagingBy(SupplierResourceMaterialInfo criteria, Integer offset, Integer limit) {
//        Map<String, Object> params = JsonMapper.nonEmptyMapper().getMapper().convertValue(criteria, Map.class);
        Map<String, Object> params = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(criteria.getSupplierName())) {
            params.put("supplierName", Strings.emptyToNull(criteria.getSupplierName()));
        }
        params.put("status", Objects.firstNonNull(criteria.getStatus(), SupplierResourceMaterialInfo.Status.SUBMITTED.value()));
        params.put("offset", offset);
        params.put("limit", limit);

        Long count = getSqlSession().selectOne(NAMESPACE + "countBy", params);
        if (count == 0L) {
            return new Paging<SupplierResourceMaterialInfo>(0L, Collections.<SupplierResourceMaterialInfo>emptyList());
        }
        List<SupplierResourceMaterialInfo> infos = getSqlSession().selectList(NAMESPACE + "pagingBy", params);
        return new Paging<SupplierResourceMaterialInfo>(count, infos);
    }
}
