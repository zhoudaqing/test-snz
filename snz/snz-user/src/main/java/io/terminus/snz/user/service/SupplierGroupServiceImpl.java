package io.terminus.snz.user.service;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.manager.SupplierGroupManager;
import io.terminus.snz.user.model.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.terminus.common.utils.Arguments.isNull;
import static io.terminus.common.utils.Arguments.positive;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 9/15/14
 */
@Slf4j
@Service
public class SupplierGroupServiceImpl implements SupplierGroupService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SupplierGroupManager supplierGroupManager;

    @Override
    public Response<Boolean> createRelation(BaseUser baseUser, Long alphaId, Long betaId) {
        Response<Boolean> result = new Response<Boolean>();
        if (isNull(baseUser)) {
            log.error("login first");
            result.setError("user.not.login");
            return result;
        }
        try {
            if ((isNull(alphaId) || !positive(alphaId))
                    ||(isNull(betaId) || !positive(betaId))) {
                log.error("wrong params");
                result.setError("illegal.argument");
                return result;
            }
            if (supplierGroupManager.hasRelation(alphaId, betaId)) {
                log.warn("{} and {} already in one circle, no need create relation", alphaId, betaId);
                result.setResult(Boolean.TRUE);
            } else {
                result.setResult(supplierGroupManager.createRelation(alphaId, betaId));
            }
        } catch (Exception e) {
            log.error("createRelation(baseUser={}, alphaId={}, betaId={}) failed, cause:{}",
                    baseUser, alphaId, betaId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.group.relation.create.fail");
        }
        return result;
    }

    @Override
    public Response<Boolean> deleteRelation(BaseUser baseUser, Long alphaId, Long betaId) {
        Response<Boolean> result = new Response<Boolean>();
        try {
            if ((isNull(alphaId) || !positive(alphaId))
                    ||(isNull(betaId) || !positive(betaId))) {
                log.error("wrong params");
                result.setError("illegal.argument");
                return result;
            }
            if (!supplierGroupManager.hasRelation(alphaId, betaId)) {
                log.warn("{} and {} does not in one circle, no need delete relation", alphaId, betaId);
                result.setResult(Boolean.TRUE);
            } else {
                result.setResult(supplierGroupManager.deleteRelation(alphaId, betaId));
            }
        } catch (Exception e) {
            log.error("deleteRelation(baseUser={}, alphaId={}, betaId={}) failed, cause:{}",
                    baseUser, alphaId, betaId, Throwables.getStackTraceAsString(e));
            result.setError("supplier.group.relation.delete.fail");
        }
        return result;
    }

    @Override
    public Response<List<Company>> getRelatedSuppliers(BaseUser baseUser, Long alphaId) {
        Response<List<Company>> result = new Response<List<Company>>();
        if (isNull(baseUser)) {
            log.error("login first");
            result.setError("user.not.login");
            return result;
        }
        try {
            if (isNull(alphaId) || !positive(alphaId)) {
                log.error("alphaId={} illegal param");
                result.setError("supplier.group.illegal.supplier");
                return result;
            }
            List<Long> supplierIds = supplierGroupManager.getRelatedSupplierIds(alphaId);
            List<Company> companies = Lists.newArrayList();
            for (Long supplierId : supplierIds) {
                Response<Company> companyResp = companyService.findCompanyById(supplierId);
                if (!companyResp.isSuccess()) {
                    log.error("find supplier(id={}) failed", supplierId);
                    continue;
                }
                companies.add(companyResp.getResult());
            }
            result.setResult(companies);
        } catch (Exception e) {
            log.error("getRelatedSuppliers(baseUser={}) failed, cause:{}",
                    baseUser, Throwables.getStackTraceAsString(e));
            result.setError("supplier.group.relation.query.fail");
        }
        return result;
    }
}
