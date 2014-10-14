package io.terminus.snz.requirement.service.mock;

import io.terminus.common.model.Paging;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRichRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialRichInfoDto;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import io.terminus.snz.user.model.SupplierResourceMaterialSubject;
import io.terminus.snz.user.service.SupplierResourceMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-09-15.
 */
@Service
public class SupplierResourceMaterialServiceMock implements SupplierResourceMaterialService {
    @Override
    public Response<SupplierResourceMaterialInfo> getInfo(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<SupplierResourceMaterialInfo> getInfoByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<Integer> getInfoInBcIds(Long supplierId, List<Long> bcIds) {
        return null;
    }

    @Override
    public Response<List<Long>> getApprovedBcIds(Long supplierId) {
        return null;
    }

    @Override
    public Response<Map<Long, List<Long>>> bulkGetApprovedBcIds(List<Long> supplierIds) {
        return null;
    }

    @Override
    public Response<Boolean> forceApprove(Long checkerId, Long supplierId, List<Long> bcIds) {
        return null;
    }

    @Override
    public Response<Boolean> forceApproveAll(Long checkerId, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> applyFor(Long supplierId, String modules, Integer type) {
        return null;
    }

    @Override
    public Response<List<SupplierResourceMaterialSubject>> getSubjectsNeedQualify(BaseUser baseUser, Long supplierId) {
        return null;
    }

    @Override
    public Response<List<SupplierResourceMaterialLogRichRecordDto>> getQualifiedSubjects(BaseUser baseUser, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> qualifyByChecker(BaseUser baseUser, String details) {
        return null;
    }

    @Override
    public Response<Boolean> askForReject(BaseUser baseUser, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> rejectByHigherChecker(BaseUser baseUser, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> inviteAnotherToCheck(BaseUser baseUser, String nick, String role, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> bulkInviteAnotherToCheck(BaseUser baseUser, String nicks, String role, Long supplierId) {
        return null;
    }

    @Override
    public Response<List<io.terminus.snz.category.model.BackendCategory>> getBcsCanInvite(BaseUser baseUser, Long supplierId) {
        return null;
    }

    @Override
    public Response<Boolean> inviteAnotherToCheckWithBcs(BaseUser baseUser, String nick, String role, List<Long> bcIds, Long supplierId) {
        return null;
    }

    @Override
    public Response<Paging<SupplierResourceMaterialRichInfoDto>> pagingBy(BaseUser baseUser, String supplierName, Integer status, Integer pageNo, Integer size) {
        return null;
    }

    @Override
    public Response<Boolean> bulkSendMessages() {
        return null;
    }

    @Override
    public Response<Long> bulkFailSuppliers() {
        return null;
    }

    @Override
    public Response<List<SupplierResourceMaterialSubject>> getAllValidSubjects() {
        return null;
    }

    @Override
    public Response<Boolean> bulkOverwriteSubjects(BaseUser baseUser, String subjects) {
        return null;
    }
}
