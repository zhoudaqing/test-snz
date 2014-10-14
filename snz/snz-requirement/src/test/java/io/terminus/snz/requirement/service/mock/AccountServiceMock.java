package io.terminus.snz.requirement.service.mock;

import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.user.dto.*;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Desc:
 * Mail:v@terminus.io
 * author:Michael Zhao
 * Date:2014-07-30.
 */
@Service
public class AccountServiceMock implements AccountService {
    @Override
    public Response findUserById(Long id) {
        return null;
    }

    @Override
    public Response findUserByNick(String nick) {
        return null;
    }

    @Override
    public Response<Boolean> updateUser(BaseUser user) {
        return null;
    }

    @Override
    public Response<List> findUserByRoleStr(String role) {
        return null;
    }

    @Override
    public Response<UserDto> login(String nick, String password) {
        return null;
    }

    @Override
    public Response<UserDto> dirtyLogin(String nick, String password) {
        return null;
    }

    @Override
    public Response<Boolean> logout(String ssoSessionId) {
        return null;
    }

    @Override
    public Response<Boolean> userExists(String nick) {
        return null;
    }

    @Override
    public Response<Boolean> changePassword(BaseUser baseUser, String oldPassword, String newPassword, String confirmPassword) {
        return null;
    }

    @Override
    public Response<Boolean> updateStatus(Long id, Integer status) {
        return null;
    }

    @Override
    public Response<PurchaserDto> findPurchaserById(Long id) {
        return null;
    }

    @Override
    public Response<Boolean> approveSupplier(BaseUser baseUser, SupplierApproveDto supplierApproveDto) {
        return null;
    }

    @Override
    public Response<Boolean> needCommitPaperwork(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<Boolean> inSupplierConsole(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<Long> createSupplier(RichSupplierDto richSupplierDto) {
        return null;
    }

    @Override
    public Response<Boolean> activeUser(BaseUser user, String code) {
        return null;
    }

    @Override
    public Response<Boolean> resendActiveCode(BaseUser user) {
        return null;
    }

    @Override
    public Response<Long> createBizSupplier(RichSupplierDto richSupplierDto) {
        return null;
    }

    @Override
    public Response<SupplierApproveExtra> findSupplierApproveExtraByUserId(Long userId) {
        return null;
    }

    @Override
    public Response<User> findStaffByWorkNo(String workNo) {
        return null;
    }

    @Override
    public Response<List<User>> findStaffByName(String name) {
        return null;
    }

    @Override
    public Response<Long> countEnterPassSupplier() {
        return null;
    }

    @Override
    public Response<Boolean> reCommitApproval(BaseUser baseUser) {
        return null;
    }

    @Override
    public Response<String> findLeader(BaseUser user) {
        return null;
    }

    @Override
    public Response<Boolean> remindOfApproving() {
        return null;
    }

    public Response<List<User>> findStaffByWorkerNo(String name) {
        return null;
    }
    @Override
    public Response<List<User>> findUserByIds(List ids) {
        return null;
    }
}
