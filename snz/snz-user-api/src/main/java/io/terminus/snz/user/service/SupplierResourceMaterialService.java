package io.terminus.snz.user.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.category.model.BackendCategory;
import io.terminus.snz.user.dto.SupplierResourceMaterialLogRichRecordDto;
import io.terminus.snz.user.dto.SupplierResourceMaterialRichInfoDto;
import io.terminus.snz.user.model.SupplierResourceMaterialInfo;
import io.terminus.snz.user.model.SupplierResourceMaterialSubject;

import java.util.List;
import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 8/25/14
 */
public interface SupplierResourceMaterialService {

    /**
     * 获得供应商资质交互信息
     * @param baseUser 当前登录用户
     * @return 资质交互信息
     */
    @Export(paramNames = {"baseUser"})
    Response<SupplierResourceMaterialInfo> getInfo(BaseUser baseUser);

    /**
     * 通过供应商的userId查询
     * @param userId 供应商用户id
     * @return 资质交互信息
     */
    Response<SupplierResourceMaterialInfo> getInfoByUserId(Long userId);

    /**
     * 查询供应商对于某些需求的审核状态
     * @param supplierId 供应商id
     * @param bcIds 后台类目id列表
     * @return 供应商状态
     */
    Response<Integer> getInfoInBcIds(Long supplierId, List<Long> bcIds);

    /**
     * 获得已通过类目id列表
     * @param supplierId 供应商id
     * @return 已通过类目id列表
     */
    Response<List<Long>> getApprovedBcIds(Long supplierId);

    /**
     * 批量获得已通过类目id列表
     * @param supplierIds 供应商id列表
     * @return 已通过类目id列表的供应商列表
     */
    Response<Map<Long, List<Long>>> bulkGetApprovedBcIds(List<Long> supplierIds);

    /**
     * 强制通过类目
     * @param checkerId 审核员id（若系统自动通过则为0）
     * @param supplierId 供应商id
     * @param bcIds 后台类目id列表
     * @return 是否通过成功
     */
    Response<Boolean> forceApprove(Long checkerId, Long supplierId, List<Long> bcIds);

    /**
     * 强制通过所有类目
     * @param checkerId 审核员id（若系统自动通过则为0）
     * @param supplierId 供应商id
     * @return 是否通过成功
     */
    Response<Boolean> forceApproveAll(Long checkerId, Long supplierId);

    /**
     * 申请资质交互
     * @param supplierId 供应商id
     * @param modules 所要申请资质交互的模块列表（json）
     * @param type 申请类型(5. 供应商主动申请, 6. 系统自动提交申请)
     * @return 将会参与审核的模块列表
     */
    @Export(paramNames = {"supplierId", "modules", "type"})
    Response<Boolean> applyFor(Long supplierId, String modules, Integer type);

    /**
     * 审核员所要审核的条目列表
     * @param baseUser 审核员
     * @param supplierId 被审核的供应商
     * @return 条目列表
     */
    @Export(paramNames = {"baseUser", "supplierId"})
    Response<List<SupplierResourceMaterialSubject>> getSubjectsNeedQualify(BaseUser baseUser, Long supplierId);

    /**
     * 查看已提交的审核
     * @param baseUser 审核员
     * @param supplierId 被审核的供应商
     * @return 审核过的条目及审核内容列表
     */
    @Export(paramNames = {"baseUser", "supplierId"})
    Response<List<SupplierResourceMaterialLogRichRecordDto>> getQualifiedSubjects(BaseUser baseUser, Long supplierId);

    /**
     * 提交审核内容
     * @param baseUser 当前登录用户（审核员）
     * @param details 审核的结果列表, json @see SupplierResourceMaterialLogRecordDto
     * @return 是否审核成功
     */
    @Export(paramNames = {"baseUser", "details"})
    Response<Boolean> qualifyByChecker(BaseUser baseUser, String details);

    /**
     * 申请修改驳回（已通过审核的供应商）
     * @param baseUser 当前登录用户（一级审核员）
     * @param supplierId 申请被驳回的供应商id
     * @return 是否申请成功
     */
    @Export(paramNames = {"baseUser", "supplierId"})
    Response<Boolean> askForReject(BaseUser baseUser, Long supplierId);

