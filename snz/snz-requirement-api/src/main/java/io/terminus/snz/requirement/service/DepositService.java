package io.terminus.snz.requirement.service;

import io.terminus.common.model.Paging;
import io.terminus.pampas.client.Export;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.Response;
import io.terminus.snz.requirement.dto.DepositPayment;
import io.terminus.snz.requirement.dto.KjtTransDto;

import java.util.List;
import java.util.Map;

/**
 * Author: Effet
 * Mail: ef@terminus.io
 * Date: 6/13/14
 */
public interface DepositService {

    /**
     * 查找某需求已经提交过保证金的供应商id列表
     * @param requirementId    需求id
     * @return 供应商id列表
     */
    Response<List<Long>> findPaidListByRequirement(Long requirementId);

    /**
     * 是否已支付
     * @param requirementId    需求id
     * @param supplierId       供应商id
     * @return 返回保证金状态
     */
    Response<Integer> checkPaid(Long requirementId, Long supplierId);

    /**
     * 供应商应交付保证金金额（供应商为当前登录用户）
     * @param requirementId    供应商参与的需求id
     * @return 所要支付的金额
     */
    @Export(paramNames = {"baseUser", "requirementId"})
    Response<DepositPayment> getPayment(BaseUser baseUser, Long requirementId);

    /**
     * 封装了 RequirementSolutionService#findByParams, 增加返回保证金信息
     * 查询供应商的需求信息（需要分页处理）
     * @param user          采购商用户
     * @param qualifyStatus 资质校验状态（-2:资质检查不通过, －1:没有提交, 1:已提交，等待审核, 2:资质检查通过）
     * @param status        需求状态（null：显示除删除状态的全部数据,3:方案交互，4:方案综投，5:选定供应商与方案，6:招标结束）
     * @param reqName       需求名称的模糊查询
     * @param startAt       开始时间
     * @param endAt         结束时间
     * @param pageNo        当前页数（默认为0）
     * @param size          分页大小（默认为20）
     * @return Paging
     */
    @Export(paramNames = {"user", "qualifyStatus", "status", "reqName", "startAt", "endAt", "pageNo", "size"})
    Response<Paging<DepositPayment>> pagingSolutionAndDeposit(BaseUser user, Integer qualifyStatus, Integer status, String reqName,
                                                              String startAt, String endAt, Integer pageNo, Integer size);

    /**
     * 同意冻结余额代替保证金
     * @param baseUser 当前登录供应商
     * @param requirementId 供应商参与的需求id
     * @return 是否冻结余额成功
     */
    @Export(paramNames = {"baseUser", "requirementId"})
    Response<Boolean> agreeUseBalance(BaseUser baseUser, Long requirementId);
    
    /**
     * 提交保证金（付款）[update-or-create]
     * requirementId 和 supplierId，共同唯一确定一个解决方案，供应商对于这个解决方案进行保证金支付
     * @param baseUser 当前登录供应商
     * @param requirementId 供应商参与的需求id
     * @param amount        保证金金额
     * @param kjtTransDto   支付保证金是要填写的退款信息（银行卡号等）以便退款
     * @return 快捷通交易链接（须跳转）
     */
    @Export(paramNames = {"baseUser", "requirementId", "amount", "kjtTransDto"})
    Response<String> submit(BaseUser baseUser, Long requirementId, Long amount, KjtTransDto kjtTransDto);

    /**
     * 返还保证金（退款）(系统调用，不提供页面)
     * @param id 付款成功的保证金id，非合法（不存在此保证金，或保证金未支付）或已提交退款申请则会返回错误
     * @return 是否提交成功（不保证退款成功）
     */
    Response<Boolean> revoke(Long id);

    /**
     * 全额退款
     * @param requirementId    需求id
     * @param supplierId       供应商id
     * @return 是否提交成功（不保证退款成功）
     */
    Response<Boolean> revokeFull(Long requirementId, Long supplierId);

    /**
     * 处理快捷通返回的信息（支付）
     * @param params 参数列表
     * @return 是否正确处理
     */
    Response<Boolean> dealResponse(Map<String, String> params);

    /**
     * 处理快捷通返回的信息（退款/转账）
     * @param params 参数列表
     * @return 是否正确处理
     */
    Response<Boolean> dealTransResponse(Map<String, String> params);
}
