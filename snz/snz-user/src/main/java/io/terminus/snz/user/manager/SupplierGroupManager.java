package io.terminus.snz.user.manager;

import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Sets;
import io.terminus.snz.user.dao.SupplierGroupDao;
import io.terminus.snz.user.model.SupplierGroupRelation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.notNull;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
@Slf4j
@Component
public class SupplierGroupManager {

    @Autowired
    private SupplierGroupDao supplierGroupDao;

    private Long getGroupId(Long supplierId) {
        SupplierGroupRelation relation = supplierGroupDao.findOneBySupplierId(supplierId);
        if (isNull(relation)) {
            // 初始自成一组群
            Long neoGroupId = supplierGroupDao.getMaxGroupId() + 1;
            checkState(supplierGroupDao.create(supplierId, neoGroupId) > 0);
            return neoGroupId;
        }
        return relation.getGroupId();
    }

    public boolean hasRelation(Long alphaId, Long betaId) {
        return Objects.equal(getGroupId(alphaId), getGroupId(betaId));
    }

    @Transactional
    public boolean createRelation(Long alphaId, Long betaId) {
        // 不能跟自己关联
        checkArgument(alphaId.longValue() != betaId.longValue());

        Long groupA = getGroupId(alphaId);
        Long groupB = getGroupId(betaId);

        if (groupA < groupB) {
            return supplierGroupDao.updateGroupId(groupB, groupA);
        } else if (groupA > groupB) {
            return supplierGroupDao.updateGroupId(groupA, groupB);
        }
        return true;
    }

    @Transactional
    public boolean deleteRelation(Long alphaId, Long betaId) {
        // 不能跟自己关联
        checkArgument(alphaId.longValue() != betaId.longValue());

        if (hasRelation(alphaId, betaId)) {
            getGroupId(alphaId);
            SupplierGroupRelation relation = checkNotNull(supplierGroupDao.findOneBySupplierId(betaId));
            // 自成一组
            relation.setGroupId(supplierGroupDao.getMaxGroupId() + 1);
            return supplierGroupDao.update(relation);
        }
        return true;
    }

    public List<Long> getRelatedSupplierIds(Long alphaId) {
        List<SupplierGroupRelation> relations = supplierGroupDao.findByGroupId(getGroupId(alphaId));
        Set<Long> betaIds = Sets.newHashSet();
        for (SupplierGroupRelation relation : relations) {
            if (Objects.equal(alphaId, relation.getSupplierId())) {
                log.warn("{} not in {}'s circle, due to same one", alphaId, alphaId);
            } else {
                betaIds.add(relation.getSupplierId());
            }
        }
        return FluentIterable.from(betaIds).toList();
    }
}