    /**
     * 上级驳回（已通过审核的供应商）
     * @param baseUser 当前登录用户（上级审核员）
     * @param supplierId 被驳回的供应商id
     * @return 是否驳回成功
     */
    @Export(paramNames = {"baseUser", "supplierId"})
    Response<Boolean> rejectByHigherChecker(BaseUser baseUser, Long supplierId);

    /**
     * 邀请其他审核员来审核供应商
     * @param baseUser 当前登录用户（审核员）
     * @param nick 被邀请的审核员工号
     * @param role 要赋予的权限
     * @param supplierId 被审核的供应商id
     * @return 是否邀请成功
     */
    @Export(paramNames = {"baseUser", "nick", "role", "supplierId"})
    @Deprecated
    Response<Boolean> inviteAnotherToCheck(BaseUser baseUser, String nick, String role, Long supplierId);

    /**
     * 邀请其他审核员来审核供应商（批量）
     * @param baseUser 当前登录用户（审核员）
     * @param nicks 被邀请的审核员工号列表，逗号分隔
     * @param role 要赋予的权限
     * @param supplierId 被审核的供应商id
     * @return 是否邀请成功
     */
    @Export(paramNames = {"baseUser", "nicks", "role", "supplierId"})
    @Deprecated
    Response<Boolean> bulkInviteAnotherToCheck(BaseUser baseUser, String nicks, String role, Long supplierId);

    /**
     * 邀请其他审核员时能够转让的类目
     * @param baseUser 当前登录用户（审核员）
     * @param supplierId 被审核的供应商id
     * @return 能够转让的类目
     */
    @Export(paramNames = {"baseUser", "supplierId"})
    Response<List<BackendCategory>> getBcsCanInvite(BaseUser baseUser, Long supplierId);

    /**
     * 邀请其他审核员来审核供应商（指定类目）
     * @param baseUser 当前登录用户（审核员）
     * @param nick 被邀请的审核员工号
     * @param role 要赋予的角色
     * @param bcIds 要赋予的后台类目
     * @param supplierId 被审核的供应商id
     * @return 是否邀请成功
     */
    @Export(paramNames = {"baseUser", "nick", "role", "bcIds", "supplierId"})
    Response<Boolean> inviteAnotherToCheckWithBcs(BaseUser baseUser, String nick, String role, List<Long> bcIds, Long supplierId);

    /**
     * 根据供应商资质交互的状态进行分页
     * @param baseUser 当前登录用户（审核员、采购商）
     * @param supplierName 资质交互筛选参数: 供应商名
     * @param status 资质交互筛选参数: 资质校验状态
     * @param pageNo 页码
     * @param size 分页大小
     * @return 供应商列表分页
     */
    @Export(paramNames = {"baseUser", "supplierName", "status", "pageNo", "size"})
    Response<Paging<SupplierResourceMaterialRichInfoDto>> pagingBy(
            BaseUser baseUser, String supplierName, Integer status, Integer pageNo, Integer size);

    /**
     * 第8天未审核完批量发送站内信
     * @return 是否发送成功
     */
    Response<Boolean> bulkSendMessages();

    /**
     * 第10天还在待审核中的供应商，自动设为审核不通过，并记录相关审核员
     * @return 是否处理成功
     */
    Response<Long> bulkFailSuppliers();

    /**
     * 所有当前使用的审核条目列表
     * @return 审核条目列表
     */
    @Export
    Response<List<SupplierResourceMaterialSubject>> getAllValidSubjects();

    /**
     * 修改条目列表信息
     * @param baseUser 当前登录用户（审核员、采购商）
     * @param subjects 要更新的条目列表（json, @see SupplierResourceMaterialSubject）
     * @return 是否更新成功
     */
    @Export(paramNames = {"baseUser", "subjects"})
    Response<Boolean> bulkOverwriteSubjects(BaseUser baseUser, String subjects);
}
